package main;

import java.util.*;

public class Schedule {
  public static enum DayOptions {
    WEEKDAYS, SATURDAY, SUNDAY
  }
  private Date validFromDate;
  private Date validToDate;
  private DayOptions operatingDay;
  private List<RouteTimetable> routeTimetableList = new ArrayList<>();
  private List<Bus> busList = new ArrayList<>();


  public Schedule (Date validFromDate, Date validToDate, DayOptions operatingDay) {
    this.validFromDate = validFromDate;
    this.validToDate = validToDate;
    this.operatingDay = operatingDay;
  }

  public void addRouteTimetable(RouteTimetable routeTimetable) throws IllegalArgumentException {
    addRouteTimetable(routeTimetable, null);
  }

  public void addRouteTimetable(RouteTimetable routeTimetable, Bus bus) {
    if (routeTimetable == null) {
      throw new IllegalArgumentException("cannot add a null RouteTimetable");
    }
    this.routeTimetableList.add(routeTimetable);
    this.busList.add(bus);
  }

  public Bus getAllocatedBus(RouteTimetable routeTimetable) throws IllegalArgumentException {
    String msg = "RouteTimetable \"" + routeTimetable + 
      "\" is not found within Schedule";
    for (int i = 0; i < routeTimetableList.size(); i++) {
      RouteTimetable thisRT = routeTimetableList.get(i);
      if (thisRT.equals(routeTimetable)) {
        return busList.get(i);
      }
    }
    throw new IllegalArgumentException(msg);
  }

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
  private int dayOfWeek(Date d) {
      Calendar c = Calendar.getInstance();
      c.setTime(d);
      return c.get(Calendar.DAY_OF_WEEK);
  }

}
