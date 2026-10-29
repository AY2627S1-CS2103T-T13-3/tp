package seedu.address.ui;

import java.util.Comparator;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.contact.Contact;
import seedu.address.model.story.StoryName;

/**
 * A UI component that displays information of a {@code Contact}.
 */
public class ContactCard extends UiPart<Region> {

    private static final String FXML = "ContactListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Contact contact;

    @FXML
    private HBox contactCardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Label stories;

    /**
     * Creates a {@code ContactCard} with the given {@code Contact} and index to display.
     */
    public ContactCard(Contact contact, int displayedIndex) {
        super(FXML);
        this.contact = contact;
        id.setText(displayedIndex + ". ");
        name.setText(contact.getName().toString());
        phone.setText(contact.getPhone().toString());
        address.setText(contact.getAddress().toString());
        email.setText(contact.getEmail().toString());
        contact.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.getName()))
                .forEach(tag -> {
                    Label label = new Label(tag.getName());
                    label.setWrapText(true);
                    label.setMinWidth(0);
                    label.setMinHeight(Region.USE_PREF_SIZE);
                    label.maxWidthProperty().bind(tags.widthProperty());
                    tags.getChildren().add(label);
                });
        if (contact.getStories().isEmpty()) {
            stories.setText("Stories: (none)");
        } else {
            String storiesText = contact.getStories().stream()
                    .map(StoryName::toString)
                    .collect(Collectors.joining(", "));
            stories.setText("Stories: " + storiesText);
        }
    }
}

