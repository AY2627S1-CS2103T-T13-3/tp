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
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindStoryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindStoryCommand() {
        FindStoryCommand expectedCommand = new FindStoryCommand(
                new StoryNameContainsKeywordsPredicate(List.of("Housing", "Investigation")));

        assertParseSuccess(parser, "Housing Investigation", expectedCommand);
        assertParseSuccess(parser, " \n Housing \n \t Investigation  \t", expectedCommand);
    }
}
