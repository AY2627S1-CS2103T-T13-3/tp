package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

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

        // single character story name
        Story singleCharStory = new Story(new StoryName("A"), new HashSet<>());
        assertParseSuccess(parser, "A", new AddStoryCommand(singleCharStory));

        // story name with single tag
        Story storyWithTag = new Story(new StoryName("Breaking News"), Set.of(new Tag("urgent")));
        assertParseSuccess(parser, "Breaking News t/urgent", new AddStoryCommand(storyWithTag));

        // story name with multiple tags and duplicates (deduplicated)
        Story storyWithMultipleTags = new Story(new StoryName("Breaking News"),
                Set.of(new Tag("urgent"), new Tag("breaking")));
        assertParseSuccess(parser, "Breaking News t/urgent t/breaking t/urgent",
                new AddStoryCommand(storyWithMultipleTags));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddStoryCommand.MESSAGE_USAGE));

        // tags provided but empty preamble
        assertParseFailure(parser, " t/urgent",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddStoryCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Invalid tag (spaces only)
        assertParseFailure(parser, "Valid Story t/   ", Tag.MESSAGE_CONSTRAINTS);

        // Story name with forward slash triggers story name constraint error
        assertParseFailure(parser, "Invalid/Story", StoryName.MESSAGE_CONSTRAINTS);

        // Valid story name but invalid tag containing forward slash
        assertParseFailure(parser, "Valid Story t/invalid/tag", Tag.MESSAGE_CONSTRAINTS);
    }
}
