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
        assertEquals(new UnshortcutCommand("ls"), parser.parse(" ls "));
    }

    @Test
    public void parse_emptyShortcut_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnshortcutCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(""));
    }

    @Test
    public void parse_multipleTokens_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnshortcutCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse("ls extra"));
    }
}
