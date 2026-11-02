package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteStoryCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteStoryCommand object
 */
public class DeleteStoryCommandParser implements Parser<DeleteStoryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteStoryCommand
     * and returns a DeleteStoryCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public DeleteStoryCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteStoryCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(pe.getMessage() + "\n" + DeleteStoryCommand.MESSAGE_USAGE, pe);
        }
    }

}
