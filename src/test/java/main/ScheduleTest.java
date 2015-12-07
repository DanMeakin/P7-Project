package main;

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
import java.util.function.Predicate;

import main.Bus;
import main.RouteTimetable;
import main.Schedule;
import main.Schedule.DayOptions;

public class ScheduleTest {

  private static Schedule schedule;
  private static Date scheduleStart;
  private static Date scheduleEnd;

  private static Schedule weekdaySchedule;
  private static Schedule saturdaySchedule;
  private static Schedule sundaySchedule;

  private static Bus mockedBus;
  private static List<RouteTimetable> busRouteTimetables;
  private static RouteTimetable mockedRouteTimetable;

  private static Bus anotherMockedBus;
  private static RouteTimetable anotherMockedRouteTimetable;

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

    anotherMockedRouteTimetable = mock(RouteTimetable.class);
    anotherMockedBus = mock(Bus.class);
    when(anotherMockedBus.equals(anotherMockedBus)).thenReturn(true);
    
    scheduleStart = new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime();
    scheduleEnd = new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 31).getTime();
    schedule = new Schedule(
        scheduleStart,
        scheduleEnd,
        DayOptions.SATURDAY
        );

    weekdaySchedule = new Schedule(
        scheduleStart,
        scheduleEnd,
        DayOptions.WEEKDAYS
        );
    saturdaySchedule = schedule;
    sundaySchedule = new Schedule(
        scheduleStart,
        scheduleEnd,
        DayOptions.SUNDAY
        );
  }

  @After
  public void tearDown() { 
    // This must be done to allow schedule to be recreated in next tests
    Schedule.removeSchedule(schedule);
    Schedule.removeSchedule(weekdaySchedule);
    Schedule.removeSchedule(saturdaySchedule);
    Schedule.removeSchedule(sundaySchedule);
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * Test the creation of an invalid Schedule.
   *
   * The class should reject an attempt to create a Schedule for a period and
   * operating day for which a Schedule has already been created.
   *
   */
  @Test
  public void testCreateInvalidSchedule() {
    thrown.expect(IllegalArgumentException.class);
    String msg = "WEEKDAYS Schedule for period is already defined";
    thrown.expectMessage(msg);
    new Schedule(
        new GregorianCalendar(2015, GregorianCalendar.JANUARY, 20).getTime(),
        new GregorianCalendar(2016, GregorianCalendar.JANUARY, 19).getTime(),
        DayOptions.WEEKDAYS
        );
  }

  /**
   * Test the findSchedule static method on Schedule.
   *
   * The findSchedule method should find the schedule applicable on a given
   * date. This test ensures that weekday, Saturday and Sunday calendars are
   * returned when dates are passed for within the relevant Schedule period.
   */
  @Test
  public void testFindSchedule() {
    Date sunday = new GregorianCalendar(2015, GregorianCalendar.MARCH, 1).getTime();
    Date weekday = new GregorianCalendar(2015, GregorianCalendar.JUNE, 10).getTime();
    Date saturday = new GregorianCalendar(2015, GregorianCalendar.NOVEMBER, 28).getTime();

    Schedule pastSchedule = new Schedule(
        new GregorianCalendar(2014, GregorianCalendar.JANUARY, 1).getTime(),
        new GregorianCalendar(2014, GregorianCalendar.DECEMBER, 31).getTime(), 
        DayOptions.WEEKDAYS);
    Schedule futureSchedule = new Schedule(
        new GregorianCalendar(2016, GregorianCalendar.JANUARY, 1).getTime(),
        new GregorianCalendar(2016, GregorianCalendar.DECEMBER, 31).getTime(), 
        DayOptions.SATURDAY);

    assertEquals(Schedule.findSchedule(weekday), weekdaySchedule);
    assertEquals(Schedule.findSchedule(saturday), saturdaySchedule);
    assertEquals(Schedule.findSchedule(sunday), sundaySchedule);
  }

  /**
   * Test values of DayOptions enum.
   *
   * The DayOptions enum should contain three fixed values: WEEKDAYS, 
   * SATURDAY & SUNDAY. This test ensures these are the only values.
   */
  @Test
  public void testDayOptionsValues() {
    DayOptions[] actualOptions = DayOptions.values();
    DayOptions[] expectedOptions = new DayOptions[] {DayOptions.WEEKDAYS, DayOptions.SATURDAY, DayOptions.SUNDAY};
    for (int i = 0; i < actualOptions.length; i++) {
      if (expectedOptions.length < i) {
        fail("there are more DayOptions than expected: " + actualOptions);
      }
      assertEquals(actualOptions[i], expectedOptions[i]);
    }
  }
  
  /**
   * Test valueOf() DayOptions enum.
   *
   * The valueOf() method should return appropriate identifiers in DayOptions.
   * This test ensures this is the case.
   */
  @Test
  public void testDayOptionsValueOf() {
    String[] dayOptionsStrings = new String[] {"WEEKDAYS", "SATURDAY", "SUNDAY"};
    DayOptions[] dayOptionsConsts = new DayOptions[] {DayOptions.WEEKDAYS, DayOptions.SATURDAY, DayOptions.SUNDAY};
    for (int i = 0; i < dayOptionsStrings.length; i++) {
      assertEquals(DayOptions.valueOf(dayOptionsStrings[i]), dayOptionsConsts[i]);
    }
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
   * Test addRouteTimetable method with invalid RouteTimetable.
   *
   * The addRouteTimetable method should not accept a null RouteTimetable 
   * value. This method tests this behaviour.
   */
  @Test
  public void testAddRouteTimetableWithNull() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("cannot add a null RouteTimetable");
    schedule.addRouteTimetable(null);
  }

  /**
   * Test hasBus method.
   *
   * The hasBus method tests whether a bus is allocated within a Schedule.
   * It should return true if it is allocated, or false if not. Additionally,
   * it should return false if a null bus is passed.
   */
  @Test
  public void testHasBus() {
    // Try with empty schedule
    assertFalse(schedule.hasBus(mockedBus));
    // Add buses to schedule and try again
    schedule.addRouteTimetable(anotherMockedRouteTimetable, anotherMockedBus);
    schedule.addRouteTimetable(anotherMockedRouteTimetable);
    schedule.addRouteTimetable(mockedRouteTimetable, mockedBus);
    assertTrue(schedule.hasBus(mockedBus));
    assertTrue(schedule.hasBus(anotherMockedBus));
    // Try with null
    assertFalse(schedule.hasBus(null));
  }

  /**
   * Test hasRouteTimetable method.
   *
   * The hasRouteTimetable method tests whether a RouteTimetable is allocated
   * within a Schedule. It should return true if allocated, else false. Also,
   * it should return false if a null reference is passed.
   */
  @Test
  public void testHasRouteTimetable() {
    // Try with empty schedule
    assertFalse(schedule.hasRouteTimetable(mockedRouteTimetable));
    // Add routetimetables and try again
    schedule.addRouteTimetable(anotherMockedRouteTimetable);
    schedule.addRouteTimetable(mockedRouteTimetable);
    assertTrue(schedule.hasRouteTimetable(mockedRouteTimetable));
    // Try with null
    assertFalse(schedule.hasRouteTimetable(null));
  }

  /**
   * Test the getAllocatedBus method.
   *
   * The getAllocatedBus method takes a RouteTimetable object and returns the
   * Bus allocated to operate that RouteTimetable.
   */
  @Test
  public void testGetAllocatedBus() {
    // Add other mocked RouteTimetable and Bus first
    schedule.addRouteTimetable(anotherMockedRouteTimetable, anotherMockedBus);

    schedule.addRouteTimetable(mockedRouteTimetable, mockedBus);
    assertEquals(schedule.getAllocatedBus(mockedRouteTimetable), mockedBus);
  }

  /**
   * Test the getAllocatedBus method with invalid RouteTimetable.
   *
   * If a RouteTimetable is passed which is not allocated in a schedule, the
   * method should throw a specific error.
   *
   */
  @Test
  public void testGetAllocatedBusWithInvalidRouteTimetable() {
    String msg = "RouteTimetable \"" + mockedRouteTimetable + 
      "\" is not found within Schedule";
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage(msg);
    schedule.getAllocatedBus(mockedRouteTimetable);
  }

  /**
   * Test the getAllocatedRouteTimetables method.
   *
   * The allocatedRouteTimetables method takes a Bus object and returns all
   * RouteTimetables allocated to that Bus within the Schedule.
   */
  @Test 
  public void testGetAllocatedRouteTimetables() {
    // Add other mocked RouteTimetable and Bus first
    schedule.addRouteTimetable(anotherMockedRouteTimetable, anotherMockedBus);

    // Add RouteTimetables with associated Bus
    for (RouteTimetable rt : busRouteTimetables) {
      schedule.addRouteTimetable(rt, mockedBus);
    }
    List<RouteTimetable> actualRouteTimetables = schedule.getAllocatedRouteTimetables(mockedBus);
    for (int i = 0; i < actualRouteTimetables.size(); i++) {
      assertEquals(actualRouteTimetables.get(i), busRouteTimetables.get(i));
    }
  }

  /**
   * Test the getAllocatedRouteTimetables method with invalid Bus.
   *
   * If a Bus is passed which is not allocated in a schedule, the
   * method should throw a specific error.
   *
   */
  @Test
  public void testGetAllocatedRouteTimetablesWithInvalidBus() {
    String msg = "Bus \"" + mockedBus + 
      "\" is not found within Schedule";
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage(msg);
    schedule.getAllocatedRouteTimetables(mockedBus);
  }

  /**
   * Test the getOperatingDay getter method.
   */
  @Test
  public void testGetOperatingDay() {
    assertEquals(weekdaySchedule.getOperatingDay(), DayOptions.WEEKDAYS);
    assertEquals(saturdaySchedule.getOperatingDay(), DayOptions.SATURDAY);
    assertEquals(sundaySchedule.getOperatingDay(), DayOptions.SUNDAY);
  }

  /**
   * Test scheduledDates method.
   *
   * This test ensures that each possible option for the days on which a
   * schedule operates - weekday, Saturday and Sundays - are tests, and that
   * all dates returned by the scheduledDates method fall on the correct days.
   *
   */
  @Test
  public void testScheduledDates() {
    // Create all required schedules
    List<Schedule> schedules = Arrays.asList(new Schedule[] {
      weekdaySchedule, 
      saturdaySchedule, 
      sundaySchedule
    });

    // Create all required predicates to test for the correct day of week
    List<Predicate<Date>> ps = new ArrayList<>();
    ps.add((Date d) -> dayOfWeek(d) >= 2 && dayOfWeek(d) <= 6);
    ps.add((Date d) -> dayOfWeek(d) == 7);
    ps.add((Date d) -> dayOfWeek(d) == 1);

    // Undertake testing on each schedule
    for (int i = 0; i < schedules.size(); i++ ) {
      Schedule s = schedules.get(i);
      Predicate<Date> p = ps.get(i);
      List<Date> actualDates = s.scheduledDates();
      if (actualDates.size() == 0) {
        fail("scheduled dates must not be empty");
      }
      for (Date d : actualDates) {
        if (d.before(scheduleStart) || d.after(scheduleEnd)) {
          String msg = "schedule dates contains dates outside of the " + 
            "specified date range for the schedule: " + d;
          fail(msg);
        }
        if (!p.test(d)) { // Expect each date to be a Saturday
          fail("scheduled dates contains incorrect date: " + d);
        }
      }
    }
  } 

  /**
   * Helper method to determine numerical day of week from a date.
   *
   * This method will return the day of week for a given date. The numbers
   * begin at 1, with Sunday = 1, Monday = 2, ... Saturday = 7.
   *
   * @param d date for which to obtain day of week
   * @return numerical day of week, with Sunday = 1, Monday = 2, ... , 
   *  Saturday = 7
   */
  private static int dayOfWeek(Date d) {
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    return dayOfWeek;
  }
}
