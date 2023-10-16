package flightsfx.model;

import flightsfx.utils.FileUtils;
import flightsfx.utils.SceneLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static flightsfx.utils.FileUtils.loadFlights;
import static flightsfx.utils.FileUtils.saveFlights;

public class FXMLMainViewController implements Initializable {
    @FXML
    private TextField tfFlightNumber;
    @FXML
    private TextField tfDestination;
    @FXML
    private Button btnAdd;
    @FXML
    private TextField tfDeparture;
    @FXML
    private TextField tfDuration;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnApply;
    @FXML
    private TextField tfSearchFlight;
    @FXML
    private TableView<Flight> tvFlights;
    @FXML
    private TableColumn<Flight, String> colFlightNumber;
    @FXML
    private TableColumn<Flight, String> colDestination;
    @FXML
    private TableColumn<Flight, LocalDateTime> colDeparture;
    @FXML
    private TableColumn<Flight, LocalTime> colDuration;
    @FXML
    private ComboBox cbFilter;
    private ObservableList<Flight> flights = null;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        //Delete button must be disabled at the beginning
        btnDelete.setDisable(true);

        //Setting items in the tableview of flights
        flights = FXCollections.observableArrayList(loadFlights());

        colFlightNumber.setCellValueFactory(new PropertyValueFactory("FlightNumber"));
        colDestination.setCellValueFactory(new PropertyValueFactory("Destination"));
        colDeparture.setCellValueFactory(new PropertyValueFactory("DepartureTime"));
        colDuration.setCellValueFactory(new PropertyValueFactory("FlightDuration"));

        tvFlights.setItems(flights);

        //Setting the different items in the combo box
        cbFilter.getItems().addAll(
                "Show all flights",
                "Show flights to currently selected city",
                "Show long flights",
                "Show next 5 flights",
                "Show flight duration average"
        );

        tfSearchFlight.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                onTypeSearch(newValue);
            }
        });
    }
    public void onClickAdd(ActionEvent actionEvent) {
        if (tfFlightNumber.getText().equals("") ||
                tfDestination.getText().equals("")||
                tfDeparture.getText().equals("")||
                tfDuration.getText().equals(""))
        {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("Error");
            dialog.setHeaderText("Error adding data");
            dialog.setContentText("No field can be empty");
            dialog.showAndWait();
        } else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

            flights.add(new Flight(tfFlightNumber.getText(),
                    tfDestination.getText(),
                    LocalDateTime.parse(tfDeparture.getText(), dateFormatter),
                    LocalTime.parse(tfDuration.getText(), timeFormatter)));

            saveFlights(flights);
            tvFlights.setItems(flights);
        }
    }

    public void onClickDelete(ActionEvent actionEvent) {
        int selectRowIndex = tvFlights.getSelectionModel().selectedIndexProperty().get();
        flights.remove(selectRowIndex);
        saveFlights(flights);
        tvFlights.setItems(flights);
    }

    public void onClickApplyFilter(ActionEvent actionEvent) {
        int selectedItem = cbFilter.getSelectionModel().selectedIndexProperty().get();
        switch(selectedItem){
            case 0:
                tvFlights.setItems(flights);
                break;
            case 1:
                Flight selectedFlight = tvFlights.getSelectionModel().getSelectedItem();

                if (selectedFlight == null) {
                    Alert dialog = new Alert(Alert.AlertType.ERROR);
                    dialog.setTitle("Error");
                    dialog.setHeaderText("No flight selected");
                    dialog.showAndWait();
                } else {
                    List<Flight> flightList = flights.stream()
                            .filter(p -> p.getDestination().equals(selectedFlight.getDestination()))
                            .collect(Collectors.toList());

                    if (flightList.isEmpty()) {
                        Alert dialog = new Alert(Alert.AlertType.ERROR);
                        dialog.setTitle("Error");
                        dialog.setHeaderText("No flights to the selected city");
                        dialog.showAndWait();
                    } else {
                        tvFlights.setItems(FXCollections.observableArrayList(flightList));
                    }
                }
                break;
            case 2:
                tvFlights.setItems(FXCollections.observableArrayList(flights.stream()
                        .filter(p -> p.flightDuration.getHour() > 3)
                        .collect(Collectors.toList())));
                break;
            case 3:
                LocalDateTime currentDay = LocalDateTime.now();
                tvFlights.setItems(FXCollections.observableArrayList(flights.stream()
                        .filter(p -> p.departureTime.isAfter(currentDay))
                        .sorted(Comparator.comparing(Flight :: getDepartureTime).reversed())
                        .limit(5)
                        .collect(Collectors.toList())));
                break;
            case 4:
                OptionalDouble durationAvg = flights.stream()
                        .mapToInt(p -> {
                            int hours = p.flightDuration.getHour();
                            int min = p.flightDuration.getMinute();
                            return hours * 60 + min;
                        })
                        .average();

                long averageMinutes = (long) durationAvg.getAsDouble();
                LocalTime averageLocalTime = LocalTime.of((int)(averageMinutes / 60), (int)(averageMinutes % 60));
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Flight duration average");
                dialog.setHeaderText(averageLocalTime.format(timeFormatter));
                dialog.showAndWait();
                break;
        }

    }

    public void onTypeSearch(String searchFlight) {
        tvFlights.setItems(FXCollections.observableArrayList(flights.stream()
                .filter(p ->
                    p.flightDuration.toString().contains(searchFlight) ||
                    p.destination.contains(searchFlight) ||
                    p.flightNumber.toString().contains(searchFlight)||
                    p.departureTime.toString().contains(searchFlight))
                .collect(Collectors.toList())));
    }

    public void onClickGoToCharts(ActionEvent actionEvent) throws IOException {
        SceneLoader.loadScreen("FXMLChartsView.fxml",
                (Stage)((Node) actionEvent.getSource()).getScene().getWindow());
    }
    public List<Flight> getFlightList(){
        return flights;
    }
}