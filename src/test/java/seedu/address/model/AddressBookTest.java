package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalFollowups.INTERVIEW_ALICE;
import static seedu.address.testutil.TypicalFollowups.NOTES_BENSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.exceptions.FollowupNotFoundException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.model.story.exceptions.StoryNotFoundException;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(List.of(), addressBook.getPersonList());
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
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = List.of(ALICE, editedAlice);
        List<Story> newStories = List.of();
        List<Followup> newFollowups = List.of();
        AddressBookStub newData = new AddressBookStub(newPersons, newStories, newFollowups);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{persons=" + addressBook.getPersonList()
                + ", stories=" + addressBook.getStoryList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    @Test
    public void removePerson_personNotAdded_throws() {
        AddressBook newData = new AddressBook();
        newData.addPerson(ALICE);
        assertThrows(PersonNotFoundException.class, () -> newData.removePerson(BOB));
        assertTrue(newData.hasPerson(ALICE));
        assertFalse(newData.hasPerson(BOB));
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

    @Test
    public void removeFollowup_followupNotAdded_throws() {
        AddressBook newData = new AddressBook();
        newData.addFollowup(INTERVIEW_ALICE);
        assertThrows(FollowupNotFoundException.class, () -> newData.removeFollowup(NOTES_BENSON));
        assertTrue(newData.hasFollowup(INTERVIEW_ALICE));
        assertFalse(newData.hasFollowup(NOTES_BENSON));
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Story> stories = FXCollections.observableArrayList();
        private final ObservableList<Followup> followups = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons, Collection<Story> stories, Collection<Followup> followups) {
            this.persons.setAll(persons);
            this.stories.setAll(stories);
            this.followups.setAll(followups);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
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
