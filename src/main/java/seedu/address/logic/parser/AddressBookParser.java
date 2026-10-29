package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandInfo;
import seedu.address.logic.commands.CommandWords;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);
    private final Supplier<Map<String, String>> shortcutsSupplier;

    /** Creates a parser without any user-defined shortcuts. */
    public AddressBookParser() {
        this(() -> Map.of());
    }

    /** Creates a parser that obtains the latest shortcuts from {@code shortcutsSupplier}. */
    public AddressBookParser(Supplier<Map<String, String>> shortcutsSupplier) {
        this.shortcutsSupplier = requireNonNull(shortcutsSupplier);
    }

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform to the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String enteredCommandWord = matcher.group("commandWord");
        final Map<String, String> shortcuts = requireNonNull(shortcutsSupplier.get());
        final String commandWord = CommandWords.isCommandWord(enteredCommandWord)
                ? enteredCommandWord
                : shortcuts.getOrDefault(enteredCommandWord, enteredCommandWord);
        final String arguments = matcher.group("arguments");

        // Note to developers: Change LOG_LEVEL in LogsCenter to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Entered command word: " + enteredCommandWord + "; Resolved command word: " + commandWord
                + "; Arguments: " + arguments);

        CommandInfo commandInfo = CommandWords.getCommandInfo(commandWord);
        if (commandInfo == null) {
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }

        return commandInfo.parse(arguments);
    }

}
