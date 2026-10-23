package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.contact.Name;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Story}.
 */
class JsonAdaptedStory {

    public static final String MISSING_FIELD_MESSAGE_FORMAT =
            "Story's %s field is missing!";

    private final String name;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    @JsonProperty("contacts")
    private final List<JsonAdaptedName> contacts = new ArrayList<>();

    /**
     * Constructs a JsonAdaptedStory with the given details.
     */
    public JsonAdaptedStory(String name, List<JsonAdaptedTag> tags) {
        this(name, tags, null);
    }

    /**
     * Constructs a JsonAdaptedStory with assigned contacts.
     */
    @JsonCreator
    public JsonAdaptedStory(
            @JsonProperty("name") String name,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("contacts") List<JsonAdaptedName> contacts) {
        this.name = name;

        if (tags != null) {
            this.tags.addAll(tags);
        }

        if (contacts != null) {
            this.contacts.addAll(contacts);
        }
    }

    /**
     * Converts a Story into a Jackson-friendly object.
     */
    public JsonAdaptedStory(Story source) {
        name = source.getName().fullName;

        source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .forEach(tags::add);

        source.getContacts().stream()
                .map(JsonAdaptedName::new)
                .forEach(contacts::add);
    }

    /**
     * Converts this object into a Story.
     */
    public Story toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(
                    MISSING_FIELD_MESSAGE_FORMAT,
                    StoryName.class.getSimpleName()));
        }

        if (!StoryName.isValidName(name)) {
            throw new IllegalValueException(
                    StoryName.MESSAGE_CONSTRAINTS);
        }

        Set<Tag> modelTags = new HashSet<>();
        for (JsonAdaptedTag tag : tags) {
            if (tag == null) {
                throw new IllegalValueException(
                        "Story contains an invalid tag.");
            }
            modelTags.add(tag.toModelType());
        }

        List<Name> modelContacts = new ArrayList<>();
        for (JsonAdaptedName contact : contacts) {
            if (contact == null) {
                throw new IllegalValueException(
                        "Story contains an invalid contact.");
            }
            modelContacts.add(contact.toModelType());
        }

        return new Story(new StoryName(name), modelTags, modelContacts);
    }
}
