package seedu.address.model.story;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
}
