package main;
import java.util.*;

public class Stop {
	private final int id;
	private final String name;
	private final double latitude;
  private final double longitude;
	private static List<Stop> allStops = new ArrayList<>();
	
	public Stop(int id, String name, double latitude, double longitude) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		addStop(this);
	}

  /**
   * Add a Stop to the list of all stops.
   *
   * @param stop The stop object to add to the list
   */
	public static void addStop(Stop stop) throws IllegalArgumentException {
    if (stopExists(stop.id)) {
      String msg = "Stop with ID #" + stop.id + " already exists";
      throw new IllegalArgumentException(msg);
    }
		allStops.add(stop);
	}

  /**
   * Remove a Stop from the list of all stops.
   *
   * @param stop The stop object to remove
   */
  public static void removeStop(Stop stop) {
    allStops.remove(stop);
  }

  /**
   * Return the total number of Stops in existence.
   *
   * @return total number of Stops
   */
  public static int numberOfStops() {
    return allStops.size();
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

  /** 
   * Check if Stop with passed ID exists.
   *
   * @param id ID number to check against
   * @return true if Stop with id already exsits, else false.
   */
  private static boolean stopExists(int id) {
    for (Stop s : allStops) {
      if (s.id == id) {
        return true;
      }
    }
    return false;
  }
}
