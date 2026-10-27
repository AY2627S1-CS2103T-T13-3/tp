package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.logic.commands.FindContactsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.contact.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindContactsCommand object
 */
public class FindContactsCommandParser implements Parser<FindContactsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindContactsCommand
     * and returns a FindContactsCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FindContactsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindContactsCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindContactsCommand(new NameContainsKeywordsPredicate(List.of(nameKeywords)));
    }

}
