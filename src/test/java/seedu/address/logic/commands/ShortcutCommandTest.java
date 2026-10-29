package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class ShortcutCommandTest {

    @Test
    public void constructor_nullShortcut_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ShortcutCommand(null, "list"));
    }

    @Test
    public void modifiesUserPrefs_assignmentAndListModes_returnsExpectedValue() {
        assertTrue(new ShortcutCommand("ls", "list").modifiesUserPrefs());
        assertFalse(new ShortcutCommand().modifiesUserPrefs());
    }

    @Test
    public void execute_assignShortcut_success() throws Exception {
        Model model = new ModelManager();

        CommandResult result = new ShortcutCommand("ls", "list").execute(model);

        assertEquals(String.format(ShortcutCommand.MESSAGE_SUCCESS, "ls", "list"), result.getFeedbackToUser());
        assertEquals("list", model.getShortcuts().get("ls"));
    }

    @Test
    public void execute_assignReplacementShortcut_oldShortcutRemovedWithWarning() throws Exception {
        Model model = new ModelManager();
        new ShortcutCommand("l", "list").execute(model);

        CommandResult result = new ShortcutCommand("ls", "list").execute(model);

        String expectedMessage = String.format(ShortcutCommand.MESSAGE_SUCCESS, "ls", "list")
                + "\n" + String.format(ShortcutCommand.MESSAGE_SHORTCUT_REPLACED, "l", "list");

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(1, model.getShortcuts().size());
        assertEquals("list", model.getShortcuts().get("ls"));
        assertFalse(model.getShortcuts().containsKey("l"));
    }

    @Test
    public void execute_reassignShortcutToExistingCommand_displaysWarning() throws Exception {
        Model model = new ModelManager();
        new ShortcutCommand("lc", "listcontacts").execute(model);
        new ShortcutCommand("ls", "liststories").execute(model);

        CommandResult result = new ShortcutCommand("lc", "liststories").execute(model);

        String expectedMessage = String.format(ShortcutCommand.MESSAGE_SUCCESS, "lc", "liststories")
                + "\n" + String.format(ShortcutCommand.MESSAGE_SHORTCUT_REPLACED, "ls", "liststories");

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals("liststories", model.getShortcuts().get("lc"));
        assertFalse(model.getShortcuts().containsKey("ls"));
    }

    @Test
    public void execute_assignSameShortcutAgain_noWarning() throws Exception {
        Model model = new ModelManager();
        new ShortcutCommand("ls", "liststories").execute(model);

        CommandResult result = new ShortcutCommand("ls", "liststories").execute(model);

        String expectedMessage = String.format(ShortcutCommand.MESSAGE_SUCCESS, "ls", "liststories");

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(1, model.getShortcuts().size());
        assertEquals("liststories", model.getShortcuts().get("ls"));
    }

    @Test
    public void execute_invalidShortcut_throwsCommandException() {
        Model model = new ModelManager();
        ShortcutCommand command = new ShortcutCommand("List!", "list");
        String expectedMessage = String.format(ShortcutCommand.MESSAGE_INVALID_SHORTCUT, "List!");

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
        assertTrue(model.getShortcuts().isEmpty());
    }

    @Test
    public void execute_builtinCommandAsShortcut_throwsCommandException() {
        Model model = new ModelManager();
        ShortcutCommand command = new ShortcutCommand("list", "findcontacts");
        String expectedMessage = String.format(ShortcutCommand.MESSAGE_RESERVED_SHORTCUT, "list");

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
        assertTrue(model.getShortcuts().isEmpty());
    }

    @Test
    public void execute_unknownTargetCommand_throwsCommandException() {
        Model model = new ModelManager();
        ShortcutCommand command = new ShortcutCommand("u", "unknown");
        String expectedMessage = String.format(ShortcutCommand.MESSAGE_UNKNOWN_COMMAND, "unknown");

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
        assertTrue(model.getShortcuts().isEmpty());
    }

    @Test
    public void execute_managementTargetCommand_throwsCommandException() {
        Model model = new ModelManager();
        ShortcutCommand command = new ShortcutCommand("sc", "shortcut");
        String expectedMessage = String.format(ShortcutCommand.MESSAGE_UNKNOWN_COMMAND, "shortcut");

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
        assertTrue(model.getShortcuts().isEmpty());
    }

    @Test
    public void execute_listEmptyShortcuts_success() throws Exception {
        CommandResult result = new ShortcutCommand().execute(new ModelManager());

        assertEquals(ShortcutCommand.MESSAGE_NO_SHORTCUTS, result.getFeedbackToUser());
    }

    @Test
    public void execute_listShortcuts_success() throws Exception {
        Model model = new ModelManager();
        model.assignShortcut("ls", "list");
        model.assignShortcut("a", "addcontact");

        CommandResult result = new ShortcutCommand().execute(model);

        assertEquals(ShortcutCommand.MESSAGE_SHORTCUTS_HEADER + "a -> addcontact\nls -> list",
                result.getFeedbackToUser());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ShortcutCommand().execute(null));
        assertThrows(NullPointerException.class, () -> new ShortcutCommand("ls", "list").execute(null));
    }

    @Test
    public void equals() {
        ShortcutCommand command = new ShortcutCommand("ls", "list");

        // same object -> returns true
        assertTrue(command.equals(command));

        // same values -> returns true
        assertTrue(command.equals(new ShortcutCommand("ls", "list")));

        // null -> returns false
        assertFalse(command.equals(null));

        // different types -> returns false
        assertFalse(command.equals(5));

        // different shortcut -> returns false
        assertFalse(command.equals(new ShortcutCommand("l", "list")));

        // different commandWord -> returns false
        assertFalse(command.equals(new ShortcutCommand("ls", "findcontacts")));

        // list mode -> equals only another list mode
        ShortcutCommand listCommand1 = new ShortcutCommand();
        ShortcutCommand listCommand2 = new ShortcutCommand();
        assertTrue(listCommand1.equals(listCommand2));
        assertFalse(command.equals(listCommand1));
    }

    @Test
    public void hashCodeMethod() {
        ShortcutCommand command1 = new ShortcutCommand("ls", "list");
        ShortcutCommand command2 = new ShortcutCommand("ls", "list");
        assertEquals(command1.hashCode(), command2.hashCode());

        ShortcutCommand listCommand1 = new ShortcutCommand();
        ShortcutCommand listCommand2 = new ShortcutCommand();
        assertEquals(listCommand1.hashCode(), listCommand2.hashCode());
    }
}
