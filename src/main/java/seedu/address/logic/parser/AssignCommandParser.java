package seedu.address.logic.parser;

import seedu.address.logic.commands.AssignCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AssignCommand object
 */
public class AssignCommandParser implements Parser<AssignCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AssignCommand
     * and returns an AssignCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public AssignCommand parse(String args) throws ParseException {
        AssignmentIndices indices = AssignmentParserUtil.parseIndices(args);
        return new AssignCommand(indices.getStoryIndex(), indices.getContactIndex());
    }

}
