package seedu.address.logic.commands;

import java.util.List;
import java.util.Set;

/**
 * Contains the command words recognised by the application.
 */
public final class CommandWords {

    private static final Set<String> ASSIGNABLE_COMMAND_WORDS = Set.of(
            AddContactCommand.COMMAND_WORD,
            AddFollowupCommand.COMMAND_WORD,
            AddStoryCommand.COMMAND_WORD,
            AssignCommand.COMMAND_WORD,
            ClearCommand.COMMAND_WORD,
            DeleteContactCommand.COMMAND_WORD,
            DeleteFollowupCommand.COMMAND_WORD,
            DeleteStoryCommand.COMMAND_WORD,
            EditContactCommand.COMMAND_WORD,
            EditStoryCommand.COMMAND_WORD,
            EditFollowupComman.COMMAND_WORD,
            ExitCommand.COMMAND_WORD,
            FindContactsCommand.COMMAND_WORD,
            HelpCommand.COMMAND_WORD,
            ListCommand.COMMAND_WORD,
            ListContactsCommand.COMMAND_WORD,
            ListStoriesCommand.COMMAND_WORD,
            UnassignCommand.COMMAND_WORD);

    private static final Set<String> MANAGEMENT_COMMAND_WORDS = Set.of(
            ShortcutCommand.COMMAND_WORD,
            UnshortcutCommand.COMMAND_WORD);

    private static final List<CommandInfo> COMMAND_INFOS = List.of(
            new CommandInfo(AddContactCommand.COMMAND_WORD,
                    "addcontact n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]...",
                    "Adds a contact to the address book."),
            new CommandInfo(EditContactCommand.COMMAND_WORD,
                    "editcontact INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]...",
                    "Edits the details of an existing contact."),
            new CommandInfo(DeleteContactCommand.COMMAND_WORD,
                    "deletecontact INDEX",
                    "Deletes the contact at the specified index."),
            new CommandInfo(FindContactsCommand.COMMAND_WORD,
                    "findcontacts KEYWORD [MORE_KEYWORDS]...",
                    "Finds contacts whose names contain any of the keywords."),
            new CommandInfo(ListContactsCommand.COMMAND_WORD,
                    "listcontacts [si/STORY_INDEX]",
                    "Lists all contacts, or contacts assigned to the specified story."),
            new CommandInfo(AddStoryCommand.COMMAND_WORD,
                    "addstory NAME [t/TAG]...",
                    "Adds a story to the address book."),
            new CommandInfo(EditStoryComman
                            .COMMAND_WORD,
                    "editstory INDEX [n/NAME] [t/TAG]...",
                    "Edits the details of an existing story."),
            new CommandInfo(DeleteStoryCommand.COMMAND_WORD,
                    "deletestory INDEX",
                    "Deletes the story at the specified index."),
            new CommandInfo(ListStoriesCommand.COMMAND_WORD,
                    "liststories [ci/CONTACT_INDEX]",
                    "Lists all stories, or stories assigned to the specified contact."),
            new CommandInfo(AssignCommand.COMMAND_WORD,
                    "assign ci/CONTACT_INDEX si/STORY_INDEX",
                    "Assigns a contact to a story."),
            new CommandInfo(UnassignCommand.COMMAND_WORD,
                    "unassign ci/CONTACT_INDEX si/STORY_INDEX",
                    "Unassigns a contact from a story."),
            new CommandInfo(AddFollowupCommand.COMMAND_WORD,
                    "addfollowup DESCRIPTION ci/CONTACT_INDEX",
                    "Adds a follow-up for the specified contact."),
            new CommandInfo(DeleteFollowupCommand.COMMAND_WORD,
                    "deletefollowup INDEX",
                    "Deletes the follow-up at the specified index."),
            new CommandInfo(ShortcutCommand.COMMAND_WORD,
                    "shortcut [s/SHORTCUT c/COMMAND_WORD]",
                    "Assigns a shortcut, or lists all shortcuts if no parameters."),
            new CommandInfo(UnshortcutCommand.COMMAND_WORD,
                    "unshortcut SHORTCUT",
                    "Removes a custom shortcut."),
            new CommandInfo(ListCommand.COMMAND_WORD,
                    "list",
                    "Lists all contacts and stories."),
            new CommandInfo(ClearCommand.COMMAND_WORD,
                    "clear",
                    "Clears all entries from the address book."),
            new CommandInfo(HelpCommand.COMMAND_WORD,
                    "help",
                    "Toggles the help panel with commands and shortcuts."),
            new CommandInfo(ExitCommand.COMMAND_WORD,
                    "exit",
                    "Exits the application."));

    private CommandWords() {}

    /** Returns whether {@code commandWord} can be assigned a shortcut. */
    public static boolean isAssignableCommandWord(String commandWord) {
        return ASSIGNABLE_COMMAND_WORDS.contains(commandWord);
    }

    /** Returns whether {@code commandWord} is reserved by any built-in command. */
    public static boolean isCommandWord(String commandWord) {
        return ASSIGNABLE_COMMAND_WORDS.contains(commandWord)
                || MANAGEMENT_COMMAND_WORDS.contains(commandWord);
    }

    /** Returns an unmodifiable list of all command metadata. */
    public static List<CommandInfo> getCommandInfos() {
        return COMMAND_INFOS;
    }
}
