package seedu.address.model.followup;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.commons.util.StringUtil;

/**
 * Represents a Followup's description in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDescription(String)}
 */
public class FollowupDescription {

    public static final String MESSAGE_CONSTRAINTS =
            "Follow-up descriptions should not be blank or start with a whitespace or forward slash (/)";

    /*
     * The first character of the description must not be a whitespace or forward slash
     */
    public static final String VALIDATION_REGEX = "^\\S.*$";

    public final String description;

    /**
     * Constructs a {@code FollowupDescription}.
     *
     * @param description A valid description.
     */
    public FollowupDescription(String description) {
        requireNonNull(description);
        checkArgument(isValidDescription(description), MESSAGE_CONSTRAINTS);
        this.description = description;
    }

    /**
     * Returns true if a given string is a valid description.
     */
    public static boolean isValidDescription(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if the description matches the given keyword.
     */
    public boolean isMatching(String keyword) {
        return StringUtil.containsWordIgnoreCase(description, keyword);
    }

    /**
     * Returns true if the description matches the given keyword.
     */
    public boolean isDescriptionMatching(String keyword) {
        return isMatching(keyword);
    }


    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FollowupDescription otherName)) {
            return false;
        }

        return description.equals(otherName.description);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }

}
