package org.test;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import main.RouteTimetable;
import main.Bus;
import main.Route;
import main.Schedule;

public class RouteTimetableTest {

  private static RouteTimetable routeTimetable;
  private static int startTime;
  private static boolean isRushHour;

  private static Route mockedRoute;
  private static List<Integer> stopTiming;
  private static List<Integer> rushHourStopTiming;

  private enum ScheduleDays { WEEKDAYS, SATURDAYS, SUNDAYS };

  private static Schedule mockedSchedule;
  private static ScheduleDays scheduleDay;
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
  public void setUpClass() {
    startTime = 10 * 60 + 30; // 10:30am
    isRushHour = false;

    mockedRoute = mock(Route.class);
    scheduleDay = ScheduleDays.SATURDAYS;
    stopTiming = Arrays.asList(new Integer[] {0, 7, 15, 16, 20, 30});
    rushHourStopTiming = Arrays.asList(new Integer[] {0, 10, 19, 25, 35, 50});
    when(mockedRoute.getCumulativeNonRushHourTiming()).thenReturn(stopTiming);
    when(mockedRoute.getCumulativeRushHourTiming()).thenReturn(rushHourStopTiming);

    mockedSchedule = mock(Schedule.class);
    validFrom = new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime();
    validTo = new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 31).getTime();
    when(mockedSchedule.getScheduleDays()).thenReturn(scheduleDays.WEEKDAYS);
    when(mockedSchedule.getValidFromDate()).thenReturn(validFrom);
    when(mockedSchedule.getValidToDate()).thenReturn(validTo);

    mockedBus = mock(Bus.class);
    when(mockedSchedule.allocatedBus(any(RouteTimetable.class))).thenReturn(mockedBus);

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
      int wrongStopTiming = rushHourStopTiming.get(i) + routeTimetable.getStartTime();
      assertEquals(actualStopTiming, expectedStopTiming);
      assertNotEquals(actualStopTiming, wrongStopTiming);
    }
  }

  /**
   * Test allocatedBus() method.
   */
  @Test
  public void testAllocatedBus() {
    assertEquals(routeTimetable.allocatedBus(), mockedBus);
  }

  /**
   * Test getScheduleDays() method.
   */
  @Test
  public void testGetScheduleDays() {
    assertEquals(routeTimetable.getScheduleDays(), scheduleDay);
  }

  /**
   * Test getValidFromDate() method.
   */
  @Test 
  public void testGetValidFromDate() {
    assertEquals(routeTimetable.getValidFromDate(), validFrom);
  }

  /**
   * Test getValidToDate() method.
   */
  @Test
  public void testGetValidToDate() {
    assertEquals(routeTimetable.getValidToDate(), validTo);
  }

  /**
   * Test scheduledDates() method.
   *
   * This method assumes that the Schedule for this RouteTimetable is for 
   * Saturdays only.
   */
  @Test
  public void testScheduledDates() {
    List<Date> actualDates = routeTimetable.scheduledDates();
    for (Date d : actualDates) {
      Calendar c = Calendar.getInstance();
      c.setTime(d);
      int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
      if (dayOfWeek != 6) { // Expect each date to be a Saturday
        fail("scheduled dates contains incorrect date: " + d);
      }
    }
  }
}
