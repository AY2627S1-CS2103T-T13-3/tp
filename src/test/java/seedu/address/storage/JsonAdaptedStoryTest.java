package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.contact.Name;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;

/**
 * Contains unit tests for JsonAdaptedStory.
 */
public class JsonAdaptedStoryTest {

    private static final String VALID_NAME = "Housing Investigation";
    private static final String INVALID_NAME = "Housing/Investigation";

    @Test
    public void toModelType_validStoryDetails_returnsStory() throws Exception {
        JsonAdaptedStory story = new JsonAdaptedStory(VALID_NAME, List.of());

        Story modelStory = story.toModelType();

        assertEquals(new StoryName(VALID_NAME), modelStory.getName());
        assertTrue(modelStory.getContacts().isEmpty());
    }

    @Test
    public void toModelType_validContacts_returnsStoryWithContacts() throws Exception {
        List<JsonAdaptedName> validContacts = List.of(
                new JsonAdaptedName("Alex Yeoh"),
                new JsonAdaptedName("Bernice Yu"));

        JsonAdaptedStory story = new JsonAdaptedStory(
                VALID_NAME, List.of(), validContacts);

        List<Name> expectedContacts = List.of(
                new Name("Alex Yeoh"),
                new Name("Bernice Yu"));

        assertEquals(expectedContacts, story.toModelType().getContacts());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedStory story = new JsonAdaptedStory(INVALID_NAME, List.of());

        assertThrows(IllegalValueException.class,
                StoryName.MESSAGE_CONSTRAINTS, story::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedStory story = new JsonAdaptedStory(null, List.of());

        String expectedMessage = String.format(
                JsonAdaptedStory.MISSING_FIELD_MESSAGE_FORMAT,
                StoryName.class.getSimpleName());

        assertThrows(IllegalValueException.class,
                expectedMessage, story::toModelType);
    }

    @Test
    public void toModelType_invalidContact_throwsIllegalValueException() {
        List<JsonAdaptedName> invalidContacts = List.of(
                new JsonAdaptedName("Alex/Yeoh"));

        JsonAdaptedStory story = new JsonAdaptedStory(
                VALID_NAME, List.of(), invalidContacts);

        assertThrows(IllegalValueException.class,
                Name.MESSAGE_CONSTRAINTS, story::toModelType);
    }

    @Test
    public void toModelType_nullContact_throwsIllegalValueException() {
        List<JsonAdaptedName> invalidContacts = new ArrayList<>();
        invalidContacts.add(null);

        JsonAdaptedStory story = new JsonAdaptedStory(
                VALID_NAME, List.of(), invalidContacts);

        assertThrows(IllegalValueException.class,
                "Story contains an invalid contact.", story::toModelType);
    }

    @Test
    public void toModelType_noContacts_returnsStoryWithEmptyContacts() throws Exception {
        JsonAdaptedStory story = new JsonAdaptedStory(
                VALID_NAME, List.of(), null);

        assertTrue(story.toModelType().getContacts().isEmpty());
    }
}
