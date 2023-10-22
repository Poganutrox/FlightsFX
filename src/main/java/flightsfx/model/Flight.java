package flightsfx.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Flight {
    private String flightNumber; //such as "IB601N"
    private String destination; //a city name
    private LocalDateTime departureTime; //The departure time and date
    private LocalTime flightDuration; //The flight duration, in hours and minutes

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

    public Flight(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Flight(String flightNumber, String destination, LocalDateTime departureTime, LocalTime flightDuration) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.departureTime = departureTime;
        this.flightDuration = flightDuration;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }
    public String getFormattedDepartureTime() {
        return departureTime.format(dateFormatter);
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getFlightDuration() {
        return flightDuration;
    }
    public String getFormattedFlightDuration() {
        return flightDuration.format(timeFormatter);
    }

    public void setFlightDuration(LocalTime flightDuration) {
        this.flightDuration = flightDuration;
    }

    @Override
    public String toString() {
        return flightNumber + ";" + destination + ";" + getFormattedDepartureTime() + ";" + getFormattedFlightDuration();
    }
}
