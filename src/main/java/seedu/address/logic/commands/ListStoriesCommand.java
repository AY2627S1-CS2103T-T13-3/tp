package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STORIES;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Lists stories in the address book to the user.
 */
public class ListStoriesCommand extends Command {

    public static final String COMMAND_WORD = "liststories";
    public static final String MESSAGE_SUCCESS = "Listed all stories.";

    private final Optional<Index> contactIndex;

    public ListStoriesCommand() {
        this.contactIndex = Optional.empty();
    }

    public ListStoriesCommand(Index contactIndex) {
        this.contactIndex = Optional.of(requireNonNull(contactIndex));
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // No index: display all stories
        if (contactIndex.isEmpty()) {
            model.updateFilteredStoryList(PREDICATE_SHOW_ALL_STORIES);
            return new CommandResult(MESSAGE_SUCCESS);
        }

        // Index provided: identify the contact
        List<Person> lastShownContactList = model.getFilteredPersonList();
        int index = contactIndex.get().getZeroBased();

        if (index >= lastShownContactList.size()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person selectedPerson = lastShownContactList.get(index);

        // Display only stories assigned to this contact
        model.updateFilteredStoryList(
                story -> story.isAssigned(selectedPerson));

        return new CommandResult(
                "Listed stories assigned to " + selectedPerson.getName());
    }
}
