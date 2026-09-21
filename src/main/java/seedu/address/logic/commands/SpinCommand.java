package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Shuffles the contacts PERMANENTLY and CHAOTICALLY.
 */
public class SpinCommand extends Command {
    public static final String COMMAND_WORD = "spin";

    public static final String MESSAGE_SUCCESS = "SPIN SPIN SPIN!";
    @Override
    public CommandResult execute(Model model) throws CommandException {
        model.shuffle();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
