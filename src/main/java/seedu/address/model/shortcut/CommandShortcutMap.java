package seedu.address.model.shortcut;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Stores the user's command shortcuts and keeps the mapping one-to-one.
 */
public class CommandShortcutMap {

    private final Map<String, String> shortcutToCommandWord = new LinkedHashMap<>();

    /**
     * Creates an empty shortcut map.
     */
    public CommandShortcutMap() {}

    /**
     * Creates a shortcut map containing the supplied mappings.
     */
    public CommandShortcutMap(Map<String, String> shortcuts) {
        requireNonNull(shortcuts);
        shortcuts.forEach(this::assignShortcut);
    }

    /**
     * Assigns {@code shortcut} to {@code commandWord}.
     * Any previous shortcut for the command, and any previous command for the shortcut, is replaced.
     */
    public void assignShortcut(String shortcut, String commandWord) {
        requireNonNull(shortcut);
        requireNonNull(commandWord);

        shortcutToCommandWord.entrySet().removeIf(entry -> entry.getValue().equals(commandWord));
        shortcutToCommandWord.put(shortcut, commandWord);
    }

    /**
     * Removes {@code shortcut}, returning whether a mapping was removed.
     */
    public boolean removeShortcut(String shortcut) {
        requireNonNull(shortcut);
        return shortcutToCommandWord.remove(shortcut) != null;
    }

    /**
     * Returns the command word assigned to {@code shortcut}, if present.
     */
    public Optional<String> resolveShortcut(String shortcut) {
        requireNonNull(shortcut);
        return Optional.ofNullable(shortcutToCommandWord.get(shortcut));
    }

    /**
     * Returns an unmodifiable copy of the current mappings.
     */
    public Map<String, String> asMap() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(shortcutToCommandWord));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CommandShortcutMap otherShortcutMap)) {
            return false;
        }
        return shortcutToCommandWord.equals(otherShortcutMap.shortcutToCommandWord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortcutToCommandWord);
    }

    @Override
    public String toString() {
        return shortcutToCommandWord.toString();
    }
}
