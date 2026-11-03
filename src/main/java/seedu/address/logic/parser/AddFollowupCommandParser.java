package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddFollowupCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.followup.FollowupDescription;

/**
 * Parses input arguments and creates a new AddFollowupCommand object
 */
public class AddFollowupCommandParser implements Parser<AddFollowupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddFollowupCommand
     * and returns an AddFollowupCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public AddFollowupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CONTACTINDEX);

        List<String> missingInputs = new ArrayList<>();
        if (!argMultimap.hasPreamble()) {
            missingInputs.add("DESCRIPTION");
        }
        argMultimap.getMissingPrefixes(PREFIX_CONTACTINDEX).stream()
                .map(Prefix::toString)
                .forEach(missingInputs::add);
        if (!missingInputs.isEmpty()) {
            throw new ParseException(Messages.getErrorMessageForMissingRequiredInputs(
                    missingInputs.toArray(String[]::new)));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CONTACTINDEX);
        FollowupDescription followupDescription = ParserUtil.parseFollowupDescription(argMultimap.getPreamble());
        Index contactIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CONTACTINDEX).get());

        return new AddFollowupCommand(followupDescription, contactIndex);
    }
}

