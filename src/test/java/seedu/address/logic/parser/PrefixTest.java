package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PrefixTest {

    @Test
    public void getPrefix() {
        Prefix prefix = new Prefix("n/");
        assertEquals("n/", prefix.getPrefix());
    }

    @Test
    public void toStringMethod() {
        Prefix prefix = new Prefix("t/");
        assertEquals("t/", prefix.toString());
    }

    @Test
    public void equals() {
        Prefix prefix = new Prefix("p/");

        // same object -> returns true
        assertTrue(prefix.equals(prefix));

        // same prefix string -> returns true
        assertTrue(prefix.equals(new Prefix("p/")));

        // null -> returns false
        assertFalse(prefix.equals(null));

        // different type -> returns false
        assertFalse(prefix.equals("p/"));

        // different prefix string -> returns false
        assertFalse(prefix.equals(new Prefix("e/")));
    }

    @Test
    public void hashCodeMethod() {
        Prefix prefix1 = new Prefix("p/");
        Prefix prefix2 = new Prefix("p/");
        Prefix nullPrefix = new Prefix(null);

        assertEquals(prefix1.hashCode(), prefix2.hashCode());
        assertNotEquals(prefix1.hashCode(), new Prefix("e/").hashCode());
        assertEquals(0, nullPrefix.hashCode());
    }
}
