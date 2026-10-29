package seedu.address.logic.parser;

import seedu.address.logic.commands.UnassignCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnassignCommand object
 */
public class UnassignCommandParser implements Parser<UnassignCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnassignCommand
     * and returns an UnassignCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public UnassignCommand parse(String args) throws ParseException {
        AssignmentIndices indices = AssignmentParserUtil.parseIndices(args, UnassignCommand.MESSAGE_USAGE);
        return new UnassignCommand(indices.getStoryIndex(), indices.getContactIndex());
    }

}
