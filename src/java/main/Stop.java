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
