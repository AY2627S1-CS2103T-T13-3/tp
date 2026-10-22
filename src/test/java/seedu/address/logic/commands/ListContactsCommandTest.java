package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STORY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_STORY;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalStories.STORY_A;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
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
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListContactsCommand(), model, ListContactsCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_validStoryIndex_showsAssignedContacts() {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Story story = model.getFilteredStoryList().get(INDEX_FIRST_STORY.getZeroBased());

        model.assignStory(person, story);

        Story assignedStory = model.getFilteredStoryList().get(INDEX_FIRST_STORY.getZeroBased());

        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredPersonList(p -> assignedStory.isAssigned(p));

        String expectedMessage = "Listed contacts assigned to " + assignedStory.getName();

        assertCommandSuccess(new ListContactsCommand(INDEX_FIRST_STORY), model, expectedMessage, expectedModel);

        assertEquals(1, model.getFilteredPersonList().size());
        assertEquals(person.getName(), model.getFilteredPersonList().get(0).getName());
    }

    @Test
    public void execute_validStoryIndexNoAssignments_showsEmptyList() {
        Story story = model.getFilteredStoryList().get(INDEX_FIRST_STORY.getZeroBased());

        expectedModel.updateFilteredPersonList(p -> false);

        String expectedMessage = "Listed contacts assigned to " + story.getName();

        assertCommandSuccess(new ListContactsCommand(INDEX_FIRST_STORY), model, expectedMessage, expectedModel);

        assertTrue(model.getFilteredPersonList().isEmpty());
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
}
