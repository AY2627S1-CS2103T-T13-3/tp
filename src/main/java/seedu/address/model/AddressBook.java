package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
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
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Replaces the given story {@code target} in the list with {@code editedStory}.
     * {@code target} must exist in the address book.
     * The story identity of {@code editedStory} must not be the same as another existing story in the address book.
     */
    public void setStory(Story target, Story editedStory) {
        requireNonNull(editedStory);

        stories.setStory(target, editedStory);
    }

    /**
     * Removes person with {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person personKey) {
        persons.remove(personKey);
    }

    /**
     * Removes story with {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeStory(Story storyKey) {
        stories.remove(storyKey);
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

        return persons.equals(otherAddressBook.persons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
