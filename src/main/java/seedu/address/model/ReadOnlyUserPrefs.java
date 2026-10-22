package seedu.address.model;

import java.util.Map;

import seedu.address.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    /** Returns an unmodifiable view of command shortcuts mapped to their command words. */
    Map<String, String> getShortcuts();

}
