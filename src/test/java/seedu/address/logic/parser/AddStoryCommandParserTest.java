package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddStoryCommand;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;

public class AddStoryCommandParserTest {

    private AddStoryCommandParser parser = new AddStoryCommandParser();

    @Test
    public void parse_validArgs_returnsAddStoryCommand() {
        Story expectedStory = new Story(new StoryName("Breaking News"), new HashSet<>());
        assertParseSuccess(parser, "Breaking News", new AddStoryCommand(expectedStory));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddStoryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Non-alphanumeric story name
        assertParseFailure(parser, "Invalid@Story", StoryName.MESSAGE_CONSTRAINTS);
    }
}
