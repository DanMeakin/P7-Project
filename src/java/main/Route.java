package main;

import java.util.ArrayList;

public class Route {
	private int number;
	private String description;
	private ArrayList<BusStop> stops;
	
	public Route(int number, String description){
		stops = new ArrayList<BusStop>();
		this.number = number;
		this.description = description;
	}
	
	public void addStop(BusStop stop){
		stops.add(stop);
	}
	
	public void removeStop(BusStop stop){
		stops.remove(stop);
	}
}
