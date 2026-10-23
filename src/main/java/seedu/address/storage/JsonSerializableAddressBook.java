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

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_CONTACT =
            "Contacts list contains duplicate contact(s).";
    public static final String MESSAGE_DUPLICATE_FOLLOWUP =
            "Followups list contains duplicate followup(s).";
    public static final String MESSAGE_INVALID_CONTACT =
            "Followup refers to a nonexistent contact.";

    private final List<JsonAdaptedContact> contacts = new ArrayList<>();
    private final List<JsonAdaptedFollowup> followups = new ArrayList<>();

    /**
     * Constructs a JsonSerializableAddressBook with the given data.
     */
    @JsonCreator
    public JsonSerializableAddressBook(
            @JsonProperty("contacts") List<JsonAdaptedContact> contacts,
            @JsonProperty("followups") List<JsonAdaptedFollowup> followups) {
        if (contacts != null) {
            this.contacts.addAll(contacts);
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
}
