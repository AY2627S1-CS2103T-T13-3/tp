package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;

/**
 * Contains unit tests for {@code AddressBookMutatingCommand}.
 */
public class AddressBookMutatingCommandTest {

    private static class StubMutatingCommand extends AddressBookMutatingCommand {
        @Override
        public CommandResult execute(Model model) {
            return new CommandResult("stub mutating");
        }
    }

    @Test
    public void modifiesAddressBook_returnsTrue() {
        assertTrue(new StubMutatingCommand().modifiesAddressBook());
    }

    @Test
    public void modifiesUserPrefs_defaultReturnsFalse() {
        assertFalse(new StubMutatingCommand().modifiesUserPrefs());
    }
}
