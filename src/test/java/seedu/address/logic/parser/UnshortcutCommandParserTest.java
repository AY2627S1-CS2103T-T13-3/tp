package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UnshortcutCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class UnshortcutCommandParserTest {

    private final UnshortcutCommandParser parser = new UnshortcutCommandParser();

    @Test
    public void parse_validShortcut_returnsCommand() throws Exception {
        // Word token
        assertEquals(new UnshortcutCommand("ls"), parser.parse(" ls "));

        // Single letter
        assertEquals(new UnshortcutCommand("a"), parser.parse("a"));

        // Single digit
        assertEquals(new UnshortcutCommand("1"), parser.parse("1"));

        // Symbol
        assertEquals(new UnshortcutCommand("!"), parser.parse(" ! "));

        // Padded whitespace
        assertEquals(new UnshortcutCommand("find"), parser.parse("   find   "));
    }

    @Test
    public void parse_emptyShortcut_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnshortcutCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(""));
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse("   "));
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse("\t \n \r"));
    }

    @Test
    public void parse_multipleTokens_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnshortcutCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse("ls extra"));
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse("a b c"));
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse("1 2"));
    }
}
