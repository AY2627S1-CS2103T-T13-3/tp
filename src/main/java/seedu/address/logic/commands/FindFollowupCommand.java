package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.followup.FollowupDescriptionContainsKeywordsPredicate;

/**
 * Finds and lists followups whose descriptions contain
 * any of the supplied keywords (case-insensitive).
 */
public class FindFollowupCommand extends Command {

    public static final String COMMAND_WORD = "findfollowup";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds followups whose descriptions contain any of "
            + "the specified keywords (case-insensitive).\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " interview meeting";

    private final FollowupDescriptionContainsKeywordsPredicate predicate;

    public FindFollowupCommand(
            FollowupDescriptionContainsKeywordsPredicate predicate) {
        this.predicate = requireNonNull(predicate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.updateFilteredFollowupList(predicate);

        return new CommandResult(
                String.format(Messages.MESSAGE_FOLLOWUPS_LISTED_OVERVIEW,
                        model.getFilteredFollowupList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FindFollowupCommand otherCommand)) {
            return false;
        }

        return predicate.equals(otherCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
