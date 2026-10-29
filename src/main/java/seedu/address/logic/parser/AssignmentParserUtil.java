package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STORYINDEX;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Contains shared parsing logic for commands that assign or unassign a contact and story.
 */
final class AssignmentParserUtil {

    private AssignmentParserUtil() {}

    /**
     * Parses the contact and story indices from {@code args}.
     *
     * @param usageMessage command-specific usage message shown when the input format is invalid
     */
    static AssignmentIndices parseIndices(String args, String usageMessage) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CONTACTINDEX, PREFIX_STORYINDEX);

        if (!arePrefixesPresent(argMultimap, PREFIX_CONTACTINDEX, PREFIX_STORYINDEX)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, usageMessage));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_CONTACTINDEX, PREFIX_STORYINDEX);
        Index storyIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_STORYINDEX).get());
        Index contactIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CONTACTINDEX).get());

        return new AssignmentIndices(storyIndex, contactIndex);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
