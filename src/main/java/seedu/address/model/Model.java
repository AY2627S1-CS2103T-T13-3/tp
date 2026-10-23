package seedu.address.model;

import java.util.Map;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.followup.Followup;
import seedu.address.model.person.Person;
import seedu.address.model.story.Story;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluates to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;
    Predicate<Story> PREDICATE_SHOW_ALL_STORIES = unused -> true;
    Predicate<Followup> PREDICATE_SHOW_ALL_FOLLOWUPS = unused -> true;

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
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Deletes the given story.
     * The story must exist in the address book.
     */
    default void deleteStory(Story target) {}

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

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
    void assignStory(Person target, Story story);

    /**
     * Unassigns the given contact from the given story
     * {@code target} must exist in the address book.
     * {@code story} must exist in the address book.
     */
    void unassignStory(Person target, Story story);

    /**
     * Returns true if a followup with the same identity as {@code followup} exists in the address book.
     */
    boolean hasFollowup(Followup followup);

    /**
     * Adds the given story.
     * {@code story} must not already exist in the address book.
     */
    void addFollowup(Followup followup);

    /**
     * Deletes the given followup.
     * The followup must exist in the address book.
     */
    void deleteFollowup(Followup followup);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /** Returns an unmodifiable view of the filtered story list */
    ObservableList<Story> getFilteredStoryList();

    /** Returns an unmodifiable view of the filtered story list */
    ObservableList<Followup> getFilteredFollowupList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Updates the filter of the filtered story list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredStoryList(Predicate<Story> predicate);

    /**
     * Updates the filter of the filtered followup list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredFollowupList(Predicate<Followup> predicate);
}
