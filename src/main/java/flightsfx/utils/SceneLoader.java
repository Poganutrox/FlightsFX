package flightsfx.utils;

import flightsfx.model.FlightsFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneLoader {
    public static void loadScreen(String viewPath, Stage stage, Scene scene)
            throws IOException
    {
        Parent root = FXMLLoader.load(
                FlightsFX.class.getResource(viewPath));
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }
}
