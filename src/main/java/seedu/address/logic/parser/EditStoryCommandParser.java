package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditStoryCommand;
import seedu.address.logic.commands.EditStoryCommand.EditStoryDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditStoryCommand object
 */
public class EditStoryCommandParser implements Parser<EditStoryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditStoryCommand
     * and returns an EditStoryCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public EditStoryCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditStoryCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME);

        EditStoryDescriptor editStoryDescriptor = new EditStoryDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editStoryDescriptor.setName(ParserUtil.parseStoryName(argMultimap.getValue(PREFIX_NAME).get()));
        }

        ParserUtil.parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editStoryDescriptor::setTags);

        if (!editStoryDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditStoryCommand.MESSAGE_NOT_EDITED);
        }

        return new EditStoryCommand(index, editStoryDescriptor);
    }
}
