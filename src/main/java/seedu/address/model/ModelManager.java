package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.followup.Followup;
import seedu.address.model.person.Person;
import seedu.address.model.story.Story;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Story> filteredStories;
    private final FilteredList<Followup> filteredFollowups;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredStories = new FilteredList<>(this.addressBook.getStoryList());
        filteredFollowups = new FilteredList<>(this.addressBook.getFollowupList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Map<String, String> getShortcuts() {
        return userPrefs.getShortcuts();
    }

    @Override
    public void assignShortcut(String shortcut, String commandWord) {
        userPrefs.assignShortcut(shortcut, commandWord);
    }

    @Override
    public boolean removeShortcut(String shortcut) {
        return userPrefs.removeShortcut(shortcut);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void deleteStory(Story target) {
        addressBook.removeStory(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    //=========== Story Operations ===========================================================================

    @Override
    public boolean hasStory(Story story) {
        requireNonNull(story);
        return addressBook.hasStory(story);
    }

    @Override
    public void addStory(Story story) {
        addressBook.addStory(story);
        updateFilteredStoryList(PREDICATE_SHOW_ALL_STORIES);
    }

    @Override
    public void assignStory(Person target, Story story) {
        requireAllNonNull(target, story);

        addressBook.assignStory(target, story);
    }

    @Override
    public void unassignStory(Person target, Story story) {
        requireAllNonNull(target, story);

        addressBook.unassignStory(target, story);
    }
    //=========== Followup Operations ===========================================================================

    @Override
    public boolean hasFollowup(Followup followup) {
        requireNonNull(followup);
        return addressBook.hasFollowup(followup);
    }

    @Override
    public void addFollowup(Followup followup) {
        addressBook.addFollowup(followup);
        updateFilteredFollowupList(PREDICATE_SHOW_ALL_FOLLOWUPS);
    }

    @Override
    public void deleteFollowup(Followup followup) {
        addressBook.removeFollowup(followup);
        updateFilteredFollowupList(PREDICATE_SHOW_ALL_FOLLOWUPS);
    }
    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Filtered Story List Accessors =============================================================
    /**
     * Returns an unmodifiable view of the list of {@code Story} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<Story> getFilteredStoryList() {
        return filteredStories;
    }

    @Override
    public void updateFilteredStoryList(Predicate<Story> predicate) {
        requireNonNull(predicate);
        filteredStories.setPredicate(predicate);
    }

    @Override
    public void setFollowup(Followup target, Followup editedFollowup) {
        requireAllNonNull(target, editedFollowup);
        addressBook.setFollowup(target, editedFollowup);
    }

    //=========== Filtered Followup List Accessors =============================================================
    /**
     * Returns an unmodifiable view of the list of {@code Followup} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<Followup> getFilteredFollowupList() {
        return filteredFollowups;
    }

    @Override
    public void updateFilteredFollowupList(Predicate<Followup> predicate) {
        requireNonNull(predicate);
        filteredFollowups.setPredicate(predicate);
    }

    //=========== Object Overrides ==========================================================================
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager otherModelManager)) {
            return false;
        }

        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && filteredStories.equals(otherModelManager.filteredStories);
    }
}
