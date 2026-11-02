package seedu.address.logic.parser.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Contains unit tests for {@code ParseException}.
 */
public class ParseExceptionTest {

    @Test
    public void constructorWithMessage() {
        ParseException exception = new ParseException("Parse error message");
        assertEquals("Parse error message", exception.getMessage());
    }

    @Test
    public void constructorWithMessageAndCause() {
        Throwable cause = new IllegalArgumentException("Invalid format cause");
        ParseException exception = new ParseException("Parse error message", cause);
        assertEquals("Parse error message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
