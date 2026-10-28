package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // invalid tag name
        assertFalse(Tag.isValidTagName("")); // empty string
        assertFalse(Tag.isValidTagName(" ")); // spaces only
        assertFalse(Tag.isValidTagName("/")); // forward slash only
        assertFalse(Tag.isValidTagName("/tag")); // starts with forward slash
        assertFalse(Tag.isValidTagName("tag/")); // ends with forward slash
        assertFalse(Tag.isValidTagName("tag/sub")); // contains forward slash

        // valid tag name
        assertTrue(Tag.isValidTagName("friend")); // alphabets only
        assertTrue(Tag.isValidTagName("12345")); // numbers only
        assertTrue(Tag.isValidTagName("hubby*")); // contains special characters
        assertTrue(Tag.isValidTagName("#friend")); // starts with special characters
        assertTrue(Tag.isValidTagName("urgent!")); // exclamation mark
        assertTrue(Tag.isValidTagName("front page")); // contains space
    }

}
