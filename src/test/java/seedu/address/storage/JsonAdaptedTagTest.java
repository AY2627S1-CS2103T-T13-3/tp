package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;

public class JsonAdaptedTagTest {

    private static final String VALID_TAG = "press";
    private static final String INVALID_TAG = " ";

    @Test
    public void toModelType_validTag_returnsTag() throws Exception {
        JsonAdaptedTag tag = new JsonAdaptedTag(VALID_TAG);

        assertEquals(new Tag(VALID_TAG), tag.toModelType());
    }

    @Test
    public void toModelType_startsWithSlash_returnsTag() throws Exception {
        JsonAdaptedTag tag = new JsonAdaptedTag("/press");

        assertEquals(new Tag("/press"), tag.toModelType());
    }

    @Test
    public void toModelType_invalidTag_throwsIllegalValueException() {
        JsonAdaptedTag tag = new JsonAdaptedTag(INVALID_TAG);

        assertThrows(IllegalValueException.class, Tag.MESSAGE_CONSTRAINTS, tag::toModelType);
    }

    @Test
    public void toModelType_nullTag_throwsIllegalValueException() {
        JsonAdaptedTag tag = new JsonAdaptedTag((String) null);

        assertThrows(IllegalValueException.class, Tag.MESSAGE_CONSTRAINTS, tag::toModelType);
    }
}
