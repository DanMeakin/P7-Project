package main;

import java.util.ArrayList;

public class Route {
	private final int number;
	private final String description;
	ArrayList<BusStop> stops = new ArrayList<BusStop>();
	
	public Route(int number, String description){
		stops = new ArrayList<BusStop>();
		this.number = number;
		this.description = description;
	}

	public void addStop(BusStop stop) {
		stops.add(stop);
		//For Testing whether entries are stored in ArrayList stops
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
