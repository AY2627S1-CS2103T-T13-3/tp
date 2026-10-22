package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class UserPrefsTest {

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        UserPrefs userPref = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPref.setGuiSettings(null));
    }

    @Test
    public void copyConstructor_shortcutsCopied() {
        UserPrefs original = new UserPrefs();
        original.assignShortcut("ls", "list");

        UserPrefs copy = new UserPrefs(original);
        original.removeShortcut("ls");

        assertEquals("list", copy.getShortcuts().get("ls"));
    }

    @Test
    public void assignShortcut_conflictingMappings_replaced() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.assignShortcut("l", "list");
        userPrefs.assignShortcut("ls", "list");
        userPrefs.assignShortcut("ls", "liststories");

        assertFalse(userPrefs.getShortcuts().containsKey("l"));
        assertEquals("liststories", userPrefs.getShortcuts().get("ls"));
        assertEquals(1, userPrefs.getShortcuts().size());
    }

}
