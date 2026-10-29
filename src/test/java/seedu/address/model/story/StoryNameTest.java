package seedu.address.model.story;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class StoryNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StoryName(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new StoryName(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> StoryName.isValidName(null));

        // invalid name
        assertFalse(StoryName.isValidName("")); // empty string
        assertFalse(StoryName.isValidName(" ")); // spaces only
        assertFalse(StoryName.isValidName("/")); // forward slash only
        assertFalse(StoryName.isValidName("/story")); // starts with forward slash
        assertFalse(StoryName.isValidName("story/")); // ends with forward slash
        assertFalse(StoryName.isValidName("story/name")); // contains forward slash

        // valid name
        assertTrue(StoryName.isValidName("Breaking News")); // alphabets only
        assertTrue(StoryName.isValidName("12345")); // numbers only
        assertTrue(StoryName.isValidName("Investigation 2026")); // alphanumeric characters
        assertTrue(StoryName.isValidName("Budget 2026: Politics & Tech")); // with special characters
        assertTrue(StoryName.isValidName("Scandal!")); // exclamation mark
        assertTrue(StoryName.isValidName("Covid-19 Report")); // hyphen
        assertTrue(StoryName.isValidName("Invalid@Story")); // at symbol
    }

    @Test
    public void equals() {
        StoryName storyName = new StoryName("Valid Story");

        // same values -> returns true
        assertTrue(storyName.equals(new StoryName("Valid Story")));

        // same object -> returns true
        assertTrue(storyName.equals(storyName));

        // null -> returns false
        assertFalse(storyName.equals(null));

        // different types -> returns false
        assertFalse(storyName.equals(5.0f));

        // different values -> returns false
        assertFalse(storyName.equals(new StoryName("Other Valid Story")));
    }

    @Test
    public void isMatching() {
        StoryName storyName = new StoryName("Breaking News");

        // matching keyword -> returns true
        assertTrue(storyName.isMatching("Breaking"));
        assertTrue(storyName.isMatching("breaking")); // case-insensitive
        assertTrue(storyName.isMatching("News"));
        assertTrue(storyName.isNameMatching("Breaking"));

        // non-matching keyword -> returns false
        assertFalse(storyName.isMatching("Sports"));
        assertFalse(storyName.isNameMatching("Sports"));

        // partial word matching -> returns false
        assertFalse(storyName.isMatching("Break"));
        assertFalse(storyName.isNameMatching("Break"));
    }
}
