package main;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

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

  private Date routeFindDate; // Date to use for all route finding

  private Schedule mockedSchedule;
  private Date scheduleDateFrom;
  private Date scheduleDateTo;

  private List<Route> routesIncludingStartingStop;
  private List<Route> routesIncludingEndingStop;
  private Route mockedDesiredRoute; // The correct route for the test journey
  private List<Bus> mockedBuses;
  private List<Stop> mockedStops;

  private Stop mockedStartingStop;
  private Stop mockedEndingStop;

  @Before
  public void setUp() {
    routeFindDate = new GregorianCalendar(2015, GregorianCalendar.MARCH, 6).getTime(); // Weekday

    scheduleDateFrom = new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime();
    scheduleDateTo = new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 31).getTime();
    mockedSchedule = new Schedule(scheduleDateFrom, scheduleDateTo, Schedule.DayOptions.WEEKDAYS);
    when(mockedSchedule.findRoutesIncludingStop(mockedStartingStop)).thenReturn(routesIncludingStartingStop);
    when(mockedSchedule.findRoutesIncludingStop(mockedEndingStop)).thenReturn(routesIncludingEndingStop);


  }

}
