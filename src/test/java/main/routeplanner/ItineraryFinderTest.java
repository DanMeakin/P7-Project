package main.routeplanner;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.time.*;

import main.Schedule;
import main.Route;
import main.RouteTimetable;
import main.Bus;
import main.Stop;

/**
 * ItineraryFinderTest class contains a series of unit tests for the ItineraryFinder class.
 *
 * The ItineraryFinder class enables a user to find the best (quickest or least crowded)
 * way of transport from a to b.
 */
public class ItineraryFinderTest {

  private ItineraryFinder itineraryFinder;

  private LocalDateTime routeFindDateTime; // DateTime to use for all route finding

  private Schedule schedule;
  private Date scheduleDateFrom;
  private Date scheduleDateTo;

  private Stop startingStop;
  private Stop changeStop1;
  private Stop changeStop2;
  private Stop endingStop;
  private List<Stop> otherStops;

  // validPaths will contain the route sequences for journeys which go from
  // startingStop to endingStop
  private List<List<Route>> validPaths; 
  private List<List<RouteTimetable>> validRouteTimetables;
  private List<Route> routes;
  private List<Bus> buses;
  private List<RouteTimetable> routeTimetables;

  private List<Bus> desiredBuses;

  /**
   * Set-up the class before testing.
   *
   * This test class requires a quite elaborate set-up. To properly test the 
   * route finder, a number of different Routes, Buses and RouteTimetables
   * must be set-up which describe paths specific buses will take at specific
   * times.
   *
   * The testing must allow for a path between two Stops which may require 
   * multiple bus journeys to complete. For these purposes, we test a 3-bus
   * journey. This will require:-
   *
   *  * one or more routes containing startingStop & changeStop1;
   *  * one or more routes containing changeStop1 & changeStop2;
   *  * one or more routes containing changeStop2 & endingStop.
   *
   * We do not want too many permutations, so we will restrict the first leg 
   * to only one route, the second leg to two routes, and the third leg to one
   * route.
   *
   * We must then create Buses and RouteTimetables and associate them with a
   * Schedule.
   *
   */
  @Before
  public void setUp() {
    routeFindDateTime = LocalDateTime.of(2015, 3, 6, 10, 0); // 6th March 2015, at 10am

    scheduleDateFrom = new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime();
    scheduleDateTo = new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 31).getTime();
    schedule = mock(Schedule.class);

    startingStop = mock(Stop.class);
    changeStop1 = mock(Stop.class);
    changeStop2 = mock(Stop.class);
    endingStop = mock(Stop.class);

    otherStops = new ArrayList<>();
    // Create 50 other stops for use in Routes
    for (int i = 0; i < 50; i++) {
      otherStops.add(mock(Stop.class));
    }

    routes = new ArrayList<>();
    buses = new ArrayList<>();
    routeTimetables = new ArrayList<>();

    Route validRoute1 = mock(Route.class);
    when(validRoute1.includesStop(startingStop)).thenReturn(true);
    when(validRoute1.includesStop(changeStop1)).thenReturn(true);
    when(validRoute1.compareStops(startingStop, changeStop1)).thenReturn(-1);
    routes.add(validRoute1);

    Route validRoute2 = mock(Route.class);
    when(validRoute2.includesStop(changeStop1)).thenReturn(true);
    when(validRoute2.includesStop(changeStop2)).thenReturn(true);
    when(validRoute2.compareStops(changeStop1, changeStop2)).thenReturn(-1);
    routes.add(validRoute2);

    Route validRoute3 = mock(Route.class);
    when(validRoute3.includesStop(changeStop1)).thenReturn(true);
    when(validRoute3.includesStop(changeStop2)).thenReturn(true);
    when(validRoute3.compareStops(changeStop1, changeStop2)).thenReturn(-1);
    routes.add(validRoute3);

    Route validRoute4 = mock(Route.class);
    when(validRoute4.includesStop(changeStop2)).thenReturn(true);
    when(validRoute4.includesStop(endingStop)).thenReturn(true);
    when(validRoute4.compareStops(changeStop2, endingStop)).thenReturn(-1);
    routes.add(validRoute4);

    // Add routes containing none of the stops
    for (int i = 0; i < 20; i++) {
      Route thisRoute = mock(Route.class);
      when(thisRoute.includesStop(startingStop)).thenReturn(false);
      when(thisRoute.includesStop(changeStop1)).thenReturn(false);
      when(thisRoute.includesStop(changeStop2)).thenReturn(false);
      when(thisRoute.includesStop(endingStop)).thenReturn(false);
      routes.add(thisRoute);
    }

    // Add some routes containing stops but in the wrong order, or only
    // containing one stop
    Route thisRoute = mock(Route.class);
    when(thisRoute.includesStop(startingStop)).thenReturn(true);
    when(thisRoute.includesStop(changeStop1)).thenReturn(true);
    when(thisRoute.compareStops(startingStop, changeStop1)).thenReturn(1);
    routes.add(thisRoute);

    thisRoute = mock(Route.class);
    when(thisRoute.includesStop(changeStop1)).thenReturn(true);
    when(thisRoute.includesStop(endingStop)).thenReturn(true);
    when(thisRoute.compareStops(changeStop1, endingStop)).thenReturn(1);
    routes.add(thisRoute);

    thisRoute = mock(Route.class);
    when(thisRoute.includesStop(changeStop2)).thenReturn(true);
    routes.add(thisRoute);

    List<Route> firstValidPath = new ArrayList<>();
    firstValidPath.add(validRoute1);
    firstValidPath.add(validRoute2);
    firstValidPath.add(validRoute4);

    List<Route> secondValidPath = new ArrayList<>();
    secondValidPath.add(validRoute1);
    secondValidPath.add(validRoute3);
    secondValidPath.add(validRoute4);

    List<List<Route>> validPaths = new ArrayList<>();
    validPaths.add(firstValidPath);
    validPaths.add(secondValidPath);

    Collections.shuffle(routes);

    // Create routeTimetables for testing timings
    RouteTimetable thisRouteTimetable;

    // (0) RT for validRoute1 @ 10.05am
    thisRouteTimetable = mock(RouteTimetable.class);
    when(thisRouteTimetable.getRoute()).thenReturn(validRoute1);
    when(thisRouteTimetable.timeAtStop(startingStop)).thenReturn(10*60 + 5);
    when(thisRouteTimetable.timeAtStop(changeStop1)).thenReturn(10*60 + 15);
    routeTimetables.add(thisRouteTimetable);

    // (1) RT for validRoute1 @ 10.10am
    thisRouteTimetable = mock(RouteTimetable.class);
    when(thisRouteTimetable.getRoute()).thenReturn(validRoute1);
    when(thisRouteTimetable.timeAtStop(startingStop)).thenReturn(10*60 + 10);
    when(thisRouteTimetable.timeAtStop(changeStop1)).thenReturn(10*60 + 20);
    routeTimetables.add(thisRouteTimetable);

    // (2) RT for validRoute2 @ 10.17am
    thisRouteTimetable = mock(RouteTimetable.class);
    when(thisRouteTimetable.getRoute()).thenReturn(validRoute2);
    when(thisRouteTimetable.timeAtStop(changeStop1)).thenReturn(10*60 + 17);
    when(thisRouteTimetable.timeAtStop(changeStop2)).thenReturn(10*60 + 30);
    routeTimetables.add(thisRouteTimetable);
 
    // (3) RT for validRoute2 @ 10.32am
    thisRouteTimetable = mock(RouteTimetable.class);
    when(thisRouteTimetable.getRoute()).thenReturn(validRoute2);
    when(thisRouteTimetable.timeAtStop(changeStop1)).thenReturn(10*60 + 32);
    when(thisRouteTimetable.timeAtStop(changeStop2)).thenReturn(10*60 + 45);
    routeTimetables.add(thisRouteTimetable);

    // (4) RT for validRoute3 @ 10.25am
    thisRouteTimetable = mock(RouteTimetable.class);
    when(thisRouteTimetable.getRoute()).thenReturn(validRoute2);
    when(thisRouteTimetable.timeAtStop(changeStop1)).thenReturn(10*60 + 25);
    when(thisRouteTimetable.timeAtStop(changeStop2)).thenReturn(10*60 + 36);
    routeTimetables.add(thisRouteTimetable);

    // (5) RT for validRoute4 @ 10.32am
    thisRouteTimetable = mock(RouteTimetable.class);
    when(thisRouteTimetable.getRoute()).thenReturn(validRoute2);
    when(thisRouteTimetable.timeAtStop(changeStop1)).thenReturn(10*60 + 32);
    when(thisRouteTimetable.timeAtStop(changeStop2)).thenReturn(10*60 + 45);
    routeTimetables.add(thisRouteTimetable);

    // (6) RT for validRoute4 @ 10.42am
    thisRouteTimetable = mock(RouteTimetable.class);
    when(thisRouteTimetable.getRoute()).thenReturn(validRoute2);
    when(thisRouteTimetable.timeAtStop(changeStop1)).thenReturn(10*60 + 42);
    when(thisRouteTimetable.timeAtStop(changeStop2)).thenReturn(10*60 + 55);
    routeTimetables.add(thisRouteTimetable);

    // (7) RT for validRoute4 @ 10.52am
    thisRouteTimetable = mock(RouteTimetable.class);
    when(thisRouteTimetable.getRoute()).thenReturn(validRoute2);
    when(thisRouteTimetable.timeAtStop(changeStop1)).thenReturn(10*60 + 52);
    when(thisRouteTimetable.timeAtStop(changeStop2)).thenReturn(11*60 + 5);
    routeTimetables.add(thisRouteTimetable);

    // Create 2d array containing indiced of RTs for valid routes.
    validRouteTimetables = new ArrayList<>();
    int[][] indices = new int[][] {
      {0, 2, 5},
      {0, 4, 6},
      {0, 3, 7},
      {1, 4, 6},
      {1, 3, 7}
    };
    for (int[] is : indices) {
      List<RouteTimetable> thisRTList = new ArrayList<>();
      for (int i : is) {
        thisRTList.add(routeTimetables.get(i));
      }
      validRouteTimetables.add(thisRTList);
    }

    itineraryFinder = new ItineraryFinder(startingStop, endingStop, routeFindDateTime);
  }

  /**
   * Test the findPaths method.
   *
   * The findPaths method returns a nested list of a list of routes, each
   * representing one path between two desired stops. It should return a list 
   * of routes, sorted by the number of different routes contained, with the
   * paths containing the fewest routes coming first.
   */
  @Test
  public void testFindPaths() {
    List<List<Route>> actualList = itineraryFinder.findPaths();
    List<List<Route>> expectedList = validPaths;

    if (actualList.size() != expectedList.size()) {
      String msg = "actual paths differ from expected paths: " +
                   "actual paths are " + actualList +
                   "expected paths are " + expectedList;
      fail(msg);
    }

    for (int i = 0; i < actualList.size(); i++) {
      if (!actualList.get(i).equals(expectedList.get(i))) {
        String msg = "actual paths differ from expected paths: " +
                     "actual path is " + actualList.get(i) +
                     "expected path is " + expectedList.get(i);
        fail(msg);
      }
    }
  }

  /**
   * Test the findTimedPaths method.
   *
   * The findTimedPaths method finds buses running on the Routes determined by 
   * the findPaths method which can be taken to make the desired trip. (This 
   * method actually finds RouteTimetables, as this is what defines the timing 
   * of a particular bus at a particular stop.)
   */
  @Test
  public void testFindTimedPaths() {
    List<List<RouteTimetable>> actualList = itineraryFinder.findTimedPaths();
    List<List<RouteTimetable>> expectedList = validRouteTimetables;

    if (actualList.size() != expectedList.size()) {
      String msg = "actual bus lists differ from expected bus lists: " +
                   "actual bus lists are " + actualList +
                   "expected bus lists are " + expectedList;
      fail(msg);
    }

    for (int i = 0; i < actualList.size(); i++) {
      if (!actualList.get(i).equals(expectedList.get(i))) {
        String msg = "actual bus lists differ from expected bus lists: " +
                     "actual bus list is " + actualList.get(i) +
                     "expected bus list is " + expectedList.get(i);
        fail(msg);
      }
    }
  }
}
