package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.UniqueFollowupList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.story.Story;
import seedu.address.model.story.UniqueStoryList;

/**
 * Wraps all data at the address-book level.
 * Duplicates are not allowed (by .isSamePerson comparison).
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons = new UniquePersonList();
    private final UniqueStoryList stories = new UniqueStoryList();
    private final UniqueFollowupList followups = new UniqueFollowupList();

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the story list with {@code stories}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setStories(List<Story> stories) {
        this.stories.setStories(stories);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setStories(newData.getStoryList());
    }

    //// person-level and story-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    //// person-level operations

    /**
     * Returns true if a story with the same identity as {@code story} exists in the address book.
     */
    public boolean hasStory(Story story) {
        requireNonNull(story);
        return stories.contains(story);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Adds a story to the address book.
     * The story must not already exist in the address book.
     */
    public void addStory(Story story) {
        stories.add(story);
    }

    /**
     * Assigns the given contact to the given story
     * {@code target} must exist in the address book.
     * {@code story} must exist in the address book.
     */
    public void assignStory(Person target, Story story) {
        requireAllNonNull(target, story);

        Person assignedPerson = target.assign(story);
        Story assignedStory = story.assign(target);

        persons.setPerson(target, assignedPerson);
        stories.setStory(story, assignedStory);
    }

    /**
     * Unassigns the given contact from the given story
     * {@code target} must exist in the address book.
     * {@code story} must exist in the address book.
     */
    public void unassignStory(Person target, Story story) {
        requireAllNonNull(target, story);

        Person unassignedPerson = target.unassign(story);
        Story unassignedStory = story.unassign(target);

        persons.setPerson(target, unassignedPerson);
        stories.setStory(story, unassignedStory);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
        stories.setStories(
                getStoryList().stream()
                        .map(story -> story.isAssigned(target)
                                ? story.unassign(target).assign(editedPerson)
                                : story
                        )
                        .toList()
        );
    }

    /**
     * Replaces the given story {@code target} in the list with {@code editedStory}.
     * {@code target} must exist in the address book.
     * The story identity of {@code editedStory} must not be the same as another existing story in the address book.
     */
    public void setStory(Story target, Story editedStory) {
        requireNonNull(editedStory);

        stories.setStory(target, editedStory);
        persons.setPersons(
                getPersonList().stream()
                        .map(person -> person.isAssigned(target)
                                ? person.unassign(target).assign(editedStory)
                                : person
                        )
                        .toList()
        );
    }

    /**
     * Removes person with {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person personKey) {
        stories.setStories(
                getStoryList().stream()
                        .map(story -> story.unassign(personKey))
                        .toList()
        );
        persons.remove(personKey);
    }

    /**
     * Removes story with {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeStory(Story storyKey) {
        persons.setPersons(
                getPersonList().stream()
                        .map(contact -> contact.unassign(storyKey))
                        .toList()
        );
        stories.remove(storyKey);
    }

    //// followups

    @Override
    public ObservableList<Followup> getFollowupList() {
        return followups.asUnmodifiableObservableList();
    }

    /**
     * Replaces the contents of the followups list with {@code followups}.
     * {@code followups} must not contain duplicate followups.
     */
    public void setFollowups(List<Followup> followups) {
        this.followups.setFollowups(followups);
    }


    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("stories", stories)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Story> getStoryList() {
        return stories.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook otherAddressBook)) {
            return false;
        }

        return persons.equals(otherAddressBook.persons) && stories.equals(otherAddressBook.stories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons, stories);
    }
}
