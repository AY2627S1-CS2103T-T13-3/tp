package seedu.address.model.story;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.contact.Contact;
import seedu.address.testutil.ContactBuilder;

public class StoryTest {

    @Test
    public void contacts_emptyInitially() {
        Story s = new Story(new StoryName("test"), Set.of());
        assertEquals(0, s.getContacts().size());
    }

    @Test
    public void isAssigned_notAssigned_returnsFalse() {
        Story s = new Story(new StoryName("test"), Set.of());
        Contact p = new ContactBuilder().build();
        assertFalse(s.isAssigned(p));
    }

    @Test
    public void assign_newContact_addsContact() {
        Story s = new Story(new StoryName("test"), Set.of());
        Contact p = new ContactBuilder().build();
        Story s1 = s.assign(p);
        assertFalse(p.isAssigned(s));
        assertTrue(s1.isAssigned(p));
        assertFalse(s.isAssigned(p));
    }

    @Test
    public void unassign_notAssigned_doesNothing() {
        Story s = new Story(new StoryName("test"), Set.of());
        Contact a = new ContactBuilder().withName("al").build();
        Contact b = new ContactBuilder().withName("bob").build();
        Story s1 = s.assign(a).unassign(b);
        assertTrue(s1.isAssigned(a));
        assertFalse(s1.isAssigned(b));
    }
}
