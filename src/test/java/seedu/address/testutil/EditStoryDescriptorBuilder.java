package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditStoryCommand.EditStoryDescriptor;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditStoryDescriptor objects.
 */
public class EditStoryDescriptorBuilder {

    private EditStoryDescriptor descriptor;

    public EditStoryDescriptorBuilder() {
        descriptor = new EditStoryDescriptor();
    }

    public EditStoryDescriptorBuilder(EditStoryDescriptor descriptor) {
        this.descriptor = new EditStoryDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditStoryDescriptor} with fields containing {@code story}'s details
     */
    public EditStoryDescriptorBuilder(Story story) {
        descriptor = new EditStoryDescriptor();
        descriptor.setName(story.getName());
        descriptor.setTags(story.getTags());
    }

    /**
     * Sets the {@code StoryName} of the {@code EditStoryDescriptor} that we are building.
     */
    public EditStoryDescriptorBuilder withName(String storyName) {
        descriptor.setName(new StoryName(storyName));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and sets it to the {@code EditStoryDescriptor}
     * that we are building.
     */
    public EditStoryDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditStoryDescriptor build() {
        return descriptor;
    }
}
