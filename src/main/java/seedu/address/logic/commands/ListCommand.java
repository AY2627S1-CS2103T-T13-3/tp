
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.story.Story;

/**
 * Lists contacts in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_SUCCESS = "Listed all persons.";

    private final Optional<Index> storyIndex;

    public ListCommand() {
        this.storyIndex = Optional.empty();
    }

    public ListCommand(Index storyIndex) {
        this.storyIndex = Optional.of(requireNonNull(storyIndex));
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // No index: display all contacts
        if (storyIndex.isEmpty()) {
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(MESSAGE_SUCCESS);
        }

        // Index provided: identify the story
        List<Story> lastShownStoryList = model.getFilteredStoryList();
        int index = storyIndex.get().getZeroBased();

        if (index >= lastShownStoryList.size()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_STORY_DISPLAYED_INDEX);
        }

        Story selectedStory = lastShownStoryList.get(index);

        // Display only contacts assigned to this story
        model.updateFilteredPersonList(
                person -> selectedStory.isAssigned(person));

        return new CommandResult(
                "Listed contacts assigned to " + selectedStory.getName());
    }
}
