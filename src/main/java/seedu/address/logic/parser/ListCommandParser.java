
package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STORYINDEX;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a ListCommand.
 */
public class ListCommandParser implements Parser<ListCommand> {

    @Override
    public ListCommand parse(String args) throws ParseException {

        // No arguments: list all contacts
        if (args.trim().isEmpty()) {
            return new ListCommand();
        }

        // Extract the story index
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_STORYINDEX);

        // Check that the input format is valid
        if (!argMultimap.getPreamble().isEmpty()
                || argMultimap.getValue(PREFIX_STORYINDEX).isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    "list [si/STORY_INDEX]"));
        }

        // Reject duplicate prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STORYINDEX);

        // Convert the string index into an Index object
        Index storyIndex = ParserUtil.parseIndex(
                argMultimap.getValue(PREFIX_STORYINDEX).get());

        return new ListCommand(storyIndex);
    }
}
