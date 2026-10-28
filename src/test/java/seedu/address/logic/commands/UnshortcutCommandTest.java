package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class UnshortcutCommandTest {

    @Test
    public void constructor_nullShortcut_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UnshortcutCommand(null));
    }

    @Test
    public void modifiesUserPrefs_returnsTrue() {
        assertTrue(new UnshortcutCommand("ls").modifiesUserPrefs());
    }

    @Test
    public void execute_existingShortcut_success() throws Exception {
        Model model = new ModelManager();
        model.assignShortcut("ls", "list");

        CommandResult result = new UnshortcutCommand("ls").execute(model);

        assertEquals(String.format(UnshortcutCommand.MESSAGE_SUCCESS, "ls"), result.getFeedbackToUser());
        assertFalse(model.getShortcuts().containsKey("ls"));
    }

    @Test
    public void execute_missingShortcut_throwsCommandException() {
        Model model = new ModelManager();
        String expectedMessage = String.format(UnshortcutCommand.MESSAGE_NOT_FOUND, "ls");

        assertThrows(CommandException.class, expectedMessage, () -> new UnshortcutCommand("ls").execute(model));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UnshortcutCommand("ls").execute(null));
    }

    @Test
    public void equals() {
        UnshortcutCommand command = new UnshortcutCommand("ls");

        // same object -> returns true
        assertTrue(command.equals(command));

        // same values -> returns true
        assertTrue(command.equals(new UnshortcutCommand("ls")));

        // null -> returns false
        assertFalse(command.equals(null));

        // different types -> returns false
        assertFalse(command.equals(5));

        // different shortcut -> returns false
        assertFalse(command.equals(new UnshortcutCommand("sc")));
    }

    @Test
    public void hashCodeMethod() {
        UnshortcutCommand command1 = new UnshortcutCommand("ls");
        UnshortcutCommand command2 = new UnshortcutCommand("ls");
        assertEquals(command1.hashCode(), command2.hashCode());
    }
}
