package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.logic.commands.FindStoryCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.story.StoryNameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new {@code FindStoryCommand}.
 */
public class FindStoryCommandParser implements Parser<FindStoryCommand> {

    /**
     * Parses the given arguments in the context of {@code FindStoryCommand}.
     *
     * @throws ParseException if no story name keywords are supplied
     */
    public FindStoryCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindStoryCommand.MESSAGE_USAGE));
        }

        String[] storyNameKeywords = trimmedArgs.split("\\s+");
        return new FindStoryCommand(new StoryNameContainsKeywordsPredicate(List.of(storyNameKeywords)));
    }
}
