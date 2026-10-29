package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PREFIX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CONTACT;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddFollowupCommand;
import seedu.address.model.followup.FollowupDescription;

public class AddFollowupCommandParserTest {
    private final AddFollowupCommandParser parser = new AddFollowupCommandParser();

    /**
     * Equivalence Partitions for AddFollowupCommand:
     * 1. FollowupDescription (Preamble):
     *    - D1: Single char ("A")
     *    - D2: Typical description ("Call Doctor Smith")
     *    - D3: Symbols & Numbers ("Interview #1: 10am urgent!")
     *    - D4: Padded with whitespace ("   Followup with John   ")
     *    Invalid:
     *    - D_INV1: Empty / whitespace preamble
     *    - D_INV2: Contains forward slash ("/urgent")
     *
     * 2. ContactIndex (ci/):
     *    - CI1: 1 (min positive index)
     *    - CI2: 2 (typical index)
     *    - CI3: Integer.MAX_VALUE (boundary max positive)
     *    Invalid:
     *    - CI_INV1: 0 (boundary non-positive)
     *    - CI_INV2: -1 (negative)
     *    - CI_INV3: 2147483648 (boundary overflow)
     *    - CI_INV4: Non-integer string ("abc")
     *    - CI_INV5: Empty or whitespace value ("ci/", "ci/   ")
     *    - CI_INV6: Duplicate prefix ("ci/1 ci/2")
     *    - CI_INV7: Missing prefix
     */
    @Test
    public void parse_allPairsCombinations_success() {
        String[] descriptions = {
            "A",
            "Call Doctor Smith",
            "Interview #1: 10am urgent!",
            "   Followup with John   "
        };
        String[] trimmedDescriptions = {
            "A",
            "Call Doctor Smith",
            "Interview #1: 10am urgent!",
            "Followup with John"
        };

        Index[] contactIndices = {
            Index.fromOneBased(1),
            Index.fromOneBased(2),
            Index.fromOneBased(Integer.MAX_VALUE)
        };
        String[] contactIndexArgs = {
            "1",
            "2",
            String.valueOf(Integer.MAX_VALUE)
        };

        // All pairs of (Description, ContactIndex) partitions
        for (int i = 0; i < descriptions.length; i++) {
            for (int j = 0; j < contactIndices.length; j++) {
                String input = descriptions[i] + " ci/" + contactIndexArgs[j];
                AddFollowupCommand expectedCommand = new AddFollowupCommand(
                        new FollowupDescription(trimmedDescriptions[i]), contactIndices[j]);
                assertParseSuccess(parser, input, expectedCommand);
            }
        }
    }

    /**
     * Isolated negative test cases covering each invalid equivalence partition in isolation
     * according to Heuristic 2 (no more than one invalid input per test case).
     */
    @Test
<<<<<<< Updated upstream
    public void parse_invalidDescription_failure() {
        // symbols not allowed in FollowupDescription
        assertParseFailure(parser, "Call Dr. Smith @ 10am ci/1", FollowupDescription.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "#urgent ci/1", FollowupDescription.MESSAGE_CONSTRAINTS);
    }
=======
    public void parse_edgeCaseIsolatedInvalidInputs_failure() {
        // D_INV1: Missing or blank description
        String expectedMissingDescMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, AddFollowupCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMissingDescMessage);
        assertParseFailure(parser, "     ", expectedMissingDescMessage);
        assertParseFailure(parser, " ci/1", expectedMissingDescMessage);
        assertParseFailure(parser, "   ci/2", expectedMissingDescMessage);

        // D_INV2: Forward slash in description
        assertParseFailure(parser, "/urgent ci/1", FollowupDescription.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "Call Dr. Smith s/o the myth ci/1", FollowupDescription.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "Interview/Investigation ci/1", FollowupDescription.MESSAGE_CONSTRAINTS);
>>>>>>> Stashed changes

        // CI_INV1: Boundary 0
        assertParseFailure(parser, "Followup A ci/0", MESSAGE_INVALID_INDEX);

        // CI_INV2: Negative index
        assertParseFailure(parser, "Followup A ci/-1", MESSAGE_INVALID_INDEX);

        // CI_INV3: Boundary overflow
        assertParseFailure(parser, "Followup A ci/2147483648", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "Followup A ci/" + Long.MAX_VALUE, MESSAGE_INVALID_INDEX);

        // CI_INV4: Non-integer string
        assertParseFailure(parser, "Followup A ci/abc", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "Followup A ci/1.5", MESSAGE_INVALID_INDEX);

        // CI_INV5: Empty or blank index
        assertParseFailure(parser, "Followup A ci/", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "Followup A ci/   ", MESSAGE_INVALID_INDEX);

        // CI_INV6: Duplicate prefix
        assertParseFailure(parser, "Followup A ci/1 ci/2",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CONTACTINDEX));

<<<<<<< Updated upstream
    @Test
    public void parse_invalidPrefix_failure() {
        assertParseFailure(parser, "Followup A z/invalid", String.format(MESSAGE_INVALID_PREFIX, 12, "ci/"));
=======
        // CI_INV7: Missing contact index prefix
        assertParseFailure(parser, "Followup A",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddFollowupCommand.MESSAGE_USAGE));

        // Unexpected prefix without contact index
        assertParseFailure(parser, "Followup A z/invalid",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddFollowupCommand.MESSAGE_USAGE));
>>>>>>> Stashed changes
    }
}
