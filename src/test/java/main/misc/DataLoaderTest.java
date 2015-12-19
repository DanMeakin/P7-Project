package main.misc;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import main.model.*;

/**
 * This class contains a suite of unit tests for the DataLoader class.
 */
public class DataLoaderTest {

  @Before
  public void setUp() {
    for (Bus b : new ArrayList<Bus>(Bus.getAllBuses())) {
      Bus.removeBus(b);
    }
    for (Path p : new ArrayList<Path>(Path.getAllPaths())) {
      Path.removePath(p);
    }
    for (Schedule s : new ArrayList<Schedule>(Schedule.getAllSchedules())) {
      Schedule.removeSchedule(s);
    }
    for (Stop s : new ArrayList<Stop>(Stop.getAllStops())) {
      Stop.removeStop(s);
    }
  }

  /**
   * Test DataLoader class.
   */
  @Test
  public void testDataLoader() {
    int preStops = Stop.getAllStops().size();
    int preRoutes = Route.getAllRoutes().size();
    int preSchedules = Schedule.getAllSchedules().size();
    new DataLoader("data/mock");
    int postStops = Stop.getAllStops().size();
    int postRoutes = Route.getAllRoutes().size();
    int postSchedules = Schedule.getAllSchedules().size();
    assertEquals(4, postRoutes - preRoutes);
    assertEquals(76, postStops - preStops);
    assertEquals(3, postSchedules - preSchedules);
  }

  /**
   * Test DataLoader with an illegal path passed to constructor.
   */
  @Test
  public void testDataLoaderWithIllegalPath() {
    String expectedMessage = "IOException: ILLEGALPATH/THISISILLEGAL/stops.csv (No such file or directory)";
    try {
      new DataLoader("ILLEGALPATH/THISISILLEGAL");
      fail("expected exception to be thrown due to illegal path");
    } catch (RuntimeException e) {
      assertEquals(expectedMessage, e.getMessage());
    }
  }
}
