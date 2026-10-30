package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindStoriesCommand;
import seedu.address.model.story.StoryNameContainsKeywordsPredicate;

public class FindStoriesCommandParserTest {

    private final FindStoryCommandParser parser = new FindStoryCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindStoriesCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "     ", expectedMessage);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "\t \r \n", expectedMessage);
    }

    @Test
    public void parse_validArgs_returnsFindStoryCommand() {
        // Single keyword
        FindStoriesCommand singleKeywordCommand = new FindStoriesCommand(
                new StoryNameContainsKeywordsPredicate(List.of("Housing")));
        assertParseSuccess(parser, "Housing", singleKeywordCommand);
        assertParseSuccess(parser, "   Housing   ", singleKeywordCommand);

        // Multiple keywords
        FindStoriesCommand expectedCommand = new FindStoriesCommand(
                new StoryNameContainsKeywordsPredicate(List.of("Housing", "Investigation", "Scandal")));
        assertParseSuccess(parser, "Housing Investigation Scandal", expectedCommand);

        // Whitespace variations (newlines, tabs)
        assertParseSuccess(parser, " \n Housing \n \t Investigation \r Scandal \t", expectedCommand);

        // Keywords with symbols or numbers
        FindStoriesCommand symbolCommand = new FindStoriesCommand(
                new StoryNameContainsKeywordsPredicate(List.of("2026", "Budget!", "COVID-19")));
        assertParseSuccess(parser, "2026 Budget! COVID-19", symbolCommand);
    }
}
