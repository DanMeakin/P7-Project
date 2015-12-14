package main;

import java.util.*;

/**
 * Defines a schedule which contains planned operations and scheduling of bus
 * travel within the Aalborg area.
 */
public class Schedule {
  
  /**
   * Provides options for the day type on which a schedule operates.
   *
   * A schedule may operate on a weekday, a Saturday, a Sunday or
   * a holiday (the last being a special case). This enum provides these values
   * for use within a schedule instance.
   */
  public static enum DayOptions {
    WEEKDAYS, SATURDAY, SUNDAY
  }

  // list containing all Schedules, current, past and prospective
  private static List<Schedule> allSchedules = new ArrayList<>();

  // the date from which this schedule is valid
  private Date validFromDate;
  // the date to which this schedule is valid
  private Date validToDate;
  // the days for which this schedule is valid
  private DayOptions operatingDay;
  // a data structure which holds all routeTimetables associated with this schedule
  private List<RouteTimetable> routeTimetableList = new ArrayList<>();
  // a data structure which holds all buses associated with this schedule
  private List<Bus> busList = new ArrayList<>();

  /**
   * Creates a schedule.
   *
   * @param validFromDate the date from which this scedule is valid.
   * @param validToDate   the date to which this scedule is valid.
   * @param operatingDay  the days for which this schedule is valid.
   */
  public Schedule (Date validFromDate, Date validToDate, DayOptions operatingDay) {
    this.validFromDate = validFromDate;
    this.validToDate = validToDate;
    this.operatingDay = operatingDay;
    addSchedule(this);
  }

  /**
   * Adds schedule to the allSchedules list.
   *
   * @param schedule the schedule to add to the list
   */
  private static void addSchedule(Schedule schedule) throws IllegalArgumentException {
    if (scheduleExists(schedule)) {
      String msg = schedule.getOperatingDay() + " Schedule for period is already defined";
      throw new IllegalArgumentException(msg);
    }
    allSchedules.add(schedule);
  }

  /**
   * Removes a schedule from list of all schedules.
   *
   * @param schedule the schedule to remove from the list
   */
  public static void removeSchedule(Schedule schedule) {
    allSchedules.remove(schedule);
  }

  /**
   * Gets all schedules within system.
   *
   * @return list of all schedules in system
   */
  public static List<Schedule> getAllSchedules() {
    return allSchedules;
  }

  /**
   * Finds the schedule for a desired date and operating day.
   *
   * @param date the date for which a schedule is desired
   * @return schedule in operation for the specified date
   */
  public static Schedule findSchedule (Date date) {
    for (Schedule s : allSchedules) {
      if (!date.before(s.getValidFromDate()) && 
          !date.after(s.getValidToDate()) && 
          operatingDayForDate(date) == s.getOperatingDay()) {
        return s;
      }
    }
    return null;
  }

  /**
   * Adds a routeTimeTable to the schedule through the routeTimetableList
   * without associating a bus with it.
   *
   * This method simply calls the 
   * {@link #addRouteTimetable(RouteTimetable, Bus) addRouteTimetable} method
   * with a null value for bus.
   *
   * @param routeTimetable the route timetable to add to the schedule.
   */
  public void addRouteTimetable(RouteTimetable routeTimetable) {
    addRouteTimetable(routeTimetable, null);
  }

  /**
   * Adds a routeTimeTable to the schedule through the routeTimetableList
   * and associate a bus with it.
   *
   * @param routeTimetable the route timetable to add to the schedule.
   * @param bus the bus that will be associated with this routeTimeTable.
   * @throws IllegalArgumentException if routeTimetable is null (a bus may
   *                                  be null, however)
   */
  public void addRouteTimetable(RouteTimetable routeTimetable, Bus bus) throws IllegalArgumentException {
    if (routeTimetable == null) {
      throw new IllegalArgumentException("cannot add a null RouteTimetable");
    }
    this.routeTimetableList.add(routeTimetable);
    this.busList.add(bus);
  }

  /**
   * Allocates a bus to a particular RouteTimetable associated with schedule.
   *
   * @param routeTimetable the route timetable with which to associate bus
   * @param bus            the bus to associate with the specified route 
   *                       timetable
   * @throws IllegalArgumentException if specified route timetable is not found
   *                                  within schedule
   */
  public void allocateBus(RouteTimetable routeTimetable, Bus bus) throws IllegalArgumentException {
    if (!hasRouteTimetable(routeTimetable)) {
      String msg = "RouteTimetable " + routeTimetable + "is not within Schedule";
      throw new IllegalArgumentException(msg);
    }
    int index = this.routeTimetableList.indexOf(routeTimetable);
    this.busList.set(index, bus);
  }

  /**
   * Determines the time of the next departure of a given route from a given stop.
   *
   * @param time  the time from which to get next departure
   * @param stop  the stop from which departure is to take place
   * @param route the route on which to travel
   * @return the departure time (as an integer representing minutes since
   *         midnight) of the next bus on route departing from stop
   * @throws UnsupportedOperationException if there is no departure information
   *                                       available for this date
   */
  public int nextDepartureTime(int time, Stop stop, Route route) throws UnsupportedOperationException {
    RouteTimetable rt = nextDepartureRouteTimetable(time, stop, route);
    if (rt == null) {
      throw new UnsupportedOperationException("no next departures available today");
    }
    return rt.timeAtStop(stop);
  }

  /**
   * Finds the RouteTimetable on which the next departure of a route from a
   * stop takes place.
   *
   * Similar to the nextDepartureTime method, this method finds the 
   * RouteTimetable representing the next departure of a bus from a given stop
   * after a particular point in time.
   *
   * @param time the time from which to get next departure
   * @param stop the stop from which departure is to take place
   * @param route the route on which to travel
   * @return the RouteTimetable representing the next departure of the next bus
   *  on the given route from the given stop
   * @throws UnsupportedOperationException if there is no departure information
   *                                       available for this date 
   */
  public RouteTimetable nextDepartureRouteTimetable(int time, Stop stop, Route route) throws UnsupportedOperationException {
    int nextDepartureTime = 1_000_000; // Set time to initial high value
    RouteTimetable nextDepartureRT = null;
    List<RouteTimetable> rts = getAllocatedRouteTimetables(route);
    for (RouteTimetable thisRT : rts) {
      if (thisRT.timeAtStop(stop) >= time && thisRT.timeAtStop(stop) < nextDepartureTime) {
        nextDepartureRT = thisRT;
        nextDepartureTime = thisRT.timeAtStop(stop);
      }
    }
    if (nextDepartureRT == null) {
      throw new UnsupportedOperationException("no next departures available today");
    }
    return nextDepartureRT;
  }

  /**
   * Gets the bus associated with a route timetable.
   *
   * @param routeTimetable the route timetable to find the assiated bus for.
   * @return the entry in busList holding the bus associated
   *         with the routeTimetable.
   * @throws IllegalArgumentException if the routeTimetable is not associated 
   *                                  with the schedule.
   */
  public Bus getAllocatedBus(RouteTimetable routeTimetable) throws IllegalArgumentException {
    String msg = "RouteTimetable \"" + routeTimetable + 
      "\" is not found within Schedule";
    for (int i = 0; i < routeTimetableList.size(); i++) {
      if (routeTimetableList.get(i).equals(routeTimetable)) {
        return busList.get(i);
      }
    }
    throw new IllegalArgumentException(msg);
  }

  /**
   * Gets the route timetable that a bus is allocated to.
   *
   * @param bus the bus to find the allocated route timetable for.
   *
   * @return allocatedRouteTimetables a list of all route timetables
   *         associated with the bus.
   *
   * @throws IllegalArgumentException if busList is empty.
   */
  public List<RouteTimetable> getAllocatedRouteTimetables(Bus bus) throws IllegalArgumentException {
    List<RouteTimetable> allocatedRouteTimetables = new ArrayList<>();
    String msg = "Bus \"" + bus + 
      "\" is not found within Schedule";
    for (int i = 0; i < busList.size(); i++) {
      if (busList.get(i).equals(bus)) {
        allocatedRouteTimetables.add(routeTimetableList.get(i));
      }
    }
    if (allocatedRouteTimetables.isEmpty()) {
      throw new IllegalArgumentException(msg);
    }
    return allocatedRouteTimetables;
  }

  /**
   * Gets the route timetables for a given route.
   *
   * @param route the route to find allocated route timetables for.
   *
   * @return a list of all route timetables for the route
   *
   * @throws IllegalArgumentException if busList is empty.
   */
  public List<RouteTimetable> getAllocatedRouteTimetables(Route route) throws IllegalArgumentException {
    List<RouteTimetable> allocatedRouteTimetables = new ArrayList<>();
    for (int i = 0; i < routeTimetableList.size(); i++) {
      if (routeTimetableList.get(i).getRoute().equals(route)) {
        allocatedRouteTimetables.add(routeTimetableList.get(i));
      }
    }
    if (allocatedRouteTimetables.isEmpty()) {
      String msg = "Route \"" + route + 
                   "\" is not found within Schedule";
      throw new IllegalArgumentException(msg);
    }
    return allocatedRouteTimetables;
  }


  /**
   * Checks whether a route timetable is associated with a schedule.
   *
   * @param routeTimetable the route timetible to check association for.
   *
   * @return true if the route timetable is associated with the schedule, else false.
   */
  public boolean hasRouteTimetable(RouteTimetable routeTimetable){
    if (routeTimetable == null) {
      return false;
    }
    for (RouteTimetable r : routeTimetableList) {
      if (routeTimetable.equals(r)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check whether a bus is associated with a schedule.
   *
   * @param bus the bus to check association for.
   *
   * @return true if the bus is associated with the schedule, else false.
   */
  public boolean hasBus(Bus bus){
    if (bus == null) {
      return false;
    }
    for (Bus b : busList) {
      if (b != null && bus.equals(b)) {
        return true;
      }
    }
    return false;
  }

  public DayOptions getOperatingDay() {
    return this.operatingDay;
  }

  public Date getValidFromDate() {
    return this.validFromDate;
  }

  public Date getValidToDate() {
    return this.validToDate;
  }

  /**
   * Get a list of all dates on which this schedule runs.
   *
   * @return list of all dates schedule runs on
   */
  public List<Date> scheduledDates() {
    List<Date> dates = new ArrayList<>();
    Date currentDate = getValidFromDate();
    while (!currentDate.after(getValidToDate())) {
      if (operatingDay == DayOptions.WEEKDAYS && 
          dayOfWeek(currentDate) >= 2 && 
          dayOfWeek(currentDate) <= 6) {
        dates.add(currentDate);
      } else if (operatingDay == DayOptions.SATURDAY &&
          dayOfWeek(currentDate) == 7) {
        dates.add(currentDate);
      } else if (operatingDay == DayOptions.SUNDAY &&
          dayOfWeek(currentDate) == 1) {
        dates.add(currentDate);
      }
      Calendar cal = Calendar.getInstance();
      cal.setTime(currentDate);
      cal.add(Calendar.DATE, 1);
      currentDate = cal.getTime();
    }
    return dates;
  }

  /**
   * Determine the day of week for a given date.
   *
   * @return integer value of day of week: Sunday = 1, Saturday = 7
   */
  private static int dayOfWeek(Date d) {
      Calendar c = Calendar.getInstance();
      c.setTime(d);
      return c.get(Calendar.DAY_OF_WEEK);
  }

  private static DayOptions operatingDayForDate(Date d) {
    if (dayOfWeek(d) == 1) {
      return DayOptions.SUNDAY;
    } else if (dayOfWeek(d) <= 6) {
      return DayOptions.WEEKDAYS;
    } else {
      return DayOptions.SATURDAY;
    }
  }

  /** 
   * Check if Schedule already exists within the system.
   *
   * A schedule already exists if:-
   *
   *  * Another schedule exists of the same type; and
   *  * It does not end before the other schedule begins, or begin after the 
   *    other schedule ends.
   *
   * @param schedule the schedule object to check for existence
   * @return true if Schedule already exists, else false.
   */
  private static boolean scheduleExists(Schedule schedule) {
    for (Schedule otherSchedule : allSchedules) {
      boolean scheduleBeforeOther = schedule.getValidToDate().before(otherSchedule.getValidFromDate());
      boolean scheduleAfterOther = schedule.getValidFromDate().after(otherSchedule.getValidToDate());
      boolean sameScheduleType = schedule.getOperatingDay() == otherSchedule.getOperatingDay();
      boolean withinOtherSchedule = !(scheduleBeforeOther || scheduleAfterOther);
      if (sameScheduleType && withinOtherSchedule) {
        return true;
      }
    }
    return false;
  }
}
