package main;
import java.util.*;

/**
 * The Stop class defines schedule type objects that hold characteristics
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
   * Calculate as-the-crow-flies distance between stops.
   *
   * This method uses the Haversine formula to calculate the distance between
   * two points on a sphere.
   * 
   * The Haversine formula calculates the distance between two points and is
   * as follows:-
   *
   *   d = 2r arcsin √{(sin²(Φ₂ - Φ₁) / 2) + cos(Φ₁) cos(Φ₂) (sin²(λ₂ - λ₁) / 2)}
   *
   * where:-
   *   d is the distance between the two points;
   *   r is the radius of the sphere;
   *   Φ₁ and Φ₂ are the latitudes of points 1 and 2;
   *   λ₁ and λ₂ are the longitudes of points 1 and 2.
   *
   * For the Earth, r = 6,371,000.
   *
   * Latitudes and longitudes are expressed in degrees, and must first be
   * converted to radians for use in this method.
   *
   * @return the distance between both stops to the nearest metre
   */
  public static long distanceBetweenStops(Stop s1, Stop s2) {
    int r = 6_371_000;
    double phi1 = s1.getLatitudeInRadians();
    double phi2 = s2.getLatitudeInRadians();
    double lambda1 = s1.getLongitudeInRadians();
    double lambda2 = s2.getLongitudeInRadians();

    double d = 2 * r * Math.asin(Math.sqrt(
          Math.pow(Math.sin((phi2 - phi1) / 2), 2) +
          Math.cos(phi1) * Math.cos(phi2) *
          Math.pow(Math.sin((lambda2 - lambda1) / 2), 2)
    ));

    return Math.round(d);
  }

  /** 
   * Calculate as-the-crow-flies distance to another stop.
   *
   * This method uses the .distanceBetweenStops method to calculate the
   * distance to another stop from the receiver of the call.
   *
   * See {@link Stop.distanceBetweenStops}.
   *
   * @param otherStop the stop to calculate the distance to
   * @return the distance to the other stop in metres
   */
  public long distanceTo(Stop otherStop) {
    return distanceBetweenStops(this, otherStop);
  }

  /**
   * List all existing stops.
   *
   * @return list of all Stops in system
   */
  public static List<Stop> getAllStops() {
    return allStops;
  }
  /**
   * Return the total number of Stops in existence.
   *
   * @return total number of Stops
   */
  public static int numberOfStops() {
    return getAllStops().size();
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

  public double getLatitude() {
    return latitude;
  }

  public double getLatitudeInRadians() {
    return getLatitude() * Math.PI / 180;
  }
  
  public double getLongitude() {
    return longitude;
  }

  public double getLongitudeInRadians() {
    return getLatitude() * Math.PI / 180;
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
