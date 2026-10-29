package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindFollowupsCommand;
import seedu.address.model.followup.FollowupDescriptionContainsKeywordsPredicate;

public class FindFollowupsCommandParserTest {

    private final FindFollowupsCommandParser parser =
            new FindFollowupsCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        FindFollowupsCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        FindFollowupsCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "\t \r \n",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        FindFollowupsCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindFollowupsCommand() {
        FindFollowupsCommand expectedCommand = new FindFollowupsCommand(
                new FollowupDescriptionContainsKeywordsPredicate(
                        List.of("interview", "meeting", "call")));

        // Normal input
        assertParseSuccess(parser, "interview meeting call", expectedCommand);

        // Extra whitespace (tabs, newlines, carriage returns)
        assertParseSuccess(parser,
                " \n interview \n \t meeting \r call \t",
                expectedCommand);

        // Symbols and numbers
        FindFollowupsCommand symbolCommand = new FindFollowupsCommand(
                new FollowupDescriptionContainsKeywordsPredicate(
                        List.of("10am", "urgent!", "hospital#2")));
        assertParseSuccess(parser, "10am urgent! hospital#2", symbolCommand);
    }

    @Test
    public void parse_singleKeyword_returnsFindFollowupsCommand() {
        FindFollowupsCommand expectedCommand = new FindFollowupsCommand(
                new FollowupDescriptionContainsKeywordsPredicate(
                        List.of("interview")));

        assertParseSuccess(parser, "interview", expectedCommand);
        assertParseSuccess(parser, "   interview   ", expectedCommand);
    }
}
