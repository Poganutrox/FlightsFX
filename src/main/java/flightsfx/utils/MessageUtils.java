package flightsfx.utils;

import javafx.scene.control.Alert;

/**
 * The `MessageUtils` class provides utility methods for displaying error and information messages
 * using JavaFX's Alert dialogs.
 */
public class MessageUtils {

    /**
     * Displays an error message in an Alert dialog.
     *
     * @param message The error message to be displayed.
     */
    public static void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setTitle("Error");
        dialog.setHeaderText("");
        dialog.setContentText(message);
        dialog.showAndWait();
    }

    /**
     * Displays an information message in an Alert dialog.
     *
     * @param message The information message to be displayed.
     */
    public static void showMessage(String message) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Information");
        dialog.setHeaderText("");
        dialog.setContentText(message);
        dialog.showAndWait();
    }
}
