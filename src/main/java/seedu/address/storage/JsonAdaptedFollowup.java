package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.contact.Name;
import seedu.address.model.followup.Followup;
import seedu.address.model.followup.FollowupDescription;

/**
 * Jackson-friendly version of {@link Followup}.
 */
class JsonAdaptedFollowup {

    public static final String MISSING_FIELD_MESSAGE_FORMAT =
            "Followup's %s field is missing!";

    private final String description;
    private final String contact;

    /**
     * Constructs a JsonAdaptedFollowup with the given details.
     */
    @JsonCreator
    public JsonAdaptedFollowup(
            @JsonProperty("description") String description,
            @JsonProperty("contact") String contact) {
        this.description = description;
        this.contact = contact;
    }

    /**
     * Converts a given Followup into this class for Jackson use.
     */
    public JsonAdaptedFollowup(Followup source) {
        description = source.getDescription().description;
        contact = source.getContact().fullName;
    }

    /**
     * Converts this adapted followup into the model's Followup.
     */
    public Followup toModelType() throws IllegalValueException {
        if (description == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT,
                            FollowupDescription.class.getSimpleName()));
        }

        if (!FollowupDescription.isValidDescription(description)) {
            throw new IllegalValueException(
                    FollowupDescription.MESSAGE_CONSTRAINTS);
        }

        if (contact == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT,
                            Name.class.getSimpleName()));
        }

        if (!Name.isValidName(contact)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }

        return new Followup(
                new FollowupDescription(description),
                new Name(contact));
    }
}
