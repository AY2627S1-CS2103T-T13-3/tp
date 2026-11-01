package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddStoryCommand;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.model.tag.Tag;

public class AddStoryCommandParserTest {

    private final AddStoryCommandParser parser = new AddStoryCommandParser();

    /**
     * Equivalence Partitions for AddStoryCommand:
     * 1. StoryName (Preamble):
     *    - SN1: Single char ("A")
     *    - SN2: Typical name ("Breaking News")
     *    - SN3: Punctuation/Symbols & Numbers ("Budget 2026: Politics & Tech!")
     *    - SN4: Padded whitespace ("   Local Elections 2026   ")
     *    Invalid:
     *    - SN_INV1: Empty / whitespace string
     *    - SN_INV2: Forward slash in story name
     *
     * 2. Tag (t/):
     *    - T1: Omitted (no tags)
     *    - T2: Single tag ("t/urgent")
     *    - T3: Multiple unique tags ("t/urgent t/breaking")
     *    - T4: Duplicate tags ("t/urgent t/breaking t/urgent" -> deduplicated)
     *    Invalid:
     *    - T_INV1: Empty tag value ("t/")
     *    - T_INV2: Whitespace tag ("t/   ")
     *    - T_INV3: Forward slash in tag ("t/urgent/tag")
     */
    @Test
    public void parse_allPairsCombinations_success() {
        String[] storyNames = {
            "A",
            "Breaking News",
            "Budget 2026: Politics & Tech!",
            "   Local Elections 2026   "
        };
        String[] trimmedStoryNames = {
            "A",
            "Breaking News",
            "Budget 2026: Politics & Tech!",
            "Local Elections 2026"
        };

        String[] tagArguments = {
            "",
            " t/urgent",
            " t/urgent t/breaking",
            " t/urgent t/breaking t/urgent"
        };

        @SuppressWarnings("unchecked")
        Set<Tag>[] expectedTags = new Set[] {
            Collections.emptySet(),
            Set.of(new Tag("urgent")),
            Set.of(new Tag("urgent"), new Tag("breaking")),
            Set.of(new Tag("urgent"), new Tag("breaking"))
        };

        // All pairs of (StoryName, Tag) partitions
        for (int i = 0; i < storyNames.length; i++) {
            for (int j = 0; j < tagArguments.length; j++) {
                String input = storyNames[i] + tagArguments[j];
                Story expectedStory = new Story(new StoryName(trimmedStoryNames[i]), expectedTags[j]);
                assertParseSuccess(parser, input, new AddStoryCommand(expectedStory));
            }
        }
    }

    /**
     * Isolated negative test cases covering each invalid equivalence partition in isolation
     * according to Heuristic 2 (no more than one invalid input per test case).
     */
    @Test
    public void parse_edgeCaseIsolatedInvalidInputs_failure() {
        // SN_INV1: Empty or whitespace-only preamble
        String missingStoryNameMessage = Messages.getErrorMessageForMissingRequiredInputs("story name");
        assertParseFailure(parser, "", missingStoryNameMessage);
        assertParseFailure(parser, "     ", missingStoryNameMessage);

        // SN_INV1 with valid tags: Empty preamble with tags
        assertParseFailure(parser, " t/urgent", missingStoryNameMessage);
        assertParseFailure(parser, "    t/urgent t/breaking", missingStoryNameMessage);

        // SN_INV2: Forward slash in story name
        assertParseFailure(parser, "Invalid/Story",
                Messages.getErrorMessageForInvalidUserString("Invalid/Story"));
        assertParseFailure(parser, "Budget / 2026",
                Messages.getErrorMessageForInvalidUserString("Budget / 2026"));
        assertParseFailure(parser, "Invalid/Story t/urgent",
                Messages.getErrorMessageForInvalidUserString("Invalid/Story"));

        // T_INV1: Empty tag value
        assertParseFailure(parser, "Valid Story t/", Tag.MESSAGE_CONSTRAINTS);

        // T_INV2: Whitespace only tag value
        assertParseFailure(parser, "Valid Story t/   ", Tag.MESSAGE_CONSTRAINTS);

        // T_INV3: Tag containing forward slash
        assertParseFailure(parser, "Valid Story t/invalid/tag",
                Messages.getErrorMessageForInvalidUserString("invalid/tag"));

        // Valid tag followed by invalid tag in isolation
        assertParseFailure(parser, "Valid Story t/urgent t/   ", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "Valid Story t/urgent t/invalid/tag",
                Messages.getErrorMessageForInvalidUserString("invalid/tag"));
    }

    @Test
    public void parse_escapedLeadingSlashFields_success() {
        Story expectedStory = new Story(new StoryName("/Budget 2026"), Set.of(new Tag("/urgent")));
        assertParseSuccess(parser, " \\/Budget 2026 t/\\/urgent", new AddStoryCommand(expectedStory));
    }
}
