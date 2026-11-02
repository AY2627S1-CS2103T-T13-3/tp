package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddStoryCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.model.tag.Tag;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        if (!argMultimap.hasPreamble()) {
            throw new ParseException(Messages.getErrorMessageForMissingRequiredInputs("story name"));
        }

        StoryName storyName = ParserUtil.parseStoryName(argMultimap.getPreamble());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        Story story = new Story(storyName, tagList);

        return new AddStoryCommand(story);
    }
}
