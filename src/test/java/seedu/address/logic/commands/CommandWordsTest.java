package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class CommandWordsTest {

    @Test
    public void isAssignableCommandWord() {
        // Assignable commands
        assertTrue(CommandWords.isAssignableCommandWord(AddContactCommand.COMMAND_WORD));
        assertTrue(CommandWords.isAssignableCommandWord(AddFollowupCommand.COMMAND_WORD));
        assertTrue(CommandWords.isAssignableCommandWord(DeleteFollowupCommand.COMMAND_WORD));
        assertTrue(CommandWords.isAssignableCommandWord(EditStoryCommand.COMMAND_WORD));
        assertTrue(CommandWords.isAssignableCommandWord(ListContactsCommand.COMMAND_WORD));
        assertTrue(CommandWords.isAssignableCommandWord(HelpCommand.COMMAND_WORD));

        // Management commands are not assignable
        assertFalse(CommandWords.isAssignableCommandWord(ShortcutCommand.COMMAND_WORD));
        assertFalse(CommandWords.isAssignableCommandWord(UnshortcutCommand.COMMAND_WORD));

        // Unknown words are not assignable
        assertFalse(CommandWords.isAssignableCommandWord("unknown"));
    }

    @Test
    public void isCommandWord() {
        // Built-in assignable commands
        assertTrue(CommandWords.isCommandWord(AddContactCommand.COMMAND_WORD));
        assertTrue(CommandWords.isCommandWord(ListCommand.COMMAND_WORD));

        // Management commands
        assertTrue(CommandWords.isCommandWord(ShortcutCommand.COMMAND_WORD));
        assertTrue(CommandWords.isCommandWord(UnshortcutCommand.COMMAND_WORD));

        // Unknown words
        assertFalse(CommandWords.isCommandWord("unknown"));
    }

    @Test
    public void getCommandInfo() {
        CommandInfo info = CommandWords.getCommandInfo(AddContactCommand.COMMAND_WORD);
        assertNotNull(info);
        assertEquals(AddContactCommand.COMMAND_WORD, info.getCommandWord());
        assertTrue(info.isAssignable());
        assertNotNull(info.getParser());

        CommandInfo shortcutInfo = CommandWords.getCommandInfo(ShortcutCommand.COMMAND_WORD);
        assertNotNull(shortcutInfo);
        assertEquals(ShortcutCommand.COMMAND_WORD, shortcutInfo.getCommandWord());
        assertFalse(shortcutInfo.isAssignable());
        assertNotNull(shortcutInfo.getParser());

        assertNull(CommandWords.getCommandInfo("unknown"));
    }

    @Test
    public void getCommandInfos() {
        List<CommandInfo> infos = CommandWords.getCommandInfos();
        assertNotNull(infos);
        assertFalse(infos.isEmpty());

        for (CommandInfo info : infos) {
            assertNotNull(info.getCommandWord());
            assertNotNull(info.getFormat());
            assertNotNull(info.getDescription());
            assertNotNull(info.getParser());
            assertTrue(CommandWords.isCommandWord(info.getCommandWord()));
            if (info.getCommandWord().equals(ShortcutCommand.COMMAND_WORD)
                    || info.getCommandWord().equals(UnshortcutCommand.COMMAND_WORD)) {
                assertFalse(info.isAssignable());
            } else {
                assertTrue(info.isAssignable());
            }
        }
    }
}
