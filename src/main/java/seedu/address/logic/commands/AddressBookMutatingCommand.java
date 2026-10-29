package seedu.address.logic.commands;

/**
 * Represents a command that changes persistent address book data when successfully executed.
 */
public abstract class AddressBookMutatingCommand extends Command {

    @Override
    public final boolean modifiesAddressBook() {
        return true;
    }
}
