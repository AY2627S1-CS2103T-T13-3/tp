package seedu.address.model.contact;

import seedu.address.model.BaseLinkedPredicate;
import seedu.address.model.story.Story;

public class ContactAssignedToStoryPredicate extends BaseLinkedPredicate<Contact, Story> {
    public ContactAssignedToStoryPredicate(Story key) {
        super(key);
    }

    @Override
    public boolean isLinked(Contact item, Story other) {
        return item.isAssigned(other);
    }
}
