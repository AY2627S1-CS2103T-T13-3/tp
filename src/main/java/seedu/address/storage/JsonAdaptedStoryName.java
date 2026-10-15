package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.story.StoryName;

/**
 * Jackson-friendly version of {@link StoryName}.
 */
class JsonAdaptedStoryName {

    private final String name;

    /**
     * Constructs a JsonAdaptedStoryName with the given name.
     */
    @JsonCreator
    public JsonAdaptedStoryName(@JsonProperty("name") String name) {
        this.name = name;
    }

    /**
     * Converts a StoryName into a Jackson-friendly object.
     */
    public JsonAdaptedStoryName(StoryName source) {
        this.name = source.fullName;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Converts this object into a StoryName.
     */
    public StoryName toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException("Story name is missing.");
        }

        if (!StoryName.isValidName(name)) {
            throw new IllegalValueException(StoryName.MESSAGE_CONSTRAINTS);
        }

        return new StoryName(name);
    }
}
