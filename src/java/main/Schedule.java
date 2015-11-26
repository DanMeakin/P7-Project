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
	private List<Bus> busRouteTimeTableList = new ArrayList<>();


	public Schedule (Date validFromDate, Date validToDate, DayOptions scheduledDays) {
		this.validFromDate = validFromDate;
		this.validToDate = validToDate;
		this.scheduledDays = scheduledDays;
	}

	public void addRouteTimetable(RouteTimetable routeTimetable) {
		this.routeTimeTableList.add(routeTimetable);
		this.busRouteTimeTableList.add(null);
	}

	public void addRouteTimetableWithBus(RouteTimetable routeTimetable, Bus bus) {
		this.routeTimeTableList.add(routeTimetable);
		this.busRouteTimeTableList.add(bus);
	}

	public boolean hasRouteTimetable(){
		if(this.routeTimeTableList.isEmpty()){
			return false;
		}
		return true;
	}

	//This will not actually test whether a Bus is assigned to a schedule, since the list could be filled with nulls.
	public boolean hasBus(){
		if(this.busRouteTimeTableList.isEmpty()){
			return false;
		}
		return true;
	}
}
