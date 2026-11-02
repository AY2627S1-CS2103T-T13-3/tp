package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.logic.commands.FindStoriesCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.story.StoryNameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new {@code FindStoriesCommand}.
 */
public class FindStoryCommandParser implements Parser<FindStoriesCommand> {

    /**
     * Parses the given arguments in the context of {@code FindStoriesCommand}.
     *
     * @throws ParseException if no story name keywords are supplied
     */
    public FindStoriesCommand parse(String args) throws ParseException {
        ArgumentMultimap argMap = ArgumentTokenizer.tokenize(args);
        String trimmedArgs = argMap.getPreamble();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindStoriesCommand.MESSAGE_USAGE));
        }

        String[] storyNameKeywords = trimmedArgs.split("\\s+");
        return new FindStoriesCommand(new StoryNameContainsKeywordsPredicate(List.of(storyNameKeywords)));
    }
}
