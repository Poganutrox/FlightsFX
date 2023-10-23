package flightsfx.model;

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
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static flightsfx.utils.FileUtils.loadFlights;
import static flightsfx.utils.FileUtils.saveFlights;
import static flightsfx.utils.MessageUtils.showError;
import static flightsfx.utils.MessageUtils.showMessage;

/**
 * This class represents the main view controller for the flight management application.
 * It implements the Initializable interface, allowing it to initialize the view components.
 * The class provides methods for handling user interactions and managing flight data.
 */
public class FXMLMainViewController implements Initializable {
    @FXML
    private TextField tfFlightNumber;
    @FXML
    private TextField tfDestination;
    @FXML
    private TextField tfDeparture;
    @FXML
    private TextField tfDuration;
    @FXML
    private Button btnDelete;
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
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

    /**
     * Initializes the flight management application.
     *
     * @param url    The URL of the FXML file.
     * @param rb     The ResourceBundle for localization.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Delete button must be disabled at the beginning
        btnDelete.setDisable(true);

        // Setting items in the tableview of flights
        flights = FXCollections.observableArrayList(loadFlights());

        colFlightNumber.setCellValueFactory(new PropertyValueFactory("FlightNumber"));
        colDestination.setCellValueFactory(new PropertyValueFactory("Destination"));
        colDeparture.setCellValueFactory(new PropertyValueFactory("FormattedDepartureTime"));
        colDuration.setCellValueFactory(new PropertyValueFactory("FormattedFlightDuration"));

        tvFlights.setItems(flights);

        // Setting the message shown in the table view when no flights are detected
        Label emptyContentMessage = new Label("No flights to show");
        tvFlights.setPlaceholder(emptyContentMessage);

        // Setting the different items in the combo box
        cbFilter.getItems().addAll(
                "Show all flights",
                "Show flights to the currently selected city",
                "Show long flights",
                "Show next 5 flights",
                "Show flight duration average"
        );

        // Listener that activates the delete button when clicking on the table view
        tvFlights.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Flight> observableValue, Flight flight, Flight t1) {
                btnDelete.setDisable(false);
            }
        });

        // Listener that detects when the user writes in the search field
        tfSearchFlight.textProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                onTypeSearch(newValue);
            }
        });
    }

    /**
     * Handles the action when the "Add" button is clicked.
     */
    public void onClickAdd() {
        try {
            if (tfFlightNumber.getText().equals("") ||
                    tfDestination.getText().equals("") ||
                    tfDeparture.getText().equals("") ||
                    tfDuration.getText().equals("")) {
                showError("No field can be empty");
            } else {
                flights.add(new Flight(tfFlightNumber.getText(),
                        tfDestination.getText(),
                        LocalDateTime.parse(tfDeparture.getText(), dateFormatter),
                        LocalTime.parse(tfDuration.getText(), timeFormatter)));

                tvFlights.setItems(flights);

                showMessage("Flight successfully added!");

                // Clean all the fields
                tfFlightNumber.clear();
                tfDestination.clear();
                tfDeparture.clear();
                tfDuration.clear();
            }
        } catch (DateTimeParseException dt) {
            showError("Incorrect date or time format");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /**
     * Handles the action when the "Delete" button is clicked.
     */
    public void onClickDelete() {
        Flight selectedFlight = tvFlights.getSelectionModel().selectedItemProperty().get();
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).equals(selectedFlight)) {
                flights.remove(i);
            }
        }
        tvFlights.setItems(flights);
    }

    /**
     * Handles the action when a filter is applied from the combo box.
     */
    public void onClickApplyFilter() {
        int selectedItem = cbFilter.getSelectionModel().selectedIndexProperty().get();
        switch (selectedItem) {
            case 0:
                tvFlights.setItems(flights);
                break;
            case 1:
                Flight selectedFlight = tvFlights.getSelectionModel().getSelectedItem();

                if (selectedFlight == null) {
                    showError("No flight selected");
                } else {
                    List<Flight> flightList = flights.stream()
                            .filter(p -> p.getDestination().equals(selectedFlight.getDestination()))
                            .collect(Collectors.toList());

                    if (flightList.isEmpty()) {
                        showError("No flights heading to the selected city");
                    } else {
                        tvFlights.setItems(FXCollections.observableArrayList(flightList));
                    }
                }
                break;
            case 2:
                tvFlights.setItems(FXCollections.observableArrayList(flights.stream()
                        .filter(p -> p.getFlightDuration().getHour() > 3)
                        .collect(Collectors.toList())));
                break;
            case 3:
                LocalDateTime currentDay = LocalDateTime.now();
                tvFlights.setItems(FXCollections.observableArrayList(flights.stream()
                        .filter(p -> p.getDepartureTime().isAfter(currentDay))
                        .sorted(Comparator.comparing(Flight::getDepartureTime))
                        .limit(5)
                        .collect(Collectors.toList())));
                break;
            case 4:
                OptionalDouble durationAvg = flights.stream()
                        .mapToInt(p -> {
                            int hours = p.getFlightDuration().getHour();
                            int min = p.getFlightDuration().getMinute();
                            return hours * 60 + min;
                        })
                        .average();

                long averageMinutes = (long) durationAvg.getAsDouble();
                LocalTime averageLocalTime = LocalTime.of((int) (averageMinutes / 60), (int) (averageMinutes % 60));
                showMessage(averageLocalTime.format(timeFormatter));
                break;
        }
    }

    /**
     * Handles the action when the user types into the search field.
     *
     * @param searchFlight The text entered the search field.
     */
    public void onTypeSearch(String searchFlight) {
        String searchFlightLower = searchFlight.toLowerCase();
        tvFlights.setItems(FXCollections.observableArrayList(flights.stream()
                .filter(p ->
                        p.getFlightDuration().toString().toLowerCase().contains(searchFlightLower) ||
                                p.getDestination().toLowerCase().contains(searchFlightLower) ||
                                p.getFlightNumber().toLowerCase().contains(searchFlightLower) ||
                                p.getDepartureTime().toString().toLowerCase().contains(searchFlightLower))
                .collect(Collectors.toList())));
    }

    /**
     * Handles the action when the "Go to Charts" button is clicked.
     *
     * @param actionEvent The event generated by the button click.
     * @throws IOException If there is an error while loading the charts view.
     */
    public void onClickGoToCharts(ActionEvent actionEvent){
        try {
            SceneLoader.loadScreen("FXMLChartsView.fxml",
                    (Stage) ((Node) actionEvent.getSource()).getScene().getWindow(),
                    ((Node) actionEvent.getSource()).getScene());
        }
        catch(IOException e){
            showError(e.getMessage());
        }
    }

    /**
     * Retrieves the list of flights.
     *
     * @return The list of flights.
     */
    public List<Flight> getFlightList() {
        return flights;
    }

    /**
     * Sets a listener for the close event of the application's stage to save flight data.
     *
     * @param stage The stage of the application.
     */
    public void setOnCloseListener(Stage stage) {
        stage.setOnCloseRequest(e -> {
            saveFlights(flights);
        });
    }
}