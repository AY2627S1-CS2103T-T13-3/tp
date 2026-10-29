package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.contact.Contact;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.testutil.TypicalContacts;
import seedu.address.testutil.TypicalStories;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_CONTACTS_FILE = TEST_DATA_FOLDER.resolve("typicalContactsAddressBook.json");
    private static final Path INVALID_CONTACT_FILE = TEST_DATA_FOLDER.resolve("invalidContactAddressBook.json");
    private static final Path DUPLICATE_CONTACT_FILE = TEST_DATA_FOLDER.resolve("duplicateContactAddressBook.json");

    private static final String VALID_DESCRIPTION = "Request interview";
    private static final StoryName ASSIGNED_STORY_NAME = new StoryName("Assigned Story");

    @Test
    public void toModelType_typicalContactsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_CONTACTS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalContactsAddressBook = TypicalContacts.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalContactsAddressBook);
    }

    @Test
    public void toModelType_invalidContactFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_CONTACT_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateContacts_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_CONTACT_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_CONTACT,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_validFollowups_success() throws Exception {
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(new JsonAdaptedContact(TypicalContacts.ALICE)),
                null,
                List.of(new JsonAdaptedFollowup(
                        VALID_DESCRIPTION,
                        TypicalContacts.ALICE.getName().toString())));

        Followup expected = new Followup(
                new FollowupDescription(VALID_DESCRIPTION),
                TypicalContacts.ALICE.getName());

        assertEquals(List.of(expected), data.toModelType().getFollowupList());
    }

    @Test
    public void toModelType_missingFollowups_success() throws Exception {
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(new JsonAdaptedContact(TypicalContacts.ALICE)),
                null,
                null);

        assertEquals(List.of(), data.toModelType().getFollowupList());
    }

    @Test
    public void toModelType_invalidFollowupContact_throwsIllegalValueException() {
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(new JsonAdaptedContact(TypicalContacts.ALICE)),
                null,
                List.of(new JsonAdaptedFollowup(
                        VALID_DESCRIPTION, "Unknown Contact")));

        assertThrows(IllegalValueException.class,
                JsonSerializableAddressBook.MESSAGE_INVALID_CONTACT,
                data::toModelType);
    }

    @Test
    public void toModelType_duplicateFollowups_throwsIllegalValueException() {
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(new JsonAdaptedContact(TypicalContacts.ALICE)),
                null,
                List.of(
                        new JsonAdaptedFollowup(
                                VALID_DESCRIPTION,
                                TypicalContacts.ALICE.getName().toString()),
                        new JsonAdaptedFollowup(
                                VALID_DESCRIPTION,
                                TypicalContacts.ALICE.getName().toString())));

        assertThrows(IllegalValueException.class,
                JsonSerializableAddressBook.MESSAGE_DUPLICATE_FOLLOWUP,
                data::toModelType);
    }

    @Test
    public void toModelType_validStories_success() throws Exception {
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                null,
                List.of(new JsonAdaptedStory(TypicalStories.STORY_A)),
                null);

        assertEquals(List.of(TypicalStories.STORY_A), data.toModelType().getStoryList());
    }

    @Test
    public void toModelType_missingStories_success() throws Exception {
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(null, null, null);

        assertEquals(List.of(), data.toModelType().getStoryList());
    }

    @Test
    public void toModelType_duplicateStories_throwsIllegalValueException() {
        JsonAdaptedStory story = new JsonAdaptedStory(TypicalStories.STORY_A);
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                null,
                List.of(story, story),
                null);

        assertThrows(IllegalValueException.class,
                JsonSerializableAddressBook.MESSAGE_DUPLICATE_STORY,
                data::toModelType);
    }

    @Test
    public void toModelType_contactRefersToNonexistentStory_throwsIllegalValueException() {
        Contact contact = contactAssignedTo(ASSIGNED_STORY_NAME);
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(new JsonAdaptedContact(contact)), null, null);

        assertThrows(IllegalValueException.class,
                JsonSerializableAddressBook.MESSAGE_CONTACT_REFERS_TO_NONEXISTENT_STORY,
                data::toModelType);
    }

    @Test
    public void toModelType_storyRefersToNonexistentContact_throwsIllegalValueException() {
        Story story = new Story(ASSIGNED_STORY_NAME, Set.of(), List.of(TypicalContacts.ALICE.getName()));
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                null, List.of(new JsonAdaptedStory(story)), null);

        assertThrows(IllegalValueException.class,
                JsonSerializableAddressBook.MESSAGE_STORY_REFERS_TO_NONEXISTENT_CONTACT,
                data::toModelType);
    }

    @Test
    public void toModelType_contactOnlyAssignment_throwsIllegalValueException() {
        Contact contact = contactAssignedTo(ASSIGNED_STORY_NAME);
        Story story = new Story(ASSIGNED_STORY_NAME, Set.of());
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(new JsonAdaptedContact(contact)), List.of(new JsonAdaptedStory(story)), null);

        assertThrows(IllegalValueException.class,
                JsonSerializableAddressBook.MESSAGE_INCONSISTENT_CONTACT_STORY_ASSIGNMENT,
                data::toModelType);
    }

    @Test
    public void toModelType_storyOnlyAssignment_throwsIllegalValueException() {
        Story story = new Story(ASSIGNED_STORY_NAME, Set.of(), List.of(TypicalContacts.ALICE.getName()));
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(new JsonAdaptedContact(TypicalContacts.ALICE)), List.of(new JsonAdaptedStory(story)), null);

        assertThrows(IllegalValueException.class,
                JsonSerializableAddressBook.MESSAGE_INCONSISTENT_CONTACT_STORY_ASSIGNMENT,
                data::toModelType);
    }

    @Test
    public void toModelType_duplicateAssignedStories_throwsIllegalValueException() {
        Contact contact = contactAssignedTo(ASSIGNED_STORY_NAME, ASSIGNED_STORY_NAME);
        Story story = new Story(ASSIGNED_STORY_NAME, Set.of(), List.of(TypicalContacts.ALICE.getName()));
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(new JsonAdaptedContact(contact)), List.of(new JsonAdaptedStory(story)), null);

        assertThrows(IllegalValueException.class,
                JsonSerializableAddressBook.MESSAGE_DUPLICATE_CONTACT_STORY_ASSIGNMENT,
                data::toModelType);
    }

    @Test
    public void toModelType_duplicateAssignedContacts_throwsIllegalValueException() {
        Contact contact = contactAssignedTo(ASSIGNED_STORY_NAME);
        Story story = new Story(ASSIGNED_STORY_NAME, Set.of(),
                List.of(TypicalContacts.ALICE.getName(), TypicalContacts.ALICE.getName()));
        JsonSerializableAddressBook data = new JsonSerializableAddressBook(
                List.of(new JsonAdaptedContact(contact)), List.of(new JsonAdaptedStory(story)), null);

        assertThrows(IllegalValueException.class,
                JsonSerializableAddressBook.MESSAGE_DUPLICATE_STORY_CONTACT_ASSIGNMENT,
                data::toModelType);
    }

    private Contact contactAssignedTo(StoryName ... stories) {
        return new Contact(TypicalContacts.ALICE.getName(), TypicalContacts.ALICE.getPhone(),
                TypicalContacts.ALICE.getEmail(), TypicalContacts.ALICE.getAddress(), TypicalContacts.ALICE.getTags(),
                List.of(stories));
    }
}
