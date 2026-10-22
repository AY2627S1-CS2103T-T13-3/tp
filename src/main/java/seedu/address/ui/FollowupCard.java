package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.followup.Followup;

/**
 * A UI component that displays information of a {@code Followup}.
 */
public class FollowupCard extends UiPart<Region> {

    private static final String FXML = "FollowupListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Followup followup;

    @FXML
    private HBox followupCardPane;
    @FXML
    private Label description;
    @FXML
    private Label id;
    @FXML
    private Label contact;

    /**
     * Creates a {@code FollowupCard} with the given {@code Followup} and index to display.
     */
    public FollowupCard(Followup followup, int displayedIndex) {
        super(FXML);
        this.followup = followup;
        id.setText(displayedIndex + ". ");
        description.setText(followup.getDescription().toString());
        contact.setText(followup.getContact().toString());
    }
}

