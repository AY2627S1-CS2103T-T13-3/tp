package seedu.address;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.util.SampleDataUtil;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.ui.Ui;
import seedu.address.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final String VERSION = "V1.4.1";

    public static final String MESSAGE_DATA_FILE_CORRUPTED = "Data file at %s could not be loaded."
            + " Will be starting with an empty PressMemo.";
    public static final String MESSAGE_PREFERENCES_CORRUPTED = "Preference file at %s could not be loaded."
            + " Using default preferences.";

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);
    private static final Path USER_PREFS_FILE_PATH = Paths.get("preferences.json");
    private static final Path ADDRESS_BOOK_FILE_PATH = Paths.get("data", "addressbook.json");

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected final List<String> initialLoadErrors = new ArrayList<>();

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing PressMemo ]===========================");
        super.init();

        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(USER_PREFS_FILE_PATH);
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(ADDRESS_BOOK_FILE_PATH);

        storage = new StorageManager(
                addressBookStorage, userPrefsStorage);

        model = initModelManager(storage, userPrefs);

        logic = initLogicManager(model, storage);

        ui = new UiManager(logic, storage.getAddressBookFilePath(), getInitialLoadMessage());
    }

    private LogicManager initLogicManager(Model model, Storage storage) {
        LogicManager logicManager;
        try {
            // TODO: throw exception in constructor AFTER all invalid shortcuts removed
            logicManager = new LogicManager(model, storage);
        } catch (Exception e) {
            // TODO: oneline message for shortcuts removed
            String errorMsg;

            // TODO: decide whether to log here or in LogicManager constructor
            logger.warning(errorMsg);

            initialLoadErrors.add(errorMsg);
            logicManager = new LogicManager(model, storage); // this will succeed as the invalid shortcuts were removed
        }
        return logicManager;
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s address book and {@code userPrefs}. <br>
     * The data from the sample address book will be used instead if {@code storage}'s address book is not found,
     * or an empty address book will be used instead if errors occur when reading {@code storage}'s address book.
     */
    protected Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        logger.info("Using data file : " + storage.getAddressBookFilePath());

        Optional<ReadOnlyAddressBook> addressBookOptional;
        ReadOnlyAddressBook initialData;
        try {
            addressBookOptional = storage.readAddressBook();
            if (addressBookOptional.isEmpty()) {
                logger.info("Creating a new data file " + storage.getAddressBookFilePath()
                        + " populated with a sample PressMemo.");
            }
            initialData = addressBookOptional.orElseGet(SampleDataUtil::getSampleAddressBook);
        } catch (DataLoadingException e) {
            String errorMsg = String.format(MESSAGE_DATA_FILE_CORRUPTED, storage.getAddressBookFilePath());
            logger.warning(errorMsg);
            initialLoadErrors.add(errorMsg);

            initialData = new AddressBook();
        }

        return new ModelManager(initialData, userPrefs);
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(JsonUserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using preference file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            if (prefsOptional.isEmpty()) {
                logger.info("Creating new preference file " + prefsFilePath);
            }
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataLoadingException e) {
            String errorMsg = String.format(MESSAGE_PREFERENCES_CORRUPTED, prefsFilePath);
            logger.warning(errorMsg);
            initialLoadErrors.add(errorMsg);
            return new UserPrefs();


        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save preference file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    public String getInitialLoadMessage() {
        if (initialLoadErrors.isEmpty()) {
            return null;
        }
        return String.join("\n", initialLoadErrors);
    }

    public List<String> getInitialLoadErrors() {
        return Collections.unmodifiableList(initialLoadErrors);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting PressMemo " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping PressMemo ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
