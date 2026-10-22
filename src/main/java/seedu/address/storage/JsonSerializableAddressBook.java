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
import seedu.address.model.followup.Followup;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON =
            "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_FOLLOWUP =
            "Followups list contains duplicate followup(s).";
    public static final String MESSAGE_INVALID_CONTACT =
            "Followup refers to a nonexistent contact.";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedFollowup> followups = new ArrayList<>();

    /**
     * Constructs a JsonSerializableAddressBook with the given data.
     */
    @JsonCreator
    public JsonSerializableAddressBook(
            @JsonProperty("persons") List<JsonAdaptedPerson> persons,
            @JsonProperty("followups") List<JsonAdaptedFollowup> followups) {
        if (persons != null) {
            this.persons.addAll(persons);
        }
        if (followups != null) {
            this.followups.addAll(followups);
        }
    }

    /**
     * Converts a ReadOnlyAddressBook into this class for Jackson use.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream()
                .map(JsonAdaptedPerson::new)
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

        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            if (jsonAdaptedPerson == null) {
                throw new IllegalValueException("Invalid person.");
            }

            Person person = jsonAdaptedPerson.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(person);
        }

        List<Followup> modelFollowups = new ArrayList<>();

        for (JsonAdaptedFollowup jsonAdaptedFollowup : followups) {
            if (jsonAdaptedFollowup == null) {
                throw new IllegalValueException("Invalid followup.");
            }

            Followup followup = jsonAdaptedFollowup.toModelType();

            boolean contactExists = addressBook.getPersonList().stream()
                    .anyMatch(person -> person.getName()
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
