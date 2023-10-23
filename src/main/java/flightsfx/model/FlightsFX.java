package flightsfx.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static flightsfx.utils.MessageUtils.showError;

/**
 * The main class of the flight management application.
 * It extends the Application class and serves as the entry point for the JavaFX application.
 * This class is responsible for starting the application, loading the main view, and initializing the
 * user interface.
 */
public class FlightsFX extends Application {

    /**
     * The entry point for the JavaFX application.
     *
     * @param stage The primary stage for the application's user interface.
     * @throws IOException If there is an error during the loading of the main view.
     */
    @Override
    public void start(Stage stage){
        try {


            FXMLLoader fxmlLoader = new FXMLLoader(FlightsFX.class.getResource("FXMLMainView.fxml"));
            Parent root = fxmlLoader.load();

            FXMLMainViewController controller = fxmlLoader.getController();
            controller.setOnCloseListener(stage);

            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add("AlternativeStyle.css");
            stage.setTitle("Flight control");
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e){
            showError(e.getMessage());
        }
    }

    /**
     * The main method of the application, responsible for launching the JavaFX application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        launch();
    }
}
