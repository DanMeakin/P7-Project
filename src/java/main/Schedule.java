package main;

import java.util.*;

public class Schedule {
	public static enum DayOptions {
		WEEKDAYS, SATURDAY, SUNDAY
	}
	private Date validFromDate;
	private Date validToDate;
	private DayOptions scheduledDays;
	private List<RouteTimetable> routeTimeTableList = new ArrayList<>();
	private List<Bus> busList = new ArrayList<>();


	public Schedule (Date validFromDate, Date validToDate, DayOptions scheduledDays) {
		this.validFromDate = validFromDate;
		this.validToDate = validToDate;
		this.scheduledDays = scheduledDays;
	}

	public void addRouteTimetable(RouteTimetable routeTimetable) {
		this.routeTimeTableList.add(routeTimetable);
		this.busList.add(null);
	}

	public void addRouteTimetable(RouteTimetable routeTimetable, Bus bus) {
		this.routeTimeTableList.add(routeTimetable);
		this.busList.add(bus);
	}

	public Bus getAllocatedBus(RouteTimetable routeTimetable){
		int i;
		String msg = "Routetimetable" + routeTimetable + "is not added to this schedule";
		for (i=0; i < routeTimeTableList.size(); i++) {
			if(routeTimeTableList.get(i).equals(routeTimetable)) {
				return busList.get(i);
			}
		}
		throw new UnsupportedOperationException(msg);
	}

	public List<RouteTimetable> getAllocatedRouteTimetables(Bus bus) {
		List<RouteTimetable> allocatedRouteTimetables = new ArrayList<>();
		int i;
		String msg = "Bus" + bus + "is not added to this schedule";
		for (i = 0; i < busList.size(); i++) {
			if (busList.get(i).equals(bus)) {
				allocatedRouteTimetables.add(routeTimeTableList.get(i));
			}
		}
		if (!busList.isEmpty()) {
			return allocatedRouteTimetables;
		}
		throw new UnsupportedOperationException(msg);
	}

	public boolean hasRouteTimetable(RouteTimetable routeTimetable){
    if (routeTimetable == null) {
      return false;
    }
    for (RouteTimetable r : routeTimeTableList) {
      if (r != null && routeTimetable.equals(r)) {
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

  public DayOptions getScheduledDays() {
    return this.scheduledDays;
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
    return new ArrayList<Date>();
  }
}
