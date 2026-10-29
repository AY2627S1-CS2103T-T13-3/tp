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
    public void equals() {
        Story story = new Story(new StoryName("test"), Set.of());
        Story storyCopy = new Story(new StoryName("test"), Set.of());
        Contact contact = new ContactBuilder().build();

        // same values -> returns true
        assertTrue(story.equals(storyCopy));
        assertEquals(story.hashCode(), storyCopy.hashCode());

        // different assigned contacts -> returns false
        Story storyWithContact = story.assign(contact);
        assertFalse(story.equals(storyWithContact));

        // same assigned contacts -> returns true
        Story storyWithContactCopy = storyCopy.assign(contact);
        assertTrue(storyWithContact.equals(storyWithContactCopy));
        assertEquals(storyWithContact.hashCode(), storyWithContactCopy.hashCode());

        // null and a different type -> return false
        assertFalse(story.equals(null));
        assertFalse(story.equals(contact));
    }

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

    @Test
    public void isNameMatching() {
        Story story = new Story(new StoryName("Tech News"), Set.of());

        // matches name keyword
        assertTrue(story.isNameMatching("Tech"));
        assertTrue(story.isNameMatching("news")); // case-insensitive

        // does not match
        assertFalse(story.isNameMatching("Sports"));
        assertFalse(story.isNameMatching("Te"));
    }
}
