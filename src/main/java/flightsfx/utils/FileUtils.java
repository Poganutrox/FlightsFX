package flightsfx.utils;

import flightsfx.model.Flight;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The `FileUtils` class provides utility methods for loading and saving flight data to a file.
 */
public class FileUtils {

    /**
     * Loads flight data from a file named "flights.txt."
     *
     * @return A list of `Flight` objects loaded from the file.
     */
    public static List<Flight> loadFlights() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        try {
            return Files.lines(Paths.get("flights.txt"))
                    .map(line -> new Flight(line.split(";")[0], line.split(";")[1],
                            LocalDateTime.parse(line.split(";")[2], dateFormatter),
                            LocalTime.parse(line.split(";")[3], timeFormatter)))
                    .collect(Collectors.toList());
        } catch (IOException fileError) {
            System.err.println(
                    "Error reading file: " +
                            fileError.getMessage());
        }

        return null;
    }

    /**
     * Saves flight data to a file named "flights.txt."
     *
     * @param flightList The list of `Flight` objects to be saved to the file.
     */
    public static void saveFlights(List<Flight> flightList) {
        try (PrintWriter pw = new PrintWriter("flights.txt")) {
            flightList.stream()
                    .forEach(f -> pw.println(f.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
