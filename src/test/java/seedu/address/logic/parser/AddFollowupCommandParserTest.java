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
    private AddFollowupCommandParser parser = new AddFollowupCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        AddFollowupCommand expectedCommand = new AddFollowupCommand(new FollowupDescription("Followup A"),
                INDEX_FIRST_CONTACT);
        assertParseSuccess(parser, "Followup A ci/1", expectedCommand);

        // single character description
        AddFollowupCommand singleCharCommand = new AddFollowupCommand(new FollowupDescription("A"),
                INDEX_FIRST_CONTACT);
        assertParseSuccess(parser, "A ci/1", singleCharCommand);

        // description with alphanumeric words and numbers
        AddFollowupCommand alphaNumCommand = new AddFollowupCommand(
                new FollowupDescription("Call Doctor Smith 10am urgent"), INDEX_FIRST_CONTACT);
        assertParseSuccess(parser, "Call Doctor Smith 10am urgent ci/1", alphaNumCommand);

        // boundary max integer index
        Index maxIndex = Index.fromOneBased(Integer.MAX_VALUE);
        AddFollowupCommand maxIndexCommand = new AddFollowupCommand(new FollowupDescription("Followup A"),
                maxIndex);
        assertParseSuccess(parser, "Followup A ci/" + Integer.MAX_VALUE, maxIndexCommand);
    }

    @Test
    public void parse_invalidDescription_failure() {
        // fslash not allowed in FollowupDescription
        assertParseFailure(parser, "Call Dr. Smith s/o the myth ci/1", FollowupDescription.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "/urgent ci/1", FollowupDescription.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_missingDescription_failure() {
        String expectedMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, AddFollowupCommand.MESSAGE_USAGE);

        assertParseFailure(parser, " ci/1", expectedMessage);
    }

    @Test
    public void parse_missingIndex_failure() {
        String expectedMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, AddFollowupCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "Followup A", expectedMessage);
    }

    @Test
    public void parse_emptyIndex_failure() {
        assertParseFailure(parser, "Followup A ci/", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "Followup A ci/   ", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser, "Followup A ci/0", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "Followup A ci/-1", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "Followup A ci/2147483648", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "Followup A ci/abc", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_duplicatePrefix_failure() {
        assertParseFailure(parser, "Followup A ci/1 ci/2",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CONTACTINDEX));
    }

    @Test
    public void parse_invalidPrefix_failure() {
        assertParseFailure(parser, "Followup A z/invalid", String.format(MESSAGE_INVALID_PREFIX, 12, "ci/"));
    }
}
