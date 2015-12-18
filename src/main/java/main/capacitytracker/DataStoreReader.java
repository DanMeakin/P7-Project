package main.capacitytracker;

import java.io.*;
import java.util.*;

import org.apache.commons.csv.*;

import main.RouteTimetable;
import main.Stop;

/**
 * This class reads bus capacity information from the datastore file upon
 * requests for capacity calculations.
 */
public class DataStoreReader {

  public CSVParser dataStore;

  private final RouteTimetable routeTimetable;
  private final Stop stop;

  /**
   * Instantiates a new DataStoreReader instance.
   *
   * @param stop           the stop for which to obtain data
   * @param routeTimetable the route timetable on which buses travel for which
   *                       to obtain data
   */
  public DataStoreReader(String dataStoreFolderPath, Stop stop, RouteTimetable routeTimetable) {
    this.stop = stop;
    this.routeTimetable = routeTimetable;
    try {
      initializeDataStore(dataStoreFolderPath);
    } catch (IOException e) {
      throw new RuntimeException("error accessing datastore: " + e);
    }
  }

  /**
   * Reads passenger data matching stop and routeTimetable.
   *
   * @return a list of maps containing data about passenger numbers on the
   *         matching RouteTimetable and at the matching Stop
   */
  public List<Map<String, Integer>> getPassengerData() {
    String[] keys = new String[] {
        "numberPassengersOnArrival",
        "numberPassengersExited",
        "numberPassengersBoarded",
        "numberPassengersOnDeparture",
        "maxSeatedPassengers",
        "maxStandingPassengers",
        "maxTotalPassengers",
    };
    List<Map<String, Integer>> data = new ArrayList<>();
    for (CSVRecord record : readMatchingData()) {
      Map<String, Integer> thisMap = new HashMap<>();
      for (String key : keys) {
        thisMap.put(key, Integer.parseInt(record.get(key)));
      }
      data.add(thisMap);
    }
    return data;
  }

  /**
   * Initializes the dataStore object associated with this.
   *
   * @param dataStoreFolderPath the path to the folder containing the datastore
   */
  private void initializeDataStore(String dataStoreFolderPath) throws IOException {
    File dsFile = new File(dataStoreFolderPath, "datastore.csv");
    BufferedReader br = new BufferedReader(new FileReader(dsFile));
    dataStore = new CSVParser(br, CSVFormat.DEFAULT);
  }

  private List<CSVRecord> readMatchingData() {
    List<CSVRecord> matchingData = new ArrayList<>();
    for (CSVRecord record : dataStore) {
      if (isMatching(record)) {
        matchingData.add(record);
      }
    }
    return matchingData;
  }

  /**
   * Tests if record matches Stop and RouteTimetable.
   *
   * The DataStoreReader processes relevant records only for a given query.
   * This method determines whether a record matches the current set of
   * criteria.
   *
   * @param record CSVRecord to test whether is matching
   * @return true if record matches, else false
   */
  private boolean isMatching(CSVRecord record) {
    return (
        Integer.parseInt(record.get("stopID")) == stop.getID() &&
        Integer.parseInt(record.get("routeTimetableID")) == routeTimetable.getID()
        );
  }

}
