package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddStoryCommand;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.model.tag.Tag;

public class AddStoryCommandParserTest {

    private AddStoryCommandParser parser = new AddStoryCommandParser();

    @Test
    public void parse_validArgs_returnsAddStoryCommand() {
        Story expectedStory = new Story(new StoryName("Breaking News"), new HashSet<>());
        assertParseSuccess(parser, "Breaking News", new AddStoryCommand(expectedStory));

        // Story name with symbols and punctuation
        Story expectedStoryWithSymbols = new Story(new StoryName("Budget 2026: Politics & Tech!"), new HashSet<>());
        assertParseSuccess(parser, "Budget 2026: Politics & Tech!", new AddStoryCommand(expectedStoryWithSymbols));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddStoryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Invalid tag (spaces only)
        assertParseFailure(parser, "Valid Story t/   ", Tag.MESSAGE_CONSTRAINTS);

        // Story name with forward slash triggers invalid prefix error
        assertParseFailure(parser, "Invalid/Story",
                String.format(Messages.MESSAGE_INVALID_PREFIX, 7, "t/"));
    }
}
