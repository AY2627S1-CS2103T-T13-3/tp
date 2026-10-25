package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.logic.commands.CommandInfo;
import seedu.address.logic.commands.CommandWords;

/**
 * A UI component that displays a panel listing available commands and their syntax.
 */
public class HelpPanel extends UiPart<Region> {

    private static final String FXML = "HelpPanel.fxml";

    private final Runnable onClose;

    @FXML
    private VBox helpPanelContainer;

    @FXML
    private Button closeButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox commandsContainer;

    /**
     * Creates a {@code HelpPanel} with the given close action handler.
     *
     * @param onClose Callback to run when the close button is clicked.
     */
    public HelpPanel(Runnable onClose) {
        super(FXML);
        this.onClose = onClose;
        populateCommands();
    }

    @FXML
    private void handleClose() {
        if (onClose != null) {
            onClose.run();
        }
    }

    private void populateCommands() {
        commandsContainer.getChildren().clear();

        for (CommandInfo info : CommandWords.getCommandInfos()) {
            VBox card = new VBox(4);
            card.getStyleClass().add("help-command-card");

            HBox header = new HBox(6);
            header.setAlignment(Pos.CENTER_LEFT);

            Label name = new Label(info.getCommandWord());
            name.getStyleClass().add("help-command-name");
            header.getChildren().add(name);

            Label format = new Label(info.getFormat());
            format.getStyleClass().add("help-command-format");
            format.setWrapText(true);

            Label desc = new Label(info.getDescription());
            desc.getStyleClass().add("help-command-desc");
            desc.setWrapText(true);

            card.getChildren().addAll(header, format, desc);
            commandsContainer.getChildren().add(card);
        }
    }
}
