package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditFollowupCommand.EditFollowupDescriptor;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;

public class EditFollowupDescriptorTest {

    @Test
    public void isAnyFieldEdited() {
        EditFollowupDescriptor descriptor = new EditFollowupDescriptor();
        assertFalse(descriptor.isAnyFieldEdited());

        descriptor.setDescription(new FollowupDescription("Call"));
        assertTrue(descriptor.isAnyFieldEdited());

        descriptor = new EditFollowupDescriptor();
        descriptor.setContactIndex(Index.fromOneBased(1));
        assertTrue(descriptor.isAnyFieldEdited());

        descriptor.setDescription(new FollowupDescription("Call"));
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void equals() {
        EditFollowupDescriptor descriptor = new EditFollowupDescriptor();
        descriptor.setDescription(new FollowupDescription("Call"));
        descriptor.setContactIndex(Index.fromOneBased(1));

        // same values -> returns true
        EditFollowupDescriptor descriptorWithSameValues = new EditFollowupDescriptor(descriptor);
        assertTrue(descriptor.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(descriptor.equals(descriptor));

        // null -> returns false
        assertFalse(descriptor.equals(null));

        // different types -> returns false
        assertFalse(descriptor.equals(5));

        // different description -> returns false
        EditFollowupDescriptor differentDesc = new EditFollowupDescriptor(descriptor);
        differentDesc.setDescription(new FollowupDescription("Email"));
        assertFalse(descriptor.equals(differentDesc));

        // different contactIndex -> returns false
        EditFollowupDescriptor differentContact = new EditFollowupDescriptor(descriptor);
        differentContact.setContactIndex(Index.fromOneBased(2));
        assertFalse(descriptor.equals(differentContact));
    }

    @Test
    public void createEditedFollowup() {
        Followup original = new Followup(
                new FollowupDescription("Call"), seedu.address.testutil.TypicalContacts.ALICE.getName());

        EditFollowupDescriptor descriptor = new EditFollowupDescriptor();
        descriptor.setDescription(new FollowupDescription("Email"));

        Followup edited = descriptor.createEditedFollowup(
                original, seedu.address.testutil.TypicalContacts.BENSON.getName());
        assertEquals(new FollowupDescription("Email"), edited.getDescription());
        assertEquals(seedu.address.testutil.TypicalContacts.BENSON.getName(), edited.getContact());

        // empty descriptor preserves original followup description
        EditFollowupDescriptor emptyDescriptor = new EditFollowupDescriptor();
        Followup untouched = emptyDescriptor.createEditedFollowup(original, original.getContact());
        assertEquals(original, untouched);
    }

    @Test
    public void toStringMethod() {
        EditFollowupDescriptor editFollowupDescriptor = new EditFollowupDescriptor();
        editFollowupDescriptor.setDescription(new FollowupDescription("Call"));
        editFollowupDescriptor.setContactIndex(Index.fromOneBased(1));

        String expected = EditFollowupDescriptor.class.getCanonicalName()
                + "{description=Call, contactIndex=" + Index.fromOneBased(1) + "}";
        assertEquals(expected, editFollowupDescriptor.toString());
    }
}
