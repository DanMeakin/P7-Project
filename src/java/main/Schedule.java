package main;

import java.util.Date;

public class Schedule {
	private Date validfromdate;
	private Date validtodate;
	private ScheduledDays ScheduledDays;
	
	public enum ScheduledDays {
		WEEKDAYS, SATURDAY, SUNDAY
	
	}
	
	public Schedule (Date validfromdate, Date validtodate, ScheduledDays scheduleddays) {
		this.validfromdate = validfromdate;
		this.validtodate = validtodate;
		this.ScheduledDays = scheduleddays;
	}
	
	public void addRouteTimetable(RouteTimetable RouteTimetable) {
		
	}
}
