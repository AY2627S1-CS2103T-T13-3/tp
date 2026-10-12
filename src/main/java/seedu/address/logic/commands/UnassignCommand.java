package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STORYINDEX;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;

/**
 * Unassigns a contact from a story.
 */
public class UnassignCommand extends Command {

    public static final String COMMAND_WORD = "unassign";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unassigns a contact from the story. "
            + "Parameters: "
            + PREFIX_CONTACTINDEX + " CONTACT's NAME "
            + PREFIX_STORYINDEX + "STORY's NAME "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CONTACTINDEX + "1 "
            + PREFIX_STORYINDEX + "1";

    // TODO
    public static final String MESSAGE_SUCCESS = "COMMAND STILL IN PROGRESS";

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
    public CommandResult execute(Model model) {
        requireNonNull(model);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
