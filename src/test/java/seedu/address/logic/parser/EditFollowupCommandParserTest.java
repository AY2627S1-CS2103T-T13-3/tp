package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditFollowupCommand;
import seedu.address.logic.commands.EditFollowupCommand.EditFollowupDescriptor;
import seedu.address.model.followup.FollowupDescription;

public class EditFollowupCommandParserTest {
    private final EditFollowupCommandParser parser = new EditFollowupCommandParser();

    /**
     * Covers pairwise combinations of valid partitions for EditFollowupCommand:
     * - Index: 1 (min), 2 (typical), Integer.MAX_VALUE (boundary max)
     * - Description: omitted, single-char ("d/A"), typical ("d/Call tomorrow"), complex ("d/Doctor Smith 10am!")
     * - ContactIndex: omitted, min ("ci/1"), typical ("ci/2"), max ("ci/" + Integer.MAX_VALUE)
     * - Prefix order: standard (d/ then ci/) and reverse (ci/ then d/)
     */
    @Test
    public void parse_pairwiseFields_success() {
        Index firstIndex = Index.fromOneBased(1);
        Index secondIndex = Index.fromOneBased(2);
        Index maxIndex = Index.fromOneBased(Integer.MAX_VALUE);

        // Pair 1: Index 1, D omitted, CI min (ci/1)
        EditFollowupDescriptor desc1 = new EditFollowupDescriptor();
        desc1.setContactIndex(firstIndex);
        assertParseSuccess(parser, "1 ci/1", new EditFollowupCommand(firstIndex, desc1));

        // Pair 2: Index 1, D single-char (d/A), CI typical (ci/2)
        EditFollowupDescriptor desc2 = new EditFollowupDescriptor();
        desc2.setDescription(new FollowupDescription("A"));
        desc2.setContactIndex(secondIndex);
        assertParseSuccess(parser, "1 d/A ci/2", new EditFollowupCommand(firstIndex, desc2));

        // Pair 3: Index 1, D typical (d/Call tomorrow), CI max (ci/MAX)
        EditFollowupDescriptor desc3 = new EditFollowupDescriptor();
        desc3.setDescription(new FollowupDescription("Call tomorrow"));
        desc3.setContactIndex(maxIndex);
        assertParseSuccess(parser, "1 d/Call tomorrow ci/" + Integer.MAX_VALUE,
                new EditFollowupCommand(firstIndex, desc3));

        // Pair 4: Index 1, D complex, CI omitted
        EditFollowupDescriptor desc4 = new EditFollowupDescriptor();
        desc4.setDescription(new FollowupDescription("Doctor Smith at 10am!"));
        assertParseSuccess(parser, "1 d/Doctor Smith at 10am!", new EditFollowupCommand(firstIndex, desc4));

        // Pair 5: Index 2, D omitted, CI typical (ci/2)
        EditFollowupDescriptor desc5 = new EditFollowupDescriptor();
        desc5.setContactIndex(secondIndex);
        assertParseSuccess(parser, "2 ci/2", new EditFollowupCommand(secondIndex, desc5));

        // Pair 6: Index 2, D single-char (d/A), CI max (ci/MAX)
        EditFollowupDescriptor desc6 = new EditFollowupDescriptor();
        desc6.setDescription(new FollowupDescription("A"));
        desc6.setContactIndex(maxIndex);
        assertParseSuccess(parser, "2 d/A ci/" + Integer.MAX_VALUE, new EditFollowupCommand(secondIndex, desc6));

        // Pair 7: Index 2, D typical (d/Call tomorrow), CI omitted
        EditFollowupDescriptor desc7 = new EditFollowupDescriptor();
        desc7.setDescription(new FollowupDescription("Call tomorrow"));
        assertParseSuccess(parser, "2 d/Call tomorrow", new EditFollowupCommand(secondIndex, desc7));

        // Pair 8: Index 2, D complex, CI min (reversed order: ci/1 then d/...)
        EditFollowupDescriptor desc8 = new EditFollowupDescriptor();
        desc8.setDescription(new FollowupDescription("Doctor Smith at 10am!"));
        desc8.setContactIndex(firstIndex);
        assertParseSuccess(parser, "2 ci/1 d/Doctor Smith at 10am!", new EditFollowupCommand(secondIndex, desc8));

        // Pair 9: Index MAX, D omitted, CI max (ci/MAX)
        EditFollowupDescriptor desc9 = new EditFollowupDescriptor();
        desc9.setContactIndex(maxIndex);
        assertParseSuccess(parser, Integer.MAX_VALUE + " ci/" + Integer.MAX_VALUE,
                new EditFollowupCommand(maxIndex, desc9));

        // Pair 10: Index MAX, D single-char (d/A), CI omitted
        EditFollowupDescriptor desc10 = new EditFollowupDescriptor();
        desc10.setDescription(new FollowupDescription("A"));
        assertParseSuccess(parser, Integer.MAX_VALUE + " d/A", new EditFollowupCommand(maxIndex, desc10));

        // Pair 11: Index MAX, D typical (d/Call tomorrow), CI min (ci/1)
        EditFollowupDescriptor desc11 = new EditFollowupDescriptor();
        desc11.setDescription(new FollowupDescription("Call tomorrow"));
        desc11.setContactIndex(firstIndex);
        assertParseSuccess(parser, Integer.MAX_VALUE + " d/Call tomorrow ci/1",
                new EditFollowupCommand(maxIndex, desc11));

        // Pair 12: Index MAX, D complex, CI typical (ci/2)
        EditFollowupDescriptor desc12 = new EditFollowupDescriptor();
        desc12.setDescription(new FollowupDescription("Doctor Smith at 10am!"));
        desc12.setContactIndex(secondIndex);
        assertParseSuccess(parser, Integer.MAX_VALUE + " d/Doctor Smith at 10am! ci/2",
                new EditFollowupCommand(maxIndex, desc12));
    }

    @Test
    public void parse_missingOrInvalidFields_failure() {
        assertParseFailure(parser, "1", EditFollowupCommand.MESSAGE_NOT_EDITED);

        // Invalid preamble boundaries
        for (String input : new String[]{
            "", " ", "0 d/Call", "-1 ci/2", "-5 d/Call", "word d/Call",
            "2147483648 d/Call", Long.toString(Long.MAX_VALUE) + " d/Call", "1 extra d/Call"
        }) {
            assertParseFailure(parser, input, ParserUtil.MESSAGE_INVALID_INDEX + "\n"
                    + EditFollowupCommand.MESSAGE_USAGE);
        }

        // Invalid description
        assertParseFailure(parser, "1 d/", FollowupDescription.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 d/   ", FollowupDescription.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 d/Call/Tomorrow",
                Messages.getErrorMessageForInvalidUserString("Call/Tomorrow"));

        // Invalid contact index boundaries
        assertParseFailure(parser, "1 ci/0", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1 ci/-1", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1 ci/2147483648", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1 ci/" + Long.MAX_VALUE, ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1 ci/abc", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1 ci/", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1 ci/   ", ParserUtil.MESSAGE_INVALID_INDEX);

        // Duplicate prefixes
        assertParseFailure(parser, "1 d/Call d/Email",
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_DESCRIPTION));
        assertParseFailure(parser, "1 ci/1 ci/2",
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_CONTACTINDEX));
    }
}
