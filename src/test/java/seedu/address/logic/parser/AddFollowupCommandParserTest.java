package seedu.address.logic.parser;

import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.AddFollowupCommand;
import seedu.address.model.followup.FollowupDescription;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

public class AddFollowupCommandParserTest {
    private AddFollowupCommandParser parser = new AddFollowupCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        AddFollowupCommand expectedCommand = new AddFollowupCommand(new FollowupDescription("Followup A"), INDEX_FIRST_PERSON);
        assertParseSuccess(parser, "Followup A ci/1", expectedCommand);
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddFollowupCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "ci/1", expectedMessage);

        assertParseFailure(parser, " ci/1", expectedMessage);

        assertParseFailure(parser, "  ci/1", expectedMessage);

        assertParseFailure(parser, "    ci/1", expectedMessage);

        assertParseFailure(parser, "Followup A", expectedMessage);

        assertParseFailure(parser, "Followup A si/", expectedMessage);

        assertParseFailure(parser, "Followup A si/1 n/Alice", expectedMessage);

        assertParseFailure(parser, "Followup A ci/Alice", MESSAGE_INVALID_INDEX);

        assertParseFailure(parser, "Followup A ci/", MESSAGE_INVALID_INDEX);

        assertParseFailure(parser, "Followup A ci/ ", MESSAGE_INVALID_INDEX);

        assertParseFailure(parser, "Followup A ci/  ", MESSAGE_INVALID_INDEX);

        assertParseFailure(parser, "Followup A ci/*", MESSAGE_INVALID_INDEX);

        assertParseFailure(parser, "Followup A ci/1 si/1", MESSAGE_INVALID_INDEX);

        assertParseFailure(parser, "Followup A si/1 ci/1", FollowupDescription.MESSAGE_CONSTRAINTS);
    }
}
