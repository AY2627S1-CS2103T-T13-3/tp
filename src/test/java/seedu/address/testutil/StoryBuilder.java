package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Story objects.
 */
public class StoryBuilder {
    
    public static final String DEFAULT_STORY_NAME = "Crime Investigation";
    public static final String DEFAULT_TAG_NAME = "Singapore";
    
    private StoryName storyName;
    private Set<Tag> tags;

    /**
     * Creates a {@code StoryBuilder} with the default details.
     */
    public StoryBuilder() {
        storyName = new StoryName(DEFAULT_STORY_NAME);
        tags = new HashSet<>();
        tags.add(new Tag(DEFAULT_TAG_NAME));
    }

    /**
     * Initializes the StoryBuilder with the data of {@code storyToCopy}.
     */
    public StoryBuilder(Story storyToCopy) {
        storyName = storyToCopy.getName();
        tags = new HashSet<>(storyToCopy.getTags());
    }

    /**
     * Sets the {@code StoryName} of the {@code Story} that we are building.
     */
    public StoryBuilder withStoryName(String storyName) {
        this.storyName = new StoryName(storyName);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and sets it to the {@code Story} that we are building.
     */
    public StoryBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    public Story build() {
        return new Story(storyName, tags);
    }
}
