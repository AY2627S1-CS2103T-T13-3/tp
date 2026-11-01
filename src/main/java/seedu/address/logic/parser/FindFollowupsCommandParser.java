package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.logic.commands.FindFollowupsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.followup.FollowupDescriptionContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindFollowupsCommand.
 */
public class FindFollowupsCommandParser implements Parser<FindFollowupsCommand> {

    @Override
    public FindFollowupsCommand parse(String args) throws ParseException {
        ArgumentMultimap argMap = ArgumentTokenizer.tokenize(args);
        String trimmedArgs = argMap.getPreamble();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            FindFollowupsCommand.MESSAGE_USAGE));
        }

        String[] descriptionKeywords = trimmedArgs.split("\\s+");

        return new FindFollowupsCommand(
                new FollowupDescriptionContainsKeywordsPredicate(
                        List.of(descriptionKeywords)));
    }
}
