package seedu.address.model.story;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Represents a Story in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Story {

    // Identity fields
    private final StoryName name;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();

    // Assigned contacts
    private final List<Person> contacts = new ArrayList<>();

    /**
     * Every field must be present and not null.
     */
    public Story(StoryName name, Set<Tag> tags) {
        requireAllNonNull(name, tags);
        this.name = name;
        this.tags.addAll(tags);
    }

    public StoryName getName() {
        return name;
    }

    /**
     * Returns an immutable list of contacts, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Person> getContacts() {
        return List.copyOf(contacts);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both stories have the same name.
     * This defines a weaker notion of equality between two stories.
     */
    public boolean isSameStory(Story otherStory) {
        if (otherStory == this) {
            return true;
        }

        return otherStory != null
                && otherStory.getName().equals(getName());
    }

    /**
     * Returns true if both stories have the same identity and data fields.
     * This defines a stronger notion of equality between two stories.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof seedu.address.model.story.Story otherStory)) {
            return false;
        }

        return name.equals(otherStory.name)
                && tags.equals(otherStory.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("tags", tags)
                .toString();
    }

}
