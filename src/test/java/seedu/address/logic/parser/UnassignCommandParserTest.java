package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STORYINDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.UnassignCommand;

public class UnassignCommandParserTest {

    private static final Index CONTACT_INDEX = Index.fromOneBased(1);
    private static final Index STORY_INDEX = Index.fromOneBased(2);

    private final UnassignCommandParser parser = new UnassignCommandParser();

    @Test
    public void parse_allFieldsPresent_returnsUnassignCommand() {
        String userInput = " " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased()
                + " " + PREFIX_STORYINDEX + STORY_INDEX.getOneBased();

        assertParseSuccess(parser, userInput, new UnassignCommand(STORY_INDEX, CONTACT_INDEX));
    }

    @Test
    public void parse_prefixesInReverseOrder_returnsUnassignCommand() {
        String userInput = " " + PREFIX_STORYINDEX + STORY_INDEX.getOneBased()
                + " " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased();

        assertParseSuccess(parser, userInput, new UnassignCommand(STORY_INDEX, CONTACT_INDEX));
    }

    @Test
    public void parse_missingRequiredPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignCommand.MESSAGE_USAGE);

        assertParseFailure(parser, " " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased(), expectedMessage);
        assertParseFailure(parser, " " + PREFIX_STORYINDEX + STORY_INDEX.getOneBased(), expectedMessage);
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        String userInput = "unexpected " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased()
                + " " + PREFIX_STORYINDEX + STORY_INDEX.getOneBased();

        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnassignCommand.MESSAGE_USAGE));
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
        String invalidContactIndex = " " + PREFIX_CONTACTINDEX + "0 "
                + PREFIX_STORYINDEX + STORY_INDEX.getOneBased();
        String invalidStoryIndex = " " + PREFIX_CONTACTINDEX + CONTACT_INDEX.getOneBased()
                + " " + PREFIX_STORYINDEX + "notAnIndex";

        assertParseFailure(parser, invalidContactIndex, ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, invalidStoryIndex, ParserUtil.MESSAGE_INVALID_INDEX);
    }
}
