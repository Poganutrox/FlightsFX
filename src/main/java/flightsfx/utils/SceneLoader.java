package flightsfx.utils;

import flightsfx.model.FlightsFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneLoader {
    public static void loadScreen(String viewPath, Stage stage)
            throws IOException
    {
        Parent root = FXMLLoader.load(
                FlightsFX.class.getResource(viewPath));
        Scene viewScene = new Scene(root,900, 600);
        stage.setScene(viewScene);
        stage.show();
    }
}
