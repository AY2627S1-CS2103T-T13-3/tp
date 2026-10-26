package seedu.address.model.followup;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a Followup's description contains
 * any of the keywords given.
 */
public class FollowupDescriptionContainsKeywordsPredicate
        implements Predicate<Followup> {

    private final List<String> keywords;

    public FollowupDescriptionContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = List.copyOf(keywords);
    }

    @Override
    public boolean test(Followup followup) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(
                        followup.getDescription().toString(), keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FollowupDescriptionContainsKeywordsPredicate otherPredicate
                && keywords.equals(otherPredicate.keywords));
    }

    @Override
    public int hashCode() {
        return keywords.hashCode();
    }
}
