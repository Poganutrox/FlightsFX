package flightsfx.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FlightsFX extends Application {
    @Override
    public void start(Stage stage) throws IOException {
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

    public static void main(String[] args) {
        launch();
    }
}
