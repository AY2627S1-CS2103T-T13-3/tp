
package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Contains unit tests for ListCommandParser.
 */
public class ListCommandParserTest {

    private final ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_noArguments_success() throws ParseException {
        assertInstanceOf(ListCommand.class, parser.parse(""));
    }

    @Test
    public void parse_validStoryIndex_success() throws ParseException {
        assertInstanceOf(ListCommand.class, parser.parse(" si/2"));
    }

    @Test
    public void parse_missingPrefix_failure() {
        assertThrows(ParseException.class, () -> parser.parse(" 2"));
    }

    @Test
    public void parse_invalidStoryIndex_failure() {
        assertThrows(ParseException.class, () -> parser.parse(" si/0"));
        assertThrows(ParseException.class, () -> parser.parse(" si/abc"));
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        assertThrows(ParseException.class, () -> parser.parse(" si/1 si/2"));
    }

    @Test
    public void parse_missingStoryIndex_failure() {
        assertThrows(ParseException.class, () -> parser.parse(" si/"));
    }

    @Test
    public void parse_unexpectedArguments_failure() {
        assertThrows(ParseException.class, () -> parser.parse("hello si/2"));
    }
}
