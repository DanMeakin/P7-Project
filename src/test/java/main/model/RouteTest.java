package main.model;

import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Arrays;

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

  private static Route invertedRoute;
  private static String invertedRouteDescription;

  private static Stop stop1;
  private static Stop stop2;
  private static Stop stop3;

  /**
   * Set-up before testing begins.
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
    invertedRouteDescription = "University - City Centre";

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

    invertedRoute = new Route(routeNumber, invertedRouteDescription, routeEnd);
    invertedRoute.addStop(routeStart, 9, 11);
  }

  @After
  public void tearDown() {
    for (Route r : Route.getAllRoutes()) {
      r.remove();
    }
  }

  /**
   * Test create duplicate Route.
   *
   * The Route class should prevent duplicates from being created. This test
   * ensures this behaviour occurs.
   */
  @Test
  public void testCreateDuplicateRoute() {
    thrown.expect(IllegalArgumentException.class);
    String msg = "Path " + route.getDescription() + " already exists";
    thrown.expectMessage(msg);
    new Route(routeNumber, routeDescription, routeStart);
  }
  /**
   * Test getAllRoutes method.
   */
  @Test
  public void testGetAllRoutes() {
    // Create mocked non-Route path and add to allPaths
    Path p = mock(Path.class);
    Path.addPath(p);
    assertEquals(Route.getAllRoutes(), Arrays.asList(route, routeWithStops, invertedRoute));
  }

  /**
   * Test findRouteByNumber method.
   */
  @Test
  public void testFindRouteByNumber() {
    assertEquals(Arrays.asList(route, invertedRoute), Route.findRouteByNumber(routeNumber));
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
   * Test getOrigin
   */
  @Test
  public void testGetOrigin() {
    assertEquals(route.getOrigin(), routeStart);
    assertEquals(routeWithStops.getOrigin(), routeWithStopsStart);
  }

  /**
   * Test getDestination
   */
  @Test
  public void testGetDestination() {
    assertEquals(route.getDestination(), routeEnd);
    assertEquals(routeWithStops.getDestination(), routeWithStopsEnd);
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

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * Test compareStops method.
   *
   * This test ensures that two Stop objects within a Route are compared
   * properly. The compareStop method should return -1 if the first stop is
   * before the second; 1 if the second stop is before the first; 0 if both
   * stops are the same.
   *
   * The method throws an IllegalArgumentException unless both stops are within
   * the route.
   */
  @Test
  public void testCompareStops() {
    assertEquals(routeWithStops.compareStops(stop1, stop2), -1);
    assertEquals(routeWithStops.compareStops(stop3, stop2), 1);
    assertEquals(routeWithStops.compareStops(stop1, stop1), 0);
  }

  /**
   * Test compareStops method with illegal first stop.
   *
   * This test ensures that two Stop objects within a Route are compared
   * properly. The compareStop method should return -1 if the first stop is
   * before the second; 1 if the second stop is before the first; 0 if both
   * stops are the same.
   *
   * The method throws an IllegalArgumentException unless both stops are within
   * the route.
   */
  @Test
  public void testCompareStopsWithIllegalFirstStop() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("stop " + routeEnd + " is not on path");
    routeWithStops.compareStops(routeEnd, stop2);
  }

  /**
   * Test compareStops method with illegal second stop.
   *
   * This test ensures that two Stop objects within a Route are compared
   * properly. The compareStop method should return -1 if the first stop is
   * before the second; 1 if the second stop is before the first; 0 if both
   * stops are the same.
   *
   * The method throws an IllegalArgumentException unless both stops are within
   * the route.
   */
  @Test
  public void testCompareStopsWithIllegalSecondStop() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("stop " + routeStart + " is not on path");
    routeWithStops.compareStops(stop1, routeStart);
  }

  /**
   * Test journeyTimeBetweenStops method.
   */
  @Test
  public void testTimeBetweenStops() {
    assertEquals(routeWithStops.journeyTimeBetweenStops(routeWithStopsStart, stop2, false), 8);
    assertEquals(routeWithStops.journeyTimeBetweenStops(stop2, routeWithStopsEnd, true), 19);
  }

  /**
   * Test journeyTimeBetweenStops method with invalid stop.
   *
   * The journeyTimeBetweenStops method should thrown an IllegalArgumentException
   * if passed a Stop not on route.
   */
  @Test
  public void testTimeBetweenStopsWithInvalidStop() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("stop " + routeEnd + " is not on path");
    routeWithStops.journeyTimeBetweenStops(stop2, routeEnd, true);
  }

  /**
   * Test journeyTimeBetweenStops method with invalid ordering of stops.
   *
   * The journeyTimeBetweenStops method should thrown an IllegalArgumentException
   * if passed two Stops which are present in Route but in the wrong order, i.e.
   * the Route travels from A -> B, but the method is called asking for travel
   * time from B -> A.
   */
  @Test
  public void testTimeBetweenStopsWithInvalidStopOrdering() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("this path does not travel from " + stop2 + " to " + routeWithStopsStart);
    routeWithStops.journeyTimeBetweenStops(stop2, routeWithStopsStart, true);
  }

  /**
   * Test findInverted method.
   *
   * The findInverted method searches for a Route which is the same as the 
   * receiver of the call, but with an inverted direction.
   */
  @Test
  public void testFindInverted() {
    assertEquals(route.findInverted(), invertedRoute);
  }

  /**
   * Test findInverted method where no inverted route exists.
   *
   * The findInverted method should return null where no inverse exists.
   */
  @Test
  public void testFindInvertedWithNoInvertedRoute() {
    assertNull(routeWithStops.findInverted());
  }

  /**
   * Test findRoutesIncludingStop method.
   */
  @Test
  public void testFindRoutesIncludingStop() {
    assertEquals(
        Route.findRoutesIncludingStop(routeStart), 
        Arrays.asList(new Route[] {route, invertedRoute})
        );
  }

  /**
   * Test equals method.
   */
  @Test
  public void testEquals() {
    // Create non-Route path instance
    Path p = mock(Path.class);
    assertTrue(route.equals(route));
    assertFalse(route.equals(invertedRoute));
    assertFalse(routeWithStops.equals(route));
    assertFalse(route.equals(p));
  }
}


