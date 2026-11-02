package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.contact.Name;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;

/**
 * Contains unit tests for JsonAdaptedFollowup.
 */
public class JsonAdaptedFollowupTest {

    private static final String VALID_DESCRIPTION = "Request interview";
    private static final String INVALID_DESCRIPTION = " ";
    private static final String VALID_CONTACT = "Alex Yeoh";
    private static final String INVALID_CONTACT = " ";

    @Test
    public void toModelType_validFollowup_returnsFollowup() throws Exception {
        JsonAdaptedFollowup followup =
                new JsonAdaptedFollowup(VALID_DESCRIPTION, VALID_CONTACT);

        Followup expected = new Followup(
                new FollowupDescription(VALID_DESCRIPTION),
                new Name(VALID_CONTACT));

        assertEquals(expected, followup.toModelType());
    }

    @Test
    public void toModelType_startsWithSlash_returnsFollowup() throws Exception {
        JsonAdaptedFollowup followup =
                new JsonAdaptedFollowup("/Request interview", "/Alex Yeoh");

        Followup expected = new Followup(
                new FollowupDescription("/Request interview"),
                new Name("/Alex Yeoh"));

        assertEquals(expected, followup.toModelType());
    }

    @Test
    public void constructor_validFollowup_convertsCorrectly() throws Exception {
        Followup modelFollowup = new Followup(
                new FollowupDescription(VALID_DESCRIPTION),
                new Name(VALID_CONTACT));

        JsonAdaptedFollowup adaptedFollowup =
                new JsonAdaptedFollowup(modelFollowup);

        assertEquals(modelFollowup, adaptedFollowup.toModelType());
    }

    @Test
    public void toModelType_invalidDescription_throwsIllegalValueException() {
        JsonAdaptedFollowup followup =
                new JsonAdaptedFollowup(INVALID_DESCRIPTION, VALID_CONTACT);

        assertThrows(IllegalValueException.class,
                FollowupDescription.MESSAGE_CONSTRAINTS, followup::toModelType);
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        JsonAdaptedFollowup followup =
                new JsonAdaptedFollowup(null, VALID_CONTACT);

        String expectedMessage = String.format(
                JsonAdaptedFollowup.MISSING_FIELD_MESSAGE_FORMAT,
                FollowupDescription.class.getSimpleName());

        assertThrows(IllegalValueException.class,
                expectedMessage, followup::toModelType);
    }

    @Test
    public void toModelType_invalidContact_throwsIllegalValueException() {
        JsonAdaptedFollowup followup =
                new JsonAdaptedFollowup(VALID_DESCRIPTION, INVALID_CONTACT);

        assertThrows(IllegalValueException.class,
                Name.MESSAGE_CONSTRAINTS, followup::toModelType);
    }

    @Test
    public void toModelType_nullContact_throwsIllegalValueException() {
        JsonAdaptedFollowup followup =
                new JsonAdaptedFollowup(VALID_DESCRIPTION, null);

        String expectedMessage = String.format(
                JsonAdaptedFollowup.MISSING_FIELD_MESSAGE_FORMAT,
                Name.class.getSimpleName());

        assertThrows(IllegalValueException.class,
                expectedMessage, followup::toModelType);
    }
}
