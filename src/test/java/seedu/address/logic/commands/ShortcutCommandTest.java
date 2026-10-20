package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class ShortcutCommandTest {

    @Test
    public void constructor_nullShortcut_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ShortcutCommand(null, "list"));
    }

    @Test
    public void execute_assignShortcut_success() throws Exception {
        Model model = new ModelManager();

        CommandResult result = new ShortcutCommand("ls", "list").execute(model);

        assertEquals(String.format(ShortcutCommand.MESSAGE_SUCCESS, "ls", "list"), result.getFeedbackToUser());
        assertEquals("list", model.getShortcuts().get("ls"));
    }

    @Test
    public void execute_assignReplacementShortcut_oldShortcutRemoved() throws Exception {
        Model model = new ModelManager();
        new ShortcutCommand("l", "list").execute(model);

        new ShortcutCommand("ls", "list").execute(model);

        assertEquals(1, model.getShortcuts().size());
        assertEquals("list", model.getShortcuts().get("ls"));
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
        model.assignShortcut("a", "add");

        CommandResult result = new ShortcutCommand().execute(model);

        assertEquals(ShortcutCommand.MESSAGE_SHORTCUTS_HEADER + "a -> add\nls -> list", result.getFeedbackToUser());
    }
}
