package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
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

    @Test
    public void parse_validFields_success() {
        EditFollowupDescriptor descriptor = new EditFollowupDescriptor();
        descriptor.setDescription(new FollowupDescription("Call tomorrow"));
        assertParseSuccess(parser, "1 d/Call tomorrow",
                new EditFollowupCommand(Index.fromOneBased(1), descriptor));

        // single character description
        EditFollowupDescriptor singleCharDesc = new EditFollowupDescriptor();
        singleCharDesc.setDescription(new FollowupDescription("A"));
        assertParseSuccess(parser, "1 d/A",
                new EditFollowupCommand(Index.fromOneBased(1), singleCharDesc));

        // both contact index and description (standard order)
        descriptor.setContactIndex(Index.fromOneBased(2));
        assertParseSuccess(parser, "1 ci/2 d/Call tomorrow",
                new EditFollowupCommand(Index.fromOneBased(1), descriptor));

        // both contact index and description (reversed order)
        assertParseSuccess(parser, "1 d/Call tomorrow ci/2",
                new EditFollowupCommand(Index.fromOneBased(1), descriptor));

        // contact index only
        descriptor.setDescription(null);
        assertParseSuccess(parser, "1 ci/2", new EditFollowupCommand(Index.fromOneBased(1), descriptor));

        // boundary max integer index for both followup and contact
        Index maxIndex = Index.fromOneBased(Integer.MAX_VALUE);
        EditFollowupDescriptor maxDesc = new EditFollowupDescriptor();
        maxDesc.setContactIndex(maxIndex);
        assertParseSuccess(parser, Integer.MAX_VALUE + " ci/" + Integer.MAX_VALUE,
                new EditFollowupCommand(maxIndex, maxDesc));
    }

    @Test
    public void parse_missingOrInvalidFields_failure() {
        assertParseFailure(parser, "1", EditFollowupCommand.MESSAGE_NOT_EDITED);
        for (String input : new String[]{"", "0 d/Call", "-1 ci/2", "word d/Call", "2147483648 d/Call"}) {
            assertParseFailure(parser, input,
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditFollowupCommand.MESSAGE_USAGE));
        }
        assertParseFailure(parser, "1 d/", FollowupDescription.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 d/   ", FollowupDescription.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 ci/0", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1 ci/-1", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1 ci/2147483648", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1 ci/", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1 ci/   ", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "1 d/Call d/Email",
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_DESCRIPTION));
        assertParseFailure(parser, "1 ci/1 ci/2",
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_CONTACTINDEX));
    }
}
