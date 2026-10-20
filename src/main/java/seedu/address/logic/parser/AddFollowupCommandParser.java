package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddFollowupCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.followup.FollowupDescription;

/**
 * Parses input arguments and creates a new AddFollowupCommand object
 */
public class AddFollowupCommandParser implements Parser<AddFollowupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddFollowupCommand
     * and returns an AddFollowupCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public AddFollowupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CONTACTINDEX);

        if (argMultimap.getValue(PREFIX_CONTACTINDEX).isEmpty()
                || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddFollowupCommand.MESSAGE_USAGE));
        }

        FollowupDescription followupDescription = ParserUtil.parseFollowupDescription(argMultimap.getPreamble());
        Index contactIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CONTACTINDEX).get());

        return new AddFollowupCommand(followupDescription, contactIndex);
    }
}

