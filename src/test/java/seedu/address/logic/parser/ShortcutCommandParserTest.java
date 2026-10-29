package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHORTCUT;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.ShortcutCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ShortcutCommandParserTest {

    private final ShortcutCommandParser parser = new ShortcutCommandParser();

    @Test
    public void parse_noArguments_returnsListCommand() throws Exception {
        assertEquals(new ShortcutCommand(), parser.parse(""));
        assertEquals(new ShortcutCommand(), parser.parse("   "));
    }

    /**
     * All-Pairs combinations of valid shortcut partitions and command partitions:
     * - Shortcut (s/): letter ("a"), symbol ("!"), number ("1"), word ("ls")
     * - Command (c/): "list", "find", "help", "delete"
     * - Prefix order: standard (s/ then c/) and reverse (c/ then s/)
     */
    @Test
    public void parse_validArguments_returnsAssignmentCommand() throws Exception {
        assertEquals(new ShortcutCommand("ls", "list"), parser.parse(" s/ls c/list"));
        // reverse prefix order
        assertEquals(new ShortcutCommand("ls", "list"), parser.parse(" c/list s/ls"));

        // letter shortcut
        assertEquals(new ShortcutCommand("a", "list"), parser.parse(" s/a c/list"));
        // symbol shortcut
        assertEquals(new ShortcutCommand("!", "find"), parser.parse(" s/! c/find"));
        // digit shortcut
        assertEquals(new ShortcutCommand("1", "delete"), parser.parse(" s/1 c/delete"));
        // word shortcut
        assertEquals(new ShortcutCommand("h", "help"), parser.parse(" c/help s/h"));
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShortcutCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse("extra s/ls c/list"));
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse("123 s/ls c/list"));
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
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" s/ls c/list all"));
    }

    @Test
    public void parse_blankOrEmptyValue_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShortcutCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" s/ c/list"));
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" s/   c/list"));
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" s/ls c/"));
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" s/ls c/   "));
    }

    @Test
    public void parse_duplicatePrefixes_throwsParseException() {
        assertThrows(ParseException.class, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SHORTCUT), ()
                -> parser.parse(" s/ls s/l c/list"));
        assertThrows(ParseException.class, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COMMAND), ()
                -> parser.parse(" s/ls c/list c/find"));
    }
}
