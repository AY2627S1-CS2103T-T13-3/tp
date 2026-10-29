package seedu.address.model.followup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalContacts.ALICE;
import static seedu.address.testutil.TypicalContacts.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.ContactBuilder;

public class FollowupTest {

    private final FollowupDescription description = new FollowupDescription("Arrange interview");
    private final Followup followup = new Followup(description, ALICE.getName());

    @Test
    public void constructor_nullFields_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Followup(null, ALICE.getName()));
        assertThrows(NullPointerException.class, () -> new Followup(description, null));
    }

    @Test
    public void constructor_validFields_preservesFields() {
        assertEquals(description, followup.getDescription());
        assertEquals(ALICE.getName(), followup.getContact());
    }

    @Test
    public void isFor_matchesContactByName() {
        assertTrue(followup.isFor(ALICE));
        assertTrue(followup.isFor(new ContactBuilder(ALICE).withPhone("91234567").build()));
        assertFalse(followup.isFor(BOB));
    }

    @Test
    public void isSameFollowup() {
        assertTrue(followup.isSameFollowup(followup));
        assertTrue(followup.isSameFollowup(new Followup(description, ALICE.getName())));
        assertFalse(followup.isSameFollowup(new Followup(description, BOB.getName())));
        assertFalse(followup.isSameFollowup(null));
        assertFalse(followup.isSameFollowup(new Followup(new FollowupDescription("Send notes"), ALICE.getName())));
    }

    @Test
    public void equals() {
        Followup copy = new Followup(new FollowupDescription("Arrange interview"), ALICE.getName());
        assertTrue(followup.equals(followup));
        assertTrue(followup.equals(copy));
        assertEquals(followup.hashCode(), copy.hashCode());
        assertFalse(followup.equals(null));
        assertFalse(followup.equals(description));
        // different linked contact -> returns false
        assertFalse(followup.equals(new Followup(description, BOB.getName())));
        assertFalse(followup.equals(new Followup(new FollowupDescription("Send notes"), ALICE.getName())));
    }
}
