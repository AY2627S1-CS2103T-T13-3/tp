package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.UnshortcutCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates an {@link UnshortcutCommand}. */
public class UnshortcutCommandParser implements Parser<UnshortcutCommand> {

    @Override
    public UnshortcutCommand parse(String args) throws ParseException {
        String shortcut = args.trim();
        if (shortcut.isEmpty() || !shortcut.matches("\\S+")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnshortcutCommand.MESSAGE_USAGE));
        }
        return new UnshortcutCommand(shortcut);
    }
}
