package main;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

/**
 * This class contains a suite of unit tests for the DataLoader class.
 */
public class DataLoaderTest {

  @BeforeClass
  public static void setUpClass() {
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

  @Test
  public void testDataLoader() {
    int preStops = Stop.getAllStops().size();
    int preRoutes = Route.getAllRoutes().size();
    int preSchedules = Schedule.getAllSchedules().size();
    DataLoader dl = new DataLoader("data/mock");
    int postStops = Stop.getAllStops().size();
    int postRoutes = Route.getAllRoutes().size();
    int postSchedules = Schedule.getAllSchedules().size();
    assertEquals(4, postRoutes - preRoutes);
    assertEquals(76, postStops - preStops);
  }
}
