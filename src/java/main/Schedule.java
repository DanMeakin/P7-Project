package main;

import java.util.Date;

public class Schedule {
	public static enum ScheduledDays {
		WEEKDAYS, SATURDAY, SUNDAY
	}

	private Date validfromdate;
	private Date validtodate;
	private ScheduledDays ScheduledDays;
	
	public Schedule (Date validfromdate, Date validtodate, ScheduledDays scheduleddays) {
		this.validfromdate = validfromdate;
		this.validtodate = validtodate;
		this.ScheduledDays = scheduleddays;
	}
	
	public void addRouteTimetable(RouteTimetable RouteTimetable) {
		
	}
}
