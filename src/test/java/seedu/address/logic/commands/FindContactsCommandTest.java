package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_CONTACTS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalContacts.CARL;
import static seedu.address.testutil.TypicalContacts.ELLE;
import static seedu.address.testutil.TypicalContacts.FIONA;
import static seedu.address.testutil.TypicalContacts.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.contact.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindContactsCommand}.
 */
public class FindContactsCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(List.of("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(List.of("second"));

        FindContactsCommand findFirstCommand = new FindContactsCommand(firstPredicate);
        FindContactsCommand findSecondCommand = new FindContactsCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindContactsCommand findFirstCommandCopy = new FindContactsCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different contact -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noContactFound() {
        String expectedMessage = String.format(MESSAGE_CONTACTS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindContactsCommand command = new FindContactsCommand(predicate);
        expectedModel.updateFilteredContactList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(List.of(), model.getFilteredContactList());
    }

    @Test
    public void execute_multipleKeywords_multipleContactsFound() {
        String expectedMessage = String.format(MESSAGE_CONTACTS_LISTED_OVERVIEW, 3);
        NameContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        FindContactsCommand command = new FindContactsCommand(predicate);
        expectedModel.updateFilteredContactList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(List.of(CARL, ELLE, FIONA), model.getFilteredContactList());
    }

    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("keyword"));
        FindContactsCommand findContactsCommand = new FindContactsCommand(predicate);
        String expected = FindContactsCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findContactsCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(List.of(userInput.split("\\s+")));
    }
}
