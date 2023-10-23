package flightsfx.utils;

import flightsfx.model.FlightsFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The `SceneLoader` class provides a utility method for loading a new FXML view and updating the
 * scene in a JavaFX application.
 */
public class SceneLoader {

    /**
     * Loads a new FXML view and updates the provided scene with the new content.
     *
     * @param viewPath The path to the FXML view to be loaded.
     * @param stage    The primary stage for the application's user interface.
     * @param scene    The scene to be updated with the new content.
     * @throws IOException If there is an error during the loading of the new view.
     */
    public static void loadScreen(String viewPath, Stage stage, Scene scene) throws IOException {
        Parent root = FXMLLoader.load(FlightsFX.class.getResource(viewPath));
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }
}
