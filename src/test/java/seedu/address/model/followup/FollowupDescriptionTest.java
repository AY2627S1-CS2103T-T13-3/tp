package seedu.address.model.followup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class FollowupDescriptionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FollowupDescription(null));
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        for (String invalid : new String[]{"", " ", " Call Alice", "Call Alice!", "Call\tAlice", "Call\nAlice"}) {
            assertThrows(IllegalArgumentException.class, () -> new FollowupDescription(invalid));
        }
    }

    @Test
    public void isValidDescription() {
        assertThrows(NullPointerException.class, () -> FollowupDescription.isValidDescription(null));
        for (String invalid : new String[]{"", " ", " Call Alice", "Call Alice!", "Call\tAlice", "Call\nAlice"}) {
            assertFalse(FollowupDescription.isValidDescription(invalid));
        }
        for (String valid : new String[]{"A", "12345", "Call Alice", "Call Alice 2", "Call  Alice "}) {
            assertTrue(FollowupDescription.isValidDescription(valid));
            assertEquals(valid, new FollowupDescription(valid).toString());
        }
    }

    @Test
    public void equals() {
        FollowupDescription description = new FollowupDescription("Call Alice");
        FollowupDescription copy = new FollowupDescription("Call Alice");
        assertTrue(description.equals(description));
        assertTrue(description.equals(copy));
        assertEquals(description.hashCode(), copy.hashCode());
        assertFalse(description.equals(null));
        assertFalse(description.equals("Call Alice"));
        assertFalse(description.equals(new FollowupDescription("Call Bob")));
        assertFalse(description.equals(new FollowupDescription("call Alice")));
    }
}
