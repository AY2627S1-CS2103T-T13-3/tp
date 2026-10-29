package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CONTACTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_FOLLOWUPS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STORIES;

import seedu.address.model.Model;

/**
 * Lists all contacts, stories and followups in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all %1$d contact(s), %2$d story(ies) and %3$d followup(s).";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredContactList(PREDICATE_SHOW_ALL_CONTACTS);
        model.updateFilteredStoryList(PREDICATE_SHOW_ALL_STORIES);
        model.updateFilteredFollowupList(PREDICATE_SHOW_ALL_FOLLOWUPS);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                model.getFilteredContactList().size(),
                model.getFilteredStoryList().size(),
                model.getFilteredFollowupList().size()
        ));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ListCommand;
    }
}
