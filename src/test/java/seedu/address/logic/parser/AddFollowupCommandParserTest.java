package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PREFIX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddFollowupCommand;
import seedu.address.model.followup.FollowupDescription;

public class AddFollowupCommandParserTest {
    private AddFollowupCommandParser parser = new AddFollowupCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        AddFollowupCommand expectedCommand = new AddFollowupCommand(new FollowupDescription("Followup A"),
                INDEX_FIRST_PERSON);
        assertParseSuccess(parser, "Followup A ci/1", expectedCommand);
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
    }

    @Test
    public void parse_invalidPrefix_failure() {
        assertParseFailure(parser, "Followup A z/invalid", String.format(MESSAGE_INVALID_PREFIX, 12, "ci/"));
    }
}
