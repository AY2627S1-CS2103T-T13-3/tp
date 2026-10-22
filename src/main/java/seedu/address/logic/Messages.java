package seedu.address.logic;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.followup.Followup;
import seedu.address.model.person.Person;
import seedu.address.model.story.Story;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command.";
    public static final String MESSAGE_INVALID_PREFIX = "Invalid prefixes at indices: %s.\nUse valid prefixes: %s.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!\n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid.";
    public static final String MESSAGE_INVALID_STORY_DISPLAYED_INDEX = "The story index provided is invalid.";
    public static final String MESSAGE_INVALID_FOLLOWUP_DISPLAYED_INDEX = "The followup index provided is invalid.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d person(s) listed!";
    public static final String MESSAGE_STORIES_LISTED_OVERVIEW = "%1$d story(ies) listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }
    /**
     * Returns an error message indicating the invalid prefixes.
     */
    public static String getErrorMessageForInvalidPrefixes(Prefix[] expectedPrefixes, List<Integer> invalidPositions) {
        assert !invalidPositions.isEmpty();

        Set<String> invalidFields =
                invalidPositions.stream()
                        .map(String::valueOf)
                        .collect(Collectors.toSet());
        Set<String> validFields =
                Stream.of(expectedPrefixes)
                        .map(Prefix::getPrefix)
                        .collect(Collectors.toSet());
        return MESSAGE_INVALID_PREFIX.formatted(
                String.join(", ", invalidFields),
                String.join(", ", validFields)
        );
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        builder.append("; Stories: ");
        person.getStories().forEach(story -> builder.append("[").append(story).append("]"));
        return builder.toString();
    }

    /**
     * Formats the {@code story} for display to the user.
     */
    public static String format(Story story) {
        final StringBuilder builder = new StringBuilder();
        builder.append(story.getName())
                .append("; Tags: ");
        story.getTags().forEach(builder::append);
        builder.append("; Contacts: ");
        story.getContacts().forEach(contact -> builder.append("[").append(contact).append("]"));
        return builder.toString();
    }

    /**
     * Formats the {@code followup} for display to the user.
     */
    public static String format(Followup followup) {
        final StringBuilder builder = new StringBuilder();
        builder.append(followup.getDescription())
                .append("; Contact: ")
                .append(followup.getContact());
        return builder.toString();
    }

}
