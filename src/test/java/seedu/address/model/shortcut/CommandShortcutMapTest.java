package seedu.address.model.shortcut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class CommandShortcutMapTest {

    @Test
    public void assignShortcut_newMapping_mappingAdded() {
        CommandShortcutMap shortcuts = new CommandShortcutMap();

        shortcuts.assignShortcut("ls", "list");

        assertEquals("list", shortcuts.resolveShortcut("ls").get());
    }

    @Test
    public void assignShortcut_commandAlreadyHasShortcut_oldShortcutReplaced() {
        CommandShortcutMap shortcuts = new CommandShortcutMap();
        shortcuts.assignShortcut("l", "list");

        shortcuts.assignShortcut("ls", "list");

        assertFalse(shortcuts.resolveShortcut("l").isPresent());
        assertEquals("list", shortcuts.resolveShortcut("ls").get());
    }

    @Test
    public void assignShortcut_shortcutAlreadyUsed_commandReplaced() {
        CommandShortcutMap shortcuts = new CommandShortcutMap();
        shortcuts.assignShortcut("ls", "list");

        shortcuts.assignShortcut("ls", "liststories");

        assertEquals("liststories", shortcuts.resolveShortcut("ls").get());
        assertEquals(1, shortcuts.asMap().size());
    }

    @Test
    public void removeShortcut_existingShortcut_mappingRemoved() {
        CommandShortcutMap shortcuts = new CommandShortcutMap();
        shortcuts.assignShortcut("ls", "list");

        assertTrue(shortcuts.removeShortcut("ls"));
        assertFalse(shortcuts.resolveShortcut("ls").isPresent());
    }

    @Test
    public void asMap_modifyMap_throwsUnsupportedOperationException() {
        CommandShortcutMap shortcuts = new CommandShortcutMap();

        assertThrows(UnsupportedOperationException.class, () -> shortcuts.asMap().put("ls", "list"));
    }
}
