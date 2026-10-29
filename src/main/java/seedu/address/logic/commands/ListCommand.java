package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CONTACTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STORIES;

import seedu.address.model.Model;

/**
 * Lists all contacts and stories in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all %1$d contact(s) and %2$d story(ies).";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredContactList(PREDICATE_SHOW_ALL_CONTACTS);
        model.updateFilteredStoryList(PREDICATE_SHOW_ALL_STORIES);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                model.getFilteredContactList().size(),
                model.getFilteredStoryList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ListCommand;
    }
}
