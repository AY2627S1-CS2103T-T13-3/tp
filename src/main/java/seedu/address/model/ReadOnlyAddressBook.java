package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.contact.Contact;
import seedu.address.model.followup.Followup;
import seedu.address.model.story.Story;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the contacts list.
     * This list will not contain any duplicate contacts.
     */
    ObservableList<Contact> getContactList();

    /**
     * Returns an unmodifiable view of the stories list.
     * This list will not contain any duplicate stories.
     */
    ObservableList<Story> getStoryList();

    /**
     * Returns an unmodifiable view of the followups list.
     * This list will not contain any duplicate followups.
     */
    ObservableList<Followup> getFollowupList();

}
