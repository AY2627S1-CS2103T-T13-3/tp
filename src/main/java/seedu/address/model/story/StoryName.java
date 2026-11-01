package seedu.address.model.story;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.commons.util.StringUtil;

/**
 * Represents a Story's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class StoryName {

    public static final String MESSAGE_CONSTRAINTS =
            "Names can take any values except forward slash (/), and should not be blank";

    /*
     * The first character of the name must not be a whitespace or forward slash
     */
    public static final String VALIDATION_REGEX = "^\\S.*$";

    private final String fullName;

    /**
     * Constructs a {@code StoryName}.
     *
     * @param name A valid story name.
     */
    public StoryName(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if the story name matches the given keyword.
     */
    public boolean isMatching(String keyword) {
        return StringUtil.containsWordIgnoreCase(fullName, keyword);
    }

    /**
     * Returns true if the story name matches the given keyword.
     */
    public boolean isNameMatching(String keyword) {
        return isMatching(keyword);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StoryName otherName)) {
            return false;
        }

        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
