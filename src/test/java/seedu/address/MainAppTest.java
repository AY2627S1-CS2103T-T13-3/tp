package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

public class MainAppTest {

    private static final Path USER_PREFS_TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonUserPrefsStorageTest");

    @TempDir
    public Path tempDir;

    private MainApp mainApp;

    @BeforeEach
    public void setUp() {
        mainApp = new MainApp();
    }

    @Test
    public void initPrefs_validPrefsFile_success() throws IOException {
        Path validPrefsPath = tempDir.resolve("TypicalUserPref.json");
        Files.copy(USER_PREFS_TEST_DATA_FOLDER.resolve("TypicalUserPref.json"), validPrefsPath);
        JsonUserPrefsStorage storage = new JsonUserPrefsStorage(validPrefsPath);

        UserPrefs prefs = mainApp.initPrefs(storage);
        assertNotNull(prefs);
        assertTrue(mainApp.getInitialLoadErrors().isEmpty());
        assertNull(mainApp.getInitialLoadMessage());
    }

    @Test
    public void initPrefs_missingPrefsFile_defaultPrefsUsedWithoutError() {
        Path missingPrefsPath = tempDir.resolve("missing_prefs.json");
        JsonUserPrefsStorage storage = new JsonUserPrefsStorage(missingPrefsPath);

        UserPrefs prefs = mainApp.initPrefs(storage);
        assertNotNull(prefs);
        assertTrue(mainApp.getInitialLoadErrors().isEmpty());
        assertNull(mainApp.getInitialLoadMessage());
        assertTrue(Files.exists(missingPrefsPath));
    }

    @Test
    public void initPrefs_corruptedPrefsFile_addsErrorMessageAndDoesNotOverwrite() throws IOException {
        Path corruptedPrefsPath = tempDir.resolve("corrupted_prefs.json");
        String corruptedContent = "This is not valid JSON content";
        Files.writeString(corruptedPrefsPath, corruptedContent);

        JsonUserPrefsStorage storage = new JsonUserPrefsStorage(corruptedPrefsPath);

        UserPrefs prefs = mainApp.initPrefs(storage);
        assertNotNull(prefs);

        List<String> errors = mainApp.getInitialLoadErrors();
        assertEquals(1, errors.size());
        String expectedMessage = String.format(MainApp.MESSAGE_PREFERENCES_CORRUPTED, corruptedPrefsPath);
        assertEquals(expectedMessage, errors.get(0));
        assertEquals(expectedMessage, mainApp.getInitialLoadMessage());

        // Corrupted file must not be overwritten with defaults
        assertEquals(corruptedContent, Files.readString(corruptedPrefsPath));
    }

    @Test
    public void initModelManager_validAddressBook_success() {
        Path validAddressBookPath = tempDir.resolve("valid_ab.json");
        JsonAddressBookStorage abStorage = new JsonAddressBookStorage(validAddressBookPath);
        JsonUserPrefsStorage prefsStorage = new JsonUserPrefsStorage(tempDir.resolve("prefs.json"));
        StorageManager storageManager = new StorageManager(abStorage, prefsStorage);

        Model model = mainApp.initModelManager(storageManager, new UserPrefs());
        assertNotNull(model);
        assertTrue(mainApp.getInitialLoadErrors().isEmpty());
        assertNull(mainApp.getInitialLoadMessage());
    }

    @Test
    public void initModelManager_corruptedAddressBook_addsErrorMessage() throws IOException {
        Path corruptedAddressBookPath = tempDir.resolve("corrupted_ab.json");
        Files.writeString(corruptedAddressBookPath, "{ invalid json data }");

        JsonAddressBookStorage abStorage = new JsonAddressBookStorage(corruptedAddressBookPath);
        JsonUserPrefsStorage prefsStorage = new JsonUserPrefsStorage(tempDir.resolve("prefs.json"));
        StorageManager storageManager = new StorageManager(abStorage, prefsStorage);

        Model model = mainApp.initModelManager(storageManager, new UserPrefs());
        assertNotNull(model);
        assertEquals(new AddressBook(), model.getAddressBook());

        List<String> errors = mainApp.getInitialLoadErrors();
        assertEquals(1, errors.size());
        String expectedMessage = String.format(MainApp.MESSAGE_DATA_FILE_CORRUPTED, corruptedAddressBookPath);
        assertEquals(expectedMessage, errors.get(0));
        assertEquals(expectedMessage, mainApp.getInitialLoadMessage());
    }

    @Test
    public void bothCorrupted_combinesErrorMessages() throws IOException {
        Path corruptedPrefsPath = tempDir.resolve("corrupted_prefs.json");
        Files.writeString(corruptedPrefsPath, "invalid json prefs");
        JsonUserPrefsStorage prefsStorage = new JsonUserPrefsStorage(corruptedPrefsPath);

        Path corruptedAddressBookPath = tempDir.resolve("corrupted_ab.json");
        Files.writeString(corruptedAddressBookPath, "invalid json ab");
        JsonAddressBookStorage abStorage = new JsonAddressBookStorage(corruptedAddressBookPath);
        StorageManager storageManager = new StorageManager(abStorage, prefsStorage);

        mainApp.initPrefs(prefsStorage);
        mainApp.initModelManager(storageManager, new UserPrefs());

        List<String> errors = mainApp.getInitialLoadErrors();
        assertEquals(2, errors.size());
        String expectedPrefsMessage = String.format(MainApp.MESSAGE_PREFERENCES_CORRUPTED, corruptedPrefsPath);
        String expectedDataMessage = String.format(MainApp.MESSAGE_DATA_FILE_CORRUPTED, corruptedAddressBookPath);
        assertEquals(expectedPrefsMessage + "\n" + expectedDataMessage, mainApp.getInitialLoadMessage());
    }
}
