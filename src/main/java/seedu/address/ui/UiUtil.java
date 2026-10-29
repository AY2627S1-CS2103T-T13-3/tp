package seedu.address.ui;

import javafx.stage.Screen;

/**
 * Utility functions for JavaFX UI operations.
 */
public class UiUtil {

    /**
     * Returns the visual bounds width of the primary screen.
     */
    public static double getPrimaryScreenWidth() {
        return Screen.getPrimary().getVisualBounds().getWidth();
    }
}
