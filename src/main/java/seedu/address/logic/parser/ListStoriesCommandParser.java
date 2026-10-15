package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ListStoriesCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a ListStoriesCommand.
 */
public class ListStoriesCommandParser implements Parser<ListStoriesCommand> {

    @Override
    public ListStoriesCommand parse(String args) throws ParseException {

        // No arguments: list all stories
        if (args.trim().isEmpty()) {
            return new ListStoriesCommand();
        }

        // Extract the contact index
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CONTACTINDEX);

        // Check that the input format is valid
        if (!argMultimap.getPreamble().isEmpty()
                || argMultimap.getValue(PREFIX_CONTACTINDEX).isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    "liststories [ci/CONTACT_INDEX]"));
        }

        // Reject duplicate prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CONTACTINDEX);

        // Convert the string index into an Index object
        Index contactIndex = ParserUtil.parseIndex(
                argMultimap.getValue(PREFIX_CONTACTINDEX).get());

        return new ListStoriesCommand(contactIndex);
    }
}
