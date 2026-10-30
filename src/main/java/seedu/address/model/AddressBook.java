package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Name;
import seedu.address.model.contact.UniqueContactList;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.UniqueFollowupList;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.model.story.UniqueStoryList;

/**
 * Wraps all data at the address-book level.
 * Duplicate contacts, stories, and followups are not allowed, based on
 * {@code isSameContact}, {@code isSameStory}, and {@code isSameFollowup} respectively.
 */
public class AddressBook implements ReadOnlyAddressBook {

    public static final String MESSAGE_INVALID_CONTACT =
            "Followup refers to a nonexistent contact.";
    public static final String MESSAGE_CONTACT_REFERS_TO_NONEXISTENT_STORY =
            "Contact refers to a nonexistent story.";
    public static final String MESSAGE_STORY_REFERS_TO_NONEXISTENT_CONTACT =
            "Story refers to a nonexistent contact.";
    public static final String MESSAGE_INCONSISTENT_CONTACT_STORY_ASSIGNMENT =
            "Contact and story assignments are inconsistent.";
    public static final String MESSAGE_DUPLICATE_CONTACT_STORY_ASSIGNMENT =
            "Contact has duplicate assigned stories.";
    public static final String MESSAGE_DUPLICATE_STORY_CONTACT_ASSIGNMENT =
            "Story has duplicate assigned contacts.";

    private final UniqueContactList contacts = new UniqueContactList();
    private final UniqueStoryList stories = new UniqueStoryList();
    private final UniqueFollowupList followups = new UniqueFollowupList();

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Contacts, Stories and Followups in the {@code toBeCopied}
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
     * {@code stories} must not contain duplicate stories.
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

    /**
     * Returns true if a contact with the same name as {@code name} exists in the address book.
     */
    public boolean hasContactName(Name name) {
        requireNonNull(name);
        return contacts.asUnmodifiableObservableList().stream()
                .anyMatch(contact -> contact.getName().equals(name));
    }

    /**
     * Returns true if a story with the same identity as {@code story} exists in the address book.
     */
    public boolean hasStory(Story story) {
        requireNonNull(story);
        return stories.contains(story);
    }

    /**
     * Returns true if a story with the same name as {@code name} exists in the address book.
     */
    public boolean hasStoryName(StoryName name) {
        requireNonNull(name);
        return stories.asUnmodifiableObservableList().stream()
                .anyMatch(story -> story.getName().equals(name));
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

    /** Replaces an existing followup with the given edited followup. */
    public void setFollowup(Followup target, Followup editedFollowup) {
        followups.setFollowup(target, editedFollowup);
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

    //// Relational integrity validation

    /**
     * Validates all relational integrity invariants in the address book:
     * - Contact-Story assignments are unique, reciprocal, and refer to existing entities.
     * - Followups refer to existing contacts.
     *
     * @throws IllegalValueException if relational constraints are violated.
     */
    public void validateRelationalIntegrity() throws IllegalValueException {
        validateContactStoryRelationships();
        validateFollowupRelationships();
    }

    /**
     * Ensures all Contact-Story assignments are unique, reciprocal, and refer to existing entities.
     *
     * @throws IllegalValueException if contact-story constraints are violated.
     */
    public void validateContactStoryRelationships() throws IllegalValueException {
        validateNoDuplicateAssignments();

        Set<ContactStoryAssignment> contactAssignments = getAssignmentsFromContacts();
        Set<ContactStoryAssignment> storyAssignments = getAssignmentsFromStories();

        validateAssignmentReferencesExist(contactAssignments, storyAssignments);
        validateAssignmentsAreReciprocal(contactAssignments, storyAssignments);
    }

    /**
     * Ensures all followups refer to existing contacts.
     *
     * @throws IllegalValueException if a followup refers to a nonexistent contact.
     */
    public void validateFollowupRelationships() throws IllegalValueException {
        Set<Name> contactNames = contacts.asUnmodifiableObservableList().stream()
                .map(Contact::getName)
                .collect(Collectors.toSet());
        boolean hasInvalidFollowup = followups.asUnmodifiableObservableList().stream()
                .anyMatch(fu -> !contactNames.contains(fu.getContact()));
        if (hasInvalidFollowup) {
            throw new IllegalValueException(MESSAGE_INVALID_CONTACT);
        }
    }

    /**
     * Ensures contacts and stories do not contain duplicate assignment references.
     */
    private void validateNoDuplicateAssignments() throws IllegalValueException {
        boolean hasDuplicateAssignedStories = contacts.asUnmodifiableObservableList().stream()
                .anyMatch(contact -> containsDuplicates(contact.getStories()));
        if (hasDuplicateAssignedStories) {
            throw new IllegalValueException(MESSAGE_DUPLICATE_CONTACT_STORY_ASSIGNMENT);
        }

        boolean hasDuplicateAssignedContacts = stories.asUnmodifiableObservableList().stream()
                .anyMatch(story -> containsDuplicates(story.getContacts()));
        if (hasDuplicateAssignedContacts) {
            throw new IllegalValueException(MESSAGE_DUPLICATE_STORY_CONTACT_ASSIGNMENT);
        }
    }

    /**
     * Ensures every assignment refers to an existing contact and story.
     */
    private void validateAssignmentReferencesExist(
            Set<ContactStoryAssignment> contactAssignments, Set<ContactStoryAssignment> storyAssignments)
            throws IllegalValueException {
        Set<Name> contactNames = contacts.asUnmodifiableObservableList().stream()
                .map(Contact::getName)
                .collect(Collectors.toSet());
        Set<StoryName> storyNames = stories.asUnmodifiableObservableList().stream()
                .map(Story::getName)
                .collect(Collectors.toSet());

        boolean contactReferencesMissingStory = contactAssignments.stream()
                .anyMatch(assignment -> !storyNames.contains(assignment.storyName()));
        if (contactReferencesMissingStory) {
            throw new IllegalValueException(MESSAGE_CONTACT_REFERS_TO_NONEXISTENT_STORY);
        }

        boolean storyReferencesMissingContact = storyAssignments.stream()
                .anyMatch(assignment -> !contactNames.contains(assignment.contactName()));
        if (storyReferencesMissingContact) {
            throw new IllegalValueException(MESSAGE_STORY_REFERS_TO_NONEXISTENT_CONTACT);
        }
    }

    /**
     * Ensures contacts and stories describe the same assignments.
     */
    private void validateAssignmentsAreReciprocal(Set<ContactStoryAssignment> contactAssignments,
            Set<ContactStoryAssignment> storyAssignments) throws IllegalValueException {
        if (!contactAssignments.equals(storyAssignments)) {
            throw new IllegalValueException(MESSAGE_INCONSISTENT_CONTACT_STORY_ASSIGNMENT);
        }
    }

    private Set<ContactStoryAssignment> getAssignmentsFromContacts() {
        return contacts.asUnmodifiableObservableList().stream()
                .flatMap(contact -> contact.getStories().stream()
                        .map(storyName -> new ContactStoryAssignment(contact.getName(), storyName)))
                .collect(Collectors.toSet());
    }

    private Set<ContactStoryAssignment> getAssignmentsFromStories() {
        return stories.asUnmodifiableObservableList().stream()
                .flatMap(story -> story.getContacts().stream()
                        .map(contactName -> new ContactStoryAssignment(contactName, story.getName())))
                .collect(Collectors.toSet());
    }

    private static boolean containsDuplicates(List<?> values) {
        return new HashSet<>(values).size() != values.size();
    }

    private record ContactStoryAssignment(Name contactName, StoryName storyName) {}

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("contacts", contacts)
                .add("stories", stories)
                .add("followups", followups)
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

        return contacts.equals(otherAddressBook.contacts)
                && stories.equals(otherAddressBook.stories)
                && followups.equals(otherAddressBook.followups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contacts, stories, followups);
    }
}
