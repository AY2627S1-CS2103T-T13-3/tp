package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHORTCUT;

import seedu.address.logic.commands.ShortcutCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates a {@link ShortcutCommand}. */
public class ShortcutCommandParser implements Parser<ShortcutCommand> {

    @Override
    public ShortcutCommand parse(String args) throws ParseException {
        if (args.trim().isEmpty()) {
            return new ShortcutCommand();
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SHORTCUT, PREFIX_COMMAND);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_SHORTCUT, PREFIX_COMMAND);

        String shortcut = argMultimap.getValue(PREFIX_SHORTCUT).orElse("");
        String commandWord = argMultimap.getValue(PREFIX_COMMAND).orElse("");
        if (!argMultimap.getPreamble().isEmpty() || !isSingleToken(shortcut) || !isSingleToken(commandWord)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShortcutCommand.MESSAGE_USAGE));
        }

        return new ShortcutCommand(shortcut, commandWord);
    }

    private static boolean isSingleToken(String value) {
        return !value.isBlank() && value.matches("\\S+");
    }
}
