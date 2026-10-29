package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;

public class ArgumentMultimapTest {
    private final ArgumentMultimap map = new ArgumentMultimap();
    private final Prefix namePrefix = new Prefix("n/");
    private final Prefix tagPrefix = new Prefix("t/");

    @Test
    public void getValue_missingPrefix_returnsEmpty() {
        assertEquals(Optional.empty(), map.getValue(namePrefix));
        assertTrue(map.getAllValues(namePrefix).isEmpty());
        assertEquals("", map.getPreamble());
    }

    @Test
    public void getValue_repeatedPrefixWithEmptyLastValue_returnsEmptyString() {
        map.put(namePrefix, "Alice");
        map.put(new Prefix("n/"), "");

        assertEquals(Optional.of(""), map.getValue(namePrefix));
        assertEquals(List.of("Alice", ""), map.getAllValues(namePrefix));
    }

    @Test
    public void getAllValues_modifyReturnedList_doesNotChangeStoredValues() {
        map.put(namePrefix, "Alice");
        List<String> values = map.getAllValues(namePrefix);
        values.clear();
        values.add("Bob");

        assertEquals(List.of("Alice"), map.getAllValues(namePrefix));
        map.getAllValues(tagPrefix).add("friend");
        assertTrue(map.getAllValues(tagPrefix).isEmpty());
    }

    @Test
    public void getAllValues_addLaterValue_doesNotChangeEarlierSnapshot() {
        map.put(namePrefix, "Alice");
        List<String> values = map.getAllValues(namePrefix);
        map.put(namePrefix, "Bob");

        assertEquals(List.of("Alice"), values);
        assertEquals(List.of("Alice", "Bob"), map.getAllValues(namePrefix));
        assertEquals(Optional.of("Bob"), map.getValue(namePrefix));
    }

    @Test
    public void arePrefixesPresent_allPresent_returnsTrue() {
        map.put(namePrefix, "Alice");
        map.put(tagPrefix, "friend");

        assertTrue(map.arePrefixesPresent(namePrefix, tagPrefix));
    }

    @Test
    public void arePrefixesPresent_missingPrefix_returnsFalse() {
        map.put(namePrefix, "Alice");

        assertFalse(map.arePrefixesPresent(namePrefix, tagPrefix));
    }

    @Test
    public void verifyNoDuplicatePrefixesFor_identicalValues_throwsParseException() {
        map.put(namePrefix, "Alice");
        map.put(namePrefix, "Alice");

        assertThrows(ParseException.class, Messages.MESSAGE_DUPLICATE_FIELDS + "n/", ()
                -> map.verifyNoDuplicatePrefixesFor(namePrefix, new Prefix("n/")));
    }

    @Test
    public void verifyNoDuplicatePrefixesFor_uncheckedDuplicates_success() throws Exception {
        map.put(tagPrefix, "friend");
        map.put(tagPrefix, "friend");

        map.verifyNoDuplicatePrefixesFor();
        map.verifyNoDuplicatePrefixesFor(namePrefix);
        map.put(namePrefix, "Alice");
        map.verifyNoDuplicatePrefixesFor(namePrefix, namePrefix);
    }
}
