package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_FOLLOWUPS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Edits the details of an existing followup in the address book.
 */
public class EditFollowupCommand extends Command {

    public static final String COMMAND_WORD = "editfollowup";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the followup identified "
            + "by the index number used in the displayed followup list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION] "
            + "[" + PREFIX_CONTACTINDEX + "CONTACT_INDEX]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DESCRIPTION + "Follow up on interview "
            + PREFIX_CONTACTINDEX + "2";

    public static final String MESSAGE_EDIT_FOLLOWUP_SUCCESS = "Edited followup: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_FOLLOWUP = "This followup already exists in the address book.";

    private final Index index;
    private final EditFollowupDescriptor editFollowupDescriptor;

    /**
     * @param index of the followup in the filtered followup list to edit
     * @param editFollowupDescriptor details to edit the followup with
     */
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
            throw new CommandException(Messages.MESSAGE_INVALID_FOLLOWUP_DISPLAYED_INDEX);
        }

        Followup followupToEdit = lastShownList.get(index.getZeroBased());
        Followup editedFollowup = createEditedFollowup(followupToEdit, editFollowupDescriptor, model);

        if (!followupToEdit.isSameFollowup(editedFollowup) && model.hasFollowup(editedFollowup)) {
            throw new CommandException(MESSAGE_DUPLICATE_FOLLOWUP);
        }

        model.setFollowup(followupToEdit, editedFollowup);
        model.updateFilteredFollowupList(PREDICATE_SHOW_ALL_FOLLOWUPS);
        return new CommandResult(String.format(MESSAGE_EDIT_FOLLOWUP_SUCCESS, Messages.format(editedFollowup)));
    }

    /**
     * Creates and returns a {@code Followup} with the details of {@code followupToEdit}
     * edited with {@code editFollowupDescriptor}.
     */
    private static Followup createEditedFollowup(Followup followupToEdit,
            EditFollowupDescriptor editFollowupDescriptor, Model model) throws CommandException {
        assert followupToEdit != null;

        FollowupDescription updatedDescription =
                editFollowupDescriptor.getDescription().orElse(followupToEdit.getDescription());

        Name updatedContact = followupToEdit.getContact();
        if (editFollowupDescriptor.getContactIndex().isPresent()) {
            Index contactIndex = editFollowupDescriptor.getContactIndex().get();
            List<Person> lastShownPersonList = model.getFilteredPersonList();
            if (contactIndex.getZeroBased() >= lastShownPersonList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            updatedContact = lastShownPersonList.get(contactIndex.getZeroBased()).getName();
        }

        return new Followup(updatedDescription, updatedContact);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditFollowupCommand otherEditFollowupCommand)) {
            return false;
        }

        return index.equals(otherEditFollowupCommand.index)
                && editFollowupDescriptor.equals(otherEditFollowupCommand.editFollowupDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editFollowupDescriptor", editFollowupDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the followup with. Each non-empty field value will replace the
     * corresponding field value of the followup.
     */
    public static class EditFollowupDescriptor {
        private FollowupDescription description;
        private Index contactIndex;

        public EditFollowupDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditFollowupDescriptor(EditFollowupDescriptor toCopy) {
            setDescription(toCopy.description);
            setContactIndex(toCopy.contactIndex);
        }

        /**
         * Returns true if at least one field is edited.
         */
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

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditFollowupDescriptor otherEditFollowupDescriptor)) {
                return false;
            }

            return Objects.equals(description, otherEditFollowupDescriptor.description)
                    && Objects.equals(contactIndex, otherEditFollowupDescriptor.contactIndex);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("description", description)
                    .add("contactIndex", contactIndex)
                    .toString();
        }
    }
}
