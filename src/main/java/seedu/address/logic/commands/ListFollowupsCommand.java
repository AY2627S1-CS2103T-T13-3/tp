package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_FOLLOWUPS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.contact.Contact;

/**
 * Lists followups in the address book to the user.
 */
public class ListFollowupsCommand extends Command {

    public static final String COMMAND_WORD = "listfollowups";
    public static final String MESSAGE_SUCCESS = "Listed all followups.";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists followups in the address book.\n"
            + "Parameters: [ci/CONTACT_INDEX]\n"
            + "Example: " + COMMAND_WORD + " ci/1";

    private final Optional<Index> contactIndex;

    public ListFollowupsCommand() {
        this.contactIndex = Optional.empty();
    }

    public ListFollowupsCommand(Index contactIndex) {
        this.contactIndex = Optional.of(requireNonNull(contactIndex));
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // No index: display all followups
        if (contactIndex.isEmpty()) {
            model.updateFilteredFollowupList(PREDICATE_SHOW_ALL_FOLLOWUPS);
            return new CommandResult(MESSAGE_SUCCESS);
        }

        // Index provided: identify the contact
        List<Contact> lastShownContactList = model.getFilteredContactList();
        int index = contactIndex.get().getZeroBased();

        if (index >= lastShownContactList.size()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX);
        }

        Contact selectedContact = lastShownContactList.get(index);

        // Display only followups assigned to this contact
        model.updateFilteredFollowupList(
                followup -> followup.isFor(selectedContact));

        return new CommandResult(
                "Listed followups assigned to " + selectedContact.getName());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ListFollowupsCommand otherListFollowupsCommand)) {
            return false;
        }

        return contactIndex.equals(otherListFollowupsCommand.contactIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("contactIndex", contactIndex)
                .toString();
    }
}
