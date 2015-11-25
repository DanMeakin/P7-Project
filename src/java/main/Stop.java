package main;
import java.util.*;

public class Stop {
	private final int id;
	private final String name;
	private final double latitude;
  private final double longitude;
	private static List<Stop> allStops = new ArrayList<>();
	
	private Stop(int id, String name, double latitude, double longitude) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		allStops.add(this);
	}

	public static Stop createStop(int id, String name, double latitude, double longitude){
		for (int i = 0; i < allStops.size(); i++) {
			Stop currentStop = allStops.get(i);
			int currentID = currentStop.getID();
			if ( id == currentID) {
				throw new IllegalArgumentException("Stop with ID #" + id + " already exists");
			}
		}
		return new Stop(id, name, latitude, longitude);
	}

	public static void addStopToList(Stop stop){
		allStops.add(stop);
	}

	public int getID(){
		return id;
	}
	
	public String getName(){
		return name;
	}

	public double[] getLocation(){
		return new double[]{latitude, longitude};
	}

}
