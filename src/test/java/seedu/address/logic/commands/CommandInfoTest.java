package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;

public class CommandInfoTest {

    private final Parser<ListCommand> dummyParser = args -> new ListCommand();

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new CommandInfo(null, "format", "desc"));
        assertThrows(NullPointerException.class, () -> new CommandInfo("word", null, "desc"));
        assertThrows(NullPointerException.class, () -> new CommandInfo("word", "format", null));

        assertThrows(NullPointerException.class, () -> new CommandInfo(null, "format", "desc", dummyParser));
        assertThrows(NullPointerException.class, () -> new CommandInfo("word", null, "desc", dummyParser));
        assertThrows(NullPointerException.class, () -> new CommandInfo("word", "format", null, dummyParser));

        assertThrows(NullPointerException.class, () -> new CommandInfo(null, "format", "desc", true, dummyParser));
        assertThrows(NullPointerException.class, () -> new CommandInfo("word", null, "desc", true, dummyParser));
        assertThrows(NullPointerException.class, () -> new CommandInfo("word", "format", null, true, dummyParser));
    }

    @Test
    public void getters_validInputs_returnsCorrectValues() {
        CommandInfo info = new CommandInfo("testword", "test format", "test description");
        assertEquals("testword", info.getCommandWord());
        assertEquals("test format", info.getFormat());
        assertEquals("test description", info.getDescription());
        assertTrue(info.isAssignable());
        assertNull(info.getParser());

        CommandInfo customInfo = new CommandInfo("testword", "test format", "test description", false, dummyParser);
        assertFalse(customInfo.isAssignable());
        assertEquals(dummyParser, customInfo.getParser());

        CommandInfo assignableWithParser = new CommandInfo("testword", "test format", "test description", dummyParser);
        assertTrue(assignableWithParser.isAssignable());
        assertEquals(dummyParser, assignableWithParser.getParser());
    }

    @Test
    public void parse_validParser_success() throws ParseException {
        CommandInfo info = new CommandInfo("list", "list", "desc", dummyParser);
        Command command = info.parse("");
        assertTrue(command instanceof ListCommand);
    }

    @Test
    public void parse_unconfiguredParser_throwsNullPointerException() {
        CommandInfo info = new CommandInfo("list", "list", "desc");
        assertThrows(NullPointerException.class, () -> info.parse(""));
    }

    @Test
    public void equalsAndHashCode() {
        CommandInfo info1 = new CommandInfo("word", "format", "desc");
        CommandInfo info2 = new CommandInfo("word", "format", "desc");
        CommandInfo infoDiffWord = new CommandInfo("other", "format", "desc");
        CommandInfo infoDiffFormat = new CommandInfo("word", "other format", "desc");
        CommandInfo infoDiffDesc = new CommandInfo("word", "format", "other desc");
        CommandInfo infoDiffAssignable = new CommandInfo("word", "format", "desc", false, null);

        // Same values -> returns true
        assertTrue(info1.equals(info2));
        assertEquals(info1.hashCode(), info2.hashCode());

        // Same object -> returns true
        assertTrue(info1.equals(info1));

        // Null or different type -> returns false
        assertFalse(info1.equals(null));
        assertFalse(info1.equals("some string"));

        // Different values -> returns false
        assertFalse(info1.equals(infoDiffWord));
        assertFalse(info1.equals(infoDiffFormat));
        assertFalse(info1.equals(infoDiffDesc));
        assertFalse(info1.equals(infoDiffAssignable));
    }

    @Test
    public void toStringMethod() {
        CommandInfo info = new CommandInfo("word", "format", "desc");
        String expected = CommandInfo.class.getCanonicalName()
                + "{commandWord=word, format=format, description=desc}";
        assertEquals(expected, info.toString());
    }
}
