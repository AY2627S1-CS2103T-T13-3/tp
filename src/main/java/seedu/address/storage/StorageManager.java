package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.story.Story;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private JsonAddressBookStorage addressBookStorage;
    private JsonUserPrefsStorage userPrefsStorage;
    private JsonStoryStorage storyStorage;

    /**
     * Creates a {@code StorageManager} with the given address book,
     * story and user preferences storage.
     */
    public StorageManager(JsonAddressBookStorage addressBookStorage,
                          JsonStoryStorage storyStorage,
                          JsonUserPrefsStorage userPrefsStorage) {
        this.addressBookStorage = addressBookStorage;
        this.storyStorage = storyStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public Path getAddressBookFilePath() {
        return addressBookStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + addressBookStorage.getAddressBookFilePath());
        return addressBookStorage.readAddressBook();
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        logger.fine("Attempting to write to data file: " + addressBookStorage.getAddressBookFilePath());
        addressBookStorage.saveAddressBook(addressBook);
    }


    // ================ Story methods ==============================

    @Override
    public Path getStoryFilePath() {
        return storyStorage.getStoryFilePath();
    }

    @Override
    public Optional<List<Story>> readStories()
            throws DataLoadingException {
        logger.fine("Attempting to read stories from file: "
                + storyStorage.getStoryFilePath());
        return storyStorage.readStories();
    }

    @Override
    public void saveStories(ReadOnlyAddressBook addressBook)
            throws IOException {
        logger.fine("Attempting to write stories to file: "
                + storyStorage.getStoryFilePath());
        storyStorage.saveStories(addressBook);
    }


}
