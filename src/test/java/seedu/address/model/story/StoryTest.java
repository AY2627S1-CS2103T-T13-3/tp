package seedu.address.model.story;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class StoryTest {

    @Test
    public void contacts_emptyInitially() {
        Story s = new Story(new StoryName("test"), Set.of());
        assertEquals(0, s.getContacts().size());
    }

    @Test
    public void isAssigned_notAssigned_returnsFalse() {
        Story s = new Story(new StoryName("test"), Set.of());
        Person p = new PersonBuilder().build();
        assertFalse(s.isAssigned(p));
    }

    @Test
    public void assign_newContact_addsContact() {
        Story s = new Story(new StoryName("test"), Set.of());
        Person p = new PersonBuilder().build();
        Story s1 = s.assign(p);
        assertFalse(p.isAssigned(s));
        assertTrue(s1.isAssigned(p));
        assertFalse(s.isAssigned(p));
    }

    @Test
    public void unassign_notAssigned_doesNothing() {
        Story s = new Story(new StoryName("test"), Set.of());
        Person a = new PersonBuilder().withName("al").build();
        Person b = new PersonBuilder().withName("bob").build();
        Story s1 = s.assign(a).unassign(b);
        assertTrue(s1.isAssigned(a));
        assertFalse(s1.isAssigned(b));
    }
}
