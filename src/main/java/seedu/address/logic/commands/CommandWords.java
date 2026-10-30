package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import seedu.address.logic.parser.AddContactCommandParser;
import seedu.address.logic.parser.AddFollowupCommandParser;
import seedu.address.logic.parser.AddStoryCommandParser;
import seedu.address.logic.parser.AssignCommandParser;
import seedu.address.logic.parser.DeleteContactCommandParser;
import seedu.address.logic.parser.DeleteFollowupCommandParser;
import seedu.address.logic.parser.DeleteStoryCommandParser;
import seedu.address.logic.parser.EditContactCommandParser;
import seedu.address.logic.parser.EditFollowupCommandParser;
import seedu.address.logic.parser.EditStoryCommandParser;
import seedu.address.logic.parser.FindContactsCommandParser;
import seedu.address.logic.parser.FindFollowupsCommandParser;
import seedu.address.logic.parser.FindStoryCommandParser;
import seedu.address.logic.parser.ListContactsCommandParser;
import seedu.address.logic.parser.ListFollowupsCommandParser;
import seedu.address.logic.parser.ListStoriesCommandParser;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ShortcutCommandParser;
import seedu.address.logic.parser.UnassignCommandParser;
import seedu.address.logic.parser.UnshortcutCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Contains the command words recognised by the application.
 */
public final class CommandWords {

    private static final List<CommandInfo> COMMAND_INFOS = List.of(
            new CommandInfo(AddContactCommand.COMMAND_WORD,
                    "addcontact n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]...",
                    "Adds a contact to PressMemo.",
                    new AddContactCommandParser()),
            new CommandInfo(EditContactCommand.COMMAND_WORD,
                    "editcontact INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]...",
                    "Edits the details of an existing contact.",
                    new EditContactCommandParser()),
            new CommandInfo(DeleteContactCommand.COMMAND_WORD,
                    "deletecontact INDEX",
                    "Deletes the contact at the specified index.",
                    new DeleteContactCommandParser()),
            new CommandInfo(FindContactsCommand.COMMAND_WORD,
                    "findcontacts KEYWORD [MORE_KEYWORDS]...",
                    "Finds contacts whose names contain any of the keywords.",
                    new FindContactsCommandParser()),
            new CommandInfo(ListContactsCommand.COMMAND_WORD,
                    "listcontacts [si/STORY_INDEX]",
                    "Lists all contacts, or contacts assigned to the specified story.",
                    new ListContactsCommandParser()),
            new CommandInfo(AddStoryCommand.COMMAND_WORD,
                    "addstory NAME [t/TAG]...",
                    "Adds a story to PressMemo.",
                    new AddStoryCommandParser()),
            new CommandInfo(EditStoryCommand.COMMAND_WORD,
                    "editstory INDEX [n/NAME] [t/TAG]...",
                    "Edits the details of an existing story.",
                    new EditStoryCommandParser()),
            new CommandInfo(DeleteStoryCommand.COMMAND_WORD,
                    "deletestory INDEX",
                    "Deletes the story at the specified index.",
                    new DeleteStoryCommandParser()),
            new CommandInfo(FindStoriesCommand.COMMAND_WORD,
                    "findstories STORY_NAME [MORE_KEYWORDS]...",
                    "Finds all stories whose names contain any of the keywords.",
                    new FindStoryCommandParser()),
            new CommandInfo(ListStoriesCommand.COMMAND_WORD,
                    "liststories [ci/CONTACT_INDEX]",
                    "Lists all stories, or stories assigned to the specified contact.",
                    new ListStoriesCommandParser()),
            new CommandInfo(AssignCommand.COMMAND_WORD,
                    "assign ci/CONTACT_INDEX si/STORY_INDEX",
                    "Assigns a contact to a story.",
                    new AssignCommandParser()),
            new CommandInfo(UnassignCommand.COMMAND_WORD,
                    "unassign ci/CONTACT_INDEX si/STORY_INDEX",
                    "Unassigns a contact from a story.",
                    new UnassignCommandParser()),
            new CommandInfo(AddFollowupCommand.COMMAND_WORD,
                    "addfollowup DESCRIPTION ci/CONTACT_INDEX",
                    "Adds a follow-up for the specified contact.",
                    new AddFollowupCommandParser()),
            new CommandInfo(EditFollowupCommand.COMMAND_WORD,
                    "editfollowup INDEX [d/DESCRIPTION] [ci/CONTACT_INDEX]",
                    "Edits the details of an existing follow-up.",
                    new EditFollowupCommandParser()),
            new CommandInfo(DeleteFollowupCommand.COMMAND_WORD,
                    "deletefollowup INDEX",
                    "Deletes the follow-up at the specified index.",
                    new DeleteFollowupCommandParser()),
            new CommandInfo(FindFollowupsCommand.COMMAND_WORD,
                    "findfollowups KEYWORD [MORE_KEYWORDS]...",
                    "Finds follow-ups whose descriptions contain any keywords.",
                    new FindFollowupsCommandParser()),
            new CommandInfo(ListFollowupsCommand.COMMAND_WORD,
                    "listfollowups [ci/CONTACT_INDEX]",
                    "Lists all follow-ups, or follow-ups for the specified contact.",
                    new ListFollowupsCommandParser()),
            new CommandInfo(ShortcutCommand.COMMAND_WORD,
                    "shortcut [s/SHORTCUT c/COMMAND_WORD]",
                    "Assigns a shortcut, or lists all shortcuts if no parameters.",
                    false,
                    new ShortcutCommandParser()),
            new CommandInfo(UnshortcutCommand.COMMAND_WORD,
                    "unshortcut SHORTCUT",
                    "Removes a custom shortcut.",
                    false,
                    new UnshortcutCommandParser()),
            new CommandInfo(ListCommand.COMMAND_WORD,
                    "list",
                    "Lists all contacts, stories and followups.",
                    noArgs(ListCommand::new, ListCommand.MESSAGE_USAGE)),
            new CommandInfo(ClearCommand.COMMAND_WORD,
                    "clear",
                    "Clears all entries from PressMemo.",
                    noArgs(ClearCommand::new, ClearCommand.MESSAGE_USAGE)),
            new CommandInfo(HelpCommand.COMMAND_WORD,
                    "help",
                    "Toggles the help panel with commands and syntax.",
                    noArgs(HelpCommand::new, HelpCommand.MESSAGE_USAGE)),
            new CommandInfo(ExitCommand.COMMAND_WORD,
                    "exit",
                    "Exits the application.",
                    noArgs(ExitCommand::new, ExitCommand.MESSAGE_USAGE)));

    private static final Map<String, CommandInfo> COMMAND_INFO_MAP = COMMAND_INFOS.stream()
            .collect(Collectors.toUnmodifiableMap(CommandInfo::getCommandWord, Function.identity()));

    private CommandWords() {}

    private static <T extends Command> Parser<T> noArgs(Supplier<T> supplier, String messageUsage) {
        return arguments -> {
            requireNonNull(arguments);
            if (!arguments.trim().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, messageUsage));
            }
            return supplier.get();
        };
    }

    /** Returns whether {@code commandWord} can be assigned a shortcut. */
    public static boolean isAssignableCommandWord(String commandWord) {
        CommandInfo info = COMMAND_INFO_MAP.get(commandWord);
        return info != null && info.isAssignable();
    }

    /** Returns whether {@code commandWord} is reserved by any built-in command. */
    public static boolean isCommandWord(String commandWord) {
        return COMMAND_INFO_MAP.containsKey(commandWord);
    }

    /** Returns the {@code CommandInfo} matching {@code commandWord}, or null if none exists. */
    public static CommandInfo getCommandInfo(String commandWord) {
        return COMMAND_INFO_MAP.get(commandWord);
    }

    /** Returns an unmodifiable list of all command metadata. */
    public static List<CommandInfo> getCommandInfos() {
        return COMMAND_INFOS;
    }
}
