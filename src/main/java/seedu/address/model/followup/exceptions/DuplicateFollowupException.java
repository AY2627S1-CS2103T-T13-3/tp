package seedu.address.model.followup.exceptions;

/**
 * Signals that the operation will result in duplicate Stories (Stories are considered duplicates if they have the same
 * identity).
 */
public class DuplicateFollowupException extends RuntimeException {
    public DuplicateFollowupException() {
        super("Operation would result in duplicate stories");
    }
}
