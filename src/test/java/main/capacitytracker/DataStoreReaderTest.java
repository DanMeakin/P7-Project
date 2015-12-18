package main.capacitytracker;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.csv.*;

import main.model.*;

public class DataStoreReaderTest {

  private static DataStoreReader dataStoreReader;
  private static String dataStorePath;
  private static String csvString;
  private static String csvStringDateFilter;

  @BeforeClass
  public static void setUpClass() {
    csvString = 
      "timestamp,busFleetNumber,routeTimetableID,routeNumber,routeDescription,routeTimetableStartTime,scheduleDayType,stopID,numberPassengersOnArrival,numberPassengersExited,numberPassengersBoarded,numberPassengersOnDeparture,maxSeatedPassengers,maxStandingPassengers,maxTotalPassengers,occupancyLevel\n" +
      "2015-12-17 21:22,740,1088,10,Test Description,05:26,WEEKDAYS,2568457,17,19,7,5,22,4,26,0.4452748033\n" +
      "2015-12-18 18:01,491,1088,10,Test Description,05:26,WEEKDAYS,2568457,17,19,7,5,22,4,26,0.4452748033\n";
    csvStringDateFilter = 
      "timestamp,busFleetNumber,routeTimetableID,routeNumber,routeDescription,routeTimetableStartTime,scheduleDayType,stopID,numberPassengersOnArrival,numberPassengersExited,numberPassengersBoarded,numberPassengersOnDeparture,maxSeatedPassengers,maxStandingPassengers,maxTotalPassengers,occupancyLevel\n" +
      "2015-12-18 18:01,491,1088,10,Test Description,05:26,WEEKDAYS,2568457,17,19,7,5,22,4,26,0.4452748033\n";
    Route r = new Route("10", "Test Description", mock(Stop.class));
    RouteTimetable rt = mock(RouteTimetable.class);
    when(rt.getID()).thenReturn(1088);
    when(rt.equals(rt)).thenReturn(true);
    Schedule s = new Schedule(LocalDate.of(2015, Month.JANUARY, 1), LocalDate.of(2015, Month.DECEMBER, 31), Schedule.DayOption.WEEKDAYS) {
      @Override
      public RouteTimetable nextDepartureRouteTimetable(int time, Stop stop, Route route) throws IllegalArgumentException {
        return rt;
      }
    };
    s.addRouteTimetable(rt);
    new Bus(740, mock(BusType.class), mock(Date.class));
    new Bus(153, mock(BusType.class), mock(Date.class));
    new Bus(491, mock(BusType.class), mock(Date.class));
    Stop stop = new Stop(2568457, "S1", 1, 2);
    new Stop(987101, "S2", 1, 2);

    dataStorePath = "data/mock/reader";
    try {
      dataStoreReader = new DataStoreReader(dataStorePath, stop, rt);
    } catch (IOException e) {
      fail("unable to create datastorereader: " + e);
    }
  }

  /**
   * Tests read method.
   */
  @Test
  public void testRead() {
    for (int i = 0; i < 5; i++) {
      CSVParser p;
      try {
        p = CSVParser.parse(csvString, CSVFormat.DEFAULT.withHeader());
        List<DataStoreRecord> expected = new ArrayList<>();
        for (CSVRecord r : p) {
          expected.add(new DataStoreRecord(r));
        }
        List<DataStoreRecord> actual = dataStoreReader.read();
        assertEquals(expected, actual);
      } catch (IOException e) {
        fail("unable to parse csvString");
      }
    }
  }

  /**
   * Tests read method with non-existent datastore file.
   */
  @Test
  public void testReadWithNonExistentFile() {
    try {
      DataStoreReader dsr = new DataStoreReader("ILLEGALFILEPATH/ILLEGAL", mock(Stop.class), mock(RouteTimetable.class));
      fail("expected IOException to be thrown");
    } catch (IOException e) {
      String msg = "unable to find datastore file: ILLEGALFILEPATH/ILLEGAL/datastore.csv";
      assertEquals(msg, e.getMessage());
    }
  }

  /**
   * Tests selectRecordForDate method.
   */
  @Test
  public void testRecordForDate() {
    CSVParser p;
    try {
      p = CSVParser.parse(csvStringDateFilter, CSVFormat.DEFAULT.withHeader());
      DataStoreRecord expected = null;
      for (CSVRecord r : p) {
        expected = new DataStoreRecord(r);
      }
      DataStoreRecord actual = dataStoreReader.selectRecordForDate(LocalDate.of(2015, Month.DECEMBER, 18));
      assertEquals(expected, actual);
    } catch (IOException e) {
      fail("unable to parse csvString");
    }
  }

}
