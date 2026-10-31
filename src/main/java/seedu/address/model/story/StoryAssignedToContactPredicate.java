package seedu.address.model.story;


import seedu.address.model.BaseLinkedPredicate;
import seedu.address.model.contact.Contact;

public class StoryAssignedToContactPredicate extends BaseLinkedPredicate<Story, Contact> {
    public StoryAssignedToContactPredicate(Contact key) {
        super(key);
    }

    @Override
    public void notifyStateChanged(Contact oldState, Contact newState) {

        super.notifyStateChanged(oldState, newState);
    }

    @Override
    public boolean isLinked(Story item, Contact other) {
        return item.isAssigned(other);
    }
}
