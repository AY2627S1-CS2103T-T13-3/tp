package seedu.address.model.story;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

public class StoryNameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        StoryNameContainsKeywordsPredicate firstPredicate =
                new StoryNameContainsKeywordsPredicate(List.of("first"));
        StoryNameContainsKeywordsPredicate firstPredicateCopy =
                new StoryNameContainsKeywordsPredicate(List.of("first"));
        StoryNameContainsKeywordsPredicate secondPredicate =
                new StoryNameContainsKeywordsPredicate(List.of("second"));

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(firstPredicateCopy));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
    }

    @Test
    public void test_nameContainsKeyword_returnsTrue() {
        Story story = new Story(new StoryName("Housing Investigation"), new HashSet<>());

        assertTrue(new StoryNameContainsKeywordsPredicate(List.of("Housing")).test(story));
        assertTrue(new StoryNameContainsKeywordsPredicate(List.of("education", "investigation")).test(story));
        assertTrue(new StoryNameContainsKeywordsPredicate(List.of("hOuSiNg")).test(story));
    }

    @Test
    public void test_nameDoesNotContainKeyword_returnsFalse() {
        Story story = new Story(new StoryName("Housing Investigation"), new HashSet<>());

        assertFalse(new StoryNameContainsKeywordsPredicate(List.of()).test(story));
        assertFalse(new StoryNameContainsKeywordsPredicate(List.of("House")).test(story));
        assertFalse(new StoryNameContainsKeywordsPredicate(List.of("Education")).test(story));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("housing", "investigation");
        StoryNameContainsKeywordsPredicate predicate = new StoryNameContainsKeywordsPredicate(keywords);

        String expected = StoryNameContainsKeywordsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
