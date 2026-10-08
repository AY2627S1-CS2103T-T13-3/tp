
package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.story.Story;

/**
 * A class to access Story data stored as a JSON file on the hard disk.
 */
public class JsonStoryStorage {

    private static final Logger logger =
            LogsCenter.getLogger(JsonStoryStorage.class);

    private final Path filePath;

    public JsonStoryStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getStoryFilePath() {
        return filePath;
    }

    /**
     * Returns stories from the storage file.
     * Returns Optional.empty() if the file is not found.
     *
     * @throws DataLoadingException if loading fails.
     */
    public Optional<List<Story>> readStories()
            throws DataLoadingException {
        return readStories(filePath);
    }

    /**
     * Reads stories from the specified file path.
     *
     * @param filePath Location of the data. Cannot be null.
     * @throws DataLoadingException if loading fails.
     */
    public Optional<List<Story>> readStories(Path filePath)
            throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableStories> jsonStories =
                JsonUtil.readJsonFile(
                        filePath, JsonSerializableStories.class);

        if (jsonStories.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonStories.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in "
                    + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    /**
     * Saves stories to the storage file.
     *
     * @param addressBook Cannot be null.
     * @throws IOException if writing fails.
     */
    public void saveStories(ReadOnlyAddressBook addressBook)
            throws IOException {
        saveStories(addressBook, filePath);
    }

    /**
     * Saves stories to the specified file path.
     *
     * @param filePath Location of the data. Cannot be null.
     * @throws IOException if writing fails.
     */
    public void saveStories(ReadOnlyAddressBook addressBook,
                            Path filePath) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(
                new JsonSerializableStories(addressBook),
                filePath);
    }
}
