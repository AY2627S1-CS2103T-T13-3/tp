package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.followup.Followup;

/**
 * Panel containing the list of Followups.
 */
public class FollowupListPanel extends UiPart<Region> {
    private static final String FXML = "FollowupListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(FollowupListPanel.class);

    @FXML
    private ListView<Followup> followupListView;

    /**
     * Creates a {@code FollowupListPanel} with the given {@code ObservableList}.
     */
    public FollowupListPanel(ObservableList<Followup> followupList) {
        super(FXML);
        followupListView.setItems(followupList);
        followupListView.setCellFactory(listView -> new FollowupListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Followup} using a {@code FollowupCard}.
     */
    class FollowupListViewCell extends ListCell<Followup> {
        @Override
        protected void updateItem(Followup followup, boolean empty) {
            super.updateItem(followup, empty);
            if (empty || followup == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new FollowupCard(followup, getIndex() + 1).getRoot());
            }
        }
    }

}
