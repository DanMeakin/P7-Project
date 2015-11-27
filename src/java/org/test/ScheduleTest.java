package org.test;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import main.Bus;
import main.RouteTimetable;
import main.Schedule;

public class ScheduleTest {

  private static Schedule schedule;

  private static Bus mockedBus;
  private static List<RouteTimetable> busRouteTimetables;
  private static RouteTimetable mockedRouteTimetable;

  @BeforeClass
  public static void setUpClass() {
  }

  @Before
  public void setUp() {
    mockedBus = mock(Bus.class);
    when(mockedBus.getFleetNumber()).thenReturn(143);
    when(mockedBus.equals(mockedBus)).thenReturn(true);
    mockedRouteTimetable = mock(RouteTimetable.class);
    busRouteTimetables = Arrays.asList(new RouteTimetable[] {
      mock(RouteTimetable.class),
      mock(RouteTimetable.class),
      mock(RouteTimetable.class),
      mock(RouteTimetable.class),
      mock(RouteTimetable.class),
      mock(RouteTimetable.class),
    });
    schedule = new Schedule(
        new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime(),
        new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 31).getTime(),
        Schedule.DayOptions.SATURDAY
        );
  }

  /**
   * Test addRouteTimetable method.
   *
   * The addRouteTimetable method is used to add a RouteTimetable into a
   * Schedule.
   */
  @Test
  public void testAddRouteTimetable() {
    schedule.addRouteTimetable(mockedRouteTimetable);
    assertTrue(schedule.hasRouteTimetable(mockedRouteTimetable));
  }

  /**
   * Test addRouteTimetable method.
   *
   * The addRouteTimetable method is used to add a RouteTimetable into a
   * Schedule. It can optionally take an allocated Bus for the RouteTimetable.
   * This test ensures that an optional Bus is properly assigned.
   */
  @Test
  public void testAddRouteTimetableWithBus() {
    schedule.addRouteTimetable(mockedRouteTimetable, mockedBus);
    assertTrue(schedule.hasRouteTimetable(mockedRouteTimetable));
    assertTrue(schedule.hasBus(mockedBus));
  }

  /**
   * Test the getAllocatedBus method.
   *
   * The getAllocatedBus method takes a RouteTimetable object and returns the
   * Bus allocated to operate that RouteTimetable.
   */
  @Test
  public void testGetAllocatedBus() {
    // Add RouteTimetable with associated Bus first
    schedule.addRouteTimetable(mockedRouteTimetable, mockedBus);
    assertEquals(schedule.getAllocatedBus(mockedRouteTimetable), mockedBus);
  }

  /**
   * Test the getAllocatedRouteTimetables method.
   *
   * The allocatedRouteTimetables method takes a Bus object and returns all
   * RouteTimetables allocated to that Bus within the Schedule.
   */
  @Test 
  public void testGetAllocatedRouteTimetables() {
    // Add RouteTimetables with associated Bus first
    for (RouteTimetable rt : busRouteTimetables) {
      schedule.addRouteTimetable(rt, mockedBus);
    }
    List<RouteTimetable> actualRouteTimetables = schedule.getAllocatedRouteTimetables(mockedBus);
    for (int i = 0; i < actualRouteTimetables.size(); i++) {
      assertEquals(actualRouteTimetables.get(i), busRouteTimetables.get(i));
    }
  }

  /**
   * Test scheduledDates method.
   *
   * This method assumes that the Schedule for this RouteTimetable is for 
   * Saturdays only.
   */
  @Test
  public void testScheduledDates() {
    List<Date> actualDates = schedule.scheduledDates();
    if (actualDates.size() == 0) {
      fail("scheduled dates must not be empty");
    }
    for (Date d : actualDates) {
      Calendar c = Calendar.getInstance();
      c.setTime(d);
      int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
      if (dayOfWeek != 7) { // Expect each date to be a Saturday
        fail("scheduled dates contains incorrect date: " + d);
      }
    }
  }
}
