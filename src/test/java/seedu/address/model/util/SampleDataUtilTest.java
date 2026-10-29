package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Name;
import seedu.address.model.followup.Followup;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.model.tag.Tag;

public class SampleDataUtilTest {

    @Test
    public void getSampleContacts_returnsExpectedContacts() {
        Contact[] sampleContacts = SampleDataUtil.getSampleContacts();
        assertEquals(7, sampleContacts.length);
        for (Contact contact : sampleContacts) {
            assertNotNull(contact);
        }
        assertEquals(new Name("Alex Yeoh"), sampleContacts[0].getName());
        assertEquals(SampleDataUtil.getTagSet("CSIT", "Cybersecurity"), sampleContacts[0].getTags());
        assertEquals(SampleDataUtil.getStoryNameList("Cybersecurity Data Breach"),
                sampleContacts[0].getStories());

        assertEquals(new Name("John Doe"), sampleContacts[6].getName());
        assertTrue(sampleContacts[6].getTags().isEmpty());
        assertTrue(sampleContacts[6].getStories().isEmpty());
    }

    @Test
    public void getSampleStories_returnsExpectedStories() {
        Story[] sampleStories = SampleDataUtil.getSampleStories();
        assertEquals(2, sampleStories.length);
        for (Story story : sampleStories) {
            assertNotNull(story);
        }
        assertEquals(new StoryName("Cybersecurity Data Breach"), sampleStories[0].getName());
        assertEquals(SampleDataUtil.getTagSet("Singapore", "Cybersecurity"), sampleStories[0].getTags());
        assertEquals(SampleDataUtil.getContactNameList("Alex Yeoh", "Bernice Yu"), sampleStories[0].getContacts());

        assertEquals(new StoryName("Fresh Graduate Unemployment"), sampleStories[1].getName());
        assertEquals(SampleDataUtil.getTagSet("Singapore", "AI", "NUS", "Unemployment", "NTU"),
                sampleStories[1].getTags());
        assertEquals(SampleDataUtil.getContactNameList("Bernice Yu", "Charlotte Oliveiro"),
                sampleStories[1].getContacts());
    }

    @Test
    public void getSampleFollowups_returnsExpectedFollowups() {
        Followup[] sampleFollowups = SampleDataUtil.getSampleFollowups();
        assertEquals(2, sampleFollowups.length);
        for (Followup followup : sampleFollowups) {
            assertNotNull(followup);
        }
        assertEquals("Attend Press Conference", sampleFollowups[0].getDescription().toString());
        assertEquals(new Name("Alex Yeoh"), sampleFollowups[0].getContact());

        assertEquals("Meet for interview", sampleFollowups[1].getDescription().toString());
        assertEquals(new Name("Bernice Yu"), sampleFollowups[1].getContact());
    }

    @Test
    public void getSampleAddressBook_containsAllSampleEntities() {
        ReadOnlyAddressBook sampleAb = SampleDataUtil.getSampleAddressBook();
        assertNotNull(sampleAb);
        assertEquals(7, sampleAb.getContactList().size());
        assertEquals(2, sampleAb.getStoryList().size());
        assertEquals(2, sampleAb.getFollowupList().size());
    }

    @Test
    public void getTagSet_createsCorrectTags() {
        Set<Tag> tags = SampleDataUtil.getTagSet("CSIT", "Cybersecurity");
        assertEquals(2, tags.size());
        assertTrue(tags.contains(new Tag("CSIT")));
        assertTrue(tags.contains(new Tag("Cybersecurity")));
    }

    @Test
    public void getStoryNameList_createsCorrectStoryNames() {
        List<StoryName> storyNames = SampleDataUtil.getStoryNameList("Cybersecurity Data Breach");
        assertEquals(1, storyNames.size());
        assertEquals(new StoryName("Cybersecurity Data Breach"), storyNames.get(0));
    }

    @Test
    public void getContactNameList_createsCorrectNames() {
        List<Name> names = SampleDataUtil.getContactNameList("Alex Yeoh", "Bernice Yu");
        assertEquals(2, names.size());
        assertEquals(new Name("Alex Yeoh"), names.get(0));
        assertEquals(new Name("Bernice Yu"), names.get(1));
    }
}
