package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.contact.Contact;
import seedu.address.model.followup.Followup;
import seedu.address.model.story.Story;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command: %s.";
    public static final String MESSAGE_INVALID_USER_STRING =
            "Unrecognised prefix or unescaped forward slash in \"%s\".";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!\n%1$s";
    public static final String MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX =
            "The contact index %1$d is invalid for the displayed list of %2$d contact(s).";
    public static final String MESSAGE_INVALID_STORY_DISPLAYED_INDEX =
            "The story index %1$d is invalid for the displayed list of %2$d story(ies).";
    public static final String MESSAGE_INVALID_FOLLOWUP_DISPLAYED_INDEX =
            "The followup index %1$d is invalid for the displayed list of %2$d followup(s).";
    public static final String MESSAGE_CONTACTS_LISTED_OVERVIEW = "%1$d contact(s) listed!";
    public static final String MESSAGE_STORIES_LISTED_OVERVIEW = "%1$d story(ies) listed!";
    public static final String MESSAGE_FOLLOWUPS_LISTED_OVERVIEW = "%1$d followup(s) listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "The following prefix(es) should not be duplicated: ";

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
     * Returns an error message indicating the input segment containing an unrecognized prefix or unescaped slash.
     */
    public static String getErrorMessageForInvalidUserString(String inputLocation, String invalidUserString) {
        return String.format(MESSAGE_INVALID_USER_STRING, inputLocation)
                + "\nProblematic segment: \"" + invalidUserString + "\"";
    }

    /**
     * Returns an error message listing the required inputs that were not provided.
     */
    public static String getErrorMessageForMissingRequiredInputs(String... missingInputs) {
        assert missingInputs.length > 0;
        String inputLabel = missingInputs.length == 1 ? "input" : "inputs";
        return "Missing required " + inputLabel + ": " + String.join(", ", missingInputs) + ".";
    }

    /**
     * Returns an error message identifying unexpected text before the first prefix.
     */
    public static String getErrorMessageForUnexpectedPreamble(String preamble) {
        return "Unexpected input before the first prefix: \"" + preamble + "\".";
    }

    /**
     * Formats the {@code contact} for display to the user.
     */
    public static String format(Contact contact) {
        final StringBuilder builder = new StringBuilder();
        builder.append(contact.getName())
                .append("; Phone: ")
                .append(contact.getPhone())
                .append("; Email: ")
                .append(contact.getEmail())
                .append("; Address: ")
                .append(contact.getAddress());

        if (!contact.getTags().isEmpty()) {
            builder.append("; Tags: ");
            contact.getTags().forEach(builder::append);
        }
        if (!contact.getStories().isEmpty()) {
            builder.append("; Stories: ");
            contact.getStories().forEach(story -> builder.append("[").append(story).append("]"));
        }
        return builder.toString();
    }

    /**
     * Formats the {@code story} for display to the user.
     */
    public static String format(Story story) {
        final StringBuilder builder = new StringBuilder();
        builder.append(story.getName());
        if (!story.getTags().isEmpty()) {
            builder.append("; Tags: ");
            story.getTags().forEach(builder::append);
        }
        if (!story.getContacts().isEmpty()) {
            builder.append("; Contacts: ");
            story.getContacts().forEach(contact -> builder.append("[").append(contact).append("]"));
        }
        return builder.toString();
    }

    /**
     * Formats the {@code Followup} for display to the user.
     */
    public static String format(Followup followup) {
        final StringBuilder builder = new StringBuilder();
        builder.append(followup.getDescription())
                .append("; Contact: ")
                .append(followup.getContact());
        return builder.toString();
    }
}
