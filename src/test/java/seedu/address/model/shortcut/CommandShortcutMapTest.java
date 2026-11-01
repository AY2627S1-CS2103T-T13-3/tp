package seedu.address.model.shortcut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class CommandShortcutMapTest {

    @Test
    public void isValidShortcutPattern() {
        assertFalse(CommandShortcutMap.isValidShortcutPattern(null));
        assertFalse(CommandShortcutMap.isValidShortcutPattern(""));
        assertFalse(CommandShortcutMap.isValidShortcutPattern(" "));
        assertFalse(CommandShortcutMap.isValidShortcutPattern("a b"));
        assertFalse(CommandShortcutMap.isValidShortcutPattern("/"));
        assertFalse(CommandShortcutMap.isValidShortcutPattern("/a"));
        assertFalse(CommandShortcutMap.isValidShortcutPattern("a/"));
        assertFalse(CommandShortcutMap.isValidShortcutPattern("a/b"));

        assertTrue(CommandShortcutMap.isValidShortcutPattern("a"));
        assertTrue(CommandShortcutMap.isValidShortcutPattern("ab"));
        assertTrue(CommandShortcutMap.isValidShortcutPattern("ab1"));
        assertTrue(CommandShortcutMap.isValidShortcutPattern("1a"));
        assertTrue(CommandShortcutMap.isValidShortcutPattern("a-b"));
        assertTrue(CommandShortcutMap.isValidShortcutPattern("Abc"));
        assertTrue(CommandShortcutMap.isValidShortcutPattern("ABC"));
        assertTrue(CommandShortcutMap.isValidShortcutPattern("list"));
    }

    @Test
    public void assignShortcut_invalidSyntax_returnsFalseAndNotAdded() {
        CommandShortcutMap shortcuts = new CommandShortcutMap();

        assertFalse(shortcuts.assignShortcut("a b", "list"));
        assertFalse(shortcuts.assignShortcut("a/b", "list"));
        assertFalse(shortcuts.assignShortcut("", "list"));
        assertTrue(shortcuts.asMap().isEmpty());
    }

    @Test
    public void assignShortcut_reservedCommandWord_returnsFalseAndNotAdded() {
        CommandShortcutMap shortcuts = new CommandShortcutMap();

        assertFalse(shortcuts.assignShortcut("list", "clear"));
        assertFalse(shortcuts.assignShortcut("exit", "clear"));
        assertFalse(shortcuts.assignShortcut("shortcut", "clear"));
        assertTrue(shortcuts.asMap().isEmpty());
    }

    @Test
    public void assignShortcut_nonAssignableCommand_returnsFalseAndNotAdded() {
        CommandShortcutMap shortcuts = new CommandShortcutMap();

        assertFalse(shortcuts.assignShortcut("sc", "invalidCommand"));
        assertFalse(shortcuts.assignShortcut("sc", "shortcut"));
        assertTrue(shortcuts.asMap().isEmpty());
    }

    @Test
    public void constructorWithMap_discardsInvalidMappings() {
        Map<String, String> initialMappings = Map.of(
                "ls", "list",
                "c", "clear",
                "inv/slash", "list",
                "exit", "list",
                "foo", "invalidCommand"
        );

        CommandShortcutMap shortcuts = new CommandShortcutMap(initialMappings);

        assertEquals(Map.of("ls", "list", "c", "clear"), shortcuts.asMap());
    }

    @Test
    public void assignShortcut_newMapping_mappingAdded() {
        CommandShortcutMap shortcuts = new CommandShortcutMap();

        assertTrue(shortcuts.assignShortcut("ls", "list"));
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
