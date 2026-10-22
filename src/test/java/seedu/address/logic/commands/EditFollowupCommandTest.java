package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_INTERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.DESC_NOTES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CONTACT_INDEX_TWO;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_NOTES;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showFollowupAtIndex;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalFollowups.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_FOLLOWUP;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_FOLLOWUP;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditFollowupCommand.EditFollowupDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditFollowupDescriptorBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditFollowupCommand.
 */
public class EditFollowupCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person secondPerson = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Followup editedFollowup = new Followup(new FollowupDescription("Follow up on quote"), secondPerson.getName());
        EditFollowupDescriptor descriptor =
                new EditFollowupDescriptorBuilder(editedFollowup, INDEX_SECOND_PERSON).build();
        EditFollowupCommand editFollowupCommand = new EditFollowupCommand(INDEX_FIRST_FOLLOWUP, descriptor);

        String expectedMessage = String.format(
                EditFollowupCommand.MESSAGE_EDIT_FOLLOWUP_SUCCESS, Messages.format(editedFollowup));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setFollowup(model.getFilteredFollowupList().get(0), editedFollowup);

        assertCommandSuccess(editFollowupCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastFollowup = Index.fromOneBased(model.getFilteredFollowupList().size());
        Followup lastFollowup = model.getFilteredFollowupList().get(indexLastFollowup.getZeroBased());

        // Only description specified
        Followup editedFollowupDescription = new Followup(
                new FollowupDescription("Updated description"), lastFollowup.getContact());
        EditFollowupDescriptor descriptorDescription = new EditFollowupDescriptorBuilder()
                .withDescription("Updated description").build();
        EditFollowupCommand editCommandDescription = new EditFollowupCommand(indexLastFollowup, descriptorDescription);

        String expectedMessageDescription = String.format(
                EditFollowupCommand.MESSAGE_EDIT_FOLLOWUP_SUCCESS, Messages.format(editedFollowupDescription));

        Model expectedModelDescription = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModelDescription.setFollowup(lastFollowup, editedFollowupDescription);

        assertCommandSuccess(editCommandDescription, model, expectedMessageDescription, expectedModelDescription);

        // Only contactIndex specified
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Followup currentLastFollowup = model.getFilteredFollowupList().get(indexLastFollowup.getZeroBased());
        Followup editedFollowupContact = new Followup(currentLastFollowup.getDescription(), firstPerson.getName());
        EditFollowupDescriptor descriptorContact = new EditFollowupDescriptorBuilder()
                .withContactIndex(INDEX_FIRST_PERSON).build();
        EditFollowupCommand editCommandContact = new EditFollowupCommand(indexLastFollowup, descriptorContact);

        String expectedMessageContact = String.format(
                EditFollowupCommand.MESSAGE_EDIT_FOLLOWUP_SUCCESS, Messages.format(editedFollowupContact));

        Model expectedModelContact = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModelContact.setFollowup(currentLastFollowup, editedFollowupContact);

        assertCommandSuccess(editCommandContact, model, expectedMessageContact, expectedModelContact);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditFollowupCommand editFollowupCommand =
                new EditFollowupCommand(INDEX_FIRST_FOLLOWUP, new EditFollowupDescriptor());
        Followup editedFollowup = model.getFilteredFollowupList().get(INDEX_FIRST_FOLLOWUP.getZeroBased());

        String expectedMessage = String.format(
                EditFollowupCommand.MESSAGE_EDIT_FOLLOWUP_SUCCESS, Messages.format(editedFollowup));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editFollowupCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showFollowupAtIndex(model, INDEX_FIRST_FOLLOWUP);

        Followup followupInFilteredList = model.getFilteredFollowupList().get(INDEX_FIRST_FOLLOWUP.getZeroBased());
        Followup editedFollowup = new Followup(
                new FollowupDescription("New followup description"), followupInFilteredList.getContact());
        EditFollowupCommand editFollowupCommand = new EditFollowupCommand(INDEX_FIRST_FOLLOWUP,
                new EditFollowupDescriptorBuilder().withDescription("New followup description").build());

        String expectedMessage = String.format(
                EditFollowupCommand.MESSAGE_EDIT_FOLLOWUP_SUCCESS, Messages.format(editedFollowup));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setFollowup(model.getFilteredFollowupList().get(0), editedFollowup);

        assertCommandSuccess(editFollowupCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateFollowupUnfilteredList_failure() {
        Followup firstFollowup = model.getFilteredFollowupList().get(INDEX_FIRST_FOLLOWUP.getZeroBased());
        EditFollowupDescriptor descriptor = new EditFollowupDescriptorBuilder()
                .withDescription(firstFollowup.getDescription().description).build();
        EditFollowupCommand editFollowupCommand = new EditFollowupCommand(INDEX_SECOND_FOLLOWUP, descriptor);

        assertCommandFailure(editFollowupCommand, model, EditFollowupCommand.MESSAGE_DUPLICATE_FOLLOWUP);
    }

    @Test
    public void execute_duplicateFollowupFilteredList_failure() {
        showFollowupAtIndex(model, INDEX_FIRST_FOLLOWUP);

        // edit followup in filtered list into a duplicate in address book
        Followup followupInList = model.getAddressBook().getFollowupList().get(INDEX_SECOND_FOLLOWUP.getZeroBased());
        EditFollowupCommand editFollowupCommand = new EditFollowupCommand(INDEX_FIRST_FOLLOWUP,
                new EditFollowupDescriptorBuilder()
                        .withDescription(followupInList.getDescription().description).build());

        assertCommandFailure(editFollowupCommand, model, EditFollowupCommand.MESSAGE_DUPLICATE_FOLLOWUP);
    }

    @Test
    public void execute_invalidFollowupIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredFollowupList().size() + 1);
        EditFollowupDescriptor descriptor = new EditFollowupDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_NOTES).build();
        EditFollowupCommand editFollowupCommand = new EditFollowupCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editFollowupCommand, model, Messages.MESSAGE_INVALID_FOLLOWUP_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidFollowupIndexFilteredList_failure() {
        showFollowupAtIndex(model, INDEX_FIRST_FOLLOWUP);
        Index outOfBoundIndex = INDEX_SECOND_FOLLOWUP;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getFollowupList().size());

        EditFollowupCommand editFollowupCommand = new EditFollowupCommand(outOfBoundIndex,
                new EditFollowupDescriptorBuilder().withDescription(VALID_DESCRIPTION_NOTES).build());

        assertCommandFailure(editFollowupCommand, model, Messages.MESSAGE_INVALID_FOLLOWUP_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidContactIndexUnfilteredList_failure() {
        Index outOfBoundContactIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditFollowupDescriptor descriptor = new EditFollowupDescriptorBuilder()
                .withContactIndex(outOfBoundContactIndex).build();
        EditFollowupCommand editFollowupCommand = new EditFollowupCommand(INDEX_FIRST_FOLLOWUP, descriptor);

        assertCommandFailure(editFollowupCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidContactIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundContactIndex = VALID_CONTACT_INDEX_TWO;
        assertTrue(outOfBoundContactIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditFollowupDescriptor descriptor = new EditFollowupDescriptorBuilder()
                .withContactIndex(outOfBoundContactIndex).build();
        EditFollowupCommand editFollowupCommand = new EditFollowupCommand(INDEX_FIRST_FOLLOWUP, descriptor);

        assertCommandFailure(editFollowupCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditFollowupCommand standardCommand = new EditFollowupCommand(INDEX_FIRST_FOLLOWUP, DESC_INTERVIEW);

        // same values -> returns true
        EditFollowupDescriptor copyDescriptor = new EditFollowupDescriptor(DESC_INTERVIEW);
        EditFollowupCommand commandWithSameValues = new EditFollowupCommand(INDEX_FIRST_FOLLOWUP, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditFollowupCommand(INDEX_SECOND_FOLLOWUP, DESC_INTERVIEW)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditFollowupCommand(INDEX_FIRST_FOLLOWUP, DESC_NOTES)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditFollowupDescriptor editFollowupDescriptor = new EditFollowupDescriptor();
        EditFollowupCommand editFollowupCommand = new EditFollowupCommand(index, editFollowupDescriptor);
        String expected = EditFollowupCommand.class.getCanonicalName() + "{index=" + index
                + ", editFollowupDescriptor=" + editFollowupDescriptor + "}";
        assertEquals(expected, editFollowupCommand.toString());
    }

}
