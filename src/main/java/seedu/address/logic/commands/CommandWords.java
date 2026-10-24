package seedu.address.logic.commands;

import java.util.Set;

/**
 * Contains the command words recognised by the application.
 */
public final class CommandWords {

    private static final Set<String> ASSIGNABLE_COMMAND_WORDS = Set.of(
            AddContactCommand.COMMAND_WORD,
            AddStoryCommand.COMMAND_WORD,
            AssignCommand.COMMAND_WORD,
            ClearCommand.COMMAND_WORD,
            DeleteContactCommand.COMMAND_WORD,
            DeleteStoryCommand.COMMAND_WORD,
            EditContactCommand.COMMAND_WORD,
            ExitCommand.COMMAND_WORD,
            FindContactsCommand.COMMAND_WORD,
            FindStoryCommand.COMMAND_WORD,
            HelpCommand.COMMAND_WORD,
            ListCommand.COMMAND_WORD,
            ListStoriesCommand.COMMAND_WORD,
            UnassignCommand.COMMAND_WORD);

    private static final Set<String> MANAGEMENT_COMMAND_WORDS = Set.of(
            ShortcutCommand.COMMAND_WORD,
            UnshortcutCommand.COMMAND_WORD);

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
}
