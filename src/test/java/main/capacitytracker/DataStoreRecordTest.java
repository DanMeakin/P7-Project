package main.capacitytracker;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.csv.*;

import main.model.*;

/**
 * This class contains the test suite for the DataStoreRecord class.
 */
public class DataStoreRecordTest {

  private static CSVRecord csvRecord;
  private static DataStoreRecord dataStoreRecord;
  private static Bus bus;
  private static Stop stop;
  private static RouteTimetable routeTimetable;

  /**
   * Sets-up fixtures etc before any tests are run.
   */
  @BeforeClass
  public static void setUpClass() {
    // Clear all existing Stops, Paths & Schedules
    for (Schedule s : new ArrayList<Schedule>(Schedule.getAllSchedules())) {
      Schedule.removeSchedule(s);
    }
    for (Stop s : new ArrayList<Stop>(Stop.getAllStops())) {
      Stop.removeStop(s);
    }
    for (Path p : new ArrayList<Path>(Path.getAllPaths())) {
      Path.removePath(p);
    }
    for (Bus b : new ArrayList<Bus>(Bus.getAllBuses())) {
      Bus.removeBus(b);
    }

    Route route = new Route("10", "Test Description", mock(Stop.class));
    routeTimetable = mock(RouteTimetable.class);
    when(routeTimetable.getID()).thenReturn(1088);
    when(routeTimetable.equals(routeTimetable)).thenReturn(true);
    Schedule s = new Schedule(LocalDate.of(2015, Month.JANUARY, 1), LocalDate.of(2015, Month.DECEMBER, 31), Schedule.DayOption.WEEKDAYS) {
      @Override
      public RouteTimetable nextDepartureRouteTimetable(int time, Stop stop, Route route) throws IllegalArgumentException {
        return routeTimetable;
      }
    };
    s.addRouteTimetable(routeTimetable);
    BusType bt = mock(BusType.class);
    when(bt.getSeatedCapacity()).thenReturn(22);
    when(bt.getStandingCapacity()).thenReturn(4);
    when(bt.getTotalCapacity()).thenReturn(26);
    bus = new Bus(740, bt, mock(Date.class));
    stop = new Stop(2568457, "S1", 1, 2);
    String csvString = 
      "timestamp,busFleetNumber,routeTimetableID,routeNumber,routeDescription,routeTimetableStartTime,scheduleDayType,stopID,numberPassengersOnArrival,numberPassengersExited,numberPassengersBoarded,numberPassengersOnDeparture,maxSeatedPassengers,maxStandingPassengers,maxTotalPassengers,occupancyLevel\n" +
      "2015-12-17 21:22,740,1088,10,Test Description,05:26,WEEKDAYS,2568457,17,19,7,5,22,4,26,0.4452748033\n";
    try {
      CSVParser p = CSVParser.parse(csvString, CSVFormat.DEFAULT.withHeader());
      for (CSVRecord r : p) {
        csvRecord = r;
        dataStoreRecord = new DataStoreRecord(r);
      }
    } catch (IOException e) {
      fail("unable to parse csvString");
    }  
  }

  /**
   * Tests getBus method.
   */
  @Test
  public void testGetBus() {
    assertEquals(bus, dataStoreRecord.getBus());
  }

  /**
   * Tests getTimestamp method.
   */
  @Test
  public void testGetTimestamp() {
    assertEquals(LocalDateTime.of(2015, Month.DECEMBER, 17, 21, 22), dataStoreRecord.getTimestamp());
  }

  /**
   * Tests getRouteTimetable method.
   */
  @Test
  public void testGetRouteTimetable() {
    assertEquals(routeTimetable, dataStoreRecord.getRouteTimetable());
  }

  /**
   * Tests getRouteTimetable method.
   */
  @Test
  public void testGetStop() {
    assertEquals(stop, dataStoreRecord.getStop());
  }

  /**
   * Tests getNumPassengersOnArrival method.
   */
  @Test
  public void testGetNumPassengersOnArrival() {
    assertEquals(17, dataStoreRecord.getNumPassengersOnArrival());
  }

  /**
   * Tests getNumPassengersOnDeparture method.
   */
  @Test
  public void testGetNumPassengersOnDeparture() {
    assertEquals(5, dataStoreRecord.getNumPassengersOnDeparture());
  }

  /**
   * Tests getNumPassengersExited method.
   */
  @Test
  public void testGetNumPassengersExited() {
    assertEquals(19, dataStoreRecord.getNumPassengersExited());
  }

  /**
   * Tests getNumPassengersBoarded method.
   */
  @Test
  public void testGetNumPassengersBoarded() {
    assertEquals(7, dataStoreRecord.getNumPassengersBoarded());
  }

  /**
   * Tests getOccupancyLevel method.
   */
  @Test
  public void testGetOccupancyLevel() {
    assertEquals(0.4452748033, dataStoreRecord.getOccupancyLevel(), 0.00000001);
  }

  /**
   * Tests getMaxSeatedCapacity method.
   */
  @Test
  public void testGetMaxSeatedCapacity() {
    assertEquals(22, dataStoreRecord.getMaxSeatedCapacity());
  }

  /**
   * Tests getMaxStandingCapacity method.
   */
  @Test
  public void testGetMaxStandingCapacity() {
    assertEquals(4, dataStoreRecord.getMaxStandingCapacity());
  }

  /**
   * Tests getMaxTotalCapacity method.
   */
  @Test
  public void testGetMaxTotalCapacity() {
    assertEquals(26, dataStoreRecord.getMaxTotalCapacity());
  }

  /**
   * Tests getSeatedOccupancyRate method.
   */
  @Test
  public void testGetSeatedOccupancyRate() {
    assertEquals((double) 5 / bus.getSeatedCapacity(), dataStoreRecord.getSeatedOccupancyRate(), 0.0000001);
  }

  /**
   * Tests getTotalOccupancyRate method.
   */
  @Test
  public void testGetTotalOccupancyRate() {
    assertEquals((double) 5 / bus.getTotalCapacity(), dataStoreRecord.getTotalOccupancyRate(), 0.00000001);
  }

}
