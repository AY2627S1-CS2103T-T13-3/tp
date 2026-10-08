package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STORIES;

import seedu.address.model.Model;

/**
 * Lists all stories in the address book to the user.
 */
public class ListStoriesCommand extends Command {

    public static final String COMMAND_WORD = "liststories";

    public static final String MESSAGE_SUCCESS = "Listed all stories.";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredStoryList(PREDICATE_SHOW_ALL_STORIES);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}

