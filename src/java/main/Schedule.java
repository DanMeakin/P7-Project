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
		int i;
		for (i=0; i < routeTimeTableList.size(); i++) {
			if(routeTimeTableList.get(i).equals(routeTimetable)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasBus(Bus bus){
		int i;
		for (i=0; i < busList.size(); i++) {
			if(busList.get(i).equals(bus)) {
				return true;
			}
		}
		return false;
	}
}
