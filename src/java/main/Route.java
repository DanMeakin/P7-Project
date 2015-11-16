package main;

import java.util.ArrayList;

public class Route {
	private int number;
	private String description;
	
	public Route(int number, String description){
		this.number = number;
		this.description = description;
	}

	public void addStop(BusStop stop) {
		ArrayList<BusStop> stops = new ArrayList<BusStop>();
		stops.add(stop);
		//For Testing whether entries are stored in ArrayList stops
		for (int i=0; i<stops.size(); i++){
			System.out.print(stops.get(i).getID() + " ");
			System.out.print(stops.get(i).getName() + " ");
			System.out.print(stops.get(i).getLocation() + " ");
			System.out.println("");
			}
		System.out.println("End of list");
		//end test
	}
}
