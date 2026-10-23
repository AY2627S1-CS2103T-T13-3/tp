package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditFollowupCommand.EditFollowupDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;

public class EditFollowupCommandTest {
    private final Model model = new ModelManager();
    private final Followup original = new Followup(new FollowupDescription("Call"), ALICE.getName());

    public EditFollowupCommandTest() {
        model.addPerson(ALICE);
        model.addPerson(BENSON);
        model.addFollowup(original);
    }

    @Test
    public void execute_descriptionOnly_preservesContact() throws Exception {
        new AddressBookParser().parseCommand("editfollowup 1 d/Email").execute(model);
        assertEquals(new Followup(new FollowupDescription("Email"), ALICE.getName()),
                model.getFilteredFollowupList().get(0));
    }

    @Test
    public void execute_contactOnly_usesFilteredContactIndex() throws Exception {
        model.updateFilteredPersonList(person -> person.equals(BENSON));
        new AddressBookParser().parseCommand("editfollowup 1 ci/1").execute(model);
        assertEquals(new Followup(original.getDescription(), BENSON.getName()),
                model.getFilteredFollowupList().get(0));
    }

    @Test
    public void execute_bothFields_usesFilteredFollowupIndexAndResetsFilter() throws Exception {
        Followup second = new Followup(new FollowupDescription("Meet"), ALICE.getName());
        model.addFollowup(second);
        model.updateFilteredFollowupList(second::equals);
        new AddressBookParser().parseCommand("editfollowup 1 d/Email ci/2").execute(model);
        assertEquals(original, model.getFilteredFollowupList().get(0));
        assertEquals(new Followup(new FollowupDescription("Email"), BENSON.getName()),
                model.getFilteredFollowupList().get(1));
    }

    @Test
    public void execute_duplicateContactChange_failure() {
        model.addFollowup(new Followup(original.getDescription(), BENSON.getName()));
        EditFollowupDescriptor descriptor = new EditFollowupDescriptor();
        descriptor.setContactIndex(Index.fromOneBased(2));
        EditFollowupCommand command = new EditFollowupCommand(Index.fromOneBased(1), descriptor);
        assertThrows(CommandException.class,
                EditFollowupCommand.MESSAGE_DUPLICATE_FOLLOWUP, () -> command.execute(model));
        assertEquals(original, model.getFilteredFollowupList().get(0));
    }

    @Test
    public void execute_invalidIndexes_failure() {
        EditFollowupDescriptor descriptor = new EditFollowupDescriptor();
        descriptor.setContactIndex(Index.fromOneBased(3));
        assertCommandFailure(new EditFollowupCommand(Index.fromOneBased(2), descriptor), model,
                Messages.MESSAGE_INVALID_FOLLOWUP_DISPLAYED_INDEX);
        assertCommandFailure(new EditFollowupCommand(Index.fromOneBased(1), descriptor), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_unchangedValues_success() throws Exception {
        new AddressBookParser().parseCommand("editfollowup 1 d/Call ci/1").execute(model);
        assertEquals(original, model.getFilteredFollowupList().get(0));
    }
}
