package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.ListFollowupsCommand;

/**
 * Contains unit tests for ListFollowupsCommandParser.
 */
public class ListFollowupsCommandParserTest {

    private final ListFollowupsCommandParser parser = new ListFollowupsCommandParser();

    @Test
    public void parse_noArguments_success() {
        assertParseSuccess(parser, "", new ListFollowupsCommand());
        assertParseSuccess(parser, "   ", new ListFollowupsCommand());
        assertParseSuccess(parser, "\t \n \r", new ListFollowupsCommand());
    }

    @Test
    public void parse_validContactIndex_success() {
        // Min valid index
        assertParseSuccess(parser, " ci/1", new ListFollowupsCommand(Index.fromOneBased(1)));

        // Arbitrary valid index
        assertParseSuccess(parser, " ci/2", new ListFollowupsCommand(Index.fromOneBased(2)));

        // Boundary max integer index
        assertParseSuccess(parser, " ci/" + Integer.MAX_VALUE,
                new ListFollowupsCommand(Index.fromOneBased(Integer.MAX_VALUE)));

        // Whitespace padded
        assertParseSuccess(parser, "   ci/2   ", new ListFollowupsCommand(Index.fromOneBased(2)));
    }

    @Test
    public void parse_invalidPreambleOrMissingPrefix_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListFollowupsCommand.MESSAGE_USAGE);

        // Missing prefix
        assertParseFailure(parser, " 2", expectedMessage);

        // Unexpected preamble
        assertParseFailure(parser, "hello ci/2", expectedMessage);
        assertParseFailure(parser, "extra", expectedMessage);
        assertParseFailure(parser, "1 ci/2", expectedMessage);
    }

    @Test
    public void parse_invalidContactIndex_failure() {
        // Boundary non-positive (0)
        assertParseFailure(parser, " ci/0", ParserUtil.MESSAGE_INVALID_INDEX);

        // Negative indices
        assertParseFailure(parser, " ci/-1", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " ci/-5", ParserUtil.MESSAGE_INVALID_INDEX);

        // Integer overflow
        assertParseFailure(parser, " ci/2147483648", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " ci/" + Long.MAX_VALUE, ParserUtil.MESSAGE_INVALID_INDEX);

        // Non-integer string
        assertParseFailure(parser, " ci/abc", ParserUtil.MESSAGE_INVALID_INDEX);

        // Empty / whitespace index value
        assertParseFailure(parser, " ci/", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " ci/   ", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        assertParseFailure(parser, " ci/1 ci/2",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CONTACTINDEX));
        assertParseFailure(parser, " ci/2 ci/1",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CONTACTINDEX));
    }
}
