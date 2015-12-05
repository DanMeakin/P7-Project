package main;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.time.*;

import main.RouteFinder;
import main.Schedule;
import main.Route;
import main.Bus;
import main.Stop;

/**
 * RouteFinderTest class contains a series of unit tests for the BusRouteFinder class.
 *
 * The BusRouteFinder class enables a user to find the best (quickest or least crowded)
 * way of transport from a to b.
 */
public class RouteFinderTest {

  private RouteFinder routeFinder;

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

    routeFinder = new RouteFinder(startingStop, endingStop, routeFindDateTime);

  }

  /**
   * Test the findPaths method.
   *
   * The routesBetween method returns a nested list of a list of routes, each
   * representing one path between two desired stops. It should return a list 
   * of routes, sorted by the number of different routes contained, with the
   * paths containing the fewest routes coming first.
   */
  @Test
  public void testFindPaths() {
    List<List<Route>> actualList = routeFinder.findPaths();
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
}
