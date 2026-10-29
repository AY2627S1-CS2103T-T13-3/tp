package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SHORTCUT;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Assigns a user-defined shortcut to a command, or lists all shortcuts.
 */
public class ShortcutCommand extends Command {

    public static final String COMMAND_WORD = "shortcut";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns a shortcut to a command, or lists all "
            + "shortcuts when used without parameters. "
            + "Parameters: " + PREFIX_SHORTCUT + "SHORTCUT " + PREFIX_COMMAND + "COMMAND_WORD "
            + "Example: " + COMMAND_WORD + " " + PREFIX_SHORTCUT + "ls " + PREFIX_COMMAND + "list";
    public static final String MESSAGE_SUCCESS = "Shortcut '%s' assigned to command '%s'.";
    public static final String MESSAGE_NO_SHORTCUTS = "No command shortcuts assigned.";
    public static final String MESSAGE_SHORTCUTS_HEADER = "Command shortcuts:\n";
    public static final String MESSAGE_SHORTCUT_REPLACED =
            "Warning: Existing shortcut '%s' for command '%s' was removed.";
    public static final String MESSAGE_INVALID_SHORTCUT = "Shortcut '%s' is invalid. Shortcuts must start with a "
            + "lowercase letter and contain only lowercase letters and digits.";
    public static final String MESSAGE_RESERVED_SHORTCUT = "'%s' is a built-in command word and cannot be used as "
            + "a shortcut.";
    public static final String MESSAGE_UNKNOWN_COMMAND = "'%s' is not a command that can be assigned a shortcut.";

    private final String shortcut;
    private final String commandWord;

    /** Creates a command that lists all shortcuts. */
    public ShortcutCommand() {
        shortcut = null;
        commandWord = null;
    }

    /** Creates a command that assigns {@code shortcut} to {@code commandWord}. */
    public ShortcutCommand(String shortcut, String commandWord) {
        this.shortcut = requireNonNull(shortcut);
        this.commandWord = requireNonNull(commandWord);
    }

    @Override
    public boolean modifiesUserPrefs() {
        return shortcut != null;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (shortcut == null) {
            return new CommandResult(formatShortcuts(model.getShortcuts()));
        }

        validateAssignment();

        String existingShortcut = model.getShortcuts().entrySet().stream()
                .filter(entry -> entry.getValue().equals(commandWord)
                        && !entry.getKey().equals(shortcut))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        model.assignShortcut(shortcut, commandWord);

        String message = String.format(MESSAGE_SUCCESS, shortcut, commandWord);
        if (existingShortcut != null) {
            message += "\n" + String.format(MESSAGE_SHORTCUT_REPLACED,
                    existingShortcut, commandWord);
        }

        return new CommandResult(message);
    }

    private void validateAssignment() throws CommandException {
        if (!shortcut.matches("[a-z][a-z0-9]*")) {
            throw new CommandException(String.format(MESSAGE_INVALID_SHORTCUT, shortcut));
        }
        if (CommandWords.isCommandWord(shortcut)) {
            throw new CommandException(String.format(MESSAGE_RESERVED_SHORTCUT, shortcut));
        }
        if (!CommandWords.isAssignableCommandWord(commandWord)) {
            throw new CommandException(String.format(MESSAGE_UNKNOWN_COMMAND, commandWord));
        }
    }

    private static String formatShortcuts(Map<String, String> shortcuts) {
        if (shortcuts.isEmpty()) {
            return MESSAGE_NO_SHORTCUTS;
        }
        String entries = shortcuts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + " -> " + entry.getValue())
                .collect(Collectors.joining("\n"));
        return MESSAGE_SHORTCUTS_HEADER + entries;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ShortcutCommand otherShortcutCommand)) {
            return false;
        }
        return Objects.equals(shortcut, otherShortcutCommand.shortcut)
                && Objects.equals(commandWord, otherShortcutCommand.commandWord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortcut, commandWord);
    }
}
