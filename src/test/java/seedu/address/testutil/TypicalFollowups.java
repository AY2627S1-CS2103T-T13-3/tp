package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;
import seedu.address.model.person.Name;

/**
 * A utility class containing a list of {@code Followup} objects to be used in tests.
 */
public class TypicalFollowups {
    public static final Followup FOLLOWUP_A = new Followup(new FollowupDescription("Followup A"),
            new Name("Alice Pauline"));
    public static final Followup FOLLOWUP_B = new Followup(new FollowupDescription("Followup B"),
            new Name("Benson Meier"));
    public static final Followup FOLLOWUP_C = new Followup(new FollowupDescription("Followup C"),
            new Name("Carl Kurz"));

    private TypicalFollowups() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical followups.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Followup followup : getTypicalFollowups()) {
            ab.addFollowup(followup);
        }
        return ab;
    }

    public static List<Followup> getTypicalFollowups() {
        return new ArrayList<>(Arrays.asList(FOLLOWUP_A, FOLLOWUP_B, FOLLOWUP_C));
    }
}
