package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedContact.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalContacts.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.contact.Address;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Email;
import seedu.address.model.contact.Name;
import seedu.address.model.contact.Phone;
import seedu.address.model.story.StoryName;
import seedu.address.model.tag.Tag;

public class JsonAdaptedContactTest {
    private static final String INVALID_NAME = " ";
    private static final String INVALID_PHONE = " ";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = " ";
    private static final String INVALID_TAG = " ";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validContactDetails_returnsContact() throws Exception {
        JsonAdaptedContact contact = new JsonAdaptedContact(BENSON);
        assertEquals(BENSON, contact.toModelType());
    }

    @Test
    public void toModelType_startsWithSlash_returnsContact() throws Exception {
        JsonAdaptedContact contact =
                new JsonAdaptedContact("/Rachel", "/651234", "/example.com", "/address",
                        List.of(new JsonAdaptedTag("/friend")));
        Contact expected = new Contact(new Name("/Rachel"), new Phone("/651234"), new Email("/example.com"),
                new Address("/address"), Set.of(new Tag("/friend")));
        assertEquals(expected, contact.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedContact contact =
                new JsonAdaptedContact(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, contact::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedContact contact = new JsonAdaptedContact(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, contact::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedContact contact =
                new JsonAdaptedContact(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, contact::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedContact contact = new JsonAdaptedContact(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, contact::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedContact contact =
                new JsonAdaptedContact(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, contact::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedContact contact = new JsonAdaptedContact(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, contact::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedContact contact =
                new JsonAdaptedContact(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, contact::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedContact contact = new JsonAdaptedContact(VALID_NAME, VALID_PHONE, VALID_EMAIL, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, contact::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedContact contact =
                new JsonAdaptedContact(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, invalidTags);
        assertThrows(IllegalValueException.class, contact::toModelType);
    }

    @Test
    public void toModelType_nullTag_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(null);
        JsonAdaptedContact contact =
                new JsonAdaptedContact(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, invalidTags);

        assertThrows(IllegalValueException.class,
                "Contact contains an invalid tag.", contact::toModelType);
    }

    @Test
    public void toModelType_validStories_returnsContactWithStories() throws Exception {
        List<JsonAdaptedStoryName> validStories = List.of(
                new JsonAdaptedStoryName("Housing Investigation"),
                new JsonAdaptedStoryName("Education Reform"));

        JsonAdaptedContact contact = new JsonAdaptedContact(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS, validStories);

        List<StoryName> expectedStories = List.of(
                new StoryName("Housing Investigation"),
                new StoryName("Education Reform"));

        assertEquals(expectedStories, contact.toModelType().getStories());
    }

    @Test
    public void toModelType_invalidStoryName_throwsIllegalValueException() {
        List<JsonAdaptedStoryName> invalidStories =
                List.of(new JsonAdaptedStoryName(""));

        JsonAdaptedContact contact = new JsonAdaptedContact(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS, invalidStories);

        assertThrows(IllegalValueException.class, StoryName.MESSAGE_CONSTRAINTS, contact::toModelType);
    }

    @Test
    public void toModelType_nullStory_throwsIllegalValueException() {
        List<JsonAdaptedStoryName> invalidStories = new ArrayList<>();
        invalidStories.add(null);

        JsonAdaptedContact contact = new JsonAdaptedContact(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS, invalidStories);

        assertThrows(IllegalValueException.class,
                "Contact contains an invalid story.", contact::toModelType);
    }

    @Test
    public void toModelType_noStories_returnsContactWithEmptyStories() throws Exception {
        JsonAdaptedContact contact = new JsonAdaptedContact(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);

        assertEquals(List.of(), contact.toModelType().getStories());
    }
}
