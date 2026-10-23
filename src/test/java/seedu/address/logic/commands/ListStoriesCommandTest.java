package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalContacts.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CONTACT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_CONTACT;
import static seedu.address.testutil.TypicalStories.STORY_A;
import static seedu.address.testutil.TypicalStories.STORY_B;

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
 * Contains integration tests (interaction with the Model) and unit tests for ListStoriesCommand.
 */
public class ListStoriesCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.addStory(STORY_A);
        model.addStory(STORY_B);
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListStoriesCommand(), model,
                ListStoriesCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        model.updateFilteredStoryList(story -> false);

        assertCommandSuccess(new ListStoriesCommand(), model,
                ListStoriesCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_validContactIndex_showsAssignedStories() {
        Contact contact = model.getFilteredContactList().get(
                INDEX_FIRST_CONTACT.getZeroBased());
        Story story = model.getFilteredStoryList().get(0);

        model.assignStory(contact, story);

        Contact assignedContact = model.getFilteredContactList().get(
                INDEX_FIRST_CONTACT.getZeroBased());

        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredStoryList(
                s -> s.isAssigned(assignedContact));

        String expectedMessage = "Listed stories assigned to "
                + assignedContact.getName();

        assertCommandSuccess(
                new ListStoriesCommand(INDEX_FIRST_CONTACT),
                model, expectedMessage, expectedModel);

        assertEquals(1, model.getFilteredStoryList().size());
        assertEquals(story.getName(),
                model.getFilteredStoryList().get(0).getName());
    }

    @Test
    public void execute_validContactIndexNoAssignments_showsEmptyList() {
        Contact contact = model.getFilteredContactList().get(
                INDEX_FIRST_CONTACT.getZeroBased());

        expectedModel.updateFilteredStoryList(story -> false);

        String expectedMessage = "Listed stories assigned to "
                + contact.getName();

        assertCommandSuccess(
                new ListStoriesCommand(INDEX_FIRST_CONTACT),
                model, expectedMessage, expectedModel);

        assertTrue(model.getFilteredStoryList().isEmpty());
    }

    @Test
    public void execute_invalidContactIndex_throwsCommandException() {
        Index invalidIndex = Index.fromOneBased(
                model.getFilteredContactList().size() + 1);

        assertCommandFailure(
                new ListStoriesCommand(invalidIndex),
                model,
                Messages.MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ListStoriesCommand listStories = new ListStoriesCommand();
        ListStoriesCommand listStoriesWithIndex = new ListStoriesCommand(INDEX_FIRST_CONTACT);

        // same object -> returns true
        assertTrue(listStories.equals(listStories));

        // same values -> returns true
        assertTrue(listStories.equals(new ListStoriesCommand()));
        assertTrue(listStoriesWithIndex.equals(new ListStoriesCommand(INDEX_FIRST_CONTACT)));

        // different types -> returns false
        assertFalse(listStories.equals(1));

        // null -> returns false
        assertFalse(listStories.equals(null));

        // different indices -> returns false
        assertFalse(listStoriesWithIndex.equals(new ListStoriesCommand(INDEX_SECOND_CONTACT)));
        assertFalse(listStories.equals(listStoriesWithIndex));
    }
}
