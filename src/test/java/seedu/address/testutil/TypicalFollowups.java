package seedu.address.testutil;

import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;

/**
 * A utility class containing a list of {@code Followup} objects to be used in tests.
 */
public class TypicalFollowups {

    public static final Followup INTERVIEW_ALICE = new Followup(
            new FollowupDescription("Arrange interview"), ALICE.getName());
    public static final Followup NOTES_BENSON = new Followup(
            new FollowupDescription("Send meeting notes"), BENSON.getName());
    public static final Followup FACT_CHECK_CARL = new Followup(
            new FollowupDescription("Fact check quote"), CARL.getName());

    private TypicalFollowups() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons and typical followups.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = TypicalPersons.getTypicalAddressBook();
        for (Followup followup : getTypicalFollowups()) {
            ab.addFollowup(followup);
        }
        return ab;
    }

    public static List<Followup> getTypicalFollowups() {
        return new ArrayList<>(Arrays.asList(INTERVIEW_ALICE, NOTES_BENSON, FACT_CHECK_CARL));
    }
}
