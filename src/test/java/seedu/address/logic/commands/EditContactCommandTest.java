package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showContactAtIndex;
import static seedu.address.testutil.TypicalContacts.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CONTACT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_CONTACT;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditContactCommand.EditContactDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.contact.Address;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Email;
import seedu.address.model.contact.Name;
import seedu.address.model.contact.Phone;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;
import seedu.address.model.followup.FollowupIsForContactPredicate;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryAssignedToContactPredicate;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.ContactBuilder;
import seedu.address.testutil.EditContactDescriptorBuilder;
import seedu.address.testutil.StoryBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditContactCommand.
 */
public class EditContactCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Contact editedContact = new ContactBuilder().build();
        EditContactDescriptor descriptor = new EditContactDescriptorBuilder(editedContact).build();
        EditContactCommand editContactCommand = new EditContactCommand(INDEX_FIRST_CONTACT, descriptor);

        String expectedMessage =
                String.format(EditContactCommand.MESSAGE_EDIT_CONTACT_SUCCESS, Messages.format(editedContact));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setContact(model.getFilteredContactList().get(0), editedContact);

        assertCommandSuccess(editContactCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastContact = Index.fromOneBased(model.getFilteredContactList().size());
        Contact lastContact = model.getFilteredContactList().get(indexLastContact.getZeroBased());

        ContactBuilder contactInList = new ContactBuilder(lastContact);
        Contact editedContact = contactInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditContactDescriptor descriptor = new EditContactDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditContactCommand editContactCommand = new EditContactCommand(indexLastContact, descriptor);

        String expectedMessage =
                String.format(EditContactCommand.MESSAGE_EDIT_CONTACT_SUCCESS, Messages.format(editedContact));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setContact(lastContact, editedContact);

        assertCommandSuccess(editContactCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditContactCommand editContactCommand = new EditContactCommand(
                INDEX_FIRST_CONTACT,
                new EditContactDescriptor()
        );
        Contact editedContact = model.getFilteredContactList().get(INDEX_FIRST_CONTACT.getZeroBased());

        String expectedMessage =
                String.format(EditContactCommand.MESSAGE_EDIT_CONTACT_SUCCESS, Messages.format(editedContact));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editContactCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showContactAtIndex(model, INDEX_FIRST_CONTACT);

        Contact contactInFilteredList = model.getFilteredContactList().get(INDEX_FIRST_CONTACT.getZeroBased());
        Contact editedContact = new ContactBuilder(contactInFilteredList).withName(VALID_NAME_BOB).build();
        EditContactCommand editContactCommand = new EditContactCommand(INDEX_FIRST_CONTACT,
                new EditContactDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage =
                String.format(EditContactCommand.MESSAGE_EDIT_CONTACT_SUCCESS, Messages.format(editedContact));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showContactAtIndex(expectedModel, INDEX_FIRST_CONTACT);
        expectedModel.setContact(model.getFilteredContactList().get(0), editedContact);

        assertCommandSuccess(editContactCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_preservesMatchingContact() {
        showContactAtIndex(model, INDEX_FIRST_CONTACT);

        Contact contactInFilteredList = model.getFilteredContactList().get(INDEX_FIRST_CONTACT.getZeroBased());
        Contact editedContact = new ContactBuilder(contactInFilteredList).withPhone(VALID_PHONE_BOB).build();
        EditContactCommand editContactCommand = new EditContactCommand(INDEX_FIRST_CONTACT,
                new EditContactDescriptorBuilder().withPhone(VALID_PHONE_BOB).build());

        String expectedMessage =
                String.format(EditContactCommand.MESSAGE_EDIT_CONTACT_SUCCESS, Messages.format(editedContact));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showContactAtIndex(expectedModel, INDEX_FIRST_CONTACT);
        expectedModel.setContact(model.getFilteredContactList().get(0), editedContact);

        assertCommandSuccess(editContactCommand, model, expectedMessage, expectedModel);
        assertEquals(List.of(editedContact), model.getFilteredContactList());
    }

    @Test
    public void execute_duplicateContactUnfilteredList_failure() {
        Contact firstContact = model.getFilteredContactList().get(INDEX_FIRST_CONTACT.getZeroBased());
        EditContactDescriptor descriptor = new EditContactDescriptorBuilder(firstContact).build();
        EditContactCommand editContactCommand = new EditContactCommand(INDEX_SECOND_CONTACT, descriptor);

        assertCommandFailure(editContactCommand, model, String.format(EditContactCommand.MESSAGE_DUPLICATE_CONTACT,
                firstContact.getName()));
    }

    @Test
    public void execute_duplicateContactFilteredList_failure() {
        showContactAtIndex(model, INDEX_FIRST_CONTACT);

        // edit contact in filtered list into a duplicate in address book
        Contact contactInList = model.getAddressBook().getContactList().get(INDEX_SECOND_CONTACT.getZeroBased());
        EditContactCommand editContactCommand = new EditContactCommand(INDEX_FIRST_CONTACT,
                new EditContactDescriptorBuilder(contactInList).build());

        assertCommandFailure(editContactCommand, model, String.format(EditContactCommand.MESSAGE_DUPLICATE_CONTACT,
                contactInList.getName()));
    }

    @Test
    public void execute_invalidContactIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredContactList().size() + 1);
        EditContactDescriptor descriptor = new EditContactDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditContactCommand editContactCommand = new EditContactCommand(outOfBoundIndex, descriptor);

        String expectedMessage = String.format(Messages.MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX,
                outOfBoundIndex.getOneBased(), model.getFilteredContactList().size());
        assertCommandFailure(editContactCommand, model, expectedMessage);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidContactIndexFilteredList_failure() {
        showContactAtIndex(model, INDEX_FIRST_CONTACT);
        Index outOfBoundIndex = INDEX_SECOND_CONTACT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getContactList().size());

        EditContactCommand editContactCommand = new EditContactCommand(outOfBoundIndex,
                new EditContactDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(Messages.MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX,
                outOfBoundIndex.getOneBased(), model.getFilteredContactList().size());
        assertCommandFailure(editContactCommand, model, expectedMessage);
    }

    @Test
    public void equals() {
        final EditContactCommand standardCommand = new EditContactCommand(INDEX_FIRST_CONTACT, DESC_AMY);

        // same values -> returns true
        EditContactDescriptor copyDescriptor = new EditContactDescriptor(DESC_AMY);
        EditContactCommand commandWithSameValues = new EditContactCommand(INDEX_FIRST_CONTACT, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditContactCommand(INDEX_SECOND_CONTACT, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditContactCommand(INDEX_FIRST_CONTACT, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditContactDescriptor editContactDescriptor = new EditContactDescriptor();
        EditContactCommand editContactCommand = new EditContactCommand(index, editContactDescriptor);
        String expected = EditContactCommand.class.getCanonicalName() + "{index=" + index + ", editContactDescriptor="
                + editContactDescriptor + "}";
        assertEquals(expected, editContactCommand.toString());
    }

    @Test
    public void execute_resetTagsOnly_success() {
        Contact contactToEdit = model.getFilteredContactList().get(INDEX_FIRST_CONTACT.getZeroBased());
        Contact editedContact = new ContactBuilder(contactToEdit).withTags().build();

        EditContactDescriptor descriptor = new EditContactDescriptorBuilder().withTags().build();
        EditContactCommand editContactCommand = new EditContactCommand(INDEX_FIRST_CONTACT, descriptor);

        String expectedMessage =
                String.format(EditContactCommand.MESSAGE_EDIT_CONTACT_SUCCESS, Messages.format(editedContact));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setContact(contactToEdit, editedContact);

        assertCommandSuccess(editContactCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_pairwiseFieldsEmailAndAddress_success() {
        Contact contactToEdit = model.getFilteredContactList().get(INDEX_FIRST_CONTACT.getZeroBased());
        Contact editedContact = new ContactBuilder(contactToEdit)
                .withEmail("a@b.co").withAddress("X").build();

        EditContactDescriptor descriptor = new EditContactDescriptorBuilder()
                .withEmail("a@b.co").withAddress("X").build();
        EditContactCommand editContactCommand = new EditContactCommand(INDEX_FIRST_CONTACT, descriptor);

        String expectedMessage =
                String.format(EditContactCommand.MESSAGE_EDIT_CONTACT_SUCCESS, Messages.format(editedContact));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setContact(contactToEdit, editedContact);

        assertCommandSuccess(editContactCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_pairwiseFieldsNameAndPhone_success() {
        Contact contactToEdit = model.getFilteredContactList().get(INDEX_FIRST_CONTACT.getZeroBased());
        Contact editedContact = new ContactBuilder(contactToEdit)
                .withName("A").withPhone("123").build();

        EditContactDescriptor descriptor = new EditContactDescriptorBuilder()
                .withName("A").withPhone("123").build();
        EditContactCommand editContactCommand = new EditContactCommand(INDEX_FIRST_CONTACT, descriptor);

        String expectedMessage =
                String.format(EditContactCommand.MESSAGE_EDIT_CONTACT_SUCCESS, Messages.format(editedContact));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setContact(contactToEdit, editedContact);

        assertCommandSuccess(editContactCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_boundaryIndexOutOfRange_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredContactList().size() + 1);
        EditContactDescriptor descriptor = new EditContactDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditContactCommand editContactCommand = new EditContactCommand(outOfBoundIndex, descriptor);

        String expectedMessage = String.format(Messages.MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX,
                outOfBoundIndex.getOneBased(), model.getFilteredContactList().size());
        assertCommandFailure(editContactCommand, model, expectedMessage);

        Index maxIntIndex = Index.fromOneBased(Integer.MAX_VALUE);
        EditContactCommand editContactMaxIndex = new EditContactCommand(maxIntIndex, descriptor);
        String expectedMaxIndexMessage = String.format(Messages.MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX,
                maxIntIndex.getOneBased(), model.getFilteredContactList().size());
        assertCommandFailure(editContactMaxIndex, model, expectedMaxIndexMessage);
    }

    @Test
    public void execute_editContactName_updatesStoryAndFollowupFilters() {
        Contact contact = model.getFilteredContactList().get(INDEX_FIRST_CONTACT.getZeroBased());
        Story story = new StoryBuilder().build();
        model.addStory(story);
        model.assignStory(contact, story);

        Contact contactToEdit = model.getFilteredContactList().get(INDEX_FIRST_CONTACT.getZeroBased());
        Followup followup = new Followup(new FollowupDescription("Follow up"), contactToEdit.getName());
        model.addFollowup(followup);

        model.updateFilteredStoryList(new StoryAssignedToContactPredicate(contactToEdit));
        model.updateFilteredFollowupList(new FollowupIsForContactPredicate(contactToEdit));

        EditContactDescriptor descriptor = new EditContactDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditContactCommand editContactCommand = new EditContactCommand(INDEX_FIRST_CONTACT, descriptor);

        Contact editedContact = new ContactBuilder(contactToEdit).withName(VALID_NAME_BOB).build();
        String expectedMessage =
                String.format(EditContactCommand.MESSAGE_EDIT_CONTACT_SUCCESS, Messages.format(editedContact));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateFilteredStoryList(new StoryAssignedToContactPredicate(contactToEdit));
        expectedModel.updateFilteredFollowupList(new FollowupIsForContactPredicate(contactToEdit));
        expectedModel.setContact(contactToEdit, editedContact);

        assertCommandSuccess(editContactCommand, model, expectedMessage, expectedModel);

        String expectedStoryStatus = new StoryAssignedToContactPredicate(editedContact).toString();
        String expectedFollowupStatus = new FollowupIsForContactPredicate(editedContact).toString();
        assertEquals(expectedStoryStatus, model.getStoryFilterStatusProperty().getValue());
        assertEquals(1, model.getFilteredStoryList().size());
        assertEquals(expectedFollowupStatus, model.getFollowupFilterStatusProperty().getValue());
        assertEquals(1, model.getFilteredFollowupList().size());
    }

    private enum TagEdit {
        NEW, CLEAR, SAME
    }

    private enum IndexChoice {
        FIRST, MID, LAST, FILTERED
    }

    /**
     * Equivalence Partitions for EditContactCommand:
     * 1. Name:
     *    - N_NEW: Changed name
     *    - N_SAME: Unchanged
     * 2. Phone:
     *    - P_NEW: Changed phone
     *    - P_SAME: Unchanged
     * 3. Email:
     *    - E_NEW: Changed email
     *    - E_SAME: Unchanged
     * 4. Address:
     *    - A_NEW: Changed address
     *    - A_SAME: Unchanged
     * 5. Tags:
     *    - T_NEW: Changed to new tags
     *    - T_CLEAR: Cleared tags (empty set)
     *    - T_SAME: Unchanged
     * 6. Contact Index:
     *    - I_FIRST: Index 1
     *    - I_MID: Index 2
     *    - I_LAST: Last index
     *    - I_FILTERED: Index 1 on filtered list
     *
     * The following 12 test cases achieve 100% pairwise (all-pairs) coverage across all 92 pairs
     * among the 6 factors without test suite explosion.
     */
    @Test
    public void execute_allPairsCombinations_success() {
        IndexChoice[] indexChoices = {
            IndexChoice.MID, IndexChoice.FILTERED, IndexChoice.LAST, IndexChoice.MID,
            IndexChoice.LAST, IndexChoice.LAST, IndexChoice.FIRST, IndexChoice.FIRST,
            IndexChoice.FILTERED, IndexChoice.MID, IndexChoice.FILTERED, IndexChoice.FIRST
        };
        boolean[] changeName = {
            true, false, true, false,
            true, false, true, false,
            true, false, true, true
        };
        boolean[] changePhone = {
            true, false, true, false,
            false, true, true, true,
            true, false, true, false
        };
        boolean[] changeEmail = {
            true, false, false, true,
            true, false, true, false,
            true, false, true, true
        };
        boolean[] changeAddress = {
            true, false, false, true,
            false, true, true, false,
            true, false, true, true
        };
        TagEdit[] tagEdits = {
            TagEdit.CLEAR, TagEdit.CLEAR, TagEdit.NEW, TagEdit.SAME,
            TagEdit.SAME, TagEdit.CLEAR, TagEdit.CLEAR, TagEdit.SAME,
            TagEdit.NEW, TagEdit.NEW, TagEdit.SAME, TagEdit.NEW
        };

        for (int i = 0; i < 12; i++) {
            Model testModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
            Index targetIndex;
            if (indexChoices[i] == IndexChoice.FILTERED) {
                showContactAtIndex(testModel, INDEX_SECOND_CONTACT);
                targetIndex = INDEX_FIRST_CONTACT;
            } else if (indexChoices[i] == IndexChoice.FIRST) {
                targetIndex = INDEX_FIRST_CONTACT;
            } else if (indexChoices[i] == IndexChoice.MID) {
                targetIndex = INDEX_SECOND_CONTACT;
            } else {
                targetIndex = Index.fromOneBased(testModel.getFilteredContactList().size());
            }

            Contact contactToEdit = testModel.getFilteredContactList().get(targetIndex.getZeroBased());
            EditContactDescriptor descriptor = new EditContactDescriptor();
            if (changeName[i]) {
                descriptor.setName(new Name("Unique Contact " + i));
            }
            if (changePhone[i]) {
                descriptor.setPhone(new Phone("9876543" + (i % 10)));
            }
            if (changeEmail[i]) {
                descriptor.setEmail(new Email("contact" + i + "@example.com"));
            }
            if (changeAddress[i]) {
                descriptor.setAddress(new Address("Blk " + i + ", Test Avenue"));
            }
            if (tagEdits[i] == TagEdit.NEW) {
                descriptor.setTags(java.util.Set.of(new Tag("vip"), new Tag("t" + i)));
            } else if (tagEdits[i] == TagEdit.CLEAR) {
                descriptor.setTags(java.util.Set.of());
            }

            Contact editedContact = descriptor.createEditedContact(contactToEdit);
            EditContactCommand command = new EditContactCommand(targetIndex, descriptor);

            Model expectedModel = new ModelManager(new AddressBook(testModel.getAddressBook()), new UserPrefs());
            if (indexChoices[i] == IndexChoice.FILTERED) {
                showContactAtIndex(expectedModel, INDEX_SECOND_CONTACT);
            }
            expectedModel.setContact(contactToEdit, editedContact);

            String expectedMessage = String.format(EditContactCommand.MESSAGE_EDIT_CONTACT_SUCCESS,
                    Messages.format(editedContact));
            assertCommandSuccess(command, testModel, expectedMessage, expectedModel);
        }
    }
}
