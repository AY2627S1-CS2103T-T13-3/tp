package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.logic.Messages.MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_INVALID_STORY_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalContacts.AMY;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.AddContactCommand;
import seedu.address.logic.commands.AddStoryCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ShortcutCommand;
import seedu.address.logic.commands.UnshortcutCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.contact.Contact;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.ContactBuilder;
import seedu.address.testutil.StoryBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;
    private JsonUserPrefsStorage userPrefsStorage;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage =
                new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "deletecontact 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS,
                model.getFilteredContactList().size(),
                model.getFilteredStoryList().size(),
                model.getFilteredFollowupList().size()
        );
        assertCommandSuccess(listCommand, expectedMessage, model);
    }

    @Test
    public void execute_addStory_storySavedInAddressBookFile() throws Exception {
        logic.execute(AddStoryCommand.COMMAND_WORD + " Investigative Story");

        ReadOnlyAddressBook savedAddressBook = new JsonAddressBookStorage(
                temporaryFolder.resolve("addressBook.json")).readAddressBook().orElseThrow();

        assertEquals(model.getAddressBook().getStoryList(), savedAddressBook.getStoryList());
        assertFalse(Files.exists(temporaryFolder.resolve("stories.json")));
    }

    @Test
    public void execute_shortcutAssignment_userPrefsSavedImmediately() throws Exception {
        logic.execute(ShortcutCommand.COMMAND_WORD + " s/ls c/list");

        UserPrefs savedPrefs = userPrefsStorage.readUserPrefs().orElseThrow();
        assertEquals("list", savedPrefs.getShortcuts().get("ls"));
    }

    @Test
    public void execute_assignedShortcut_commandExecuted() throws Exception {
        logic.execute(ShortcutCommand.COMMAND_WORD + " s/ls c/list");

        CommandResult result = logic.execute("ls");

        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS,
                model.getFilteredContactList().size(),
                model.getFilteredStoryList().size(),
                model.getFilteredFollowupList().size()
        );
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void execute_unshortcut_userPrefsSavedImmediately() throws Exception {
        logic.execute(ShortcutCommand.COMMAND_WORD + " s/ls c/list");

        logic.execute(UnshortcutCommand.COMMAND_WORD + " ls");

        UserPrefs savedPrefs = userPrefsStorage.readUserPrefs().orElseThrow();
        assertFalse(savedPrefs.getShortcuts().containsKey("ls"));
    }

    @Test
    public void execute_userPrefsStorageThrowsIoException_throwsCommandException() {
        IOException exception = new IOException("prefs save failed");
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage failingUserPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json")) {
                    @Override
                    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
                        throw exception;
                    }
                };
        StorageManager storage = new StorageManager(addressBookStorage, failingUserPrefsStorage);
        logic = new LogicManager(model, storage);
        Model expectedModel = new ModelManager();
        expectedModel.assignShortcut("ls", "list");
        String expectedMessage = String.format(LogicManager.FILE_OPS_ERROR_FORMAT, exception.getMessage());

        assertCommandFailure(ShortcutCommand.COMMAND_WORD + " s/ls c/list", CommandException.class,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_listShortcuts_userPrefsNotSaved() throws Exception {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage failingUserPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json")) {
                    @Override
                    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
                        throw new AssertionError("Listing shortcuts should not save user preferences.");
                    }
                };
        StorageManager storage = new StorageManager(addressBookStorage, failingUserPrefsStorage);
        logic = new LogicManager(model, storage);

        CommandResult result = logic.execute(ShortcutCommand.COMMAND_WORD);

        assertEquals(ShortcutCommand.MESSAGE_NO_SHORTCUTS, result.getFeedbackToUser());
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_invalidCommands_doesNotPersistData() {
        assertParseException("unknownCommand", MESSAGE_UNKNOWN_COMMAND);
        assertCommandException("deletecontact 1", MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX);

        assertFalse(Files.exists(temporaryFolder.resolve("addressBook.json")));
        assertFalse(Files.exists(temporaryFolder.resolve("userPrefs.json")));
    }

    @Test
    public void execute_removedShortcut_throwsParseException() throws Exception {
        logic.execute("shortcut s/ls c/list");
        logic.execute("unshortcut ls");

        assertParseException("ls", MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_assignOrUnassignEmptyContactList_throwsCommandException() {
        model.addStory(new StoryBuilder().build());

        assertCommandException("assign ci/1 si/1", MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX);
        assertCommandException("unassign ci/1 si/1", MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_assignOrUnassignEmptyStoryList_throwsCommandException() {
        model.addContact(AMY);

        assertCommandException("assign ci/1 si/1", MESSAGE_INVALID_STORY_DISPLAYED_INDEX);
        assertCommandException("unassign ci/1 si/1", MESSAGE_INVALID_STORY_DISPLAYED_INDEX);
    }

    @Test
    public void execute_assignOrUnassignIndexJustPastEnd_throwsCommandException() {
        model.addContact(AMY);
        model.addStory(new StoryBuilder().build());

        assertCommandException("assign ci/2 si/1", MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX);
        assertCommandException("unassign ci/2 si/1", MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX);
        assertCommandException("assign ci/1 si/2", MESSAGE_INVALID_STORY_DISPLAYED_INDEX);
        assertCommandException("unassign ci/1 si/2", MESSAGE_INVALID_STORY_DISPLAYED_INDEX);
    }

    @Test
    public void getFilteredContactList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredContactList().remove(0));
    }

    @Test
    public void getFilteredStoryList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredStoryList().remove(0));
    }

    @Test
    public void getFilteredFollowupList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredFollowupList().remove(0));
    }

    @Test
    public void guiSettings_getAndSet_success() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        logic.setGuiSettings(guiSettings);
        assertEquals(guiSettings, logic.getGuiSettings());
    }

    @Test
    public void getShortcuts_returnsCorrectShortcuts() {
        assertEquals(model.getShortcuts(), logic.getShortcuts());
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with a JsonAddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        StorageManager storage =
                new StorageManager(addressBookStorage, userPrefsStorage);

        logic = new LogicManager(model, storage);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddContactCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        Contact expectedContact = new ContactBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addContact(expectedContact);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }
}
