package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Removes a user-defined command shortcut.
 */
public class UnshortcutCommand extends Command {

    public static final String COMMAND_WORD = "unshortcut";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes a command shortcut. "
            + "Parameters: SHORTCUT\n"
            + "Example: " + COMMAND_WORD + " ls";
    public static final String MESSAGE_SUCCESS = "Shortcut '%s' removed.";
    public static final String MESSAGE_NOT_FOUND = "Shortcut '%s' does not exist.";

    private final String shortcut;

    public UnshortcutCommand(String shortcut) {
        this.shortcut = requireNonNull(shortcut);
    }

    @Override
    public boolean modifiesUserPrefs() {
        return true;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.removeShortcut(shortcut)) {
            throw new CommandException(String.format(MESSAGE_NOT_FOUND, shortcut));
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, shortcut));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UnshortcutCommand otherUnshortcutCommand)) {
            return false;
        }
        return shortcut.equals(otherUnshortcutCommand.shortcut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortcut);
    }
}
