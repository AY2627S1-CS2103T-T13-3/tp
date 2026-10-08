package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.HashSet;

import seedu.address.logic.commands.AddStoryCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;

/**
 * Parses input arguments and creates a new AddStoryCommand object
 */
public class AddStoryCommandParser implements Parser<AddStoryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddStoryCommand
     * and returns an AddStoryCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public AddStoryCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddStoryCommand.MESSAGE_USAGE));
        }

        StoryName storyName = ParserUtil.parseStoryName(trimmedArgs);
        Story story = new Story(storyName, new HashSet<>());

        return new AddStoryCommand(story);
    }
}
