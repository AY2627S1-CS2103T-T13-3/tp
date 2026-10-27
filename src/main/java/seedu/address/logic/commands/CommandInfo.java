package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Encapsulates the display metadata for a command.
 */
public class CommandInfo {

    private final String commandWord;
    private final String format;
    private final String description;

    /**
     * Constructs a {@code CommandInfo}.
     *
     * @param commandWord The primary word used to invoke the command.
     * @param format The syntax format for running the command.
     * @param description A brief summary of what the command does.
     */
    public CommandInfo(String commandWord, String format, String description) {
        requireNonNull(commandWord);
        requireNonNull(format);
        requireNonNull(description);
        this.commandWord = commandWord;
        this.format = format;
        this.description = description;
    }

    public String getCommandWord() {
        return commandWord;
    }

    public String getFormat() {
        return format;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CommandInfo otherInfo)) {
            return false;
        }
        return commandWord.equals(otherInfo.commandWord)
                && format.equals(otherInfo.format)
                && description.equals(otherInfo.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandWord, format, description);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("commandWord", commandWord)
                .add("format", format)
                .add("description", description)
                .toString();
    }
}
