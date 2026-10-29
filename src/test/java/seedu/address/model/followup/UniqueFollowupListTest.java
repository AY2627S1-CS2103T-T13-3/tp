package seedu.address.model.followup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalContacts.ALICE;
import static seedu.address.testutil.TypicalContacts.BOB;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.model.followup.exceptions.DuplicateFollowupException;
import seedu.address.model.followup.exceptions.FollowupNotFoundException;

public class UniqueFollowupListTest {

    private final Followup interview = new Followup(new FollowupDescription("Arrange interview"), ALICE.getName());
    private final Followup notes = new Followup(new FollowupDescription("Send notes"), ALICE.getName());
    private final Followup interviewWithBob = new Followup(interview.getDescription(), BOB.getName());
    private final UniqueFollowupList followups = new UniqueFollowupList();

    @Test
    public void contains_matchesDescriptionRegardlessOfContact() {
        assertFalse(followups.contains(interview));
        followups.add(interview);
        assertTrue(followups.contains(interview));
        assertFalse(followups.contains(interviewWithBob));
        assertFalse(followups.contains(notes));
    }

    @Test
    public void nullArguments_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> followups.contains(null));
        assertThrows(NullPointerException.class, () -> followups.add(null));
        assertThrows(NullPointerException.class, () -> followups.remove(null));
        assertThrows(NullPointerException.class, () -> followups.setFollowup(null, interview));
        assertThrows(NullPointerException.class, () -> followups.setFollowup(interview, null));
        assertThrows(NullPointerException.class, () -> followups.setFollowups((UniqueFollowupList) null));
        assertThrows(NullPointerException.class, () -> followups.setFollowups((List<Followup>) null));
    }

    @Test
    public void add_distinctDescriptions_preservesOrder() {
        followups.add(interview);
        followups.add(notes);
        assertEquals(List.of(interview, notes), followups.asUnmodifiableObservableList());
    }

    @Test
    public void add_duplicateDescription_throwsDuplicateFollowupException() {
        followups.add(interview);
        assertThrows(DuplicateFollowupException.class, () -> followups.add(interview));
        assertEquals(List.of(interview), followups.asUnmodifiableObservableList());
    }

    @Test
    public void setFollowup_missingTarget_throwsFollowupNotFoundException() {
        assertThrows(FollowupNotFoundException.class, () -> followups.setFollowup(interview, notes));
        followups.add(interview);
        assertThrows(FollowupNotFoundException.class, () -> followups.setFollowup(interviewWithBob, notes));
        assertEquals(List.of(interview), followups.asUnmodifiableObservableList());
    }

    @Test
    public void setFollowup_sameIdentity_replacesContactAtOriginalPosition() {
        followups.setFollowups(List.of(interview, notes));
        followups.setFollowup(interview, interview);
        assertEquals(List.of(interview, notes), followups.asUnmodifiableObservableList());
        followups.setFollowup(interview, interviewWithBob);
        assertEquals(List.of(interviewWithBob, notes), followups.asUnmodifiableObservableList());
    }

    @Test
    public void setFollowup_newIdentity_replacesFollowup() {
        followups.add(interview);
        followups.setFollowup(interview, notes);
        assertEquals(List.of(notes), followups.asUnmodifiableObservableList());
    }

    @Test
    public void setFollowup_duplicateIdentity_throwsDuplicateFollowupException() {
        followups.setFollowups(List.of(interview, notes));
        assertThrows(DuplicateFollowupException.class, () -> followups.setFollowup(notes, interview));
        assertEquals(List.of(interview, notes), followups.asUnmodifiableObservableList());
    }

    @Test
    public void remove_missingOrDifferentContact_throwsFollowupNotFoundException() {
        assertThrows(FollowupNotFoundException.class, () -> followups.remove(interview));
        followups.add(interview);
        assertThrows(FollowupNotFoundException.class, () -> followups.remove(interviewWithBob));
        assertEquals(List.of(interview), followups.asUnmodifiableObservableList());
    }

    @Test
    public void remove_equalFollowup_removesOnlyMatchingFollowup() {
        followups.setFollowups(List.of(interview, notes));
        followups.remove(new Followup(interview.getDescription(), ALICE.getName()));
        assertEquals(List.of(notes), followups.asUnmodifiableObservableList());
    }

    @Test
    public void setFollowups_uniqueFollowupList_copiesReplacement() {
        followups.add(interview);
        UniqueFollowupList replacement = new UniqueFollowupList();
        replacement.add(notes);
        followups.setFollowups(replacement);
        replacement.add(interview);
        assertEquals(List.of(notes), followups.asUnmodifiableObservableList());
    }

    @Test
    public void setFollowups_list_replacesContentsAndAcceptsEmptyList() {
        followups.add(interview);
        followups.setFollowups(List.of(notes, interviewWithBob));
        assertEquals(List.of(notes, interviewWithBob), followups.asUnmodifiableObservableList());
        followups.setFollowups(List.of());
        assertTrue(followups.asUnmodifiableObservableList().isEmpty());
    }

    @Test
    public void setFollowups_duplicateFollowups_throwsDuplicateFollowupException() {
        followups.add(notes);
        assertThrows(DuplicateFollowupException.class, () -> followups.setFollowups(List.of(interview, interview)));
        assertEquals(List.of(notes), followups.asUnmodifiableObservableList());
    }

    @Test
    public void setFollowups_duplicateDescriptionsDiffContacts_accepts() {
        followups.setFollowups(List.of(interview, notes, interviewWithBob));
        assertTrue(followups.contains(interview));
        assertTrue(followups.contains(interviewWithBob));
    }

    @Test
    public void setFollowups_nullElement_throwsNullPointerException() {
        followups.add(notes);
        assertThrows(NullPointerException.class, () -> followups.setFollowups(Arrays.asList(interview, null)));
        assertEquals(List.of(notes), followups.asUnmodifiableObservableList());
    }

    @Test
    public void asUnmodifiableObservableList_isReadOnlyAndReflectsChanges() {
        ObservableList<Followup> view = followups.asUnmodifiableObservableList();
        followups.add(interview);
        assertEquals(List.of(interview), view);
        assertThrows(UnsupportedOperationException.class, () -> view.add(notes));
        assertThrows(UnsupportedOperationException.class, () -> view.remove(interview));
        followups.setFollowup(interview, notes);
        assertEquals(List.of(notes), view);
        followups.remove(notes);
        assertTrue(view.isEmpty());
    }
}
