package seedu.address.model.followup;

import seedu.address.model.BaseKeywordPredicate;
import seedu.address.model.contact.Contact;

import java.util.List;

/**
 * Tests that a Followup's description contains
 * any of the keywords given.
 */
public class FollowupDescriptionContainsKeywordsPredicate
        extends BaseKeywordPredicate<Followup, Contact> {

    public FollowupDescriptionContainsKeywordsPredicate(List<String> keywords) {
        super(keywords);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FollowupDescriptionContainsKeywordsPredicate otherPredicate
                && super.equals(otherPredicate));
    }

    @Override
    public boolean matchesKeyword(Followup item, String keyword) {
        return item.isDescriptionMatching(keyword);
    }
}
