package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_INTERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.DESC_NOTES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CONTACT_INDEX_TWO;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_NOTES;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditFollowupCommand.EditFollowupDescriptor;
import seedu.address.testutil.EditFollowupDescriptorBuilder;

public class EditFollowupDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditFollowupDescriptor descriptorWithSameValues = new EditFollowupDescriptor(DESC_INTERVIEW);
        assertTrue(DESC_INTERVIEW.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_INTERVIEW.equals(DESC_INTERVIEW));

        // null -> returns false
        assertFalse(DESC_INTERVIEW.equals(null));

        // different types -> returns false
        assertFalse(DESC_INTERVIEW.equals(5));

        // different values -> returns false
        assertFalse(DESC_INTERVIEW.equals(DESC_NOTES));

        // different description -> returns false
        EditFollowupDescriptor editedInterview = new EditFollowupDescriptorBuilder(DESC_INTERVIEW)
                .withDescription(VALID_DESCRIPTION_NOTES).build();
        assertFalse(DESC_INTERVIEW.equals(editedInterview));

        // different contact index -> returns false
        editedInterview = new EditFollowupDescriptorBuilder(DESC_INTERVIEW)
                .withContactIndex(VALID_CONTACT_INDEX_TWO).build();
        assertFalse(DESC_INTERVIEW.equals(editedInterview));
    }

    @Test
    public void toStringMethod() {
        EditFollowupDescriptor editFollowupDescriptor = new EditFollowupDescriptor();
        String expected = EditFollowupDescriptor.class.getCanonicalName() + "{description="
                + editFollowupDescriptor.getDescription().orElse(null) + ", contactIndex="
                + editFollowupDescriptor.getContactIndex().orElse(null) + "}";
        assertEquals(expected, editFollowupDescriptor.toString());
    }
}
