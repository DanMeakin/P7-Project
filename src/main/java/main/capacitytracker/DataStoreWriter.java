package main.capacitytracker;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.csv.*;

import main.Bus;
import main.RouteTimetable;
import main.Stop;

/**
 * This class writes bus capacity information to the datastore file upon
 * state changes on a bus.
 *
 * It is notified by each bus instance it is attached to of state changes. This
 * triggers the write method and ensures that all state changes are retained
 * for reference in future capacity calculations.
 */
public class DataStoreWriter implements Observer {

  public CSVPrinter dataStore;

  /**
   * Instantiates a new DataStoreWriter instance.
   *
   * @param dataStoreFolderPath the path to the folder containing the datastore
   */
  public DataStoreWriter(String dataStoreFolderPath) {
    try {
      initializeDataStore(dataStoreFolderPath);
    } catch (IOException e) {
      throw new RuntimeException("error accessing datastore: " + e);
    }
  }

  /**
   * Initializes the dataStore object associated with this.
   *
   * @param dataStoreFolderPath the path to the folder containing the datastore
   */
  private void initializeDataStore(String dataStoreFolderPath) throws IOException {
    File dsFile = new File(dataStoreFolderPath, "datastore.csv");
    BufferedWriter bw = new BufferedWriter(new FileWriter(dsFile));
    dataStore = new CSVPrinter(bw, CSVFormat.DEFAULT);
    if (fileIsEmpty(dsFile)) {
      writeHeader();
    }
  }

  /**
   * Updates this Observer object.
   *
   * This method is called by the Observable to which this is attached,
   * namely a bus instance. When called, this triggers the write method
   * to store the state of the bus at the relevant time.
   *
   * @param o the observable object calling update
   * @param arg an argument passed to notifyObservers method on observable
   * @throws IllegalArgumentException if passed a non-Bus Observable
   */
  @Override
  public void update(Observable o, Object arg) throws IllegalArgumentException {
    if (o instanceof Bus) {
    write((Bus) o);
    } else {
    String msg = "CapacityDataStoreWriter must be attached only to Bus; " +
           "got " + o.getClass().getName();
    throw new IllegalArgumentException(msg);
    }
  }

  /**
   * Writes a record to datastore.
   *
   * This method is only called by the update method triggered by a bus instance
   * to which this is attached.
   *
   * @param bus the bus for which data is to be written
   */
  private void write(Bus bus){

    try {
      dataStore.printRecord(generateRecord(bus));
    } catch (IOException e) {
      String msg = "unable to write to datastore: " + e;
      throw new RuntimeException(msg);
    }
  }

  /**
   * Generates a record for writing to file.
   *
   * @param bus the bus for which to data is to be generated
   * @return a list of strings containing data to be written to datastore
   */
  List<String> generateRecord(Bus bus) {
    // Get Stop and RouteTimetable
    RouteTimetable rt = bus.getRouteTimetable();
    Stop stop = bus.getStop();

    // Get passenger number information
    int passengersBoarded = bus.getNumPassengersBoarded();
    int passengersExited = bus.getNumPassengersExited();
    int passengersOnDeparture = bus.getNumPassengers();
    int passengersOnArrival = passengersOnDeparture + passengersExited - passengersBoarded;

    // Set dateformat
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Create record for datastore
    List<String> record = Arrays.asList(
        LocalDateTime.now().format(dateFormatter),                                // Timestamp
        Integer.toString(bus.getFleetNumber()),                                   // Fleet number
        Integer.toString(rt.getID()),                                             // Routetimetable ID#
        rt.getRoute().getNumber(),                                                // Route number
        rt.getRoute().getDescription(),                                           // Route description
        String.format("%2d:%2d", rt.getStartTime() / 60, rt.getStartTime() % 60), // Starting time
        rt.getSchedule().getOperatingDay().toString(),                            // Operating day (WEEKDAY, SATURDAY, SUNDAY)
        Integer.toString(stop.getID()),                                           // Stop ID#
        Integer.toString(passengersOnArrival),                                    // Passengers on bus at arrival at stop
        Integer.toString(passengersExited),                                       // Passengers exiting at stop 
        Integer.toString(passengersBoarded),                                      // Passengers boarding at stop
        Integer.toString(passengersOnDeparture),                                  // Passengers on bus at departure from stop
        Integer.toString(bus.getSeatedCapacity()),                                // Total seated capacity
        Integer.toString(bus.getStandingCapacity()),                              // Total standing capacity
        Integer.toString(bus.getTotalCapacity()),                                 // Total capacity
        Double.toString(bus.getOccupancyLevel())                                  // Total occupancy
        );

    return record;
  }

  /**
   * Writes header to DataStore.
   *
   * This method is called if DataStore is empty. If so, a header must be
   * written prior to any data being entered into datastore.
   *
   * @throws IOException if datastore cannot be written to
   */
  private void writeHeader() throws IOException {
    List<String> columns = Arrays.asList(
        "timestamp",
        "busFleetNumber",
        "routeTimetableID",
        "routeNumber",
        "routeDescription",
        "routeTimetableStartTime",
        "scheduleDayType",
        "stopID",
        "numberPassengersOnArrival",
        "numberPassengersExited",
        "numberPassengersBoarded",
        "numberPassengersOnDeparture",
        "maxSeatedPassengers",
        "maxStandingPassengers",
        "maxTotalPassengers",
        "occupancyLevel"
        );
    dataStore.printRecords(columns);
  }

  /**
   * Tests if file is empty.
   *
   * @param f file to test if empty
   * @return true if file empty, else false
   */
  private boolean fileIsEmpty(File f) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(f));
    try {
      return (br.readLine() == null);
    } finally {
      br.close();
    }
  }

}
