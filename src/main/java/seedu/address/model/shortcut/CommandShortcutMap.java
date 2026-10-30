package seedu.address.model.shortcut;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.CommandWords;

/**
 * Stores the user's command shortcuts and keeps the mapping one-to-one.
 */
public class CommandShortcutMap {

    public static final String SHORTCUT_VALIDATION_REGEX = "[a-z][a-z0-9]*";

    private static final Logger logger = LogsCenter.getLogger(CommandShortcutMap.class);

    private final Map<String, String> shortcutToCommandWord = new LinkedHashMap<>();

    /**
     * Creates an empty shortcut map.
     */
    public CommandShortcutMap() {}

    /**
     * Creates a shortcut map containing the supplied mappings.
     * Any invalid mappings are discarded and logged.
     */
    @JsonCreator
    public CommandShortcutMap(@JsonProperty("shortcutToCommandWord") Map<String, String> shortcuts) {
        if (shortcuts != null) {
            shortcuts.forEach(this::assignShortcut);
        }
    }

    /**
     * Returns true if {@code shortcut} conforms to the shortcut syntax requirements.
     */
    public static boolean isValidShortcutPattern(String shortcut) {
        return shortcut != null && shortcut.matches(SHORTCUT_VALIDATION_REGEX);
    }

    /**
     * Assigns {@code shortcut} to {@code commandWord}.
     * Any previous shortcut for the command, and any previous command for the shortcut, is replaced.
     * If the mapping violates shortcut syntax, is a reserved keyword, or targets a non-assignable command,
     * the mapping is discarded, a warning is logged, and {@code false} is returned.
     */
    public boolean assignShortcut(String shortcut, String commandWord) {
        requireNonNull(shortcut);
        requireNonNull(commandWord);

        if (!isValidShortcutPattern(shortcut)) {
            logger.warning(String.format("Discarding invalid shortcut mapping '%s' -> '%s': "
                    + "shortcuts must start with a lowercase letter and contain only lowercase letters and digits.",
                    shortcut, commandWord));
            return false;
        }

        if (CommandWords.isCommandWord(shortcut)) {
            logger.warning(String.format("Discarding invalid shortcut mapping '%s' -> '%s': "
                    + "'%s' is a built-in command word and cannot be used as a shortcut.",
                    shortcut, commandWord, shortcut));
            return false;
        }

        if (!CommandWords.isAssignableCommandWord(commandWord)) {
            logger.warning(String.format("Discarding invalid shortcut mapping '%s' -> '%s': "
                    + "'%s' is not an assignable command.",
                    shortcut, commandWord, commandWord));
            return false;
        }

        shortcutToCommandWord.entrySet().removeIf(entry -> entry.getValue().equals(commandWord));
        shortcutToCommandWord.put(shortcut, commandWord);
        return true;
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
