package seedu.address.model.followup;

import seedu.address.model.BaseLinkedPredicate;
import seedu.address.model.contact.Contact;

public class FollowupIsForContactPredicate extends BaseLinkedPredicate<Followup, Contact> {
    public FollowupIsForContactPredicate(Contact key) {
        super(key);
    }

    @Override
    public boolean isLinked(Followup item, Contact other) {
        return item.isFor(other);
    }
}

