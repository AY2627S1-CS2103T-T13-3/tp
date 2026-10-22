package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;
import seedu.address.testutil.TypicalPersons;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");

    private static final String VALID_DESCRIPTION = "Request interview";

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalPersonsAddressBook = TypicalPersons.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalPersonsAddressBook);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_validFollowups_success() throws Exception {
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(new JsonAdaptedPerson(TypicalPersons.ALICE)),
                List.of(new JsonAdaptedFollowup(
                        VALID_DESCRIPTION,
                        TypicalPersons.ALICE.getName().fullName)));

        Followup expected = new Followup(
                new FollowupDescription(VALID_DESCRIPTION),
                TypicalPersons.ALICE.getName());

        assertEquals(List.of(expected), data.toModelType().getFollowupList());
    }

    @Test
    public void toModelType_missingFollowups_success() throws Exception {
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(new JsonAdaptedPerson(TypicalPersons.ALICE)),
                null);

        assertEquals(List.of(), data.toModelType().getFollowupList());
    }

    @Test
    public void toModelType_invalidFollowupContact_throwsIllegalValueException() {
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(new JsonAdaptedPerson(TypicalPersons.ALICE)),
                List.of(new JsonAdaptedFollowup(
                        VALID_DESCRIPTION, "Unknown Contact")));

        assertThrows(IllegalValueException.class,
                JsonSerializableAddressBook.MESSAGE_INVALID_CONTACT,
                data::toModelType);
    }

    @Test
    public void toModelType_duplicateFollowups_throwsIllegalValueException() {
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(new JsonAdaptedPerson(TypicalPersons.ALICE)),
                List.of(
                        new JsonAdaptedFollowup(
                                VALID_DESCRIPTION,
                                TypicalPersons.ALICE.getName().fullName),
                        new JsonAdaptedFollowup(
                                VALID_DESCRIPTION,
                                TypicalPersons.ALICE.getName().fullName)));

        assertThrows(IllegalValueException.class,
                JsonSerializableAddressBook.MESSAGE_DUPLICATE_FOLLOWUP,
                data::toModelType);
    }
}
