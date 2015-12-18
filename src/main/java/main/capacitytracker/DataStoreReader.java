package main.capacitytracker;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
   * Reads matching records from the dataStore.
   *
   * A record matches if it relates to the same RouteTimetable and Stop as
   * those passed into the reader.
   *
   * @return CSVRecords containing matching data
   */
  public List<DataStoreRecord> read() {
    List<DataStoreRecord> matchingData = new ArrayList<>();
    for (CSVRecord record : dataStore) {
      if (isMatching(record)) {
        matchingData.add(new DataStoreRecord(record));
      }
    }
    return matchingData;
  }

  /**
   * Selects the record matching the passed date.
   *
   * As a RouteTimetable only runs once a day, there will only be at most
   * one record for a Stop on a RouteTimetable each day.
   *
   * @param date the date for which to get the record
   * @return record matching passed date
   */
  public DataStoreRecord selectRecordForDate(LocalDate date) {
    for (DataStoreRecord r : read()) {
      if (r.getTimestamp().toLocalDate().equals(date)) {
        return r;
      }
    }
    return null;
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
    return (getRouteTimetable().getID() == Integer.parseInt(record.get("routeTimetableID")) &&
            getStop().getID() == Integer.parseInt(record.get("stopID")));
  }

  /**
   * Gets RouteTimetable associated with this.
   *
   * @return RouteTimetable associated with this.
   */
  public RouteTimetable getRouteTimetable() {
    return routeTimetable;
  }

  /**
   * Gets Stop associated with this.
   *
   * A Stop does not need to be passed to DataStoreReader. As such, this method
   * might return a null value.
   *
   * @return Stop associated with this.
   */
  public Stop getStop() {
    return stop;
  }

}
