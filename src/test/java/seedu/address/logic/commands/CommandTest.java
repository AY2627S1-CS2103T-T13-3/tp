package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;

/**
 * Contains unit tests for default methods of {@code Command}.
 */
public class CommandTest {

    private static class StubCommand extends Command {
        @Override
        public CommandResult execute(Model model) {
            return new CommandResult("stub");
        }
    }

    @Test
    public void modifiesAddressBook_defaultReturnsFalse() {
        assertFalse(new StubCommand().modifiesAddressBook());
    }

    @Test
    public void modifiesUserPrefs_defaultReturnsFalse() {
        assertFalse(new StubCommand().modifiesUserPrefs());
    }
}
