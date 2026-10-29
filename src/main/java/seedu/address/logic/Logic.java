package seedu.address.logic;

import java.util.Map;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.contact.Contact;
import seedu.address.model.followup.Followup;
import seedu.address.model.story.Story;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /** Returns an unmodifiable view of the filtered list of contacts */
    ObservableList<Contact> getFilteredContactList();

    /** Returns an unmodifiable view of the filtered list of stories */
    ObservableList<Story> getFilteredStoryList();

    /** Returns an unmodifiable view of the filtered list of followups */
    ObservableList<Followup> getFilteredFollowupList();

    /** Returns the filter status property of the contact list. */
    ObservableValue<String> getContactFilterStatusProperty();

    /** Returns the filter status property of the story list. */
    ObservableValue<String> getStoryFilterStatusProperty();

    /** Returns the filter status property of the followup list. */
    ObservableValue<String> getFollowupFilterStatusProperty();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /** Returns the user's command shortcuts mapped to their command words. */
    Map<String, String> getShortcuts();
}
