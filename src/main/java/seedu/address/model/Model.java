package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Name;
import seedu.address.model.followup.Followup;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluates to true */
    Predicate<Contact> PREDICATE_SHOW_ALL_CONTACTS = unused -> true;
    Predicate<Story> PREDICATE_SHOW_ALL_STORIES = unused -> true;
    Predicate<Followup> PREDICATE_SHOW_ALL_FOLLOWUPS = unused -> true;

    /** Default filter status when all items are shown. */
    String FILTER_STATUS_ALL = "All";

    /** Format string for filter status when items are filtered by a contact. */
    String FILTER_STATUS_BY_CONTACT_FORMAT = "Filtered by contact: \"%s\"";

    /** Format string for filter status when items are filtered by a story. */
    String FILTER_STATUS_BY_STORY_FORMAT = "Filtered by story: \"%s\"";

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /** Returns the user's command shortcuts mapped to their command words. */
    Map<String, String> getShortcuts();

    /** Assigns a shortcut to a command, replacing conflicting mappings. */
    void assignShortcut(String shortcut, String commandWord);

    /** Removes a shortcut, returning whether a mapping was removed. */
    boolean removeShortcut(String shortcut);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a contact with the same identity as {@code contact} exists in the address book.
     */
    boolean hasContact(Contact contact);

    /**
     * Deletes the given contact.
     * The contact must exist in the address book.
     */
    void deleteContact(Contact target);

    /**
     * Deletes the given story.
     * The story must exist in the address book.
     */
    default void deleteStory(Story target) {}

    /**
     * Adds the given contact.
     * {@code contact} must not already exist in the address book.
     */
    void addContact(Contact contact);

    /**
     * Replaces the given contact {@code target} with {@code editedContact}.
     * {@code target} must exist in the address book.
     * The contact identity of {@code editedContact} must not be the same as another existing contact in the address
     * book.
     */
    void setContact(Contact target, Contact editedContact);

    /**
     * Returns true if a story with the same identity as {@code story} exists in the address book.
     */
    boolean hasStory(Story story);

    /**
     * Adds the given story.
     * {@code story} must not already exist in the address book.
     */
    void addStory(Story story);

    /**
     * Replaces the given story {@code target} with {@code editedStory}.
     * {@code target} must exist in the address book.
     * The story identity of {@code editedStory} must not be the same as another existing story in the address book.
     */
    void setStory(Story target, Story editedStory);

    /**
     * Assigns the given contact to the given story
     * {@code target} must exist in the address book.
     * {@code story} must exist in the address book.
     */
    void assignStory(Contact target, Story story);

    /**
     * Unassigns the given contact from the given story
     * {@code target} must exist in the address book.
     * {@code story} must exist in the address book.
     */
    void unassignStory(Contact target, Story story);

    /**
     * Returns true if a followup with the same identity as {@code followup} exists in the address book.
     */
    boolean hasFollowup(Followup followup);

    /**
     * Adds the given followup.
     * {@code followup} must not already exist in the address book.
     */
    void addFollowup(Followup followup);

    /**
     * Deletes the given followup.
     * The followup must exist in the address book.
     */
    void deleteFollowup(Followup followup);

    /** Replaces an existing followup with the given edited followup. */
    void setFollowup(Followup target, Followup editedFollowup);

    /** Returns an unmodifiable view of the filtered contact list */
    ObservableList<Contact> getFilteredContactList();

    /** Returns an unmodifiable view of the filtered story list */
    ObservableList<Story> getFilteredStoryList();

    /** Returns an unmodifiable view of the filtered followup list */
    ObservableList<Followup> getFilteredFollowupList();

    /**
     * Updates the filter of the filtered contact list to show contacts assigned to the specified {@code story}.
     * @throws NullPointerException if {@code story} is null.
     */
    default void updateFilteredContactList(Story story) {
        requireNonNull(story);
        updateFilteredContactList(story::isAssigned, story.getName());
    }

    /**
     * Updates the filter of the filtered contact list to filter by the given {@code predicate}
     * associated with the specified {@code storyName}.
     * @throws NullPointerException if {@code predicate} or {@code storyName} is null.
     */
    default void updateFilteredContactList(Predicate<Contact> predicate, StoryName storyName) {
        throw new AssertionError("This method should not be called.");
    }

    /**
     * Updates the filter of the filtered contact list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredContactList(Predicate<Contact> predicate);

    /**
     * Updates the filter of the filtered contact list to filter by the given {@code predicate}
     * with the specified {@code filterStatus} description.
     * @throws NullPointerException if {@code predicate} or {@code filterStatus} is null.
     */
    void updateFilteredContactList(Predicate<Contact> predicate, String filterStatus);

    /**
     * Updates the filter of the filtered story list to show stories assigned to the specified {@code contact}.
     * @throws NullPointerException if {@code contact} is null.
     */
    default void updateFilteredStoryList(Contact contact) {
        requireNonNull(contact);
        updateFilteredStoryList(story -> story.isAssigned(contact), contact.getName());
    }

    /**
     * Updates the filter of the filtered story list to filter by the given {@code predicate}
     * associated with the specified {@code contactName}.
     * @throws NullPointerException if {@code predicate} or {@code contactName} is null.
     */
    default void updateFilteredStoryList(Predicate<Story> predicate, Name contactName) {
        throw new AssertionError("This method should not be called.");
    }

    /**
     * Updates the filter of the filtered story list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredStoryList(Predicate<Story> predicate);

    /**
     * Updates the filter of the filtered story list to filter by the given {@code predicate}
     * with the specified {@code filterStatus} description.
     * @throws NullPointerException if {@code predicate} or {@code filterStatus} is null.
     */
    void updateFilteredStoryList(Predicate<Story> predicate, String filterStatus);

    /**
     * Updates the filter of the filtered followup list to show followups for the specified {@code contact}.
     * @throws NullPointerException if {@code contact} is null.
     */
    default void updateFilteredFollowupList(Contact contact) {
        requireNonNull(contact);
        updateFilteredFollowupList(followup -> followup.isFor(contact), contact.getName());
    }

    /**
     * Updates the filter of the filtered followup list to filter by the given {@code predicate}
     * associated with the specified {@code contactName}.
     * @throws NullPointerException if {@code predicate} or {@code contactName} is null.
     */
    default void updateFilteredFollowupList(Predicate<Followup> predicate, Name contactName) {
        throw new AssertionError("This method should not be called.");
    }

    /**
     * Updates the filter of the filtered followup list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredFollowupList(Predicate<Followup> predicate);

    /**
     * Updates the filter of the filtered followup list to filter by the given {@code predicate}
     * with the specified {@code filterStatus} description.
     * @throws NullPointerException if {@code predicate} or {@code filterStatus} is null.
     */
    void updateFilteredFollowupList(Predicate<Followup> predicate, String filterStatus);

    /** Returns the active story filter for the contact list, if any. */
    default Optional<StoryName> getActiveStoryFilter() {
        return Optional.empty();
    }

    /** Returns the active contact filter for the story list, if any. */
    default Optional<Name> getActiveContactFilterForStories() {
        return Optional.empty();
    }

    /** Returns the active contact filter for the followup list, if any. */
    default Optional<Name> getActiveContactFilterForFollowups() {
        return Optional.empty();
    }

    /** Returns the filter status property of the contact list. */
    ObservableValue<String> getContactFilterStatusProperty();

    /** Returns the filter status property of the story list. */
    ObservableValue<String> getStoryFilterStatusProperty();

    /** Returns the filter status property of the followup list. */
    ObservableValue<String> getFollowupFilterStatusProperty();
}
