package seedu.address.logic.commands.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Contains unit tests for {@code CommandException}.
 */
public class CommandExceptionTest {

    @Test
    public void constructorWithMessage() {
        CommandException exception = new CommandException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    public void constructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Underlying cause");
        CommandException exception = new CommandException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
