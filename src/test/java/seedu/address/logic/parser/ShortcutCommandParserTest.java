package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ShortcutCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ShortcutCommandParserTest {

    private final ShortcutCommandParser parser = new ShortcutCommandParser();

    @Test
    public void parse_noArguments_returnsListCommand() throws Exception {
        assertEquals(new ShortcutCommand(), parser.parse(""));
    }

    @Test
    public void parse_validArguments_returnsAssignmentCommand() throws Exception {
        assertEquals(new ShortcutCommand("ls", "list"), parser.parse(" s/ls c/list"));
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShortcutCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" s/ls"));
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" c/list"));
    }

    @Test
    public void parse_multiWordValue_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShortcutCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" s/list all c/list"));
    }
}
