package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STORYINDEX;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STORIES;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.story.Story;

/**
 * Unassigns a contact from a story.
 */
public class UnassignCommand extends Command {

    public static final String COMMAND_WORD = "unassign";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unassigns a contact from the story. "
            + "Parameters: "
            + PREFIX_CONTACTINDEX + " CONTACT's INDEX "
            + PREFIX_STORYINDEX + "STORY's INDEX "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CONTACTINDEX + "1 "
            + PREFIX_STORYINDEX + "1";

    public static final String MESSAGE_SUCCESS = "Unassigned %s from %s";

    private final Index storyIndex;
    private final Index contactIndex;
    /**
     * @param storyIndex of the story in the filtered story list to unassign contact from
     * @param contactIndex of the contact in the filtered story list to unassign from story
     */
    public UnassignCommand(Index storyIndex, Index contactIndex) {
        requireNonNull(storyIndex);
        requireNonNull(contactIndex);

        this.storyIndex = storyIndex;
        this.contactIndex = contactIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownContactList = model.getFilteredPersonList();
        List<Story> lastShownStoryList = model.getFilteredStoryList();

        if (contactIndex.getZeroBased() >= lastShownContactList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        if (storyIndex.getZeroBased() >= lastShownStoryList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STORY_DISPLAYED_INDEX);
        }

        Person personToUnassign = lastShownContactList.get(contactIndex.getZeroBased());
        Story storyToUnassign = lastShownStoryList.get(storyIndex.getZeroBased());
        Person unassignedPerson = personToUnassign.unassign(storyToUnassign);
        Story unassignedStory = storyToUnassign.unassign(personToUnassign);

        model.setPerson(personToUnassign, unassignedPerson);
        model.setStory(storyToUnassign, unassignedStory);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateFilteredStoryList(PREDICATE_SHOW_ALL_STORIES);
        return new CommandResult(String.format(MESSAGE_SUCCESS, personToUnassign.getName(), storyToUnassign.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UnassignCommand otherUnassignCommand)) {
            return false;
        }

        return storyIndex.equals(otherUnassignCommand.storyIndex)
                && contactIndex.equals(otherUnassignCommand.contactIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("storyIndex", storyIndex)
                .add("contactIndex", contactIndex)
                .toString();
    }
}
