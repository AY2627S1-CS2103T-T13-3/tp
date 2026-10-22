package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.model.tag.Tag;

public class MessagesTest {

    @Test
    public void format_personWithoutStories() {
        Person person = new Person(new Name("Alice Pauline"), new Phone("94351253"),
                new Email("alice@example.com"), new Address("123, Jurong West Ave 6, #08-111"),
                Set.of(new Tag("friends")));
        String expected = "Alice Pauline; Phone: 94351253; Email: alice@example.com; "
                + "Address: 123, Jurong West Ave 6, #08-111; Tags: [friends]; Stories: ";
        assertEquals(expected, Messages.format(person));
    }

    @Test
    public void format_personWithStories() {
        Person person = new Person(new Name("Alice Pauline"), new Phone("94351253"),
                new Email("alice@example.com"), new Address("123, Jurong West Ave 6, #08-111"),
                Set.of(new Tag("friends")), List.of(new StoryName("Housing Investigation")));
        String expected = "Alice Pauline; Phone: 94351253; Email: alice@example.com; "
                + "Address: 123, Jurong West Ave 6, #08-111; Tags: [friends]; Stories: [Housing Investigation]";
        assertEquals(expected, Messages.format(person));
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

    @Test
    public void format_followup() {
        Followup followup = new Followup(new FollowupDescription("Arrange interview"), new Name("Alice Pauline"));
        String expected = "Arrange interview; Contact: Alice Pauline";
        assertEquals(expected, Messages.format(followup));
    }
}
