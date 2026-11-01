package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.story.StoryName;

/**
 * Contains unit tests for JsonAdaptedStoryName.
 */
public class JsonAdaptedStoryNameTest {

    private static final String VALID_NAME = "Housing Investigation";
    private static final String INVALID_NAME = "/Housing Investigation";

    @Test
    public void toModelType_validName_returnsStoryName() throws Exception {
        JsonAdaptedStoryName storyName = new JsonAdaptedStoryName(VALID_NAME);

        assertEquals(new StoryName(VALID_NAME), storyName.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedStoryName storyName = new JsonAdaptedStoryName(INVALID_NAME);

        assertThrows(IllegalValueException.class,
                StoryName.MESSAGE_CONSTRAINTS, storyName::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedStoryName storyName = new JsonAdaptedStoryName((String) null);

        assertThrows(IllegalValueException.class,
                "Story name is missing.", storyName::toModelType);
    }

    @Test
    public void constructor_validName_convertsCorrectly() throws Exception {
        StoryName modelStoryName = new StoryName(VALID_NAME);

        JsonAdaptedStoryName adaptedStoryName = new JsonAdaptedStoryName(modelStoryName);

        assertEquals(modelStoryName, adaptedStoryName.toModelType());
    }
}
