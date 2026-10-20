package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.Objects;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.shortcut.CommandShortcutMap;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();
    private CommandShortcutMap shortcuts = new CommandShortcutMap();

    /**
     * Creates a {@code UserPrefs} with default values.
     */
    public UserPrefs() {}

    /**
     * Creates a {@code UserPrefs} with the prefs in {@code userPrefs}.
     */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        setGuiSettings(userPrefs.getGuiSettings());
        setShortcuts(userPrefs.getShortcuts());
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    @Override
    public Map<String, String> getShortcuts() {
        return shortcuts.asMap();
    }

    /**
     * Replaces all command shortcuts with {@code shortcuts}.
     */
    public void setShortcuts(Map<String, String> shortcuts) {
        this.shortcuts = new CommandShortcutMap(requireNonNull(shortcuts));
    }

    /**
     * Assigns a shortcut to a command, replacing conflicting mappings.
     */
    public void assignShortcut(String shortcut, String commandWord) {
        shortcuts.assignShortcut(shortcut, commandWord);
    }

    /**
     * Removes a shortcut, returning whether a mapping was removed.
     */
    public boolean removeShortcut(String shortcut) {
        return shortcuts.removeShortcut(shortcut);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UserPrefs otherUserPrefs)) {
            return false;
        }

        return guiSettings.equals(otherUserPrefs.guiSettings)
                && shortcuts.equals(otherUserPrefs.shortcuts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, shortcuts);
    }

    @Override
    public String toString() {
        return "Gui Settings : " + guiSettings + "; Shortcuts : " + shortcuts;
    }

}
