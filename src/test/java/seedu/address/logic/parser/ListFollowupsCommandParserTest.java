package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ListFollowupsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Contains unit tests for ListFollowupsCommandParser.
 */
public class ListFollowupsCommandParserTest {

    private final ListFollowupsCommandParser parser = new ListFollowupsCommandParser();

    @Test
    public void parse_noArguments_success() throws ParseException {
        assertEquals(new ListFollowupsCommand(), parser.parse(""));
        assertEquals(new ListFollowupsCommand(), parser.parse("   "));
    }

    @Test
    public void parse_validContactIndex_success() throws ParseException {
        assertEquals(new ListFollowupsCommand(Index.fromOneBased(2)), parser.parse(" ci/2"));
    }

    @Test
    public void parse_missingPrefix_failure() {
        assertThrows(ParseException.class, () -> parser.parse(" 2"));
    }

    @Test
    public void parse_invalidContactIndex_failure() {
        assertThrows(ParseException.class, () -> parser.parse(" ci/0"));
        assertThrows(ParseException.class, () -> parser.parse(" ci/-1"));
        assertThrows(ParseException.class, () -> parser.parse(" ci/abc"));
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        assertThrows(ParseException.class, () -> parser.parse(" ci/1 ci/2"));
    }

    @Test
    public void parse_missingContactIndex_failure() {
        assertThrows(ParseException.class, () -> parser.parse(" ci/"));
    }

    @Test
    public void parse_unexpectedArguments_failure() {
        assertThrows(ParseException.class, () -> parser.parse("hello ci/2"));
    }
}
