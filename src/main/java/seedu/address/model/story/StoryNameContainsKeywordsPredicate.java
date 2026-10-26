package seedu.address.model.story;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Story}'s name matches any of the keywords given.
 */
public class StoryNameContainsKeywordsPredicate implements Predicate<Story> {
    private final List<String> keywords;

    public StoryNameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Story story) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(story.getName().fullName, keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof StoryNameContainsKeywordsPredicate otherPredicate)) {
            return false;
        }

        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
