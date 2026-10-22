package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ListContactsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Contains unit tests for ListContactsCommandParser.
 */
public class ListContactsCommandParserTest {

    private final ListContactsCommandParser parser = new ListContactsCommandParser();

    @Test
    public void parse_noArguments_success() throws ParseException {
        assertEquals(new ListContactsCommand(), parser.parse(""));
        assertEquals(new ListContactsCommand(), parser.parse("   "));
    }

    @Test
    public void parse_validStoryIndex_success() throws ParseException {
        assertEquals(new ListContactsCommand(Index.fromOneBased(2)), parser.parse(" si/2"));
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
