package org.test;

import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

import main.Stop;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class StopTest {

  private static List<Stop> stops;
  private static List<Integer> stopIDs;
  private static List<String> stopNames;
  private static List<Double> stopLatitudes;
  private static List<Double> stopLongitudes;

  @BeforeClass
  public static void setUpClass() {
    stopIDs = Arrays.asList(1, 2, 3);
    stopNames = Arrays.asList("Ritavej", "AAU Busterminal", "Boulevarden");
    stopLatitudes = Arrays.asList(59.1092, 59.201, 42.123);
    stopLongitudes = Arrays.asList(23.123, 23.165, 40.10);
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

  @Before
  public void setUp() {
  }

  @Test
  public void testGetId() {
    for (int i = 0; i < stops.size(); i++) {
      assertEquals(stops.get(i).getID(), (int) stopIDs.get(i));
    }
  }

  @Test
  public void testGetName() {
    for (int i = 0; i < stops.size(); i++) {
      assertEquals(stops.get(i).getName(), stopNames.get(i));
    }
  }

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
}
