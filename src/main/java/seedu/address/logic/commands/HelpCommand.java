package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Formats full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String USER_GUIDE_LINK = "https://ay2627s1-cs2103t-t13-3.github.io/tp/UserGuide.html";

    public static final String SHOWING_HELP_MESSAGE = "Toggled help panel.\nYou may view the user guide here: "
            + USER_GUIDE_LINK;

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
