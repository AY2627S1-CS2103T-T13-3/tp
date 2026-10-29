package seedu.address.logic.parser;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.core.index.Index;

/**
 * Stores the story and contact indices parsed from an assignment command.
 */
final class AssignmentIndices {

    private final Index storyIndex;
    private final Index contactIndex;

    AssignmentIndices(Index storyIndex, Index contactIndex) {
        requireAllNonNull(storyIndex, contactIndex);
        this.storyIndex = storyIndex;
        this.contactIndex = contactIndex;
    }

    Index getStoryIndex() {
        return storyIndex;
    }

    Index getContactIndex() {
        return contactIndex;
    }
}
