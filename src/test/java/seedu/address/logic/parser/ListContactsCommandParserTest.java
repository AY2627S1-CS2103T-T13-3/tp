package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STORYINDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.ListContactsCommand;

/**
 * Contains unit tests for ListContactsCommandParser.
 */
public class ListContactsCommandParserTest {

    private final ListContactsCommandParser parser = new ListContactsCommandParser();

    @Test
    public void parse_noArguments_success() {
        assertParseSuccess(parser, "", new ListContactsCommand());
        assertParseSuccess(parser, "   ", new ListContactsCommand());
        assertParseSuccess(parser, "\t \n \r", new ListContactsCommand());
    }

    @Test
    public void parse_validStoryIndex_success() {
        // Min valid index
        assertParseSuccess(parser, " si/1", new ListContactsCommand(Index.fromOneBased(1)));

        // Arbitrary valid index
        assertParseSuccess(parser, " si/2", new ListContactsCommand(Index.fromOneBased(2)));

        // Boundary max integer index
        assertParseSuccess(parser, " si/" + Integer.MAX_VALUE,
                new ListContactsCommand(Index.fromOneBased(Integer.MAX_VALUE)));

        // Whitespace padded
        assertParseSuccess(parser, "   si/2   ", new ListContactsCommand(Index.fromOneBased(2)));
    }

    @Test
    public void parse_invalidPreambleOrMissingPrefix_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListContactsCommand.MESSAGE_USAGE);

        // Missing prefix
        assertParseFailure(parser, " 2", expectedMessage);

        // Unexpected preamble
        assertParseFailure(parser, "hello si/2", expectedMessage);
        assertParseFailure(parser, "extra", expectedMessage);
        assertParseFailure(parser, "1 si/2", expectedMessage);
    }

    @Test
    public void parse_invalidStoryIndex_failure() {
        // Boundary non-positive (0)
        assertParseFailure(parser, " si/0", ParserUtil.MESSAGE_INVALID_INDEX);

        // Negative indices
        assertParseFailure(parser, " si/-1", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " si/-5", ParserUtil.MESSAGE_INVALID_INDEX);

        // Integer overflow
        assertParseFailure(parser, " si/2147483648", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " si/" + Long.MAX_VALUE, ParserUtil.MESSAGE_INVALID_INDEX);

        // Non-integer string
        assertParseFailure(parser, " si/abc", ParserUtil.MESSAGE_INVALID_INDEX);

        // Empty / whitespace index value
        assertParseFailure(parser, " si/", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " si/   ", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        assertParseFailure(parser, " si/1 si/2",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STORYINDEX));
        assertParseFailure(parser, " si/2 si/1",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STORYINDEX));
    }
}
