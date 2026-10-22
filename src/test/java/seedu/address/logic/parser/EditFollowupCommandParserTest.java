package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.CONTACT_INDEX_DESC_ONE;
import static seedu.address.logic.commands.CommandTestUtil.CONTACT_INDEX_DESC_TWO;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_INTERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_NOTES;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_CONTACT_INDEX_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CONTACT_INDEX_ONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CONTACT_INDEX_TWO;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_INTERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_FOLLOWUP;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_FOLLOWUP;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_FOLLOWUP;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditFollowupCommand;
import seedu.address.logic.commands.EditFollowupCommand.EditFollowupDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.followup.FollowupDescription;
import seedu.address.testutil.EditFollowupDescriptorBuilder;

public class EditFollowupCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditFollowupCommand.MESSAGE_USAGE);

    private EditFollowupCommandParser parser = new EditFollowupCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_DESCRIPTION_INTERVIEW, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditFollowupCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + DESCRIPTION_DESC_INTERVIEW, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + DESCRIPTION_DESC_INTERVIEW, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertThrows(ParseException.class, () -> parser.parse("1 i/ string"));
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_DESCRIPTION_DESC,
                FollowupDescription.MESSAGE_CONSTRAINTS); // invalid description
        assertParseFailure(parser, "1" + INVALID_CONTACT_INDEX_DESC,
                MESSAGE_INVALID_INDEX); // invalid contact index

        // invalid description followed by valid contact index
        assertParseFailure(parser, "1" + INVALID_DESCRIPTION_DESC + CONTACT_INDEX_DESC_ONE,
                FollowupDescription.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_DESCRIPTION_DESC + INVALID_CONTACT_INDEX_DESC,
                FollowupDescription.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_FOLLOWUP;
        String userInput = targetIndex.getOneBased() + CONTACT_INDEX_DESC_TWO + DESCRIPTION_DESC_INTERVIEW;

        EditFollowupDescriptor descriptor = new EditFollowupDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_INTERVIEW)
                .withContactIndex(VALID_CONTACT_INDEX_TWO).build();
        EditFollowupCommand expectedCommand = new EditFollowupCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // description
        Index targetIndex = INDEX_THIRD_FOLLOWUP;
        String userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_NOTES;
        EditFollowupDescriptor descriptor = new EditFollowupDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_NOTES).build();
        EditFollowupCommand expectedCommand = new EditFollowupCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // contact index
        userInput = targetIndex.getOneBased() + CONTACT_INDEX_DESC_ONE;
        descriptor = new EditFollowupDescriptorBuilder()
                .withContactIndex(VALID_CONTACT_INDEX_ONE).build();
        expectedCommand = new EditFollowupCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_FOLLOWUP;
        String userInput = targetIndex.getOneBased() + INVALID_DESCRIPTION_DESC + DESCRIPTION_DESC_NOTES;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_NOTES + INVALID_DESCRIPTION_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION));

        // multiple valid fields repeated
        userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_INTERVIEW + CONTACT_INDEX_DESC_ONE
                + DESCRIPTION_DESC_NOTES + CONTACT_INDEX_DESC_TWO;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION, PREFIX_CONTACTINDEX));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_DESCRIPTION_DESC + INVALID_CONTACT_INDEX_DESC
                + INVALID_DESCRIPTION_DESC + INVALID_CONTACT_INDEX_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION, PREFIX_CONTACTINDEX));
    }
}
