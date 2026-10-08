package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.story.Story;

/**
 * Panel containing the list of Storys.
 */
public class StoryListPanel extends UiPart<Region> {
    private static final String FXML = "StoryListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(StoryListPanel.class);

    @FXML
    private ListView<Story> storyListView;

    /**
     * Creates a {@code StoryListPanel} with the given {@code ObservableList}.
     */
    public StoryListPanel(ObservableList<Story> storyList) {
        super(FXML);
        storyListView.setItems(storyList);
        storyListView.setCellFactory(listView -> new StoryListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Story} using a {@code StoryCard}.
     */
    class StoryListViewCell extends ListCell<Story> {
        @Override
        protected void updateItem(Story story, boolean empty) {
            super.updateItem(story, empty);
            if (empty || story == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new StoryCard(story, getIndex() + 1).getRoot());
            }
        }
    }

}
