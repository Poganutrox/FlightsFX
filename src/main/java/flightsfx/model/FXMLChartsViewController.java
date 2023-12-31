package flightsfx.model;

import flightsfx.utils.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static flightsfx.utils.MessageUtils.showError;

/**
 * This class represents a controller for the charts view in the flight management application.
 * It implements the Initializable interface to initialize the view components and provides
 * functionality for displaying a pie chart that represents the number of flights per destination.
 */
public class FXMLChartsViewController implements Initializable {

    @FXML
    private PieChart chart;

    /**
     * Initializes the charts view controller.
     *
     * @param url The URL of the FXML file.
     * @param rb  The ResourceBundle for localization.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Getting the main controller to access the flight list
        FXMLLoader loader = new FXMLLoader(FlightsFX.class.getResource("FXMLMainView.fxml"));
        try {
            Parent root = loader.load();
        } catch (IOException e) {
            showError(e.getMessage());
        }
        FXMLMainViewController controller = loader.getController();
        List<Flight> flights = controller.getFlightList();

        // Setting up the chart with flight data
        chart.setTitle("Number of flights per destination");
        Map<String, Long> result;
        result = flights.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getDestination(),
                        Collectors.counting()));

        result.forEach((destination, sum) -> {
            chart.getData().add(new PieChart.Data(destination, sum));
        });
    }

    /**
     * Handles the action when the "Back to Main View" button is clicked.
     *
     * @param actionEvent The event generated by the button click.
     * @throws IOException If there is an error while loading the main view.
     */
    public void onClickBackToMainView(ActionEvent actionEvent) {
        try {
            SceneLoader.loadScreen("FXMLMainView.fxml",
                    (Stage) ((Node) actionEvent.getSource()).getScene().getWindow(),
                    ((Node) actionEvent.getSource()).getScene());
        }
        catch(IOException e){
            showError(e.getMessage());
        }
    }
}
