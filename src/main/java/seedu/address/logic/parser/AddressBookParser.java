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
import seedu.address.logic.commands.AddContactCommand;
import seedu.address.logic.commands.AddFollowupCommand;
import seedu.address.logic.commands.AddStoryCommand;
import seedu.address.logic.commands.AssignCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandWords;
import seedu.address.logic.commands.DeleteContactCommand;
import seedu.address.logic.commands.DeleteFollowupCommand;
import seedu.address.logic.commands.DeleteStoryCommand;
import seedu.address.logic.commands.EditContactCommand;
import seedu.address.logic.commands.EditFollowupCommand;
import seedu.address.logic.commands.EditStoryCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindContactsCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListContactsCommand;
import seedu.address.logic.commands.ListFollowupsCommand;
import seedu.address.logic.commands.ListStoriesCommand;
import seedu.address.logic.commands.ShortcutCommand;
import seedu.address.logic.commands.UnassignCommand;
import seedu.address.logic.commands.UnshortcutCommand;
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

        return switch (commandWord) {
            case AddContactCommand.COMMAND_WORD -> new AddContactCommandParser().parse(arguments);
            case AddStoryCommand.COMMAND_WORD -> new AddStoryCommandParser().parse(arguments);
            case AddFollowupCommand.COMMAND_WORD -> new AddFollowupCommandParser().parse(arguments);
            case EditContactCommand.COMMAND_WORD -> new EditContactCommandParser().parse(arguments);
            case EditFollowupCommand.COMMAND_WORD -> new EditFollowupCommandParser().parse(arguments);
            case EditStoryCommand.COMMAND_WORD -> new EditStoryCommandParser().parse(arguments);
            case DeleteContactCommand.COMMAND_WORD -> new DeleteContactCommandParser().parse(arguments);
            case DeleteFollowupCommand.COMMAND_WORD -> new DeleteFollowupCommandParser().parse(arguments);
            case DeleteStoryCommand.COMMAND_WORD -> new DeleteStoryCommandParser().parse(arguments);
            case ClearCommand.COMMAND_WORD -> new ClearCommand();
            case FindContactsCommand.COMMAND_WORD -> new FindContactsCommandParser().parse(arguments);
            case ListCommand.COMMAND_WORD -> new ListCommand();
            case ListContactsCommand.COMMAND_WORD -> new ListContactsCommandParser().parse(arguments);
            case ListFollowupsCommand.COMMAND_WORD -> new ListFollowupsCommandParser().parse(arguments);
            case ListStoriesCommand.COMMAND_WORD -> new ListStoriesCommandParser().parse(arguments);
            case ExitCommand.COMMAND_WORD -> new ExitCommand();
            case HelpCommand.COMMAND_WORD -> new HelpCommand();
            case AssignCommand.COMMAND_WORD -> new AssignCommandParser().parse(arguments);
            case UnassignCommand.COMMAND_WORD -> new UnassignCommandParser().parse(arguments);
            case ShortcutCommand.COMMAND_WORD -> new ShortcutCommandParser().parse(arguments);
            case UnshortcutCommand.COMMAND_WORD -> new UnshortcutCommandParser().parse(arguments);
            default -> {
                logger.finer("This user input caused a ParseException: " + userInput);
                throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
            }
        };
    }

}
