package seedu.address.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandInfo;
import seedu.address.logic.commands.CommandWords;

/**
 * A UI component that displays a panel listing available commands and active custom shortcuts.
 */
public class HelpPanel extends UiPart<Region> {

    private static final String FXML = "HelpPanel.fxml";

    private final Logic logic;
    private final Runnable onClose;

    @FXML
    private VBox helpPanelContainer;

    @FXML
    private Button closeButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox shortcutsContainer;

    @FXML
    private VBox commandsContainer;

    /**
     * Creates a {@code HelpPanel} with the given {@code Logic} and close action handler.
     *
     * @param logic Logic component providing access to shortcuts.
     * @param onClose Callback to run when the close button is clicked.
     */
    public HelpPanel(Logic logic, Runnable onClose) {
        super(FXML);
        this.logic = logic;
        this.onClose = onClose;
        refresh();
    }

    /**
     * Refreshes the custom shortcuts list and available commands list.
     */
    public void refresh() {
        populateShortcuts();
        populateCommands();
    }

    @FXML
    private void handleClose() {
        if (onClose != null) {
            onClose.run();
        }
    }

    private void populateShortcuts() {
        shortcutsContainer.getChildren().clear();
        Map<String, String> shortcuts = logic.getShortcuts();
        if (shortcuts.isEmpty()) {
            Label emptyLabel = new Label("No custom shortcuts assigned.");
            emptyLabel.getStyleClass().add("help-empty-label");
            shortcutsContainer.getChildren().add(emptyLabel);
            return;
        }

        shortcuts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    HBox row = new HBox(6);
                    row.getStyleClass().add("shortcut-item");
                    row.setAlignment(Pos.CENTER_LEFT);

                    Label shortcutBadge = new Label(entry.getKey());
                    shortcutBadge.getStyleClass().add("shortcut-badge");

                    Label arrow = new Label("→");
                    arrow.getStyleClass().add("shortcut-arrow");

                    Label target = new Label(entry.getValue());
                    target.getStyleClass().add("shortcut-target");

                    row.getChildren().addAll(shortcutBadge, arrow, target);
                    shortcutsContainer.getChildren().add(row);
                });
    }

    private void populateCommands() {
        commandsContainer.getChildren().clear();
        Map<String, String> shortcuts = logic.getShortcuts();

        Map<String, List<String>> commandToShortcuts = new HashMap<>();
        shortcuts.forEach((shortcut, commandWord) -> {
            commandToShortcuts.computeIfAbsent(commandWord, k -> new ArrayList<>()).add(shortcut);
        });

        for (CommandInfo info : CommandWords.getCommandInfos()) {
            VBox card = new VBox(4);
            card.getStyleClass().add("help-command-card");

            HBox header = new HBox(6);
            header.setAlignment(Pos.CENTER_LEFT);

            Label name = new Label(info.getCommandWord());
            name.getStyleClass().add("help-command-name");
            header.getChildren().add(name);

            List<String> assignedShortcuts = commandToShortcuts.get(info.getCommandWord());
            if (assignedShortcuts != null && !assignedShortcuts.isEmpty()) {
                Collections.sort(assignedShortcuts);
                for (String s : assignedShortcuts) {
                    Label badge = new Label("alias: " + s);
                    badge.getStyleClass().add("help-alias-badge");
                    header.getChildren().add(badge);
                }
            }

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
