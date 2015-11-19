package main;

import java.util.ArrayList;

public class Route {
	private final int number;
	private final String description;
	private ArrayList<Stop> stops = new ArrayList<Stop>();
	private ArrayList<Integer> timeBetweenStops = new ArrayList<Integer>(); 
	private ArrayList<Integer> rushHourTimeBetweenStops = new ArrayList<Integer>();
	
	public Route(int number, String description){
		this.number = number;
		this.description = description;
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
	
}
