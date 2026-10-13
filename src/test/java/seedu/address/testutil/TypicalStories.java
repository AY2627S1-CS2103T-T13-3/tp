package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;

/**
 * A utility class containing a list of {@code Story} objects to be used in tests.
 */
public class TypicalStories {

    public static final Story STORY_A = new Story(new StoryName("Story A"), new HashSet<>());
    public static final Story STORY_B = new Story(new StoryName("Story B"), new HashSet<>());
    public static final Story STORY_C = new Story(new StoryName("Story C"), new HashSet<>());

    private TypicalStories() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical stories.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Story story : getTypicalStories()) {
            ab.addStory(story);
        }
        return ab;
    }

    public static List<Story> getTypicalStories() {
        return new ArrayList<>(Arrays.asList(STORY_A, STORY_B, STORY_C));
    }
}
