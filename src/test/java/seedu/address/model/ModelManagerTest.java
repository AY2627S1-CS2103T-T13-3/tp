package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CONTACTS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_FOLLOWUPS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STORIES;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalContacts.ALICE;
import static seedu.address.testutil.TypicalContacts.BENSON;
import static seedu.address.testutil.TypicalContacts.BOB;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.contact.NameContainsKeywordsPredicate;
import seedu.address.model.contact.exceptions.ContactNotFoundException;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.model.story.exceptions.StoryNotFoundException;
import seedu.address.testutil.AddressBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void constructor_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager = new ModelManager(new AddressBook(), userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setGuiSettings(new GuiSettings(5, 6, 7, 8));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void hasContact_nullContact_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasContact(null));
    }

    @Test
    public void hasContact_contactNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasContact(ALICE));
    }

    @Test
    public void hasContact_contactInAddressBook_returnsTrue() {
        modelManager.addContact(ALICE);
        assertTrue(modelManager.hasContact(ALICE));
    }

    @Test
    public void removeContact_contactNotInAddressBook_throws() {
        modelManager.addContact(ALICE);
        assertThrows(ContactNotFoundException.class, () -> modelManager.deleteContact(BOB));
        assertTrue(modelManager.hasContact(ALICE));
        assertFalse(modelManager.hasContact(BOB));
    }

    @Test
    public void removeStory_storyNotInAddressBook_throws() {
        Story s = new Story(new StoryName("ant"), Set.of());
        Story s1 = new Story(new StoryName("bee"), Set.of());
        modelManager.addStory(s);
        assertThrows(StoryNotFoundException.class, () -> modelManager.deleteStory(s1));
        assertTrue(modelManager.hasStory(s));
        assertFalse(modelManager.hasStory(s1));
    }

    @Test
    public void getFilteredContactList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredContactList().remove(0));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withContact(ALICE).withContact(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().toString().split("\\s+");
        modelManager.updateFilteredContactList(new NameContainsKeywordsPredicate(List.of(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredContactList(PREDICATE_SHOW_ALL_CONTACTS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }

    @Test
    public void getContactFilterStatusProperty_defaultAndUpdates_success() {
        ModelManager modelManager = new ModelManager();
        assertEquals(Model.FILTER_STATUS_ALL, modelManager.getContactFilterStatusProperty().getValue());

        modelManager.updateFilteredContactList(c -> true, "Filtered by story: \"Story\"");
        assertEquals("Filtered by story: \"Story\"", modelManager.getContactFilterStatusProperty().getValue());

        modelManager.updateFilteredContactList(PREDICATE_SHOW_ALL_CONTACTS);
        assertEquals(Model.FILTER_STATUS_ALL, modelManager.getContactFilterStatusProperty().getValue());
    }

    @Test
    public void getStoryFilterStatusProperty_defaultAndUpdates_success() {
        ModelManager modelManager = new ModelManager();
        assertEquals(Model.FILTER_STATUS_ALL, modelManager.getStoryFilterStatusProperty().getValue());

        modelManager.updateFilteredStoryList(s -> true, "Filtered by contact: \"Alex\"");
        assertEquals("Filtered by contact: \"Alex\"", modelManager.getStoryFilterStatusProperty().getValue());

        modelManager.updateFilteredStoryList(PREDICATE_SHOW_ALL_STORIES);
        assertEquals(Model.FILTER_STATUS_ALL, modelManager.getStoryFilterStatusProperty().getValue());
    }

    @Test
    public void getFollowupFilterStatusProperty_defaultAndUpdates_success() {
        ModelManager modelManager = new ModelManager();
        assertEquals(Model.FILTER_STATUS_ALL, modelManager.getFollowupFilterStatusProperty().getValue());

        modelManager.updateFilteredFollowupList(f -> true, "Filtered by contact: \"Alex\"");
        assertEquals("Filtered by contact: \"Alex\"", modelManager.getFollowupFilterStatusProperty().getValue());

        modelManager.updateFilteredFollowupList(PREDICATE_SHOW_ALL_FOLLOWUPS);
        assertEquals(Model.FILTER_STATUS_ALL, modelManager.getFollowupFilterStatusProperty().getValue());
    }
}
