package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalFollowups.FOLLOWUP_A;
import static seedu.address.testutil.TypicalFollowups.FOLLOWUP_B;
import static seedu.address.testutil.TypicalFollowups.FOLLOWUP_C;
import static seedu.address.testutil.TypicalFollowups.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.followup.FollowupDescriptionContainsKeywordsPredicate;

public class FindFollowupCommandTest {

    private final Model model =
            new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel =
            new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        FollowupDescriptionContainsKeywordsPredicate firstPredicate =
                new FollowupDescriptionContainsKeywordsPredicate(List.of("first"));
        FollowupDescriptionContainsKeywordsPredicate secondPredicate =
                new FollowupDescriptionContainsKeywordsPredicate(List.of("second"));

        FindFollowupCommand firstCommand = new FindFollowupCommand(firstPredicate);
        FindFollowupCommand secondCommand = new FindFollowupCommand(secondPredicate);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new FindFollowupCommand(firstPredicate)));
        assertFalse(firstCommand.equals(secondCommand));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
    }

    @Test
    public void execute_singleKeyword_oneFollowupFound() {
        FollowupDescriptionContainsKeywordsPredicate predicate =
                new FollowupDescriptionContainsKeywordsPredicate(List.of("B"));

        FindFollowupCommand command = new FindFollowupCommand(predicate);
        expectedModel.updateFilteredFollowupList(predicate);

        assertCommandSuccess(command, model, "1 followup(s) listed!", expectedModel);
        assertEquals(List.of(FOLLOWUP_B), model.getFilteredFollowupList());
    }

    @Test
    public void execute_multipleKeywords_multipleFollowupsFound() {
        FollowupDescriptionContainsKeywordsPredicate predicate =
                new FollowupDescriptionContainsKeywordsPredicate(List.of("A", "C"));

        FindFollowupCommand command = new FindFollowupCommand(predicate);
        expectedModel.updateFilteredFollowupList(predicate);

        assertCommandSuccess(command, model, "2 followup(s) listed!", expectedModel);
        assertEquals(List.of(FOLLOWUP_A, FOLLOWUP_C), model.getFilteredFollowupList());
    }

    @Test
    public void execute_nonMatchingKeyword_noFollowupFound() {
        FollowupDescriptionContainsKeywordsPredicate predicate =
                new FollowupDescriptionContainsKeywordsPredicate(List.of("missing"));

        FindFollowupCommand command = new FindFollowupCommand(predicate);
        expectedModel.updateFilteredFollowupList(predicate);

        assertCommandSuccess(command, model, "0 followup(s) listed!", expectedModel);
        assertEquals(List.of(), model.getFilteredFollowupList());
    }

    @Test
    public void constructor_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new FindFollowupCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        FollowupDescriptionContainsKeywordsPredicate predicate =
                new FollowupDescriptionContainsKeywordsPredicate(List.of("interview"));
        FindFollowupCommand command = new FindFollowupCommand(predicate);
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void toStringMethod() {
        FollowupDescriptionContainsKeywordsPredicate predicate =
                new FollowupDescriptionContainsKeywordsPredicate(List.of("interview"));
        FindFollowupCommand command = new FindFollowupCommand(predicate);
        String expected = FindFollowupCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }
}
