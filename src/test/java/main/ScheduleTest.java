package main;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Predicate;
import java.time.Month;

import main.Bus;
import main.Route;
import main.RouteTimetable;
import main.Schedule;
import main.Schedule.DayOption;

public class ScheduleTest {

  private static LocalDate weekdayScheduleStart;
  private static LocalDate weekdayScheduleEnd;
  private static LocalDate saturdayScheduleStart;
  private static LocalDate saturdayScheduleEnd;
  private static LocalDate sundayScheduleStart;
  private static LocalDate sundayScheduleEnd;

  private static Schedule weekdaySchedule;
  private static Schedule saturdaySchedule;
  private static Schedule sundaySchedule;

  private static Route mockedRoute1;
  private static Route mockedRoute2;

  private static Bus mockedBus;
  private static List<RouteTimetable> busRouteTimetables;
  private static RouteTimetable mockedRouteTimetable;

  private static Bus anotherMockedBus;
  private static RouteTimetable anotherMockedRouteTimetable;

  @BeforeClass
  public static void setUpClass() {
    for (Schedule s : new ArrayList<>(Schedule.getAllSchedules())) {
      Schedule.removeSchedule(s);
    }
  }

  @Before
  public void setUp() {
    mockedBus = mock(Bus.class);
    when(mockedBus.getFleetNumber()).thenReturn(143);
    when(mockedBus.equals(mockedBus)).thenReturn(true);

    mockedRoute1 = mock(Route.class);
    when(mockedRoute1.equals(mockedRoute1)).thenReturn(true);
    mockedRoute2 = mock(Route.class);
    when(mockedRoute2.equals(mockedRoute2)).thenReturn(true);

    mockedRouteTimetable = mock(RouteTimetable.class);
    when(mockedRouteTimetable.getRoute()).thenReturn(mockedRoute1);
    when(mockedRouteTimetable.equals(mockedRouteTimetable)).thenReturn(true);

    // Create 6 mocked routeTimetables, with the first three on route1 and
    // the second three on route 2.
    busRouteTimetables = new ArrayList<RouteTimetable>();
    for (int i = 0; i < 6; i++) {
      RouteTimetable rt = mock(RouteTimetable.class);
      if (i < 3) {
        when(rt.getRoute()).thenReturn(mockedRoute1);
      } else {
        when(rt.getRoute()).thenReturn(mockedRoute2);
      }
      // Define timeAtStop for nextDepartureRouteTimetable test
      when(rt.timeAtStop(any(Stop.class))).thenReturn(10 * 60 + i * 5);
      busRouteTimetables.add(rt);
    }

    anotherMockedRouteTimetable = mock(RouteTimetable.class);
    when(anotherMockedRouteTimetable.getRoute()).thenReturn(mockedRoute2);
    when(anotherMockedRouteTimetable.equals(anotherMockedRouteTimetable)).thenReturn(true);

    anotherMockedBus = mock(Bus.class);
    when(anotherMockedBus.equals(anotherMockedBus)).thenReturn(true);
    
    weekdayScheduleStart = LocalDate.of(2016, Month.JANUARY, 1);
    weekdayScheduleEnd = LocalDate.of(2016, Month.DECEMBER, 31);
    saturdayScheduleStart = LocalDate.of(2015, Month.JANUARY, 1);
    saturdayScheduleEnd = LocalDate.of(2015, Month.DECEMBER, 31);
    sundayScheduleStart = LocalDate.of(2014, Month.JANUARY, 1);
    sundayScheduleEnd = LocalDate.of(2014, Month.DECEMBER, 31);

    weekdaySchedule = new Schedule(
        weekdayScheduleStart,
        weekdayScheduleEnd,
        DayOption.WEEKDAYS
        );
    saturdaySchedule = new Schedule(
        saturdayScheduleStart,
        saturdayScheduleEnd,
        DayOption.SATURDAY
        );
    sundaySchedule = new Schedule(
        sundayScheduleStart,
        sundayScheduleEnd,
        DayOption.SUNDAY
        );
  }

  @After
  public void tearDown() { 
    // This must be done to allow schedule to be recreated in next tests
    List<Schedule> allSchedules = new ArrayList<>(Schedule.getAllSchedules());
    for (Schedule s : allSchedules) {
      Schedule.removeSchedule(s);
    }
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * Test the creation of a schedule overlapping with an earlier saturdaySchedule.
   *
   * The class should reject an attempt to create a Schedule for a period and
   * operating day for which a Schedule has already been created. In this case,
   * the schedule has a partial overlap with an earlier saturdaySchedule.
   *
   */
  @Test
  public void testCreateScheduleOverlappingWithEarlier() {
    thrown.expect(IllegalArgumentException.class);
    String msg = "SUNDAY Schedule for period is already defined";
    thrown.expectMessage(msg);
    new Schedule(
        LocalDate.of(2014, Month.JANUARY, 20),
        LocalDate.of(2015, Month.JANUARY, 19),
        DayOption.SUNDAY
        );
  }

  /**
   * Test the creation of a schedule overlapping with a later saturdaySchedule.
   *
   * The class should reject an attempt to create a Schedule for a period and
   * operating day for which a Schedule has already been created. In this case,
   * the schedule has a partial overlap with a later saturdaySchedule.
   *
   */
  @Test
  public void testCreateScheduleOverlappingWithLater() {
    thrown.expect(IllegalArgumentException.class);
    String msg = "WEEKDAYS Schedule for period is already defined";
    thrown.expectMessage(msg);
    new Schedule(
        LocalDate.of(2015, Month.JANUARY, 20),
        LocalDate.of(2016, Month.JANUARY, 19),
        DayOption.WEEKDAYS
        );
  }

  /**
   * Test the creation of a schedule for a period falling within another.
   *
   * The class should reject an attempt to create a Schedule for a period and
   * operating day for which a Schedule has already been created. In this case,
   * the schedule falls wholly within a period defined by another.
   *
   */
  @Test
  public void testCreateScheduleWithinAnother() {
    thrown.expect(IllegalArgumentException.class);
    String msg = "SATURDAY Schedule for period is already defined";
    thrown.expectMessage(msg);
    new Schedule(
        LocalDate.of(2015, Month.MAY, 3),
        LocalDate.of(2015, Month.JUNE, 30),
        DayOption.SATURDAY
        );
  }

  /**
   * Test the creation of a schedule for a period wholly covering that of 
   * another saturdaySchedule.
   *
   * The class should reject an attempt to create a Schedule for a period and
   * operating day for which a Schedule has already been created. In this case,
   * the schedule entirely covers the period of another schedule, with a period
   * in addition to this also.
   *
   */
  @Test
  public void testCreateScheduleWhollyCoveringAnother() {
    thrown.expect(IllegalArgumentException.class);
    String msg = "SATURDAY Schedule for period is already defined";
    thrown.expectMessage(msg);
    new Schedule(
        LocalDate.of(2014, Month.AUGUST, 15),
        LocalDate.of(2016, Month.FEBRUARY, 27),
        DayOption.SATURDAY
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
    LocalDate weekday = LocalDate.of(2016, Month.SEPTEMBER, 14);
    LocalDate saturday = LocalDate.of(2015, Month.NOVEMBER, 28);
    LocalDate sunday = LocalDate.of(2014, Month.MARCH, 9);

    LocalDate futureDate = LocalDate.of(2025, Month.OCTOBER, 7);
    LocalDate pastDate = LocalDate.of(2000, Month.JANUARY, 1);
    LocalDate weekday2015 = LocalDate.of(2015, Month.APRIL, 23);

    assertEquals(weekdaySchedule, Schedule.findSchedule(weekday));
    assertEquals(saturdaySchedule, Schedule.findSchedule(saturday));
    assertEquals(sundaySchedule, Schedule.findSchedule(sunday));

    assertEquals(null, Schedule.findSchedule(pastDate));
    assertEquals(null, Schedule.findSchedule(futureDate));
    assertEquals(null, Schedule.findSchedule(weekday2015));
  }

  /**
   * Test scheduleExists method.
   *
   * The scheduleExists method is a private method, called only through
   * Schedule.addSchedule, which is called in-turn only by the constructor.
   * To test the scheduleExists method, this test instantiates a number of
   * different Schedules with properties testing the logic of the scheduleExists
   * method.
   */
  @Test
  public void testScheduleExists() {
    try {
      new Schedule(
          LocalDate.of(2013, Month.DECEMBER, 1),
          LocalDate.of(2013, Month.DECEMBER, 31),
          DayOption.SUNDAY
          );
      new Schedule(
          LocalDate.of(2015, Month.JANUARY, 1),
          LocalDate.of(2015, Month.JANUARY, 31),
          DayOption.SUNDAY
          );
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void testGetAllSchedules() {
    List<Schedule> expected = Arrays.asList(weekdaySchedule, saturdaySchedule, sundaySchedule);
    List<Schedule> actual = Schedule.getAllSchedules();
    assertEquals(expected, actual);
  }
  /**
   * Test values of DayOption enum.
   *
   * The DayOption enum should contain three fixed values: WEEKDAYS, 
   * SATURDAY & SUNDAY. This test ensures these are the only values.
   */
  @Test
  public void testDayOptionValues() {
    DayOption[] actualOptions = DayOption.values();
    DayOption[] expectedOptions = new DayOption[] {DayOption.WEEKDAYS, DayOption.SATURDAY, DayOption.SUNDAY};
    for (int i = 0; i < actualOptions.length; i++) {
      if (expectedOptions.length < i) {
        fail("there are more DayOption than expected: " + actualOptions);
      }
      assertEquals(actualOptions[i], expectedOptions[i]);
    }
  }
  
  /**
   * Test valueOf() DayOption enum.
   *
   * The valueOf() method should return appropriate identifiers in DayOption.
   * This test ensures this is the case.
   */
  @Test
  public void testDayOptionValueOf() {
    String[] dayOptionsStrings = new String[] {"WEEKDAYS", "SATURDAY", "SUNDAY"};
    DayOption[] dayOptionsConsts = new DayOption[] {DayOption.WEEKDAYS, DayOption.SATURDAY, DayOption.SUNDAY};
    for (int i = 0; i < dayOptionsStrings.length; i++) {
      assertEquals(DayOption.valueOf(dayOptionsStrings[i]), dayOptionsConsts[i]);
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
    saturdaySchedule.addRouteTimetable(mockedRouteTimetable);
    assertTrue(saturdaySchedule.hasRouteTimetable(mockedRouteTimetable));
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
    saturdaySchedule.addRouteTimetable(mockedRouteTimetable, mockedBus);
    assertTrue(saturdaySchedule.hasRouteTimetable(mockedRouteTimetable));
    assertTrue(saturdaySchedule.hasBus(mockedBus));
    assertEquals(saturdaySchedule.getAllocatedBus(mockedRouteTimetable), mockedBus);
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
    saturdaySchedule.addRouteTimetable(null);
  }

  /**
   * Test allocateBus method.
   *
   * The allocateBus method is used to allocate a bus to a RouteTimetable
   * where either no bus is allocated to a particular RouteTimetable or where
   * the allocation requires to change.
   */
  @Test
  public void testAllocateBus() {
    saturdaySchedule.addRouteTimetable(mockedRouteTimetable);
    saturdaySchedule.allocateBus(mockedRouteTimetable, mockedBus);
    assertTrue(saturdaySchedule.hasBus(mockedBus));
    assertEquals(saturdaySchedule.getAllocatedBus(mockedRouteTimetable), mockedBus);
  }

  /**
   * test allocateBus method with non-existence RouteTimetable.
   *
   * Where an attempt is made to allocate a bus to a RouteTimetable not found
   * within a schedule, an IllegalArgumentException should be thrown.
   */
  @Test
  public void testAllocateBusWithInvalidRouteTimetable() {
    RouteTimetable invalidRT = mock(RouteTimetable.class);
    thrown.expect(IllegalArgumentException.class);
    String msg = "RouteTimetable " + invalidRT + "is not within Schedule";
    thrown.expectMessage(msg);
    saturdaySchedule.allocateBus(invalidRT, mock(Bus.class));
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
    assertFalse(saturdaySchedule.hasBus(mockedBus));
    // Add buses to schedule and try again
    saturdaySchedule.addRouteTimetable(anotherMockedRouteTimetable, anotherMockedBus);
    saturdaySchedule.addRouteTimetable(anotherMockedRouteTimetable);
    saturdaySchedule.addRouteTimetable(mockedRouteTimetable, mockedBus);
    assertTrue(saturdaySchedule.hasBus(mockedBus));
    assertTrue(saturdaySchedule.hasBus(anotherMockedBus));
    // Try with null
    assertFalse(saturdaySchedule.hasBus(null));
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
    assertFalse(saturdaySchedule.hasRouteTimetable(mockedRouteTimetable));
    // Add routetimetables and try again
    saturdaySchedule.addRouteTimetable(anotherMockedRouteTimetable);
    saturdaySchedule.addRouteTimetable(mockedRouteTimetable);
    assertTrue(saturdaySchedule.hasRouteTimetable(mockedRouteTimetable));
    // Try with null
    assertFalse(saturdaySchedule.hasRouteTimetable(null));
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
    saturdaySchedule.addRouteTimetable(anotherMockedRouteTimetable, anotherMockedBus);

    saturdaySchedule.addRouteTimetable(mockedRouteTimetable, mockedBus);
    assertEquals(saturdaySchedule.getAllocatedBus(mockedRouteTimetable), mockedBus);
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
    saturdaySchedule.getAllocatedBus(mockedRouteTimetable);
  }

  /**
   * Test the getAllocatedRouteTimetables with bus method.
   *
   * The allocatedRouteTimetables method takes a Bus object and returns all
   * RouteTimetables allocated to that Bus within the Schedule.
   */
  @Test 
  public void testGetAllocatedRouteTimetablesWithBus() {
    // Add other mocked RouteTimetable and Bus first
    saturdaySchedule.addRouteTimetable(anotherMockedRouteTimetable, anotherMockedBus);

    // Add RouteTimetables with associated Bus
    for (RouteTimetable rt : busRouteTimetables) {
      saturdaySchedule.addRouteTimetable(rt, mockedBus);
    }
    List<RouteTimetable> actualRouteTimetables = saturdaySchedule.getAllocatedRouteTimetables(mockedBus);
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
    saturdaySchedule.getAllocatedRouteTimetables(mockedBus);
  }

  /**
   * Test the getAllocatedRouteTimetables with route method.
   *
   * The allocatedRouteTimetables method takes a Route object and returns all
   * RouteTimetables associated with that Route.
   */
  @Test 
  public void testGetAllocatedRouteTimetablesWithRoute() {
    // Add RouteTimetables
    saturdaySchedule.addRouteTimetable(mockedRouteTimetable);
    saturdaySchedule.addRouteTimetable(anotherMockedRouteTimetable);
    for (RouteTimetable rt : busRouteTimetables) {
      saturdaySchedule.addRouteTimetable(rt);
    }

    List<RouteTimetable> actualRTforRoute1 = saturdaySchedule.getAllocatedRouteTimetables(mockedRoute1);
    List<RouteTimetable> actualRTforRoute2 = saturdaySchedule.getAllocatedRouteTimetables(mockedRoute2);
    assertEquals(actualRTforRoute1.size(), 4);
    assertEquals(actualRTforRoute2.size(), 4);
  }

  /**
   * Test the getAllocatedRouteTimetables method with invalid Route.
   *
   * If a Route is passed which is not allocated in a schedule, the
   * method should throw a specific error.
   *
   */
  @Test
  public void testGetAllocatedRouteTimetablesWithInvalidRoute() {
    Route anotherMockedRoute = mock(Route.class);
    String msg = "Route \"" + anotherMockedRoute + 
      "\" is not found within Schedule";
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage(msg);
    saturdaySchedule.getAllocatedRouteTimetables(anotherMockedRoute);
  }

  /**
   * Test the getOperatingDay getter method.
   */
  @Test
  public void testGetOperatingDay() {
    assertEquals(weekdaySchedule.getOperatingDay(), DayOption.WEEKDAYS);
    assertEquals(saturdaySchedule.getOperatingDay(), DayOption.SATURDAY);
    assertEquals(sundaySchedule.getOperatingDay(), DayOption.SUNDAY);
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
    List<Predicate<LocalDate>> ps = new ArrayList<>();
    ps.add((LocalDate d) -> dayOfWeek(d) >= 2 && dayOfWeek(d) <= 6);
    ps.add((LocalDate d) -> dayOfWeek(d) == 7);
    ps.add((LocalDate d) -> dayOfWeek(d) == 1);

    // Undertake testing on each schedule
    for (int i = 0; i < schedules.size(); i++ ) {
      Schedule s = schedules.get(i);
      Predicate<LocalDate> p = ps.get(i);
      List<LocalDate> actualDates = s.scheduledDates();
      if (actualDates.size() == 0) {
        fail("scheduled dates must not be empty");
      }
      for (LocalDate d : actualDates) {
        if ((i == 0 && (d.isBefore(weekdayScheduleStart) || d.isAfter(weekdayScheduleEnd))) || 
            (i == 1 && (d.isBefore(saturdayScheduleStart) || d.isAfter(saturdayScheduleEnd))) ||
            (i == 2 && (d.isBefore(sundayScheduleStart) || d.isAfter(sundayScheduleEnd)))) {
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
   * Test the nextDepartureRouteTimetable method.
   *
   * The nextDepartureRouteTimetable method returns a route timetable which
   * represents the RT of the next service departing a given stop on or after
   * a given time.
   */
  @Test
  public void testNextDepartureRouteTimetable() {
    // Add all route timetables to schedule in a semi-random order
    // The order is picked to test all logical options in method
    int[] indices = new int[] {0, 2, 4, 5, 3, 1};
    for (int i = 0; i < 6; i++) {
      saturdaySchedule.addRouteTimetable(busRouteTimetables.get(indices[i]));
    }
    assertEquals(saturdaySchedule.nextDepartureRouteTimetable(10 * 60 + 9, mock(Stop.class), mockedRoute1), busRouteTimetables.get(2));
    assertEquals(saturdaySchedule.nextDepartureRouteTimetable(10 * 60 + 18, mock(Stop.class), mockedRoute2), busRouteTimetables.get(4));
  }

  /**
   * Test the nextDepartureRouteTimetable method when no further departures
   * are available on a given day.
   *
   * Calling this method where no departures are available should throw an
   * IllegalArgumentException.
   */
  @Test
  public void testNextDepartureRouteTimetableWithNoFurtherDepartures() {
    thrown.expect(IllegalArgumentException.class);
    String msg = "no next departures available today";
    thrown.expectMessage(msg);
    int[] indices = new int[] {0, 2, 4, 5, 3, 1};
    for (int i = 0; i < 6; i++) {
      saturdaySchedule.addRouteTimetable(busRouteTimetables.get(indices[i]));
    }
    saturdaySchedule.nextDepartureRouteTimetable(23 * 60 + 55, mock(Stop.class), mockedRoute1);
  }

  /**
   * Test the nextDepartureTime method.
   *
   * This method relies upon the nextDepartureRouteTimetable method, and this
   * test reflects this.
   */
  @Test
  public void testNextDepartureTime() { 
    testNextDepartureRouteTimetable(); // Run test to create dependent variables
    assertEquals(saturdaySchedule.nextDepartureTime(10 * 60 + 9, mock(Stop.class), mockedRoute1), 10 * 60 + 10);
  }

  /**
   * Test the nextDepartureTime method when no further departures
   * are available on a given day.
   *
   * Calling this method where no departures are available should throw an
   * IllegalArgumentException.
   */
  @Test
  public void testNextDepartureTimeWithNoFurtherDepartures() {
    thrown.expect(IllegalArgumentException.class);
    String msg = "no next departures available today";
    thrown.expectMessage(msg);
    testNextDepartureRouteTimetable();
    saturdaySchedule.nextDepartureTime(23 * 60 + 55, mock(Stop.class), mockedRoute1);
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
  private static int dayOfWeek(LocalDate d) {
    return d.getDayOfWeek().getValue() % 7 + 1;
  }
}
