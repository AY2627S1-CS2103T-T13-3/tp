package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.UniqueContactList;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.UniqueFollowupList;
import seedu.address.model.story.Story;
import seedu.address.model.story.UniqueStoryList;

/**
 * Wraps all data at the address-book level.
 * Duplicates are not allowed (by .isSameContact comparison).
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniqueContactList contacts = new UniqueContactList();
    private final UniqueStoryList stories = new UniqueStoryList();
    private final UniqueFollowupList followups = new UniqueFollowupList();

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Contacts in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the contact list with {@code contacts}.
     * {@code contacts} must not contain duplicate contacts.
     */
    public void setContacts(List<Contact> contacts) {
        this.contacts.setContacts(contacts);
    }

    /**
     * Replaces the contents of the story list with {@code stories}.
     * {@code contacts} must not contain duplicate contacts.
     */
    public void setStories(List<Story> stories) {
        this.stories.setStories(stories);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setContacts(newData.getContactList());
        setStories(newData.getStoryList());
        setFollowups(newData.getFollowupList());
    }

    //// contact-level and story-level operations

    /**
     * Returns true if a contact with the same identity as {@code contact} exists in the address book.
     */
    public boolean hasContact(Contact contact) {
        requireNonNull(contact);
        return contacts.contains(contact);
    }

    //// contact-level operations

    /**
     * Returns true if a story with the same identity as {@code story} exists in the address book.
     */
    public boolean hasStory(Story story) {
        requireNonNull(story);
        return stories.contains(story);
    }

    /**
     * Adds a contact to the address book.
     * The contact must not already exist in the address book.
     */
    public void addContact(Contact p) {
        contacts.add(p);
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
    public void assignStory(Contact target, Story story) {
        requireAllNonNull(target, story);

        Contact assignedContact = target.assign(story);
        Story assignedStory = story.assign(target);

        contacts.setContact(target, assignedContact);
        stories.setStory(story, assignedStory);
    }

    /**
     * Unassigns the given contact from the given story
     * {@code target} must exist in the address book.
     * {@code story} must exist in the address book.
     */
    public void unassignStory(Contact target, Story story) {
        requireAllNonNull(target, story);

        Contact unassignedContact = target.unassign(story);
        Story unassignedStory = story.unassign(target);

        contacts.setContact(target, unassignedContact);
        stories.setStory(story, unassignedStory);
    }

    /**
     * Replaces the given contact {@code target} in the list with {@code editedContact}.
     * {@code target} must exist in the address book.
     * The contact identity of {@code editedContact} must not be the same as another existing contact in the address
     * book.
     */
    public void setContact(Contact target, Contact editedContact) {
        requireNonNull(editedContact);

        contacts.setContact(target, editedContact);
        stories.setStories(
                getStoryList().stream()
                        .map(story -> story.isAssigned(target)
                                ? story.unassign(target).assign(editedContact)
                                : story
                        )
                        .toList()
        );
        followups.setFollowups(
                getFollowupList().stream()
                        .map(fu -> fu.isFor(target)
                            ? new Followup(fu.getDescription(), editedContact.getName())
                            : fu
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
        contacts.setContacts(
                getContactList().stream()
                        .map(contact -> contact.isAssigned(target)
                                ? contact.unassign(target).assign(editedStory)
                                : contact
                        )
                        .toList()
        );
    }

    /**
     * Removes contact with {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeContact(Contact contactKey) {
        stories.setStories(
                getStoryList().stream()
                        .map(story -> story.unassign(contactKey))
                        .toList()
        );
        followups.setFollowups(
                getFollowupList().stream()
                        .filter(fu -> !fu.isFor(contactKey))
                        .toList()
        );
        contacts.remove(contactKey);
    }

    /**
     * Removes story with {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeStory(Story storyKey) {
        contacts.setContacts(
                getContactList().stream()
                        .map(contact -> contact.unassign(storyKey))
                        .toList()
        );
        stories.remove(storyKey);
    }

    //// followups

    /**
     * Returns true if a followup with the same identity as {@code followup} exists in the address book.
     */
    public boolean hasFollowup(Followup followup) {
        requireNonNull(followup);
        return followups.contains(followup);
    }

    /**
     * Adds a followup to the address book.
     * The followup must not already exist in the address book.
     */
    public void addFollowup(Followup followup) {
        followups.add(followup);
    }

    /**
     * Removes the specified followup from the address book.
     * The followup must exist in the address book.
     */
    public void removeFollowup(Followup followup) {
        followups.remove(followup);
    }

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
                .add("contacts", contacts)
                .add("stories", stories)
                .toString();
    }

    @Override
    public ObservableList<Contact> getContactList() {
        return contacts.asUnmodifiableObservableList();
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

        return contacts.equals(otherAddressBook.contacts) && stories.equals(otherAddressBook.stories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contacts, stories);
    }
}
