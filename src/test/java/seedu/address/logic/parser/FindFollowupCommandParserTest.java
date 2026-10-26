package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindFollowupCommand;
import seedu.address.model.followup.FollowupDescriptionContainsKeywordsPredicate;

public class FindFollowupCommandParserTest {

    private final FindFollowupCommandParser parser =
            new FindFollowupCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        FindFollowupCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        FindFollowupCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindFollowupCommand() {
        FindFollowupCommand expectedCommand = new FindFollowupCommand(
                new FollowupDescriptionContainsKeywordsPredicate(
                        List.of("interview", "meeting")));

        // Normal input
        assertParseSuccess(parser, "interview meeting", expectedCommand);

        // Extra whitespace
        assertParseSuccess(parser,
                " \n interview \n \t meeting \t",
                expectedCommand);
    }

    @Test
    public void parse_singleKeyword_returnsFindFollowupCommand() {
        FindFollowupCommand expectedCommand = new FindFollowupCommand(
                new FollowupDescriptionContainsKeywordsPredicate(
                        List.of("interview")));

        assertParseSuccess(parser, "interview", expectedCommand);
    }
}
