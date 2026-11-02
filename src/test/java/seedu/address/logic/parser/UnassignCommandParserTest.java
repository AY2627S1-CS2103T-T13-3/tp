package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STORYINDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.UnassignCommand;

public class UnassignCommandParserTest {

    private static final Index CONTACT_INDEX = Index.fromOneBased(1);
    private static final Index STORY_INDEX = Index.fromOneBased(2);

    private final UnassignCommandParser parser = new UnassignCommandParser();

    /**
     * All-Pairs combinations of valid partitions for UnassignCommand:
     * - ContactIndex (ci/): 1 (min), 2 (typical), Integer.MAX_VALUE (max)
     * - StoryIndex (si/): 1 (min), 2 (typical), Integer.MAX_VALUE (max)
     * - Prefix order: standard (ci/ then si/) and reversed (si/ then ci/)
     */
    @Test
    public void parse_allPairsCombinations_success() {
        Index[] indices = {
            Index.fromOneBased(1),
            Index.fromOneBased(2),
            Index.fromOneBased(Integer.MAX_VALUE)
        };
        String[] indexStrings = {
            "1",
            "2",
            String.valueOf(Integer.MAX_VALUE)
        };

        // All pairs of (ContactIndex, StoryIndex) partitions in standard prefix order
        for (int c = 0; c < indices.length; c++) {
            for (int s = 0; s < indices.length; s++) {
                String input = " " + PREFIX_CONTACTINDEX + indexStrings[c] + " " + PREFIX_STORYINDEX + indexStrings[s];
                assertParseSuccess(parser, input, new UnassignCommand(indices[s], indices[c]));
            }
        }

        // All pairs in reverse prefix order (si/ then ci/)
        for (int c = 0; c < indices.length; c++) {
            for (int s = 0; s < indices.length; s++) {
                String input = " " + PREFIX_STORYINDEX + indexStrings[s] + " " + PREFIX_CONTACTINDEX + indexStrings[c];
                assertParseSuccess(parser, input, new UnassignCommand(indices[s], indices[c]));
            }
        }
    }

    @Test
    public void parse_missingRequiredPrefix_throwsParseException() {
        String expectedBothMissing = Messages.getErrorMessageForMissingRequiredInputs(
                PREFIX_CONTACTINDEX.toString(), PREFIX_STORYINDEX.toString());

        // Missing both prefixes
        assertParseFailure(parser, "", expectedBothMissing);
        assertParseFailure(parser, "   ", expectedBothMissing);

        // Missing story index
        assertParseFailure(parser, " " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased(),
                Messages.getErrorMessageForMissingRequiredInputs(PREFIX_STORYINDEX.toString()));

        // Missing contact index
        assertParseFailure(parser, " " + PREFIX_STORYINDEX + STORY_INDEX.getOneBased(),
                Messages.getErrorMessageForMissingRequiredInputs(PREFIX_CONTACTINDEX.toString()));
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        assertParseFailure(parser, "unexpected " + PREFIX_CONTACTINDEX + "1 " + PREFIX_STORYINDEX + "1",
                Messages.getErrorMessageForUnexpectedPreamble("unexpected"));
        assertParseFailure(parser, "123 " + PREFIX_CONTACTINDEX + "1 " + PREFIX_STORYINDEX + "1",
                Messages.getErrorMessageForUnexpectedPreamble("123"));
    }

    @Test
    public void parse_duplicatePrefix_throwsParseException() {
        String duplicateContactIndex = " " + PREFIX_CONTACTINDEX + "1 "
                + PREFIX_CONTACTINDEX + "2 "
                + PREFIX_STORYINDEX + "1";
        String duplicateStoryIndex = " " + PREFIX_CONTACTINDEX + "1 "
                + PREFIX_STORYINDEX + "1 "
                + PREFIX_STORYINDEX + "2";

        assertParseFailure(parser, duplicateContactIndex,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CONTACTINDEX));
        assertParseFailure(parser, duplicateStoryIndex,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STORYINDEX));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        // Zero
        assertParseFailure(parser, " " + PREFIX_CONTACTINDEX + "0 " + PREFIX_STORYINDEX + "1",
                ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " " + PREFIX_CONTACTINDEX + "1 " + PREFIX_STORYINDEX + "0",
                ParserUtil.MESSAGE_INVALID_INDEX);

        // Negative
        assertParseFailure(parser, " " + PREFIX_CONTACTINDEX + "-1 " + PREFIX_STORYINDEX + "1",
                ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " " + PREFIX_CONTACTINDEX + "1 " + PREFIX_STORYINDEX + "-1",
                ParserUtil.MESSAGE_INVALID_INDEX);

        // Integer overflow
        assertParseFailure(parser, " " + PREFIX_CONTACTINDEX + "2147483648 " + PREFIX_STORYINDEX + "1",
                ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " " + PREFIX_CONTACTINDEX + "1 " + PREFIX_STORYINDEX + "2147483648",
                ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " " + PREFIX_CONTACTINDEX + Long.MAX_VALUE + " " + PREFIX_STORYINDEX + "1",
                ParserUtil.MESSAGE_INVALID_INDEX);

        // Non-integer string
        assertParseFailure(parser, " " + PREFIX_CONTACTINDEX + "abc " + PREFIX_STORYINDEX + "1",
                ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " " + PREFIX_CONTACTINDEX + "1 " + PREFIX_STORYINDEX + "abc",
                ParserUtil.MESSAGE_INVALID_INDEX);

        // Empty / whitespace prefix values
        assertParseFailure(parser, " " + PREFIX_CONTACTINDEX + " " + PREFIX_STORYINDEX + "1",
                ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " " + PREFIX_CONTACTINDEX + "1 " + PREFIX_STORYINDEX + " ",
                ParserUtil.MESSAGE_INVALID_INDEX);
    }
}
