package flightsfx.utils;

import javafx.scene.control.Alert;

public class MessageUtils {
    public static void showError(String message){
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setTitle("Error");
        dialog.setContentText(message);
        dialog.showAndWait();
    }
    public static void showMessage(String message){
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Information");
        dialog.setContentText(message);
        dialog.showAndWait();
    }
}
