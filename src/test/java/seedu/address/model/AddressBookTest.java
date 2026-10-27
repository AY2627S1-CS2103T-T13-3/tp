package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalContacts.ALICE;
import static seedu.address.testutil.TypicalContacts.BOB;
import static seedu.address.testutil.TypicalContacts.getTypicalAddressBook;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.exceptions.ContactNotFoundException;
import seedu.address.model.contact.exceptions.DuplicateContactException;
import seedu.address.model.followup.Followup;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.model.story.exceptions.StoryNotFoundException;
import seedu.address.testutil.ContactBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(List.of(), addressBook.getContactList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicateContacts_throwsDuplicateContactException() {
        // Two contacts with the same identity fields
        Contact editedAlice = new ContactBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Contact> newContacts = List.of(ALICE, editedAlice);
        List<Story> newStories = List.of();
        List<Followup> newFollowups = List.of();
        AddressBookStub newData = new AddressBookStub(newContacts, newStories, newFollowups);

        assertThrows(DuplicateContactException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasContact_nullContact_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasContact(null));
    }

    @Test
    public void hasContact_contactNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasContact(ALICE));
    }

    @Test
    public void hasContact_contactInAddressBook_returnsTrue() {
        addressBook.addContact(ALICE);
        assertTrue(addressBook.hasContact(ALICE));
    }

    @Test
    public void hasContact_contactWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addContact(ALICE);
        Contact editedAlice = new ContactBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasContact(editedAlice));
    }

    @Test
    public void getContactList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getContactList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{contacts=" + addressBook.getContactList()
                + ", stories=" + addressBook.getStoryList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    @Test
    public void removeContact_contactNotAdded_throws() {
        AddressBook newData = new AddressBook();
        newData.addContact(ALICE);
        assertThrows(ContactNotFoundException.class, () -> newData.removeContact(BOB));
        assertTrue(newData.hasContact(ALICE));
        assertFalse(newData.hasContact(BOB));
    }

    @Test
    public void removeStory_storyNotAdded_throws() {
        AddressBook newData = new AddressBook();
        Story s = new Story(new StoryName("ant"), Set.of());
        Story s1 = new Story(new StoryName("bee"), Set.of());
        newData.addStory(s);
        assertThrows(StoryNotFoundException.class, () -> newData.removeStory(s1));
        assertTrue(newData.hasStory(s));
        assertFalse(newData.hasStory(s1));
    }

    /**
     * A stub ReadOnlyAddressBook whose contacts list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Contact> contacts = FXCollections.observableArrayList();
        private final ObservableList<Story> stories = FXCollections.observableArrayList();
        private final ObservableList<Followup> followups = FXCollections.observableArrayList();

        AddressBookStub(Collection<Contact> contacts, Collection<Story> stories, Collection<Followup> followups) {
            this.contacts.setAll(contacts);
            this.stories.setAll(stories);
        }

        @Override
        public ObservableList<Contact> getContactList() {
            return contacts;
        }

        @Override
        public ObservableList<Story> getStoryList() {
            return stories;
        }

        @Override
        public ObservableList<Followup> getFollowupList() {
            return followups;
        }
    }

}
