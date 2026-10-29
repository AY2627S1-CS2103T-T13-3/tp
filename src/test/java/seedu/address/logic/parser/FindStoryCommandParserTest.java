package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindStoryCommand;
import seedu.address.model.story.StoryNameContainsKeywordsPredicate;

public class FindStoryCommandParserTest {

    private final FindStoryCommandParser parser = new FindStoryCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindStoryCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "     ", expectedMessage);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "\t \r \n", expectedMessage);
    }

    @Test
    public void parse_validArgs_returnsFindStoryCommand() {
        // Single keyword
        FindStoryCommand singleKeywordCommand = new FindStoryCommand(
                new StoryNameContainsKeywordsPredicate(List.of("Housing")));
        assertParseSuccess(parser, "Housing", singleKeywordCommand);
        assertParseSuccess(parser, "   Housing   ", singleKeywordCommand);

        // Multiple keywords
        FindStoryCommand expectedCommand = new FindStoryCommand(
                new StoryNameContainsKeywordsPredicate(List.of("Housing", "Investigation", "Scandal")));
        assertParseSuccess(parser, "Housing Investigation Scandal", expectedCommand);

        // Whitespace variations (newlines, tabs)
        assertParseSuccess(parser, " \n Housing \n \t Investigation \r Scandal \t", expectedCommand);

        // Keywords with symbols or numbers
        FindStoryCommand symbolCommand = new FindStoryCommand(
                new StoryNameContainsKeywordsPredicate(List.of("2026", "Budget!", "COVID-19")));
        assertParseSuccess(parser, "2026 Budget! COVID-19", symbolCommand);
    }
}
