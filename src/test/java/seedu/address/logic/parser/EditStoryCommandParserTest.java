package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_USER_STRING;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_STORYNAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_STORYTAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_CRIME;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_FRAUD;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_MALAYSIA;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_SINGAPORE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STORYNAME_CRIME;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STORYNAME_FRAUD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STORYTAG_MALAYSIA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STORYTAG_SINGAPORE;
import static seedu.address.logic.commands.EditStoryCommand.MESSAGE_NOT_EDITED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STORY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_STORY;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_STORY;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditStoryCommand;
import seedu.address.logic.commands.EditStoryCommand.EditStoryDescriptor;
import seedu.address.model.story.StoryName;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditStoryDescriptorBuilder;

public class EditStoryCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditStoryCommand.MESSAGE_USAGE);
    private EditStoryCommandParser parser = new EditStoryCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_STORYNAME_CRIME, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_CRIME, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "-1" + NAME_DESC_CRIME, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_CRIME, MESSAGE_INVALID_FORMAT);

        // boundary integer overflow
        assertParseFailure(parser, "2147483648" + NAME_DESC_CRIME, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, Long.toString(Long.MAX_VALUE) + NAME_DESC_CRIME, MESSAGE_INVALID_FORMAT);

        // non-integer string
        assertParseFailure(parser, "abc" + NAME_DESC_CRIME, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_USER_STRING);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_STORYNAME_DESC, StoryName.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1 n/", StoryName.MESSAGE_CONSTRAINTS); // empty name
        assertParseFailure(parser, "1 n/   ", StoryName.MESSAGE_CONSTRAINTS); // whitespace name
        assertParseFailure(parser, "1 n/Story/1", MESSAGE_INVALID_USER_STRING); // slash in name

        assertParseFailure(parser, "1" + INVALID_STORYTAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag
        assertParseFailure(parser, "1 t/tag/1", MESSAGE_INVALID_USER_STRING); // slash in tag

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Story} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_SINGAPORE + TAG_DESC_MALAYSIA + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_SINGAPORE + TAG_EMPTY + TAG_DESC_MALAYSIA, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_SINGAPORE + TAG_DESC_MALAYSIA, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_STORYNAME_DESC + INVALID_STORYTAG_DESC,
                StoryName.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_STORY;
        String userInput = targetIndex.getOneBased() + TAG_DESC_SINGAPORE
                + TAG_DESC_MALAYSIA + NAME_DESC_FRAUD;

        EditStoryDescriptor descriptor = new EditStoryDescriptorBuilder().withName(VALID_STORYNAME_FRAUD)
                .withTags(VALID_STORYTAG_SINGAPORE, VALID_STORYTAG_MALAYSIA).build();
        EditStoryCommand expectedCommand = new EditStoryCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_onlyNameFieldSpecified_success() {
        Index targetIndex = INDEX_FIRST_STORY;
        String userInput = targetIndex.getOneBased() + NAME_DESC_FRAUD;

        EditStoryDescriptor descriptor = new EditStoryDescriptorBuilder().withName(VALID_STORYNAME_FRAUD).build();
        EditStoryCommand expectedCommand = new EditStoryCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_onlyTagFieldSpecified_success() {
        Index targetIndex = INDEX_THIRD_STORY;
        String userInput = targetIndex.getOneBased() + TAG_DESC_MALAYSIA;
        EditStoryDescriptor descriptor = new EditStoryDescriptorBuilder().withTags(VALID_STORYTAG_MALAYSIA).build();
        EditStoryCommand expectedCommand = new EditStoryCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddStoryCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_STORY;
        String userInput = targetIndex.getOneBased() + INVALID_STORYNAME_DESC + NAME_DESC_FRAUD;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + NAME_DESC_FRAUD + INVALID_STORYNAME_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple valid fields repeated
        userInput = targetIndex.getOneBased() + NAME_DESC_FRAUD + NAME_DESC_CRIME + TAG_DESC_MALAYSIA
                + TAG_DESC_SINGAPORE;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_STORYNAME_DESC + INVALID_STORYTAG_DESC + INVALID_STORYNAME_DESC
                + INVALID_STORYTAG_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_STORY;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditStoryDescriptor descriptor = new EditStoryDescriptorBuilder().withTags().build();
        EditStoryCommand expectedCommand = new EditStoryCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
        assertParseSuccess(parser, targetIndex.getOneBased() + " t/   ", expectedCommand);
    }

    /**
     * Covers pairwise combinations of valid partitions for EditStoryCommand:
     * - Index: 1 (min), 2 (typical), Integer.MAX_VALUE (boundary max)
     * - Name: omitted, single-char ("A"), typical ("Crime"), complex ("Election 2026!")
     * - Tags: omitted, reset ("t/"), single ("t/urgent"), multiple ("t/urgent t/breaking")
     * - Prefix order: name then tags, tags then name
     */
    @Test
    public void parse_pairwiseNameAndTags_success() {
        Index firstIndex = INDEX_FIRST_STORY;
        Index secondIndex = INDEX_SECOND_STORY;
        Index maxIndex = Index.fromOneBased(Integer.MAX_VALUE);

        // Pair 1: Index 1, Name omitted, Tag single
        EditStoryDescriptor desc1 = new EditStoryDescriptorBuilder().withTags("urgent").build();
        assertParseSuccess(parser, firstIndex.getOneBased() + " t/urgent",
                new EditStoryCommand(firstIndex, desc1));

        // Pair 2: Index 1, Name single-char, Tag multiple
        EditStoryDescriptor desc2 = new EditStoryDescriptorBuilder()
                .withName("A").withTags("urgent", "breaking").build();
        assertParseSuccess(parser, firstIndex.getOneBased() + " n/A t/urgent t/breaking",
                new EditStoryCommand(firstIndex, desc2));

        // Pair 3: Index 1, Name typical, Tag reset
        EditStoryDescriptor desc3 = new EditStoryDescriptorBuilder()
                .withName(VALID_STORYNAME_CRIME).withTags().build();
        assertParseSuccess(parser, firstIndex.getOneBased() + NAME_DESC_CRIME + TAG_EMPTY,
                new EditStoryCommand(firstIndex, desc3));

        // Pair 4: Index 2, Name complex, Tag omitted
        EditStoryDescriptor desc4 = new EditStoryDescriptorBuilder()
                .withName("Election 2026!").build();
        assertParseSuccess(parser, secondIndex.getOneBased() + " n/Election 2026!",
                new EditStoryCommand(secondIndex, desc4));

        // Pair 5: Index 2, Name omitted, Tag multiple
        EditStoryDescriptor desc5 = new EditStoryDescriptorBuilder()
                .withTags("urgent", "breaking").build();
        assertParseSuccess(parser, secondIndex.getOneBased() + " t/urgent t/breaking",
                new EditStoryCommand(secondIndex, desc5));

        // Pair 6: Index 2, Name single-char, Tag reset
        EditStoryDescriptor desc6 = new EditStoryDescriptorBuilder()
                .withName("A").withTags().build();
        assertParseSuccess(parser, secondIndex.getOneBased() + " n/A" + TAG_EMPTY,
                new EditStoryCommand(secondIndex, desc6));

        // Pair 7: Index 2, Name typical, Tag single (reversed prefix order: tag then name)
        EditStoryDescriptor desc7 = new EditStoryDescriptorBuilder()
                .withName(VALID_STORYNAME_CRIME).withTags("urgent").build();
        assertParseSuccess(parser, secondIndex.getOneBased() + " t/urgent" + NAME_DESC_CRIME,
                new EditStoryCommand(secondIndex, desc7));

        // Pair 8: Boundary max integer Index, Name complex, Tag single
        EditStoryDescriptor desc8 = new EditStoryDescriptorBuilder()
                .withName("Budget 2026: Politics & Tech!").withTags("breaking").build();
        assertParseSuccess(parser, Integer.MAX_VALUE + " n/Budget 2026: Politics & Tech! t/breaking",
                new EditStoryCommand(maxIndex, desc8));

        // Pair 9: Boundary max integer Index, Name omitted, Tag reset
        EditStoryDescriptor desc9 = new EditStoryDescriptorBuilder().withTags().build();
        assertParseSuccess(parser, Integer.MAX_VALUE + TAG_EMPTY,
                new EditStoryCommand(maxIndex, desc9));

        // Pair 10: Boundary max integer Index, Name typical, Tag multiple
        EditStoryDescriptor desc10 = new EditStoryDescriptorBuilder()
                .withName(VALID_STORYNAME_CRIME).withTags("urgent", "breaking").build();
        assertParseSuccess(parser, Integer.MAX_VALUE + NAME_DESC_CRIME + " t/urgent t/breaking",
                new EditStoryCommand(maxIndex, desc10));

        // Pair 11: Boundary max integer Index, Name single-char, Tag omitted
        EditStoryDescriptor desc11 = new EditStoryDescriptorBuilder().withName("A").build();
        assertParseSuccess(parser, Integer.MAX_VALUE + " n/A",
                new EditStoryCommand(maxIndex, desc11));
    }

    @Test
    public void parse_boundaryIndex_success() {
        Index boundaryIndex = Index.fromOneBased(Integer.MAX_VALUE);
        String userInput = Integer.MAX_VALUE + NAME_DESC_CRIME;
        EditStoryDescriptor descriptor = new EditStoryDescriptorBuilder().withName(VALID_STORYNAME_CRIME).build();
        EditStoryCommand expectedCommand = new EditStoryCommand(boundaryIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}

