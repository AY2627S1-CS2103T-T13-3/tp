package seedu.address.model.story.exceptions;

/**
 * Signals that the operation will result in duplicate Stories (Stories are considered duplicates if they have the same
 * identity).
 */
public class DuplicateStoryException extends RuntimeException {
    public DuplicateStoryException() {
        super("Operation would result in duplicate stories");
    }
}
