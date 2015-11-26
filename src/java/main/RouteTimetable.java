package main;


import main.Route;

public class RouteTimetable {
	private final Route route;
	private int startTime;
	private int endTime;
	private boolean isRushHour;


	public RouteTimetable(int starttime, int endtime, boolean isrushhour, Route route) {
		this.startTime = starttime;
		this.endTime = endtime;
		this.isRushHour = isrushhour;
		this.route = route;

	}

	public static void addRoute(Route route){


	}
	public static void removeRoute(Route route){


	}
	public static void timetabled(int startTime, int endTime){

	}
	public void getStartTime(){

	}
	public boolean isRushHour(){

	}
    public void getStopTimes(){

    }
    public void getStops(){

    }




}

