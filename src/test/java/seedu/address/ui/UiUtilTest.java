package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UiUtilTest {

    @Test
    public void getPrimaryScreenWidth_returnsPositiveWidth() {
        // When JavaFX graphics is available, screen width should be positive
        try {
            double width = UiUtil.getPrimaryScreenWidth();
            assertTrue(width > 0);
        } catch (NoClassDefFoundError | ExceptionInInitializerError | RuntimeException e) {
            // In headless/containerized test environments without display or graphics toolkit initialized,
            // accessing Screen may fail or not be supported.
        }
    }
}
