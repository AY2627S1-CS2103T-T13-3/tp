package seedu.address.model.followup;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Name;

/**
 * Represents a Followup in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Followup {

    // Identity fields
    private final FollowupDescription description;

    // Linked contact
    private final Name contact;

    /**
     * Every field must be present and not null.
     */
    public Followup(FollowupDescription description, Name contact) {
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
        return contact;
    }

    /**
     * Returns whether the contact is linked to this Followup
     */
    public boolean isFor(Contact contact) {
        return this.contact.equals(contact.getName());
    }

    /**
     * Returns true if both followups are semantically equal.
     */
    public boolean isSameFollowup(Followup otherFollowup) {
        return equals(otherFollowup);
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
