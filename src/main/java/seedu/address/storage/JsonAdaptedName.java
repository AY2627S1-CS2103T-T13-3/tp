
package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Name;

/**
 * Jackson-friendly version of {@link Name}.
 */
class JsonAdaptedName {

    private final String name;

    /**
     * Constructs a JsonAdaptedName with the given name.
     */
    @JsonCreator
    public JsonAdaptedName(@JsonProperty("name") String name) {
        this.name = name;
    }

    /**
     * Converts a Name into a Jackson-friendly object.
     */
    public JsonAdaptedName(Name source) {
        this.name = source.fullName;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * Converts this object into a Name.
     */
    public Name toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException("Contact name is missing.");
        }

        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }

        return new Name(name);
    }
}
