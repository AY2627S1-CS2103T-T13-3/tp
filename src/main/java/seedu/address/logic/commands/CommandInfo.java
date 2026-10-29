package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Encapsulates the display metadata and parser for a command.
 */
public class CommandInfo {

    private final String commandWord;
    private final String format;
    private final String description;
    private final boolean isAssignable;
    private final Parser<? extends Command> parser;

    /**
     * Constructs a {@code CommandInfo}.
     *
     * @param commandWord The primary word used to invoke the command.
     * @param format The syntax format for running the command.
     * @param description A brief summary of what the command does.
     * @param isAssignable Whether a shortcut can be assigned to this command.
     * @param parser The parser to parse arguments for this command.
     */
    public CommandInfo(String commandWord, String format, String description,
                       boolean isAssignable, Parser<? extends Command> parser) {
        requireNonNull(commandWord);
        requireNonNull(format);
        requireNonNull(description);
        this.commandWord = commandWord;
        this.format = format;
        this.description = description;
        this.isAssignable = isAssignable;
        this.parser = parser;
    }

    /**
     * Constructs a shortcut-assignable {@code CommandInfo}.
     *
     * @param commandWord The primary word used to invoke the command.
     * @param format The syntax format for running the command.
     * @param description A brief summary of what the command does.
     * @param parser The parser to parse arguments for this command.
     */
    public CommandInfo(String commandWord, String format, String description,
                       Parser<? extends Command> parser) {
        this(commandWord, format, description, true, parser);
    }

    /**
     * Constructs a display-only {@code CommandInfo} without a parser.
     *
     * @param commandWord The primary word used to invoke the command.
     * @param format The syntax format for running the command.
     * @param description A brief summary of what the command does.
     */
    public CommandInfo(String commandWord, String format, String description) {
        this(commandWord, format, description, true, null);
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

    public boolean isAssignable() {
        return isAssignable;
    }

    public Parser<? extends Command> getParser() {
        return parser;
    }

    /**
     * Parses the command arguments using this command's parser.
     *
     * @param arguments Arguments string passed to the command.
     * @return The resulting {@code Command}.
     * @throws ParseException if parsing fails.
     */
    public Command parse(String arguments) throws ParseException {
        requireNonNull(parser, "Parser has not been configured for command: " + commandWord);
        return parser.parse(arguments);
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
                && description.equals(otherInfo.description)
                && isAssignable == otherInfo.isAssignable;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandWord, format, description, isAssignable);
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
