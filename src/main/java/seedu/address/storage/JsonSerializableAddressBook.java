package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.contact.Contact;
import seedu.address.model.followup.Followup;
import seedu.address.model.story.Story;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_CONTACT =
            "Contacts list contains duplicate contact(s).";
    public static final String MESSAGE_DUPLICATE_FOLLOWUP =
            "Followups list contains duplicate followup(s).";
    public static final String MESSAGE_DUPLICATE_STORY =
            "Stories list contains duplicate story(s).";
    public static final String MESSAGE_INVALID_CONTACT =
            AddressBook.MESSAGE_INVALID_CONTACT;
    public static final String MESSAGE_CONTACT_REFERS_TO_NONEXISTENT_STORY =
            AddressBook.MESSAGE_CONTACT_REFERS_TO_NONEXISTENT_STORY;
    public static final String MESSAGE_STORY_REFERS_TO_NONEXISTENT_CONTACT =
            AddressBook.MESSAGE_STORY_REFERS_TO_NONEXISTENT_CONTACT;
    public static final String MESSAGE_INCONSISTENT_CONTACT_STORY_ASSIGNMENT =
            AddressBook.MESSAGE_INCONSISTENT_CONTACT_STORY_ASSIGNMENT;
    public static final String MESSAGE_DUPLICATE_CONTACT_STORY_ASSIGNMENT =
            AddressBook.MESSAGE_DUPLICATE_CONTACT_STORY_ASSIGNMENT;
    public static final String MESSAGE_DUPLICATE_STORY_CONTACT_ASSIGNMENT =
            AddressBook.MESSAGE_DUPLICATE_STORY_CONTACT_ASSIGNMENT;

    private final List<JsonAdaptedContact> contacts = new ArrayList<>();
    private final List<JsonAdaptedStory> stories = new ArrayList<>();
    private final List<JsonAdaptedFollowup> followups = new ArrayList<>();

    /**
     * Constructs a JsonSerializableAddressBook with the given data.
     */
    @JsonCreator
    public JsonSerializableAddressBook(
            @JsonProperty("contacts") List<JsonAdaptedContact> contacts,
            @JsonProperty("stories") List<JsonAdaptedStory> stories,
            @JsonProperty("followups") List<JsonAdaptedFollowup> followups) {
        if (contacts != null) {
            this.contacts.addAll(contacts);
        }
        if (stories != null) {
            this.stories.addAll(stories);
        }
        if (followups != null) {
            this.followups.addAll(followups);
        }
    }

    /**
     * Converts a ReadOnlyAddressBook into this class for Jackson use.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        contacts.addAll(source.getContactList().stream()
                .map(JsonAdaptedContact::new)
                .collect(Collectors.toList()));

        stories.addAll(source.getStoryList().stream()
                .map(JsonAdaptedStory::new)
                .collect(Collectors.toList()));

        followups.addAll(source.getFollowupList().stream()
                .map(JsonAdaptedFollowup::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's AddressBook object.
     *
     * @throws IllegalValueException if data constraints are violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();

        for (JsonAdaptedContact jsonAdaptedContact : contacts) {
            if (jsonAdaptedContact == null) {
                throw new IllegalValueException("Invalid contact.");
            }

            Contact contact = jsonAdaptedContact.toModelType();
            if (addressBook.hasContact(contact)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_CONTACT);
            }
            addressBook.addContact(contact);
        }

        for (JsonAdaptedStory jsonAdaptedStory : stories) {
            if (jsonAdaptedStory == null) {
                throw new IllegalValueException("Invalid story.");
            }

            Story story = jsonAdaptedStory.toModelType();
            if (addressBook.hasStory(story)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_STORY);
            }
            addressBook.addStory(story);
        }

        for (JsonAdaptedFollowup jsonAdaptedFollowup : followups) {
            if (jsonAdaptedFollowup == null) {
                throw new IllegalValueException("Invalid followup.");
            }

            Followup followup = jsonAdaptedFollowup.toModelType();
            if (addressBook.hasFollowup(followup)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_FOLLOWUP);
            }
            addressBook.addFollowup(followup);
        }

        addressBook.validateRelationalIntegrity();
        return addressBook;
    }
}
