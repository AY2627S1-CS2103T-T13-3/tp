package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
}
