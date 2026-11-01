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
        for (String invalid : new String[]{"", " ", " Call Alice"}) {
            assertThrows(IllegalArgumentException.class, () -> new FollowupDescription(invalid));
        }
    }

    @Test
    public void isValidDescription() {
        assertThrows(NullPointerException.class, () -> FollowupDescription.isValidDescription(null));
        for (String invalid : new String[]{"", " ", " Call Alice"}) {
            assertFalse(FollowupDescription.isValidDescription(invalid));
        }
        for (String valid : new String[]{
            "/", "/Call Alice", "A", "12345", "Call Alice", "Call/Alice", "Call Alice/", "Call Alice 2", "Call  Alice ",
            "Call Alice!", "Call Alice - urgent!", "Interview prep (round 1)"}) {
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

    @Test
    public void isMatching() {
        FollowupDescription description = new FollowupDescription("Schedule interview");

        // matching keyword -> returns true
        assertTrue(description.isMatching("Schedule"));
        assertTrue(description.isMatching("schedule")); // case-insensitive
        assertTrue(description.isMatching("interview"));
        assertTrue(description.isDescriptionMatching("Schedule"));

        // non-matching keyword -> returns false
        assertFalse(description.isMatching("Meeting"));
        assertFalse(description.isDescriptionMatching("Meeting"));

        // partial word matching -> returns false
        assertFalse(description.isMatching("inter"));
        assertFalse(description.isDescriptionMatching("inter"));
    }
}
