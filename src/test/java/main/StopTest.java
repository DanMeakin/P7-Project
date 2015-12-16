package main; 

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

import main.Stop;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class StopTest {

  private List<Stop> stops;
  private static List<Integer> stopIDs;
  private static List<String> stopNames;
  private static List<Double> stopLatitudes;
  private static List<Double> stopLongitudes;
  private static List<List<Integer>> stopDistances;

  /**
   * Sets-up fixtures and test data before any testing begins.
   */
  @BeforeClass
  public static void setUpClass() {
    stopIDs = Arrays.asList(1, 2, 3);
    stopNames = Arrays.asList("Ritavej", "AAU Busterminal", "Boulevarden");
    stopLatitudes = Arrays.asList(57.027063, 57.016123, 57.046237);
    stopLongitudes = Arrays.asList(9.959283, 9.991114, 9.918913);
    // Distances calculated using online calculator found at
    // http://www.movable-type.co.uk/scripts/latlong.html
    stopDistances  = Arrays.asList(
        Arrays.asList(0, 2279, 3242), // Stop 0 -> 0, 0 -> 1, 0 -> 2
        Arrays.asList(2279, 0, 5505), // Stop 1 -> 0, 1 -> 1, 1 -> 2
        Arrays.asList(3242, 5505, 0)  // Stop 2 -> 0, 2 -> 1, 2 -> 2
        );
  }

  /**
   * Executes set-up before each test.
   */
  @Before
  public void setUp() {
    stops = new ArrayList<Stop>();
    for (int i = 0; i < stopIDs.size(); i++) {
      stops.add(
          new Stop(
            stopIDs.get(i),
            stopNames.get(i), 
            stopLatitudes.get(i), 
            stopLongitudes.get(i)
            )
          );
    }
  }
  
  /**
   * Executes tear-down after each test.
   */
  @After
  public void tearDown() {
    for (Stop s : new ArrayList<>(Stop.getAllStops())) {
      Stop.removeStop(s);
    }
  }

  /**
   * Test getID method.
   */
  @Test
  public void testGetId() {
    for (int i = 0; i < stops.size(); i++) {
      assertEquals(stops.get(i).getID(), (int) stopIDs.get(i));
    }
  }

  /**
   * Test getName method.
   */
  @Test
  public void testGetName() {
    for (int i = 0; i < stops.size(); i++) {
      assertEquals(stops.get(i).getName(), stopNames.get(i));
    }
  }

  /**
   * Test getLocation method.
   */
  @Test
  public void testGetLocation() {
    for (int i = 0; i < stops.size(); i++) {
      double[] expectedLocation = new double[] {
        (double) stopLatitudes.get(i), 
        (double) stopLongitudes.get(i)
      };
      assertEquals(stops.get(i).getLocation()[0], expectedLocation[0], 0.00001);
      assertEquals(stops.get(i).getLocation()[1], expectedLocation[1], 0.00001);
    }
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * Test creation of a duplicate stop.
   *
   * It should not be possible to create a stop which is identical to another
   * stop. This method tests for this condition.
   */
  @Test
  public void testCreateDuplicateStop() throws IllegalArgumentException {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Stop with ID #" + stopIDs.get(0) + " already exists");
    new Stop(stopIDs.get(0), stopNames.get(0), stopLatitudes.get(0), stopLongitudes.get(0));
  }

  /**
   * Test creation of a stop with duplicate ID#.
   *
   * It should not be possible to create a stop which has the same ID# as
   * another stop. This method tests for this condition.
   */
  @Test
  public void testCreateStopWithDuplicateID() throws IllegalArgumentException {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Stop with ID #" + stopIDs.get(0) + " already exists");
    new Stop(stopIDs.get(0), "Another Stop", 999.123, 10.4543);
  }

  /**
   * Test creation of a stop with duplicate name.
   *
   * Certain stops (e.g. two on opposite sides of the same road) may have the
   * same name. As such, we must permit several stops to have identical names.
   * This method tests for this condition.
   */
  @Test
  public void testCreateStopWithDuplicateName() {
    Stop duplicateNameStop = new Stop(555, stopNames.get(0), 1234.55, 67.12);
    assertEquals(stopNames.get(0), duplicateNameStop.getName());
  }

  /**
   * Test removal of an existing stop.
   *
   * Existing stops should be removable when no longer in use. This method
   * tests that stops can be removed after creation.
   */
  @Test
  public void testRemoveStop() {
    for (Stop s : stops) {
      Stop.removeStop(s);
    }
    assertEquals(Stop.numberOfStops(), 0);
  }

  /**
   * Test counting of existing stops.
   *
   * Stop provides a rolling count of stops in existence. This method ensures
   * that the counting method functions correctly. There are three test Stop
   * objects created for the testing, so the count should equal three when this
   * test is run.
   */
  @Test
  public void testCountStops() {
    assertEquals(Stop.numberOfStops(), 3);
  }

  /**
   * Test distanceBetweenStops method.
   */
  @Test
  public void testDistanceBetweenStops() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        assertEquals(Stop.getDistanceBetweenStops(stops.get(i), stops.get(j)), (int) stopDistances.get(i).get(j));
      }
    }
  }

  /**
   * Test distanceTo method.
   */
  @Test
  public void testDistanceTo() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        System.out.println(stops.get(i).distanceTo(stops.get(j)));
        assertEquals(stops.get(i).distanceTo(stops.get(j)), (int) stopDistances.get(i).get(j));
      }
    }
  }
  /**
   * Test getLatitude method.
   *
   * This tests the getLatitude getter method on Stop.
   */
  @Test
  public void testGetLatitude() {
    for (int i = 0; i < 3; i++) {
      assertEquals(stops.get(i).getLatitude(), (double) stopLatitudes.get(i), 0.000000001);
    }
  }

  /**
   * Test getLatitudeInRadians method.
   *
   * This tests the conversion of latitude to radians.
   */
  @Test
  public void testGetLatitudeInRadians() {
    for (int i = 0; i < 3; i++) {
      double expected = stops.get(i).getLatitude() / 180 * Math.PI;
      double actual = stops.get(i).getLatitudeInRadians();
      assertEquals(expected, actual, 0.1);
    }
  }

  /**
   * Test getLongitude method.
   *
   * This tests the getLongitude getter method on Stop.
   */
  @Test
  public void testGetLongitude() {
    for (int i = 0; i < 3; i++) {
      assertEquals(stops.get(i).getLongitude(), (double) stopLongitudes.get(i), 0.000000001);
    }
  }

  /**
   * Test getLongitudeInRadians method.
   *
   * This tests the conversion of longitude to radians.
   */
  @Test
  public void testGetLongitudeInRadians() {
    for (int i = 0; i < 3; i++) {
      double expected = stops.get(i).getLongitude() / 180 * Math.PI;
      double actual = stops.get(i).getLongitudeInRadians();
      assertEquals(expected, actual, 0.1);
    }
  }
}
