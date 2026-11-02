package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;

/**
 * Contains unit tests for {@code AssignmentIndices}.
 */
public class AssignmentIndicesTest {

    private static final Index STORY_INDEX = Index.fromOneBased(1);
    private static final Index CONTACT_INDEX = Index.fromOneBased(2);

    /**
     * Equivalence Partitions for constructor parameters:
     * - StoryIndex: Non-null (valid), Null (invalid)
     * - ContactIndex: Non-null (valid), Null (invalid)
     */
    @Test
    public void constructor_validIndices_success() {
        AssignmentIndices indices = new AssignmentIndices(STORY_INDEX, CONTACT_INDEX);
        assertEquals(STORY_INDEX, indices.getStoryIndex());
        assertEquals(CONTACT_INDEX, indices.getContactIndex());
    }

    @Test
    public void constructor_nullStoryIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AssignmentIndices(null, CONTACT_INDEX));
    }

    @Test
    public void constructor_nullContactIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AssignmentIndices(STORY_INDEX, null));
    }

    @Test
    public void constructor_bothNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AssignmentIndices(null, null));
    }
}
