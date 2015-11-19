package main;

import java.util.ArrayList;
import java.util.HashMap;

public class Route {
	private final String routeNumber;
	private final String routeDescription;
	private ArrayList<Stop> stops = new ArrayList<Stop>();
	private HashMap<Stop, Integer> timeBetweenStops = new HashMap<Stop, Integer>(); 
	private HashMap<Stop, Integer> rushHourTimeBetweenStops = new HashMap<Stop, Integer>();
	
	public Route(String routeNumber, String routeDescription, Stop routeStart){
		this.routeNumber = routeNumber;
		this.routeDescription = routeDescription;
	}

	public void addStop(Stop stop, int time, int rushHourTime) {
		stops.add(stop);
		timeBetweenStops.add(time);
		rushHourTimeBetweenStops.add(rushHourTime);
		//For testing whether entries are stored in ArrayList stops
		for (int i=0; i<stops.size(); i++){
			System.out.print(stops.get(i).getID() + " ");
			System.out.print(stops.get(i).getName() + " ");
			System.out.println(stops.get(i).getLocation() + " ");
			}
		System.out.println("End of list");
		System.out.println("number of entries in ArrayList route for this route: " + stops.size());
		System.out.println("");
		//end test
	}
	public String getNumber(){
		return routeNumber;
	}
	public String getDescription(){
		return routeDescription;
	}
	public ArrayList<Stop> getStops(){
		return stops;
	
	}
	public HashMap<Stop, Integer> viewStopTimes(){
		return timeBetweenStops;
	}
	public HashMap<Stop, Integer> viewRushHourStopTimes(){
		return rushHourTimeBetweenStops;
	}
	
	
}
