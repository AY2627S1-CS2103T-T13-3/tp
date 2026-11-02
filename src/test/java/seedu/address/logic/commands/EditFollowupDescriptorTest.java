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

    @Test
    public void copyConstructor_allPairsFieldCombinations_success() {
        // Equivalence Partitioning & All-Pairs combinations of field presence:
        // Factor 1: Description (Present, Absent)
        // Factor 2: ContactIndex (Present, Absent)
        // All 4 pairs covered:
        // Pair 1: Description=Present, ContactIndex=Present
        // Pair 2: Description=Present, ContactIndex=Absent
        // Pair 3: Description=Absent, ContactIndex=Present
        // Pair 4: Description=Absent, ContactIndex=Absent

        EditFollowupDescriptor bothPresent = new EditFollowupDescriptor();
        bothPresent.setDescription(new FollowupDescription("Call"));
        bothPresent.setContactIndex(Index.fromOneBased(1));

        EditFollowupDescriptor descOnly = new EditFollowupDescriptor();
        descOnly.setDescription(new FollowupDescription("Email"));

        EditFollowupDescriptor indexOnly = new EditFollowupDescriptor();
        indexOnly.setContactIndex(Index.fromOneBased(2));

        EditFollowupDescriptor neitherPresent = new EditFollowupDescriptor();

        EditFollowupDescriptor[] testDescriptors = new EditFollowupDescriptor[] {
            bothPresent, descOnly, indexOnly, neitherPresent
        };

        for (EditFollowupDescriptor descriptor : testDescriptors) {
            EditFollowupDescriptor copy = new EditFollowupDescriptor(descriptor);
            assertEquals(descriptor, copy);
            assertEquals(descriptor.getDescription(), copy.getDescription());
            assertEquals(descriptor.getContactIndex(), copy.getContactIndex());
        }
    }
}
