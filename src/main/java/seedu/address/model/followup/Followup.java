package seedu.address.model.followup;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Represents a Followup in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Followup {

    // Identity fields
    private final FollowupDescription description;

    // Linked contact
    private final Person contact;

    /**
     * Every field must be present and not null.
     */
    public Followup(FollowupDescription description, Person contact) {
        requireAllNonNull(description, contact);
        this.description = description;
        this.contact = contact;
    }

    public FollowupDescription getDescription() {
        return description;
    }

    /**
     * Returns the {@code Name} of the linked contact
     */
    public Name getContact() {
        return contact.getName();
    }

    /**
     * Returns whether the contact is linked to this Followup
     */
    public boolean isFor(Person contact) {
        return this.contact.equals(contact);
    }

    /**
     * Returns true if both followups have the same name.
     * This defines a weaker notion of equality between two followups.
     */
    public boolean isSameFollowup(Followup otherFollowup) {
        if (otherFollowup == this) {
            return true;
        }

        return otherFollowup != null
                && otherFollowup.getDescription().equals(getDescription());
    }

    /**
     * Returns true if both followups have the same identity and data fields.
     * This defines a stronger notion of equality between two followups.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Followup otherFollowup)) {
            return false;
        }

        return description.equals(otherFollowup.description)
                && contact.equals(otherFollowup.contact);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(description, contact);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", description)
                .add("contact", contact)
                .toString();
    }

}
