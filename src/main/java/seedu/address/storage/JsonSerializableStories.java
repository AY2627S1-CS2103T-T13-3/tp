
package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;

/**
 * An immutable collection of stories that is serializable to JSON format.
 */
@JsonRootName(value = "stories")
class JsonSerializableStories {

    public static final String MESSAGE_DUPLICATE_STORY =
            "Stories list contains duplicate story(s).";

    public static final String MESSAGE_NULL_STORY =
            "Stories list contains a null story.";

    private final List<JsonAdaptedStory> stories = new ArrayList<>();

    /**
     * Constructs a JsonSerializableStories with the given stories.
     */
    @JsonCreator
    public JsonSerializableStories(
            @JsonProperty("stories") List<JsonAdaptedStory> stories) {
        if (stories != null) {
            this.stories.addAll(stories);
        }
    }

    /**
     * Converts stories from a ReadOnlyAddressBook into
     * JSON-compatible objects.
     */
    public JsonSerializableStories(ReadOnlyAddressBook source) {
        source.getStoryList().stream()
                .map(JsonAdaptedStory::new)
                .forEach(stories::add);
    }

    /**
     * Converts the stored stories into a list of Story objects.
     *
     * @throws IllegalValueException if the data is invalid.
     */
    public List<Story> toModelType() throws IllegalValueException {
        List<Story> modelStories = new ArrayList<>();
        Set<StoryName> storyNames = new HashSet<>();

        for (JsonAdaptedStory jsonAdaptedStory : stories) {
            if (jsonAdaptedStory == null) {
                throw new IllegalValueException(MESSAGE_NULL_STORY);
            }

            Story story = jsonAdaptedStory.toModelType();

            if (!storyNames.add(story.getName())) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_STORY);
            }

            modelStories.add(story);
        }

        return modelStories;
    }
}
