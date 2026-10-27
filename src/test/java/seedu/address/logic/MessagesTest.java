package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.contact.Address;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Email;
import seedu.address.model.contact.Name;
import seedu.address.model.contact.Phone;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.model.tag.Tag;

public class MessagesTest {

    @Test
    public void format_contactWithoutStories() {
        Contact contact = new Contact(new Name("Alice Pauline"), new Phone("94351253"),
                new Email("alice@example.com"), new Address("123, Jurong West Ave 6, #08-111"),
                Set.of(new Tag("friends")));
        String expected = "Alice Pauline; Phone: 94351253; Email: alice@example.com; "
                + "Address: 123, Jurong West Ave 6, #08-111; Tags: [friends]; Stories: ";
        assertEquals(expected, Messages.format(contact));
    }

    @Test
    public void format_contactWithStories() {
        Contact contact = new Contact(new Name("Alice Pauline"), new Phone("94351253"),
                new Email("alice@example.com"), new Address("123, Jurong West Ave 6, #08-111"),
                Set.of(new Tag("friends")), List.of(new StoryName("Housing Investigation")));
        String expected = "Alice Pauline; Phone: 94351253; Email: alice@example.com; "
                + "Address: 123, Jurong West Ave 6, #08-111; Tags: [friends]; Stories: [Housing Investigation]";
        assertEquals(expected, Messages.format(contact));
    }

    @Test
    public void format_storyWithoutContacts() {
        Story story = new Story(new StoryName("Housing Investigation"), Set.of(new Tag("urgent")));
        String expected = "Housing Investigation; Tags: [urgent]; Contacts: ";
        assertEquals(expected, Messages.format(story));
    }

    @Test
    public void format_storyWithContacts() {
        Story story = new Story(new StoryName("Housing Investigation"), Set.of(new Tag("urgent")),
                List.of(new Name("Alice Pauline")));
        String expected = "Housing Investigation; Tags: [urgent]; Contacts: [Alice Pauline]";
        assertEquals(expected, Messages.format(story));
    }
}
