package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_CRIME;
import static seedu.address.logic.commands.CommandTestUtil.DESC_FRAUD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STORYNAME_FRAUD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STORYTAG_MALAYSIA;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditStoryCommand.EditStoryDescriptor;
import seedu.address.model.story.Story;
import seedu.address.model.story.StoryName;
import seedu.address.testutil.EditStoryDescriptorBuilder;

public class EditStoryDescriptorTest {

    @Test
    public void isAnyFieldEdited() {
        EditStoryDescriptor descriptor = new EditStoryDescriptor();
        assertFalse(descriptor.isAnyFieldEdited());

        // Name only
        descriptor = new EditStoryDescriptorBuilder().withName(VALID_STORYNAME_FRAUD).build();
        assertTrue(descriptor.isAnyFieldEdited());

        // Tags only
        descriptor = new EditStoryDescriptorBuilder().withTags(VALID_STORYTAG_MALAYSIA).build();
        assertTrue(descriptor.isAnyFieldEdited());

        // Both name and tags
        descriptor = new EditStoryDescriptorBuilder()
                .withName(VALID_STORYNAME_FRAUD).withTags(VALID_STORYTAG_MALAYSIA).build();
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void equals() {
        // same values -> returns true
        EditStoryDescriptor descriptorWithSameValues = new EditStoryDescriptor(DESC_CRIME);
        assertTrue(DESC_CRIME.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_CRIME.equals(DESC_CRIME));

        // null -> returns false
        assertFalse(DESC_CRIME.equals(null));

        // different types -> returns false
        assertFalse(DESC_CRIME.equals(5));

        // different values -> returns false
        assertFalse(DESC_CRIME.equals(DESC_FRAUD));

        // different name -> returns false
        EditStoryDescriptor editedCrime =
                new EditStoryDescriptorBuilder(DESC_CRIME).withName(VALID_STORYNAME_FRAUD).build();
        assertFalse(DESC_CRIME.equals(editedCrime));

        // different tags -> returns false
        editedCrime = new EditStoryDescriptorBuilder(DESC_CRIME).withTags(VALID_STORYTAG_MALAYSIA).build();
        assertFalse(DESC_CRIME.equals(editedCrime));
    }

    @Test
    public void createEditedStory() {
        EditStoryDescriptor descriptor = new EditStoryDescriptorBuilder(DESC_FRAUD).build();
        Story editedStory = descriptor.createEditedStory(seedu.address.testutil.TypicalStories.STORY_A);

        assertEquals(new StoryName(VALID_STORYNAME_FRAUD), editedStory.getName());
        assertEquals(descriptor.getTags().get(), editedStory.getTags());
        assertEquals(seedu.address.testutil.TypicalStories.STORY_A.getContacts(), editedStory.getContacts());

        // empty descriptor preserves original story details
        EditStoryDescriptor emptyDescriptor = new EditStoryDescriptor();
        Story untouchedStory = emptyDescriptor.createEditedStory(seedu.address.testutil.TypicalStories.STORY_A);
        assertEquals(seedu.address.testutil.TypicalStories.STORY_A, untouchedStory);
    }

    @Test
    public void toStringMethod() {
        EditStoryDescriptor editStoryDescriptor = new EditStoryDescriptor();
        String expected = EditStoryDescriptor.class.getCanonicalName() + "{name="
                + editStoryDescriptor.getName().orElse(null) + ", tags="
                + editStoryDescriptor.getTags().orElse(null) + "}";
        assertEquals(expected, editStoryDescriptor.toString());
    }

    @Test
    public void copyConstructor_allPairsFieldCombinations_success() {
        // Equivalence Partitioning & All-Pairs combinations of field presence:
        // Factor 1: Name (Present, Absent)
        // Factor 2: Tags (Present, Absent)
        // All 4 pairs covered:
        // Pair 1: Name=Present, Tags=Present
        // Pair 2: Name=Present, Tags=Absent
        // Pair 3: Name=Absent, Tags=Present
        // Pair 4: Name=Absent, Tags=Absent

        EditStoryDescriptor[] testDescriptors = new EditStoryDescriptor[] {
            new EditStoryDescriptorBuilder().withName(VALID_STORYNAME_FRAUD).withTags(VALID_STORYTAG_MALAYSIA).build(),
            new EditStoryDescriptorBuilder().withName(VALID_STORYNAME_FRAUD).build(),
            new EditStoryDescriptorBuilder().withTags(VALID_STORYTAG_MALAYSIA).build(),
            new EditStoryDescriptor()
        };

        for (EditStoryDescriptor descriptor : testDescriptors) {
            EditStoryDescriptor copy = new EditStoryDescriptor(descriptor);
            assertEquals(descriptor, copy);
            assertEquals(descriptor.getName(), copy.getName());
            assertEquals(descriptor.getTags(), copy.getTags());
        }
    }
}
