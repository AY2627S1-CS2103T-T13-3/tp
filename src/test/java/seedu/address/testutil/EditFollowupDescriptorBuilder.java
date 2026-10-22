package seedu.address.testutil;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditFollowupCommand.EditFollowupDescriptor;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;

/**
 * A utility class to help with building EditFollowupDescriptor objects.
 */
public class EditFollowupDescriptorBuilder {

    private EditFollowupDescriptor descriptor;

    public EditFollowupDescriptorBuilder() {
        descriptor = new EditFollowupDescriptor();
    }

    public EditFollowupDescriptorBuilder(EditFollowupDescriptor descriptor) {
        this.descriptor = new EditFollowupDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditFollowupDescriptor} with fields containing {@code followup}'s description
     * and {@code contactIndex}.
     */
    public EditFollowupDescriptorBuilder(Followup followup, Index contactIndex) {
        descriptor = new EditFollowupDescriptor();
        descriptor.setDescription(followup.getDescription());
        descriptor.setContactIndex(contactIndex);
    }

    /**
     * Sets the {@code FollowupDescription} of the {@code EditFollowupDescriptor} that we are building.
     */
    public EditFollowupDescriptorBuilder withDescription(String description) {
        descriptor.setDescription(new FollowupDescription(description));
        return this;
    }

    /**
     * Sets the {@code contactIndex} of the {@code EditFollowupDescriptor} that we are building.
     */
    public EditFollowupDescriptorBuilder withContactIndex(Index contactIndex) {
        descriptor.setContactIndex(contactIndex);
        return this;
    }

    public EditFollowupDescriptor build() {
        return descriptor;
    }
}
