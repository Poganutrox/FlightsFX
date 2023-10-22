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

public class FXMLChartsViewController implements Initializable {
    @FXML
    private PieChart chart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Getting the main controller in order to get the flight list
        FXMLLoader loader = new FXMLLoader(FlightsFX.class.getResource("FXMLMainView.fxml"));
        try {
            Parent root = loader.load();
        } catch (IOException e) {
            showError(e.getMessage());
        }
        FXMLMainViewController controller = (FXMLMainViewController)loader.getController();
        List<Flight> flights = controller.getFlightList();

        //Setting chart settings
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
    public void onClickBackToMainView(ActionEvent actionEvent) throws IOException {
        SceneLoader.loadScreen("FXMLMainView.fxml",
                (Stage)((Node) actionEvent.getSource()).getScene().getWindow());
    }
}
