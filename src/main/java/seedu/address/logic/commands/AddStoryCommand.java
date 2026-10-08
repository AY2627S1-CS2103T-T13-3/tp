package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.story.Story;

/**
 * Adds a story to the address book.
 */
public class AddStoryCommand extends Command {

    public static final String COMMAND_WORD = "addstory";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a story to the address book.\n"
            + "Parameters: NAME\n"
            + "Example: " + COMMAND_WORD + " hello";

    public static final String MESSAGE_SUCCESS = "Story \"%1$s\" has been added.";
    public static final String MESSAGE_DUPLICATE_STORY = "Error: there is already a story named \"%1$s\"!";

    private final Story toAdd;

    /**
     * Creates an AddStoryCommand to add the specified {@code Story}
     */
    public AddStoryCommand(Story story) {
        requireNonNull(story);
        toAdd = story;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasStory(toAdd)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_STORY, toAdd.getName()));
        }

        model.addStory(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddStoryCommand otherAddStoryCommand)) {
            return false;
        }

        return toAdd.equals(otherAddStoryCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
