package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STORYINDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AssignCommand;

public class AssignCommandParserTest {

    private static final Index CONTACT_INDEX = Index.fromOneBased(1);
    private static final Index STORY_INDEX = Index.fromOneBased(2);

    private final AssignCommandParser parser = new AssignCommandParser();

    @Test
    public void parse_allFieldsPresent_returnsAssignCommand() {
        String userInput = " " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased()
                + " " + PREFIX_STORYINDEX + STORY_INDEX.getOneBased();

        assertParseSuccess(parser, userInput, new AssignCommand(STORY_INDEX, CONTACT_INDEX));
    }

    @Test
    public void parse_prefixesInReverseOrder_returnsAssignCommand() {
        String userInput = " " + PREFIX_STORYINDEX + STORY_INDEX.getOneBased()
                + " " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased();

        assertParseSuccess(parser, userInput, new AssignCommand(STORY_INDEX, CONTACT_INDEX));
    }

    @Test
    public void parse_missingRequiredPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE);

        assertParseFailure(parser, " " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased(), expectedMessage);
        assertParseFailure(parser, " " + PREFIX_STORYINDEX + STORY_INDEX.getOneBased(), expectedMessage);
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        String userInput = "unexpected " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased()
                + " " + PREFIX_STORYINDEX + STORY_INDEX.getOneBased();

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicatePrefix_throwsParseException() {
        String duplicateContactIndex = " " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased()
                + " " + PREFIX_CONTACTINDEX + STORY_INDEX.getOneBased()
                + " " + PREFIX_STORYINDEX + STORY_INDEX.getOneBased();
        String duplicateStoryIndex = " " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased()
                + " " + PREFIX_STORYINDEX + STORY_INDEX.getOneBased()
                + " " + PREFIX_STORYINDEX + CONTACT_INDEX.getOneBased();

        assertParseFailure(parser, duplicateContactIndex,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CONTACTINDEX));
        assertParseFailure(parser, duplicateStoryIndex,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STORYINDEX));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        String invalidContactZero = " " + PREFIX_CONTACTINDEX + "0 "
                + PREFIX_STORYINDEX + STORY_INDEX.getOneBased();
        String invalidStoryZero = " " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased()
                + " " + PREFIX_STORYINDEX + "0";
        String invalidContactNegative = " " + PREFIX_CONTACTINDEX + "-1 "
                + PREFIX_STORYINDEX + STORY_INDEX.getOneBased();
        String invalidStoryNegative = " " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased()
                + " " + PREFIX_STORYINDEX + "-1";
        String invalidContactOverflow = " " + PREFIX_CONTACTINDEX + "2147483648 "
                + PREFIX_STORYINDEX + STORY_INDEX.getOneBased();
        String invalidStoryOverflow = " " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased()
                + " " + PREFIX_STORYINDEX + "2147483648";
        String invalidStoryString = " " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased()
                + " " + PREFIX_STORYINDEX + "notAnIndex";

        assertParseFailure(parser, invalidContactZero, ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, invalidStoryZero, ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, invalidContactNegative, ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, invalidStoryNegative, ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, invalidContactOverflow, ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, invalidStoryOverflow, ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, invalidStoryString, ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_boundaryMaxIntegerIndices_success() {
        String maxIndex = Integer.toString(Integer.MAX_VALUE);
        String userInput = " " + PREFIX_CONTACTINDEX + maxIndex + " " + PREFIX_STORYINDEX + maxIndex;
        Index expectedIndex = Index.fromOneBased(Integer.MAX_VALUE);

        assertParseSuccess(parser, userInput, new AssignCommand(expectedIndex, expectedIndex));
    }
}
