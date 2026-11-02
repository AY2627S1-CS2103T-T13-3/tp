package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STORYINDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Contains shared parsing logic for commands that assign or unassign a contact and story.
 */
final class AssignmentParserUtil {

    private AssignmentParserUtil() {}

    /**
     * Parses the contact and story indices from {@code args}.
     *
     */
    static AssignmentIndices parseIndices(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CONTACTINDEX, PREFIX_STORYINDEX);

        if (argMultimap.hasPreamble()) {
            throw new ParseException(Messages.getErrorMessageForUnexpectedPreamble(argMultimap.getPreamble()));
        }

        List<String> missingInputs = argMultimap
                .getMissingPrefixes(PREFIX_CONTACTINDEX, PREFIX_STORYINDEX).stream()
                .map(Prefix::toString)
                .toList();
        if (!missingInputs.isEmpty()) {
            throw new ParseException(Messages.getErrorMessageForMissingRequiredInputs(
                    missingInputs.toArray(String[]::new)));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CONTACTINDEX, PREFIX_STORYINDEX);
        Index storyIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_STORYINDEX).get());
        Index contactIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CONTACTINDEX).get());

        return new AssignmentIndices(storyIndex, contactIndex);
    }
}
