package flightsfx.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * The `Flight` class represents a flight entity with attributes such as flight number, destination,
 * departure time, and flight duration. It also provides methods for formatting date and time, and
 * includes a `toString` method for converting the flight information to a string format.
 */
public class Flight {
    private String flightNumber; // Such as "IB601N"
    private String destination; // A city name
    private LocalDateTime departureTime; // The departure time and date
    private LocalTime flightDuration; // The flight duration, in hours and minutes

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

    /**
     * Constructs a `Flight` object with the given flight number.
     *
     * @param flightNumber The flight number.
     */
    public Flight(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * Constructs a `Flight` object with the provided flight details.
     *
     * @param flightNumber     The flight number.
     * @param destination      The destination city name.
     * @param departureTime    The departure time and date.
     * @param flightDuration   The flight duration in hours and minutes.
     */
    public Flight(String flightNumber, String destination, LocalDateTime departureTime, LocalTime flightDuration) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.departureTime = departureTime;
        this.flightDuration = flightDuration;
    }

    /**
     * Retrieves the flight number.
     *
     * @return The flight number.
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * Sets the flight number.
     *
     * @param flightNumber The flight number.
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * Retrieves the destination city name.
     *
     * @return The destination city name.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets the destination city name.
     *
     * @param destination The destination city name.
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Retrieves the departure time and date.
     *
     * @return The departure time and date.
     */
    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    /**
     * Formats and retrieves the departure time in the "dd/MM/yyyy HH:mm" format.
     *
     * @return The formatted departure time.
     */
    public String getFormattedDepartureTime() {
        return departureTime.format(dateFormatter);
    }

    /**
     * Sets the departure time and date.
     *
     * @param departureTime The departure time and date.
     */
    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * Retrieves the flight duration in hours and minutes.
     *
     * @return The flight duration.
     */
    public LocalTime getFlightDuration() {
        return flightDuration;
    }

    /**
     * Formats and retrieves the flight duration in the "H:mm" format.
     *
     * @return The formatted flight duration.
     */
    public String getFormattedFlightDuration() {
        return flightDuration.format(timeFormatter);
    }

    /**
     * Sets the flight duration.
     *
     * @param flightDuration The flight duration in hours and minutes.
     */
    public void setFlightDuration(LocalTime flightDuration) {
        this.flightDuration = flightDuration;
    }

    /**
     * Converts the `Flight` object to a string format.
     *
     * @return A string representation of the flight information.
     */
    @Override
    public String toString() {
        return flightNumber + ";" + destination + ";" + getFormattedDepartureTime() + ";" + getFormattedFlightDuration();
    }
}
