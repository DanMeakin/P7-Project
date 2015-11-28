package org.test;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

import main.RouteTimetable;
import main.Bus;
import main.Route;
import main.Schedule;
import main.Stop;

public class RouteTimetableTest {

  private static RouteTimetable routeTimetable;
  private static int startTime;
  private static boolean isRushHour;

  private static Route mockedRoute;
  private static List<Integer> stopTiming;
  private static List<Integer> rushHourStopTiming;

  private static List<Stop> mockedStops;

  private static Schedule mockedSchedule;
  private static Date validFrom;
  private static Date validTo;

  private static Bus mockedBus;

  /**
   * Set-up before testing begins.
   *
   * A RouteTimetable aggregates a Route and exists as part of a Schedule.
   * These dependencies need to be set-up to ensure we can test the 
   * RouteTimetable funtionality.
   */
  @BeforeClass
  public static void setUpClass() {
    startTime = 10 * 60 + 30; // 10:30am
    isRushHour = false;

    mockedRoute = mock(Route.class);
    stopTiming = Arrays.asList(new Integer[] {0, 7, 15, 16, 20, 30});
    rushHourStopTiming = Arrays.asList(new Integer[] {0, 10, 19, 25, 35, 50});
    when(mockedRoute.getCumulativeNonRushHourTiming()).thenReturn(stopTiming);
    when(mockedRoute.getCumulativeRushHourTiming()).thenReturn(rushHourStopTiming);
    when(mockedRoute.getStopTiming(false, true)).thenReturn(stopTiming);
    when(mockedRoute.getStopTiming(true, true)).thenReturn(rushHourStopTiming);

    mockedStops = Arrays.asList(new Stop[] {
      mock(Stop.class), 
      mock(Stop.class), 
      mock(Stop.class), 
      mock(Stop.class), 
      mock(Stop.class)
    });
    when(mockedRoute.getStops()).thenReturn(mockedStops);

    mockedSchedule = mock(Schedule.class);
    validFrom = new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime();
    validTo = new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 31).getTime();
    when(mockedSchedule.getOperatingDay()).thenReturn(Schedule.DayOptions.WEEKDAYS);
    when(mockedSchedule.getValidFromDate()).thenReturn(validFrom);
    when(mockedSchedule.getValidToDate()).thenReturn(validTo);

    mockedBus = mock(Bus.class);
    when(mockedSchedule.getAllocatedBus(any(RouteTimetable.class))).thenReturn(mockedBus);

    routeTimetable = new RouteTimetable(mockedRoute, mockedSchedule, startTime, isRushHour);
  }

  /**
   * Test getStartTime() getter method.
   */
  @Test
  public void testGetStartTime() {
    assertEquals(routeTimetable.getStartTime(), startTime);
  }

  /**
   * Test isRushHour() getter method.
   */
  @Test
  public void testIsRushHour() {
    assertEquals(routeTimetable.isRushHour(), isRushHour);
  }

  /**
   * Test getStopTimes() method.
   *
   * Each entry in getStopTimes() should be equal to the starting time of the
   * route, plus the cumulative time for that particular stop from the start
   * of the route.
   */
  @Test
  public void testGetStopTimes() {
    for (int i = 0; i < stopTiming.size(); i++) {
      int actualStopTiming = routeTimetable.getStopTimes().get(i);
      int expectedStopTiming = stopTiming.get(i) + routeTimetable.getStartTime();
      assertEquals(actualStopTiming, expectedStopTiming);
    }
  }

  /**
   * Test getStops() method.
   *
   * A RouteTimetable must provide access to the Stops exposed by the Route.
   * This test ensures this is provided.
   */
  @Test
  public void testGetStops() {
    for (int i = 0; i < mockedStops.size(); i++) {
      Stop actualStop = routeTimetable.getStops().get(i);
      Stop expectedStop = mockedStops.get(i);
      assertEquals(actualStop, expectedStop);
    }
  }

  /**
   * Test allocatedBus() method.
   */
  @Test
  public void testAllocatedBus() {
    assertEquals(routeTimetable.getAllocatedBus(), mockedBus);
  }

}
