package org.test;

import org.junit.*;
import java.util.HashMap;

import main.Route;
import main.Stop;

public class RouteTest {

  Route route;
  String routeNumber;
  String routeDescription;
  Stop routeStart;
  Stop routeEnd;

  Route routeWithStops;
  String routeWithStopsNumber;
  String routeWithStopsDescription;
  Stop routeWithStopsStart;
  Stop routeWithStopsEnd;

  Stop stop1;
  Stop stop2;
  Stop stop3;

  /**
   * Set-up before testing begins.
   *
   * This creates a series of stops, numbers and descriptions which are used
   * to create route objects, and new stops in route objects. This ensures
   * consistency between tests using the same fixtures.
   */
  @BeforeClass
  public void setUpClass() {
    // Create stops to be used in fixtures
    routeStart = new Stop(123, "City Centre", 0, 0);
    routeEnd = new Stop(990, "University", 100, 100);

    routeWithStopsStart = new Stop(1, "Klarup", 11, 12);
    routeWithStopsEnd = new Stop(124, "Bus Terminal", 1, 0);

    stop1 = new Stop(555, "Klarup 2", 10, 10);
    stop2 = new Stop(701, "Second Stop", 5, 6);
    stop3 = new Stop(59, "Jyllandsgade", 0, 1);

    routeNumber = "12";
    routeDescription = "City Centre - University";

    routeWithStopsNumber = "2A";
    routeWithStopsDescription = "Klarup - Bus Terminal";
  }

  /** 
   * Set-up before each test.
   *
   * This creates two route instances: route and routeWithStops. The former is
   * a simple route with only a start and end point. The latter has a start
   * point, end point, and three stops in between with associated timings.
   */
  @Before
  public void setUp() {
    route = new Route(routeNumber, routeDescription, routeStart);
    route.addStop(routeEnd, 10, 13);

    routeWithStops = new Route(routeWithStopsNumber, routeWithStopsDescription, routeWithStopsStart);
    routeWithStops.addStop(stop1, 3, 4);
    routeWithStops.addStop(stop2, 5, 6);
    routeWithStops.addStop(stop3, 11, 13);
    routeWithStops.addStop(routeWithStopsEnd, 4, 6);
  }

  /**
   * Test has number.
   *
   * This method tests that a route's number is accessible through a Route
   * object.
   */
  @Test
  public void testHasNumber() {
    assertEquals(route.getNumber(), routeNumber);
    assertEquals(routeWithStops.getNumber(), routeWithStopsNumber);
  }

  /**
   * Test has description.
   *
   * This method tests that a route's description is accessible through a Route
   * object.
   */
  @Test
  public void testHasDescription() {
    assertEquals(route.getDescription(), routeDescription);
    assertEquals(routeWithStops.getDescription(), routeWithStopsDescription);
  }

  /**
   * Test has stops.
   *
   * This method ensures that a list of stops can be obtained from a route.
   */
  @Test
  public void testHasStops() {
    assertArrayEquals(route.getStops(), new Stop[]{
      routeStart, 
      routeEnd
    });
    assertArrayEquals(routeWithStops.getStops(), new Stop[]{
      routeWithStopsStart,
      stop1, 
      stop2, 
      stop3, 
      routeWithStopsEnd
    });
  }

  /** 
   * Test add stop.
   *
   * This test ensures that additional stops can be added to a route and that
   * the additional stops are properly associated with the route.
   */
  @Test
  public void testAddStop() {
    route.addStop(stop3, 5, 7);
    assertArrayEquals(route.getStops(), new Stop[]{
      routeStart,
      routeEnd,
      stop3
    });
  }

  /**
   * Test view stop times.
   *
   * This method tests whether the viewStopTimes method correctly returns the
   * stops and associated time offsets from the start of a journey on a route.
   */
  @Test
  public void testViewStopTimes() {
    HashMap<Stop, Integer> stopTimes = routeWithStops.viewStopTimes();
    HashMap<Stop, Integer> expectedStopTimes = new HashMap<Stop, Integer>();
    expectedStopTimes.put(routeWithStopsStart, 0);
    expectedStopTimes.put(stop1, 3);
    expectedStopTimes.put(stop2, 8);
    expectedStopTimes.put(stop3, 19);
    expectedStopTimes.put(routeWithStopsEnd, 23);
    for (Stop stop : stopTimes.keySet()) {
      assertEquals(stopTimes.get(stop), expectedStopTimes.get(stop));      
    }
  }

  /** 
   * Test view rush hour stop times.
   *
   * This method tests whether the viewRushHourStopTimes method correctly
   * returns the stops and associated time offsets from the start of the
   * journey on a route.
   *
   */
  @Test 
  public void testViewRushHourStopTimes() {
    HashMap<Stop, Integer> stopTimes = routeWithStops.viewRushHourStopTimes();
    HashMap<Stop, Integer> expectedStopTimes = new HashMap<Stop, Integer>();
    expectedStopTimes.put(routeWithStopsStart, 0);
    expectedStopTimes.put(stop1, 4);
    expectedStopTimes.put(stop2, 10);
    expectedStopTimes.put(stop3, 23);
    expectedStopTimes.put(routeWithStopsEnd, 29);
    for (Stop stop : stopTimes.keySet()) {
      assertEquals(stopTimes.get(stop), expectedStopTimes.get(stop));      
    }
  }
}
