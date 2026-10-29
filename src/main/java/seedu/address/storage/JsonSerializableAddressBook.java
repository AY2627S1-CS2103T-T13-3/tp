package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Name;
import seedu.address.model.followup.Followup;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;

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
            "Followup refers to a nonexistent contact.";
    public static final String MESSAGE_CONTACT_REFERS_TO_NONEXISTENT_STORY =
            "Contact refers to a nonexistent story.";
    public static final String MESSAGE_STORY_REFERS_TO_NONEXISTENT_CONTACT =
            "Story refers to a nonexistent contact.";
    public static final String MESSAGE_INCONSISTENT_CONTACT_STORY_ASSIGNMENT =
            "Contact and story assignments are inconsistent.";
    public static final String MESSAGE_DUPLICATE_CONTACT_STORY_ASSIGNMENT =
            "Contact has duplicate assigned stories.";
    public static final String MESSAGE_DUPLICATE_STORY_CONTACT_ASSIGNMENT =
            "Story has duplicate assigned contacts.";

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

        validateContactStoryRelationships(addressBook);

        List<Followup> modelFollowups = new ArrayList<>();

        for (JsonAdaptedFollowup jsonAdaptedFollowup : followups) {
            if (jsonAdaptedFollowup == null) {
                throw new IllegalValueException("Invalid followup.");
            }

            Followup followup = jsonAdaptedFollowup.toModelType();

            boolean contactExists = addressBook.getContactList().stream()
                    .anyMatch(contact -> contact.getName()
                            .equals(followup.getContact()));

            if (!contactExists) {
                throw new IllegalValueException(MESSAGE_INVALID_CONTACT);
            }

            boolean isDuplicate = modelFollowups.stream()
                    .anyMatch(followup::isSameFollowup);

            if (isDuplicate) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_FOLLOWUP);
            }

            modelFollowups.add(followup);
        }

        addressBook.setFollowups(modelFollowups);
        return addressBook;
    }

    /**
     * Ensures all Contact-Story assignments are unique, reciprocal, and refer to existing entities.
     */
    private void validateContactStoryRelationships(AddressBook addressBook) throws IllegalValueException {
        validateNoDuplicateAssignments(addressBook);

        Set<ContactStoryAssignment> contactAssignments = getAssignmentsFromContacts(addressBook);
        Set<ContactStoryAssignment> storyAssignments = getAssignmentsFromStories(addressBook);

        validateAssignmentReferencesExist(addressBook, contactAssignments, storyAssignments);
        validateAssignmentsAreReciprocal(contactAssignments, storyAssignments);
    }

    /**
     * Ensures contacts and stories do not contain duplicate assignment references.
     */
    private void validateNoDuplicateAssignments(AddressBook addressBook) throws IllegalValueException {
        boolean hasDuplicateAssignedStories = addressBook.getContactList().stream()
                .anyMatch(contact -> containsDuplicates(contact.getStories()));
        if (hasDuplicateAssignedStories) {
            throw new IllegalValueException(MESSAGE_DUPLICATE_CONTACT_STORY_ASSIGNMENT);
        }

        boolean hasDuplicateAssignedContacts = addressBook.getStoryList().stream()
                .anyMatch(story -> containsDuplicates(story.getContacts()));
        if (hasDuplicateAssignedContacts) {
            throw new IllegalValueException(MESSAGE_DUPLICATE_STORY_CONTACT_ASSIGNMENT);
        }
    }

    /**
     * Ensures every assignment refers to an existing contact and story.
     */
    private void validateAssignmentReferencesExist(AddressBook addressBook,
            Set<ContactStoryAssignment> contactAssignments, Set<ContactStoryAssignment> storyAssignments)
            throws IllegalValueException {
        Set<Name> contactNames = addressBook.getContactList().stream()
                .map(Contact::getName)
                .collect(Collectors.toSet());
        Set<StoryName> storyNames = addressBook.getStoryList().stream()
                .map(Story::getName)
                .collect(Collectors.toSet());

        boolean contactReferencesMissingStory = contactAssignments.stream()
                .anyMatch(assignment -> !storyNames.contains(assignment.storyName()));
        if (contactReferencesMissingStory) {
            throw new IllegalValueException(MESSAGE_CONTACT_REFERS_TO_NONEXISTENT_STORY);
        }

        boolean storyReferencesMissingContact = storyAssignments.stream()
                .anyMatch(assignment -> !contactNames.contains(assignment.contactName()));
        if (storyReferencesMissingContact) {
            throw new IllegalValueException(MESSAGE_STORY_REFERS_TO_NONEXISTENT_CONTACT);
        }
    }

    /**
     * Ensures contacts and stories describe the same assignments.
     */
    private void validateAssignmentsAreReciprocal(Set<ContactStoryAssignment> contactAssignments,
            Set<ContactStoryAssignment> storyAssignments) throws IllegalValueException {
        if (!contactAssignments.equals(storyAssignments)) {
            throw new IllegalValueException(MESSAGE_INCONSISTENT_CONTACT_STORY_ASSIGNMENT);
        }
    }

    private Set<ContactStoryAssignment> getAssignmentsFromContacts(AddressBook addressBook) {
        return addressBook.getContactList().stream()
                .flatMap(contact -> contact.getStories().stream()
                        .map(storyName -> new ContactStoryAssignment(contact.getName(), storyName)))
                .collect(Collectors.toSet());
    }

    private Set<ContactStoryAssignment> getAssignmentsFromStories(AddressBook addressBook) {
        return addressBook.getStoryList().stream()
                .flatMap(story -> story.getContacts().stream()
                        .map(contactName -> new ContactStoryAssignment(contactName, story.getName())))
                .collect(Collectors.toSet());
    }

    private boolean containsDuplicates(List<?> values) {
        return new HashSet<>(values).size() != values.size();
    }

    private record ContactStoryAssignment(Name contactName, StoryName storyName) {}
}
