package seedu.address.model.followup;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.followup.exceptions.DuplicateFollowupException;
import seedu.address.model.followup.exceptions.FollowupNotFoundException;

/**
 * A list of followups that enforces uniqueness between its elements and does not allow nulls.
 * A followup is considered unique by comparing using {@code Followup#isSameFollowup(Followup)}. As such, adding and
 * updating of
 * followups uses Followup#isSameFollowup(Followup) for equality so as to ensure that the followup being added or
 * updated is
 * unique in terms of identity in the UniqueFollowupList. However, the removal of a followup uses
 * Followup#equals(Object) so
 * as to ensure that the followup with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Followup#isSameFollowup(Followup)
 */

public class UniqueFollowupList implements Iterable<Followup> {

    private final ObservableList<Followup> internalList = FXCollections.observableArrayList();
    private final ObservableList<Followup> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent followup as the given argument.
     */
    public boolean contains(Followup toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::equals);
    }

    /**
     * Adds a followup to the list.
     * The followup must not already exist in the list.
     */
    public void add(Followup toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateFollowupException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the followup {@code target} in the list with {@code editedFollowup}.
     * {@code target} must exist in the list.
     * The followup identity of {@code editedFollowup} must not be the same as another existing followup in the list.
     */
    public void setFollowup(Followup target, Followup editedFollowup) {
        requireAllNonNull(target, editedFollowup);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new FollowupNotFoundException();
        }

        if (!target.isSameFollowup(editedFollowup) && contains(editedFollowup)) {
            throw new DuplicateFollowupException();
        }

        internalList.set(index, editedFollowup);
    }

    /**
     * Removes the equivalent followup from the list.
     * The followup must exist in the list.
     */
    public void remove(Followup toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new FollowupNotFoundException();
        }
    }

    public void setFollowups(UniqueFollowupList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code followups}.
     * {@code followups} must not contain duplicate followups.
     */
    public void setFollowups(List<Followup> followups) {
        requireAllNonNull(followups);
        if (!followupsAreUnique(followups)) {
            throw new DuplicateFollowupException();
        }

        internalList.setAll(followups);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Followup> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Followup> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueFollowupList otherUniqueFollowupList)) {
            return false;
        }

        return internalList.equals(otherUniqueFollowupList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code followups} contains only unique followups.
     */
    private boolean followupsAreUnique(List<Followup> followups) {
        for (int i = 0; i < followups.size() - 1; i++) {
            for (int j = i + 1; j < followups.size(); j++) {
                if (followups.get(i).isSameFollowup(followups.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
