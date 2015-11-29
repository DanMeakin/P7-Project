package main;

import java.util.*;

/**
 * The Schedule class defines schedule type objects that hold
 * characteristics of the schedule such as validFromDate, validToDate etc.
 * and implements several domain specific methods such as addRoutetimeTable.
 * @authors Ivo Hendriks, Janus Avbæk Larsen, Helle Hyllested Larsen, Dan Meakin.
 */
public class Schedule {
<<<<<<< HEAD
	/** the options for the days for which this schedule is valid  */
	public static enum DayOptions {
		WEEKDAYS, SATURDAY, SUNDAY
	}
	/** the date from which this schedule is valid  */
	private Date validFromDate;
	/** the date to which this schedule is valid  */
	private Date validToDate;
	/** the days for which this schedule is valid  */
	private DayOptions operatingDay;
	/** a data structure which holds all routeTimetables associated with this schedule  */
	private List<RouteTimetable> routeTimetableList = new ArrayList<>();
	/** a data structure which holds all buses associated with this schedule  */
	private List<Bus> busList = new ArrayList<>();

	/**
	 * Creates a schedule.
	 *
	 * @param validFromDate the date from which this scedule is valid.
	 * @param validToDate the date to which this scedule is valid.
	 * @param operatingDay the days for which this schedule is valid.
	 */
	public Schedule (Date validFromDate, Date validToDate, DayOptions operatingDay) {
		this.validFromDate = validFromDate;
		this.validToDate = validToDate;
		this.operatingDay = operatingDay;
	}

	/**
	 * Add a routeTimeTable to the schedule through the routeTimetableList
	 * without associating a bus with it.
	 *
	 * @param routeTimetable the route timetable to add to the schedule.
	 */
	public void addRouteTimetable(RouteTimetable routeTimetable) {
		this.routeTimetableList.add(routeTimetable);
		this.busList.add(null);
	}

	/**
	 * Add a routeTimeTable to the schedule through the routeTimetableList
	 * and associate a bus with it.
	 *
	 * @param routeTimetable the route timetable to add to the schedule.
	 * @param bus the bus that will be associated with this routeTimeTable.
	 */
	public void addRouteTimetable(RouteTimetable routeTimetable, Bus bus) {
		this.routeTimetableList.add(routeTimetable);
		this.busList.add(bus);
	}

	/**
	 * Get the bus associated with a route timetable.
	 *
	 * @param routeTimetable the route timetable to find the assiated bus for.
	 *
	 * @return busList.get(i) the entry in busList holding the bus associated
	 * with the routeTimetable.
	 *
	 * @exception msg if the routeTimetable is not assoiated with the schedule.
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
	 * Get the route timetable that a bus is allocated to.
	 *
	 * @param bus the bus to find the allocated route timetable for.
	 *
	 * @return allocatedRouteTimetables a list of all route timetables
	 * associated with the bus.
	 *
	 * @exception msg if busList is empty.
	 */
	public List<RouteTimetable> getAllocatedRouteTimetables(Bus bus) throws IllegalArgumentException {
		List<RouteTimetable> allocatedRouteTimetables = new ArrayList<>();
    String msg = "Bus \"" + bus + 
      "\" is not found within Schedule";
		for (int i = 0; i < busList.size(); i++) {
			if (busList.get(i).equals(bus)) {
				allocatedRouteTimetables.add(routeTimetableListableList.get(i));
			}
		}
		if (busList.isEmpty()) {
=======
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
>>>>>>> 85d914469ca84ba7c6960db8c3fe0e456f32dc0a
      throw new IllegalArgumentException(msg);
    }
    return allocatedRouteTimetables;
  }

<<<<<<< HEAD
	/**
	 * Check whether a route timetable is associated with a schedule.
	 *
	 * @param routeTimetable the route timetible to check association for.
	 *
	 * @return true if the route timetable is associated with the schedule, else false.
	 */
	public boolean hasRouteTimetable(RouteTimetable routeTimetable){
=======
  public boolean hasRouteTimetable(RouteTimetable routeTimetable){
>>>>>>> 85d914469ca84ba7c6960db8c3fe0e456f32dc0a
    if (routeTimetable == null) {
      return false;
    }
    for (RouteTimetable r : routeTimetableList) {
<<<<<<< HEAD
      if (r != null && routeTimetable.equals(r)) {
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
=======
      if (routeTimetable.equals(r)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasBus(Bus bus){
>>>>>>> 85d914469ca84ba7c6960db8c3fe0e456f32dc0a
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
