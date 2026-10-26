package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditFollowupCommand;
import seedu.address.logic.commands.EditFollowupCommand.EditFollowupDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates a new EditFollowupCommand object. */
public class EditFollowupCommandParser implements Parser<EditFollowupCommand> {

    @Override
    public EditFollowupCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DESCRIPTION, PREFIX_CONTACTINDEX);
        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditFollowupCommand.MESSAGE_USAGE), pe);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DESCRIPTION, PREFIX_CONTACTINDEX);

        EditFollowupDescriptor descriptor = new EditFollowupDescriptor();
        if (argMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
            descriptor.setDescription(
                    ParserUtil.parseFollowupDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get()));
        }
        if (argMultimap.getValue(PREFIX_CONTACTINDEX).isPresent()) {
            descriptor.setContactIndex(ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CONTACTINDEX).get()));
        }
        if (!descriptor.isAnyFieldEdited()) {
            throw new ParseException(EditFollowupCommand.MESSAGE_NOT_EDITED);
        }
        return new EditFollowupCommand(index, descriptor);
    }
}
