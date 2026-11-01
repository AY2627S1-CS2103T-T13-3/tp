package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CONTACT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteFollowupCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteContactCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteContactCommand, and therefore we test only one of them.
 * The path variation for those two cases occurs inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteFollowupCommandParserTest {
    private final DeleteFollowupCommandParser parser = new DeleteFollowupCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        // Min valid index
        assertParseSuccess(parser, "1", new DeleteFollowupCommand(INDEX_FIRST_CONTACT));

        // Arbitrary valid index
        assertParseSuccess(parser, "2", new DeleteFollowupCommand(Index.fromOneBased(2)));

        // Max integer boundary
        assertParseSuccess(parser, String.valueOf(Integer.MAX_VALUE),
                new DeleteFollowupCommand(Index.fromOneBased(Integer.MAX_VALUE)));

        // Whitespace padded
        assertParseSuccess(parser, "   1   ", new DeleteFollowupCommand(INDEX_FIRST_CONTACT));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String expectedMessage = MESSAGE_INVALID_INDEX + "\n" + DeleteFollowupCommand.MESSAGE_USAGE;

        // Non-numeric strings
        assertParseFailure(parser, "a", expectedMessage);
        assertParseFailure(parser, "abc", expectedMessage);

        // Empty / whitespace
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "     ", expectedMessage);

        // Multiple tokens
        assertParseFailure(parser, "1 2", expectedMessage);
        assertParseFailure(parser, "1 abc", expectedMessage);

        // Boundary non-positive (0)
        assertParseFailure(parser, "0", expectedMessage);

        // Negative values
        assertParseFailure(parser, "-1", expectedMessage);
        assertParseFailure(parser, "-10", expectedMessage);

        // Integer overflow
        assertParseFailure(parser, "2147483648", expectedMessage);
        assertParseFailure(parser, Long.toString(Long.MAX_VALUE), expectedMessage);
    }
}
