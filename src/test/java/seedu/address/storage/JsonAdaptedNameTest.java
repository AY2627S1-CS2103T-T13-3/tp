package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.contact.Name;

/**
 * Contains unit tests for JsonAdaptedName.
 */
public class JsonAdaptedNameTest {

    private static final String VALID_NAME = "Alex Yeoh";
    private static final String INVALID_NAME = "/Alex Yeoh";

    @Test
    public void toModelType_validName_returnsName() throws Exception {
        JsonAdaptedName name = new JsonAdaptedName(VALID_NAME);

        assertEquals(new Name(VALID_NAME), name.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedName name = new JsonAdaptedName(INVALID_NAME);

        assertThrows(IllegalValueException.class,
                Name.MESSAGE_CONSTRAINTS, name::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedName name = new JsonAdaptedName((String) null);

        assertThrows(IllegalValueException.class,
                "Contact name is missing.", name::toModelType);
    }

    @Test
    public void constructor_validName_convertsCorrectly() throws Exception {
        Name modelName = new Name(VALID_NAME);

        JsonAdaptedName adaptedName = new JsonAdaptedName(modelName);

        assertEquals(modelName, adaptedName.toModelType());
    }
}
