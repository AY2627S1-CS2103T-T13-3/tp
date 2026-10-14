package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Remark remark;
    private final Set<Tag> tags = new HashSet<>();

    // Assigned stories
    private final List<StoryName> stories;

    /**
     * Every field except stories must be present and not null.
     * Initialises with no assigned stories.
     */
    public Person(Name name, Phone phone, Email email, Address address, Remark remark, Set<Tag> tags) {
        this(name, phone, email, address, remark, tags, List.of());
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Remark remark, Set<Tag> tags,
            List<StoryName> stories) {
        requireAllNonNull(name, phone, email, address, remark, tags, stories);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.remark = remark;
        this.tags.addAll(tags);
        this.stories = stories;
    }


    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Remark getRemark() {
        return remark;
    }

    /**
     * Returns whether the story has been assigned to this Person
     */
    public boolean isAssigned(Story story) {
        return stories.contains(story.getName());
    }

    /**
     * Assigns a story to this Person, but does not assign this Person to the story.
     * Does nothing and if the story has already been assigned.
     * @return  An updated Person with the assigned story
     */
    public Person assign(Story story) {
        if (isAssigned(story)) {
            return this;
        }
        List<StoryName> updatedStories = IntStream
                .range(0, stories.size() + 1)
                .mapToObj(idx -> idx < stories.size() ? stories.get(idx) : story.getName())
                .toList();
        return new Person(name, phone, email, address, remark, tags, updatedStories);
    }

    /**
     * Unassigns a story from this contact, but does not unassign this contact from the story.
     * Does nothing if the story has already been unassigned.
     * @return  An updated Person with the assigned story
     *
     */
    public Person unassign(Story story) {
        if (!isAssigned(story)) {
            return this;
        }
        StoryName removedStoryName = story.getName();
        List<StoryName> updatedStories = stories.stream()
                        .filter(storyName -> !removedStoryName.equals(storyName))
                        .toList();
        return new Person(name, phone, email, address, remark, tags, updatedStories);
    }

    /**
     * Returns an immutable list of stories, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<StoryName> getStories() {
        return List.copyOf(stories);
    }


    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person otherPerson)) {
            return false;
        }

        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .add("stories", stories)
                .toString();
    }

}
