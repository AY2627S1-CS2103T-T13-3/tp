package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Name;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;



/** Edits the details of an existing followup in the address book. */
public class EditFollowupCommand extends AddressBookMutatingCommand {
    public static final String COMMAND_WORD = "editfollowup";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the followup identified "
            + "by the index number used in the displayed followup list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION] [" + PREFIX_CONTACTINDEX + "CONTACT_INDEX]\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_DESCRIPTION + "Call tomorrow " + PREFIX_CONTACTINDEX + "2";
    public static final String MESSAGE_EDIT_FOLLOWUP_SUCCESS = "Edited followup: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_FOLLOWUP =
            "Error: there is already a followup \"%1$s\" for the contact \"%2$s\"!";

    private final Index index;
    private final EditFollowupDescriptor editFollowupDescriptor;

    /** Creates a command to edit the followup at the given displayed index. */
    public EditFollowupCommand(Index index, EditFollowupDescriptor editFollowupDescriptor) {
        requireNonNull(index);
        requireNonNull(editFollowupDescriptor);
        this.index = index;
        this.editFollowupDescriptor = new EditFollowupDescriptor(editFollowupDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Followup> lastShownList = model.getFilteredFollowupList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_FOLLOWUP_DISPLAYED_INDEX,
                    index.getOneBased(), lastShownList.size()));
        }

        Followup followupToEdit = lastShownList.get(index.getZeroBased());
        Name updatedContact = followupToEdit.getContact();
        if (editFollowupDescriptor.getContactIndex().isPresent()) {
            Index contactIndex = editFollowupDescriptor.getContactIndex().get();
            List<Contact> lastShownContacts = model.getFilteredContactList();
            if (contactIndex.getZeroBased() >= lastShownContacts.size()) {
                throw new CommandException(String.format(Messages.MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX,
                        contactIndex.getOneBased(), lastShownContacts.size()));
            }
            updatedContact = lastShownContacts.get(contactIndex.getZeroBased()).getName();
        }
        Followup editedFollowup = editFollowupDescriptor.createEditedFollowup(followupToEdit, updatedContact);
        if (!followupToEdit.equals(editedFollowup) && model.hasFollowup(editedFollowup)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_FOLLOWUP, editedFollowup.getDescription(),
                    updatedContact));
        }

        model.setFollowup(followupToEdit, editedFollowup);
        return new CommandResult(String.format(MESSAGE_EDIT_FOLLOWUP_SUCCESS, Messages.format(editedFollowup)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EditFollowupCommand otherCommand)) {
            return false;
        }
        return index.equals(otherCommand.index)
                && editFollowupDescriptor.equals(otherCommand.editFollowupDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("index", index)
                .add("editFollowupDescriptor", editFollowupDescriptor).toString();
    }

    /** Stores optional replacement values for a followup. */
    public static class EditFollowupDescriptor {
        private FollowupDescription description;
        private Index contactIndex;

        public EditFollowupDescriptor() {}

        /** Copy constructor. */
        public EditFollowupDescriptor(EditFollowupDescriptor toCopy) {
            setDescription(toCopy.description);
            setContactIndex(toCopy.contactIndex);
        }

        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(description, contactIndex);
        }

        public void setDescription(FollowupDescription description) {
            this.description = description;
        }

        public Optional<FollowupDescription> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setContactIndex(Index contactIndex) {
            this.contactIndex = contactIndex;
        }

        public Optional<Index> getContactIndex() {
            return Optional.ofNullable(contactIndex);
        }

        /**
         * Creates and returns a {@code Followup} with the details of {@code followupToEdit}
         * edited with this descriptor's fields and the resolved {@code updatedContact}.
         */
        public Followup createEditedFollowup(Followup followupToEdit, Name updatedContact) {
            assert followupToEdit != null;
            assert updatedContact != null;

            FollowupDescription updatedDescription = getDescription().orElse(followupToEdit.getDescription());
            return new Followup(updatedDescription, updatedContact);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof EditFollowupDescriptor otherDescriptor)) {
                return false;
            }
            return Objects.equals(description, otherDescriptor.description)
                    && Objects.equals(contactIndex, otherDescriptor.contactIndex);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).add("description", description)
                    .add("contactIndex", contactIndex).toString();
        }
    }
}
