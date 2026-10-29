package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.contact.Contact;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;

/**
 * Adds a followup to a contact in PressMemo.
 */
public class AddFollowupCommand extends Command {

    public static final String COMMAND_WORD = "addfollowup";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a followup to a contact in PressMemo.\n"
            + "Parameters: DESCRIPTION "
            + PREFIX_CONTACTINDEX + "CONTACT INDEX"
            + "Example: " + COMMAND_WORD + " meeting " + PREFIX_CONTACTINDEX + "1";

    public static final String MESSAGE_SUCCESS = "Followup %1$s has been added to %2$s.";
    public static final String MESSAGE_DUPLICATE_FOLLOWUP = "This followup already exists for %1$s.";

    private final FollowupDescription followupDescription;
    private final Index contactIndex;

    /**
     * Creates an AddFollowupCommand to add a followup with the specified {@code FollowupDescription} to the contact at
     * the specified {@code contactIndex}
     */
    public AddFollowupCommand(FollowupDescription followupDescription, Index contactIndex) {
        requireNonNull(followupDescription);
        requireNonNull(contactIndex);

        this.followupDescription = followupDescription;
        this.contactIndex = contactIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Contact> lastShownContactList = model.getFilteredContactList();

        if (contactIndex.getZeroBased() >= lastShownContactList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX);
        }

        Contact target = lastShownContactList.get(contactIndex.getZeroBased());
        Followup followupToAdd = new Followup(followupDescription, target.getName());

        if (model.hasFollowup(followupToAdd)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_FOLLOWUP, target.getName()));
        }

        model.addFollowup(followupToAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, followupDescription, target.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddFollowupCommand otherAddFollowupCommand)) {
            return false;
        }

        return followupDescription.equals(otherAddFollowupCommand.followupDescription)
                && contactIndex.equals(otherAddFollowupCommand.contactIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("followupDescription", followupDescription)
                .add("contactIndex", contactIndex)
                .toString();
    }
}
