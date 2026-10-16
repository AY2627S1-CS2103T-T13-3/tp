package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalStories.STORY_A;
import static seedu.address.testutil.TypicalStories.STORY_B;

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
 * Contains integration tests and unit tests for ListStoriesCommand.
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
        Person person = model.getFilteredPersonList().get(
                INDEX_FIRST_PERSON.getZeroBased());
        Story story = model.getFilteredStoryList().get(0);
        Person assignedPerson = person.assign(story);
        Story assignedStory = story.assign(person);

        model.setPerson(person, assignedPerson);
        model.setStory(story, assignedStory);

        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.updateFilteredStoryList(
                s -> s.isAssigned(assignedPerson));

        String expectedMessage = "Listed stories assigned to "
                + assignedPerson.getName();

        assertCommandSuccess(
                new ListStoriesCommand(INDEX_FIRST_PERSON),
                model, expectedMessage, expectedModel);

        assertEquals(1, model.getFilteredStoryList().size());
        assertEquals(story.getName(),
                model.getFilteredStoryList().get(0).getName());
    }

    @Test
    public void execute_validContactIndexNoAssignments_showsEmptyList() {
        Person person = model.getFilteredPersonList().get(
                INDEX_FIRST_PERSON.getZeroBased());

        expectedModel.updateFilteredStoryList(story -> false);

        String expectedMessage = "Listed stories assigned to "
                + person.getName();

        assertCommandSuccess(
                new ListStoriesCommand(INDEX_FIRST_PERSON),
                model, expectedMessage, expectedModel);

        assertTrue(model.getFilteredStoryList().isEmpty());
    }

    @Test
    public void execute_invalidContactIndex_throwsCommandException() {
        Index invalidIndex = Index.fromOneBased(
                model.getFilteredPersonList().size() + 1);

        assertCommandFailure(
                new ListStoriesCommand(invalidIndex),
                model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
}
