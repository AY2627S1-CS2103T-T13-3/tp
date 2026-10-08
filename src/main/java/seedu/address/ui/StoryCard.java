package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.story.Story;
/**
 * A UI component that displays information of a {@code Story}.
 */
public class StoryCard extends UiPart<Region> {

    private static final String FXML = "StoryListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Story story;

    @FXML
    private HBox storyCardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code StoryCard} with the given {@code Story} and index to display.
     */
    public StoryCard(Story Story, int displayedIndex) {
        super(FXML);
        this.story = Story;
        id.setText(displayedIndex + ". ");
        name.setText(Story.getName().fullName);
        Story.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}
