package main;
import java.util.*;

/**
 * The Stop class defines schedule type objects that hold characteristicsv
 * of stops such as id, name latutude and longitude.
 * @authors Ivo Hendriks, Janus Avbæk Larsen, Helle Hyllested Larsen, Dan Meakin.
 */
public class Stop {
    /** the stop id  */
    private final int id;
    /** the stop name  */
    private final String name;
    /** the latitudal coordinate of the stop */
    private final double latitude;
    /** the longitudal coordinate of the stop */
    private final double longitude;
    /** a data structure containing all objects of type stop*/
    private static List<Stop> allStops = new ArrayList<>();

  /**
   * Creates a stop and add it to the stops list.
   *
   * @param id the stop id.
   * @param name the stop name.
   * @param latitude the latitudal coordinate of the stop.
   * @param longitude the longitudal coordinate of the stop.
   */
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
    if (stopExists(stop)) {
      String msg = "Stop with ID #" + stop.getID() + " already exists";
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
   * Check if two Stop instances are to be considered the same.
   *
   * @param otherStop the stop against which to compare
   * @return true if both stops are equal, else false
   */
  public boolean equals(Stop otherStop) {
    return (getID() == otherStop.getID());
  }

  /** 
   * Check if Stop already exists within the system.
   *
   * @param stop Stop object to check against
   * @return true if Stop already exists, else false.
   */
  private static boolean stopExists(Stop stop) {
    for (Stop s : allStops) {
      if (s.equals(stop)) {
        return true;
      }
    }
    return false;
  }

}
