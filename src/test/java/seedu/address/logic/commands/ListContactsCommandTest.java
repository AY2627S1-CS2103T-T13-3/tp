package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showContactAtIndex;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalContacts.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CONTACT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STORY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_STORY;
import static seedu.address.testutil.TypicalStories.STORY_A;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.contact.Contact;
import seedu.address.model.story.Story;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListContactsCommand.
 */
public class ListContactsCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addStory(STORY_A);
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListContactsCommand(), model, ListContactsCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showContactAtIndex(model, INDEX_FIRST_CONTACT);
        assertCommandSuccess(new ListContactsCommand(), model, ListContactsCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_validStoryIndex_showsAssignedContacts() {
        Contact contact = model.getFilteredContactList().get(INDEX_FIRST_CONTACT.getZeroBased());
        Story story = model.getFilteredStoryList().get(INDEX_FIRST_STORY.getZeroBased());

        model.assignStory(contact, story);

        Story assignedStory = model.getFilteredStoryList().get(INDEX_FIRST_STORY.getZeroBased());

        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredContactList(p -> assignedStory.isAssigned(p));

        String expectedMessage = "Listed contacts assigned to " + assignedStory.getName();

        assertCommandSuccess(new ListContactsCommand(INDEX_FIRST_STORY), model, expectedMessage, expectedModel);

        assertEquals(1, model.getFilteredContactList().size());
        assertEquals(contact.getName(), model.getFilteredContactList().get(0).getName());
    }

    @Test
    public void execute_validStoryIndexNoAssignments_showsEmptyList() {
        Story story = model.getFilteredStoryList().get(INDEX_FIRST_STORY.getZeroBased());

        expectedModel.updateFilteredContactList(p -> false);

        String expectedMessage = "Listed contacts assigned to " + story.getName();

        assertCommandSuccess(new ListContactsCommand(INDEX_FIRST_STORY), model, expectedMessage, expectedModel);

        assertTrue(model.getFilteredContactList().isEmpty());
    }

    @Test
    public void execute_invalidStoryIndex_throwsCommandException() {
        Index invalidIndex = Index.fromOneBased(model.getFilteredStoryList().size() + 1);

        assertCommandFailure(
                new ListContactsCommand(invalidIndex),
                model,
                Messages.MESSAGE_INVALID_STORY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ListContactsCommand listContacts = new ListContactsCommand();
        ListContactsCommand listContactsWithIndex = new ListContactsCommand(INDEX_FIRST_STORY);

        // same object -> returns true
        assertTrue(listContacts.equals(listContacts));

        // same values -> returns true
        assertTrue(listContacts.equals(new ListContactsCommand()));
        assertTrue(listContactsWithIndex.equals(new ListContactsCommand(INDEX_FIRST_STORY)));

        // different types -> returns false
        assertFalse(listContacts.equals(1));

        // null -> returns false
        assertFalse(listContacts.equals(null));

        // different indices -> returns false
        assertFalse(listContactsWithIndex.equals(new ListContactsCommand(INDEX_SECOND_STORY)));
        assertFalse(listContacts.equals(listContactsWithIndex));
    }

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ListContactsCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ListContactsCommand().execute(null));
        assertThrows(NullPointerException.class, () -> new ListContactsCommand(INDEX_FIRST_STORY).execute(null));
    }

    @Test
    public void hashCodeMethod() {
        ListContactsCommand listContacts1 = new ListContactsCommand();
        ListContactsCommand listContacts2 = new ListContactsCommand();
        assertEquals(listContacts1.hashCode(), listContacts2.hashCode());

        ListContactsCommand listContactsWithIndex1 = new ListContactsCommand(INDEX_FIRST_STORY);
        ListContactsCommand listContactsWithIndex2 = new ListContactsCommand(INDEX_FIRST_STORY);
        assertEquals(listContactsWithIndex1.hashCode(), listContactsWithIndex2.hashCode());
    }

    @Test
    public void toStringMethod() {
        ListContactsCommand listContacts = new ListContactsCommand(INDEX_FIRST_STORY);
        String expected = ListContactsCommand.class.getCanonicalName()
                + "{storyIndex=Optional[" + INDEX_FIRST_STORY + "]}";
        assertEquals(expected, listContacts.toString());
    }
}
