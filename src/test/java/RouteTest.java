package org.test;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Arrays;

import main.Route;
import main.Stop;

public class RouteTest {

  private static Route route;
  private static String routeNumber;
  private static String routeDescription;
  private static Stop routeStart;
  private static Stop routeEnd;

  private static Route routeWithStops;
  private static String routeWithStopsNumber;
  private static String routeWithStopsDescription;
  private static Stop routeWithStopsStart;
  private static Stop routeWithStopsEnd;

  private static Stop stop1;
  private static Stop stop2;
  private static Stop stop3;

  /**
   * Set-up before testing begins.
   *
   * This creates a series of stops, numbers and descriptions which are used
   * to create route objects, and new stops in route objects. This ensures
   * consistency between tests using the same fixtures.
   */
  @BeforeClass
  public static void setUpClass() {
    // Create stops to be used in fixtures
    routeStart = mock(Stop.class);
    routeEnd = mock(Stop.class);

    routeWithStopsStart = mock(Stop.class);
    routeWithStopsEnd = mock(Stop.class);

    stop1 = mock(Stop.class);
    stop2 = mock(Stop.class);
    stop3 = mock(Stop.class);

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
  public void testGetNumber() {
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
  public void testGetDescription() {
    assertEquals(route.getDescription(), routeDescription);
    assertEquals(routeWithStops.getDescription(), routeWithStopsDescription);
  }

  /**
   * Test has stops.
   *
   * This method ensures that a list of stops can be obtained from a route.
   */
  @Test
  public void testGetStops() {
    List<Stop> actualStops = route.getStops();
    List<Stop> expectedStops = Arrays.asList(routeStart, routeEnd);
    assertEquals(actualStops.size(), expectedStops.size()); 
    for (int i = 0; i < actualStops.size(); i++) {
      assertEquals(actualStops.get(i), expectedStops.get(i));
    }

    actualStops = routeWithStops.getStops();
    expectedStops = Arrays.asList(routeWithStopsStart, stop1, stop2, stop3, routeWithStopsEnd);
    for (int i = 0; i < actualStops.size(); i++) {
      assertEquals(actualStops.get(i), expectedStops.get(i));
    }
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
    List<Stop> actualStops = route.getStops();
    List<Stop> expectedStops = Arrays.asList(routeStart, routeEnd, stop3);
    assertEquals(actualStops.size(), expectedStops.size());
    for (int i = 0; i < actualStops.size(); i++) {
      assertEquals(actualStops.get(i), expectedStops.get(i));
    }
  }

  /**
   * Test getRushHourTiming method.
   *
   * This test ensures that a Route object provides details of the timing of
   * stops to a requesting object. The getRushHourTiming method obtains the bus
   * stop timings during rush hour periods. 
   *
   * This method is one of four similar which tests acquiring timings for buses.
   */
  @Test
  public void testGetRushHourTiming() {
    List<Integer> actualTiming = routeWithStops.getRushHourTiming();
    List<Integer> expectedTiming = Arrays.asList(new Integer[]{0, 4, 6, 13, 6});
    for (int i = 0; i < actualTiming.size(); i++) {
      assertEquals(actualTiming.get(i), expectedTiming.get(i));
    }
  }

  /**
   * Test getNonRushHourTiming method.
   *
   * This test ensures that a Route object provides details of the timing of
   * stops to a requesting object. The getNonRushHourTiming method obtains the 
   * bus stop timings outside of rush hour periods. 
   *
   * This method is one of four similar which tests acquiring timings for buses.
   */
  @Test
  public void testGetNonRushHourTiming() {
    List<Integer> actualTiming = routeWithStops.getNonRushHourTiming();
    List<Integer> expectedTiming = Arrays.asList(new Integer[]{0, 3, 5, 11, 4});
    for (int i = 0; i < actualTiming.size(); i++) {
      assertEquals(actualTiming.get(i), expectedTiming.get(i));
    }
  }

  /**
   * Test getCumulativeRushHourTiming method.
   *
   * This test ensures that a Route object provides details of the timing of
   * stops to a requesting object. The getCumulativeRushHourTiming method 
   * obtains the cumulative bus stop timings during rush hour periods.
   *
   * This method is one of four similar which tests acquiring timings for buses.
   */
  @Test
  public void testGetCumulativeRushHourTiming() {
    List<Integer> actualTiming = routeWithStops.getCumulativeRushHourTiming();
    List<Integer> expectedTiming = Arrays.asList(new Integer[]{0, 4, 10, 23, 29});
    for (int i = 0; i < actualTiming.size(); i++) {
      assertEquals(actualTiming.get(i), expectedTiming.get(i));
    }
  }

  /**
   * Test getCumulativeNonRushHourTiming method.
   *
   * This test ensures that a Route object provides details of the timing of
   * stops to a requesting object. The getCumulativeRushHourTiming method 
   * obtains the cumulative bus stop timings during rush hour periods.
   *
   * This method is one of four similar which tests acquiring timings for buses.
   */
  @Test
  public void testGetCumulativeNonRushHourTiming() {
    List<Integer> actualTiming = routeWithStops.getCumulativeNonRushHourTiming();
    List<Integer> expectedTiming = Arrays.asList(new Integer[]{0, 3, 8, 19, 23});
    for (int i = 0; i < actualTiming.size(); i++) {
      assertEquals(actualTiming.get(i), expectedTiming.get(i));
    }
  }

  /**
   * Test includesStop method.
   *
   * This test ensures that a Route object provides a way of identifying
   * whether the Route includes a particular Stop.
   */
  @Test
  public void testIncludesStop() {
    Stop[] routeStops = new Stop[] {routeStart, routeEnd};
    Stop[] routeWithStopsStops = new Stop[] {routeWithStopsStart, stop1, stop2, stop3, routeWithStopsEnd};
    for (Stop thisStop : routeStops) {
      assertTrue(route.includesStop(thisStop));
      assertFalse(routeWithStops.includesStop(thisStop));
    }
    for (Stop thisStop : routeWithStopsStops) {
      assertTrue(routeWithStops.includesStop(thisStop));
      assertFalse(route.includesStop(thisStop));
    }
  }
}
