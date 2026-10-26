package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_STORIES_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStories.STORY_A;
import static seedu.address.testutil.TypicalStories.STORY_C;
import static seedu.address.testutil.TypicalStories.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.story.StoryNameContainsKeywordsPredicate;

public class FindStoryCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        StoryNameContainsKeywordsPredicate firstPredicate =
                new StoryNameContainsKeywordsPredicate(List.of("first"));
        StoryNameContainsKeywordsPredicate secondPredicate =
                new StoryNameContainsKeywordsPredicate(List.of("second"));
        FindStoryCommand firstCommand = new FindStoryCommand(firstPredicate);
        FindStoryCommand secondCommand = new FindStoryCommand(secondPredicate);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new FindStoryCommand(firstPredicate)));
        assertFalse(firstCommand.equals(secondCommand));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
    }

    @Test
    public void execute_multipleKeywords_multipleStoriesFound() {
        StoryNameContainsKeywordsPredicate predicate =
                new StoryNameContainsKeywordsPredicate(List.of("A", "C"));
        FindStoryCommand command = new FindStoryCommand(predicate);
        expectedModel.updateFilteredStoryList(predicate);

        assertCommandSuccess(command, model,
                String.format(MESSAGE_STORIES_LISTED_OVERVIEW, 2), expectedModel);
        assertEquals(List.of(STORY_A, STORY_C), model.getFilteredStoryList());
    }

    @Test
    public void execute_nonMatchingKeyword_noStoryFound() {
        StoryNameContainsKeywordsPredicate predicate =
                new StoryNameContainsKeywordsPredicate(List.of("missing"));
        FindStoryCommand command = new FindStoryCommand(predicate);
        expectedModel.updateFilteredStoryList(predicate);

        assertCommandSuccess(command, model,
                String.format(MESSAGE_STORIES_LISTED_OVERVIEW, 0), expectedModel);
        assertEquals(List.of(), model.getFilteredStoryList());
    }

    @Test
    public void toStringMethod() {
        StoryNameContainsKeywordsPredicate predicate =
                new StoryNameContainsKeywordsPredicate(List.of("keyword"));
        FindStoryCommand command = new FindStoryCommand(predicate);

        String expected = FindStoryCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }
}
