package seedu.address.ui;

import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";
    private static final double HELP_EXPANSION_THRESHOLD_WIDTH = 1000;
    private static final double HELP_EXPANDED_WIDTH = 1050;

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;
    private Path dataFilePath;
    private String initialMessage;

    // Independent Ui parts residing in this Ui container
    private ContactListPanel contactListPanel;
    private StoryListPanel storyListPanel;
    private FollowupListPanel followupListPanel;
    private ResultDisplay resultDisplay;
    private HelpPanel helpPanel;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane contactListPanelPlaceholder;

    @FXML
    private StackPane storyListPanelPlaceholder;

    @FXML
    private StackPane followupListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane helpPanelPlaceholder;

    @FXML
    private Label contactFilterIndicator;

    @FXML
    private Label storyFilterIndicator;

    @FXML
    private Label followupFilterIndicator;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage}, {@code Logic},
     * and the data file path to show in the status bar.
     */
    public MainWindow(Stage primaryStage, Logic logic, Path dataFilePath) {
        this(primaryStage, logic, dataFilePath, null);
    }

    /**
     * Creates a {@code MainWindow} with the given {@code Stage}, {@code Logic},
     * data file path to show in the status bar, and initial message to display.
     */
    public MainWindow(Stage primaryStage, Logic logic, Path dataFilePath, String initialMessage) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.dataFilePath = dataFilePath;
        this.initialMessage = initialMessage;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }



    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        contactListPanel = new ContactListPanel(logic.getFilteredContactList());
        contactListPanelPlaceholder.getChildren().add(contactListPanel.getRoot());

        storyListPanel = new StoryListPanel(logic.getFilteredStoryList());
        storyListPanelPlaceholder.getChildren().add(storyListPanel.getRoot());

        followupListPanel = new FollowupListPanel(logic.getFilteredFollowupList());
        followupListPanelPlaceholder.getChildren().add(followupListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        if (initialMessage != null && !initialMessage.isBlank()) {
            resultDisplay.setFeedbackToUser(initialMessage);
        }

        StatusBarFooter statusBarFooter = new StatusBarFooter(dataFilePath);
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        helpPanel = new HelpPanel(() -> {
            helpPanelPlaceholder.setVisible(false);
            helpPanelPlaceholder.setManaged(false);
        });
        helpPanelPlaceholder.getChildren().add(helpPanel.getRoot());

        setupFilterIndicators();
    }

    /**
     * Configures the filter indicator badges for the contact, story, and followup panels.
     */
    private void setupFilterIndicators() {
        setupFilterIndicator(contactFilterIndicator, logic.getContactFilterStatusProperty());
        setupFilterIndicator(storyFilterIndicator, logic.getStoryFilterStatusProperty());
        setupFilterIndicator(followupFilterIndicator, logic.getFollowupFilterStatusProperty());
    }

    /**
     * Binds the text and style of {@code indicator} to {@code statusProperty}.
     */
    private void setupFilterIndicator(Label indicator, ObservableValue<String> statusProperty) {
        indicator.textProperty().bind(
                Bindings.createStringBinding(() -> "[" + statusProperty.getValue() + "]", statusProperty));
        Tooltip tooltip = new Tooltip();
        tooltip.textProperty().bind(statusProperty);
        indicator.setTooltip(tooltip);

        statusProperty.addListener((obs, oldVal, newVal) -> updateFilterBadgeStyle(indicator, newVal));
        updateFilterBadgeStyle(indicator, statusProperty.getValue());
    }

    /**
     * Updates the CSS style class of {@code indicator} based on whether {@code status} is "All" or a filter.
     */
    private void updateFilterBadgeStyle(Label indicator, String status) {
        indicator.getStyleClass().removeAll("filter-status-all", "filter-status-filtered");
        String styleClass = Model.FILTER_STATUS_ALL.equals(status) ? "filter-status-all" : "filter-status-filtered";
        indicator.getStyleClass().add(styleClass);
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Toggles the help panel.
     */
    @FXML
    public void handleHelp() {
        boolean isVisible = helpPanelPlaceholder.isVisible();
        if (!isVisible) {
            if (!primaryStage.isMaximized() && !primaryStage.isFullScreen()
                    && primaryStage.getWidth() < HELP_EXPANSION_THRESHOLD_WIDTH) {
                primaryStage.setWidth(Math.min(UiUtil.getPrimaryScreenWidth(), HELP_EXPANDED_WIDTH));
            }
        }
        helpPanelPlaceholder.setVisible(!isVisible);
        helpPanelPlaceholder.setManaged(!isVisible);
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        primaryStage.hide();
    }

    public ContactListPanel getContactListPanel() {
        return contactListPanel;
    }

    public ResultDisplay getResultDisplay() {
        return resultDisplay;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
