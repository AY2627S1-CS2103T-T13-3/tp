package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CONTACTS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.story.Story;

/**
 * Lists contacts in the address book to the user.
 */
public class ListContactsCommand extends Command {

    public static final String COMMAND_WORD = "listcontacts";
    public static final String MESSAGE_SUCCESS = "Listed all contacts.";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists contacts in the address book.\n"
            + "Parameters: [si/STORY_INDEX]\n"
            + "Example: " + COMMAND_WORD + " si/1";

    private final Optional<Index> storyIndex;

    public ListContactsCommand() {
        this.storyIndex = Optional.empty();
    }

    public ListContactsCommand(Index storyIndex) {
        this.storyIndex = Optional.of(requireNonNull(storyIndex));
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // No index: display all contacts
        if (storyIndex.isEmpty()) {
            model.updateFilteredContactList(PREDICATE_SHOW_ALL_CONTACTS);
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
        model.updateFilteredContactList(
                contact -> selectedStory.isAssigned(contact));

        return new CommandResult(
                "Listed contacts assigned to " + selectedStory.getName());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ListContactsCommand otherListContactsCommand)) {
            return false;
        }

        return storyIndex.equals(otherListContactsCommand.storyIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storyIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("storyIndex", storyIndex)
                .toString();
    }
}
