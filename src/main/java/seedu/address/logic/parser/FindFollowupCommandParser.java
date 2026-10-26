package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.logic.commands.FindFollowupCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.followup.FollowupDescriptionContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindFollowupCommand.
 */
public class FindFollowupCommandParser implements Parser<FindFollowupCommand> {

    @Override
    public FindFollowupCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            FindFollowupCommand.MESSAGE_USAGE));
        }

        String[] descriptionKeywords = trimmedArgs.split("\\s+");

        return new FindFollowupCommand(
                new FollowupDescriptionContainsKeywordsPredicate(
                        List.of(descriptionKeywords)));
    }
}
