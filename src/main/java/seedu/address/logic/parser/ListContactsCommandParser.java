package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STORYINDEX;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ListContactsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a ListContactsCommand.
 */
public class ListContactsCommandParser implements Parser<ListContactsCommand> {

    @Override
    public ListContactsCommand parse(String args) throws ParseException {
        // No arguments: list all contacts
        if (args.trim().isEmpty()) {
            return new ListContactsCommand();
        }

        // Extract the story index
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_STORYINDEX);

        // Check that the input format is valid
        if (!argMultimap.getPreamble().isEmpty()
                || argMultimap.getValue(PREFIX_STORYINDEX).isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    ListContactsCommand.MESSAGE_USAGE));
        }

        // Reject duplicate prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STORYINDEX);

        // Convert the string index into an Index object
        Index storyIndex = ParserUtil.parseIndex(
                argMultimap.getValue(PREFIX_STORYINDEX).get());

        return new ListContactsCommand(storyIndex);
    }
}
