
package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalContacts.ALICE;
import static seedu.address.testutil.TypicalContacts.HOON;
import static seedu.address.testutil.TypicalContacts.IDA;
import static seedu.address.testutil.TypicalContacts.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;
import seedu.address.model.util.SampleDataUtil;

public class JsonAddressBookStorageTest {

    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonAddressBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readAddressBook(null));
    }

    private Optional<ReadOnlyAddressBook> readAddressBook(String filePath) throws Exception {
        return new JsonAddressBookStorage(Paths.get(filePath))
                .readAddressBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("notJsonFormatAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidContactAddressBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("invalidContactAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidAndValidContactAddressBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("invalidAndValidContactAddressBook.json"));
    }

    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempAddressBook.json");
        AddressBook original = getTypicalAddressBook();
        JsonAddressBookStorage jsonAddressBookStorage = new JsonAddressBookStorage(filePath);

        // Save in new file and read back
        jsonAddressBookStorage.saveAddressBook(original, filePath);
        ReadOnlyAddressBook readBack = jsonAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        // Modify data, overwrite existing file, and read back
        original.addContact(HOON);
        original.removeContact(ALICE);
        jsonAddressBookStorage.saveAddressBook(original, filePath);
        readBack = jsonAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        // Save and read without specifying file path
        original.addContact(IDA);
        jsonAddressBookStorage.saveAddressBook(original);
        readBack = jsonAddressBookStorage.readAddressBook().get();
        assertEquals(original, new AddressBook(readBack));
    }

    @Test
    public void readAndSaveAddressBook_withFollowups_success() throws Exception {
        Path filePath = testFolder.resolve("TempAddressBook.json");
        AddressBook original = getTypicalAddressBook();

        Followup followup1 = new Followup(
                new FollowupDescription("Request interview"),
                ALICE.getName());

        Followup followup2 = new Followup(
                new FollowupDescription("Confirm appointment"),
                HOON.getName());

        original.addContact(HOON);
        original.setFollowups(List.of(followup1, followup2));

        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);

        storage.saveAddressBook(original);
        ReadOnlyAddressBook readBack = storage.readAddressBook().get();

        assertEquals(original.getContactList(), readBack.getContactList());
        assertEquals(original.getFollowupList(), readBack.getFollowupList());
    }

    @Test
    public void readAndSaveAddressBook_withStoriesAndAssignments_success() throws Exception {
        Path filePath = testFolder.resolve("TempAddressBook.json");
        AddressBook original = new AddressBook(SampleDataUtil.getSampleAddressBook());
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);

        storage.saveAddressBook(original);
        ReadOnlyAddressBook readBack = storage.readAddressBook().orElseThrow();

        assertEquals(original.getStoryList(), readBack.getStoryList());
        assertEquals(
                original.getStoryList().stream().map(story -> story.getContacts()).toList(),
                readBack.getStoryList().stream().map(story -> story.getContacts()).toList());
        assertEquals(
                original.getContactList().stream().map(contact -> contact.getStories()).toList(),
                readBack.getContactList().stream().map(contact -> contact.getStories()).toList());
    }

    @Test
    public void readAndSaveAddressBook_deletedFollowup_success() throws Exception {
        Path filePath = testFolder.resolve("TempAddressBook.json");
        AddressBook original = getTypicalAddressBook();

        Followup followup = new Followup(
                new FollowupDescription("Request interview"),
                ALICE.getName());

        original.setFollowups(List.of(followup));

        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);

        // Save with followup
        storage.saveAddressBook(original);
        assertEquals(List.of(followup),
                storage.readAddressBook().get().getFollowupList());

        // Remove followup and save again
        original.setFollowups(List.of());
        storage.saveAddressBook(original);

        ReadOnlyAddressBook readBack = storage.readAddressBook().get();
        assertEquals(List.of(), readBack.getFollowupList());
    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(null, "SomeFile.json"));
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveAddressBook(ReadOnlyAddressBook addressBook, String filePath) {
        try {
            new JsonAddressBookStorage(Paths.get(filePath))
                    .saveAddressBook(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(new AddressBook(), null));
    }
}
