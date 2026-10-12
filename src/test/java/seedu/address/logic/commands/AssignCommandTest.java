package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;

public class AssignCommandTest {

    private static final Index STORY_INDEX = Index.fromOneBased(1);
    private static final Index CONTACT_INDEX = Index.fromOneBased(2);

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AssignCommand(null, CONTACT_INDEX));
        assertThrows(NullPointerException.class, () -> new AssignCommand(STORY_INDEX, null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        AssignCommand command = new AssignCommand(STORY_INDEX, CONTACT_INDEX);

        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void equals() {
        AssignCommand command = new AssignCommand(STORY_INDEX, CONTACT_INDEX);

        assertTrue(command.equals(command));
        assertTrue(command.equals(new AssignCommand(STORY_INDEX, CONTACT_INDEX)));
        assertFalse(command.equals(new AssignCommand(Index.fromOneBased(2), CONTACT_INDEX)));
        assertFalse(command.equals(new AssignCommand(STORY_INDEX, Index.fromOneBased(1))));
        assertFalse(command.equals(null));
        assertFalse(command.equals(new Object()));
    }

    @Test
    public void toStringMethod() {
        AssignCommand command = new AssignCommand(STORY_INDEX, CONTACT_INDEX);
        String expected = AssignCommand.class.getCanonicalName()
                + "{storyIndex=" + STORY_INDEX + ", contactIndex=" + CONTACT_INDEX + "}";

        assertEquals(expected, command.toString());
    }
}
