package main;

import java.util.ArrayList;
import java.util.List;

/**
 * The Path abstract class represents a path between two or more stops.
 *
 * It is designed to be inherited by bus routes and by walking routes.
 */
public abstract class Path {

  public abstract Path inverted();
  public abstract String getDescription();
  public abstract boolean equals(Path otherPath);
  public abstract int journeyTimeBetweenStops(Stop origin, Stop destination, boolean isRushHour);

  public static List<Path> allPaths = new ArrayList<>(); 

	// a data structure wherin all stops associated with this path are stored
	private List<Stop> stops = new ArrayList<Stop>();

  public Path() {
    addPath(this);
  }

  /**
   * Add path to list of all paths.
   *
   * @param p path to add to list
   */
  public static void addPath(Path p) throws IllegalArgumentException {
    if (exists(p)) {
      String msg = "Path " + p.getDescription() + " already exists";
      throw new IllegalArgumentException(msg);
    }
    allPaths.add(p);
  }

  /**
   * Remove a path from the list of all paths.
   *
   * @param p the path object to remove
   */
  public static void removePath(Path p) {
    allPaths.remove(p);
  }

  /**
   * Get list of all paths.
   *
   * @return list of all paths within system
   */
  public static List<Path> getAllPaths() {
    return allPaths;
  }

  /**
   * Get list of all paths including a given stop.
   *
   * @param s stop for which to test inclusion within paths
   * @return list of all paths including stop
   */
  public static List<Path> findPathsIncludingStop(Stop s) {
    List<Path> pathsIncludingStop = new ArrayList<>();
    for (Path p : getAllPaths()) {
      if (p.includesStop(s)) {
        pathsIncludingStop.add(p);
      }
    }
    return pathsIncludingStop;
  }
  
	/**
	 * Get the stops associated with a path.
	 *
	 * @return stops the list of stops associated with this path.
	 */
	public List<Stop> getStops() {
		return stops;
	}

  /**
   * Get origin stop.
   *
   * @return first stop on path
   */
  public Stop getOrigin() {
    return getStops().get(0);
  }

  /**
   * Get destination stop.
   *
   * @return last stop on path
   */
  public Stop getDestination() {
    return getStops().get(getStops().size()-1);
  }

	/**
	 * Add a stop to a path.
	 *
	 * @param stop the stop to add to the path
	 * @param time the non-rush hour time between the last and this stop
	 * @param rushHourTime the rush hour time between the last and this stop
	 */
	public void addStop(Stop stop) {
		stops.add(stop);
	}

  /**
   * Compare stops to determine which of two stops comes first in path.
   *
   * Throws IllegalArgumentException if a Stop is passed which is not on this
   * path.
   *
   * @param firstStop the first stop to compare
   * @param secondStop the second stop to compare
   * @return -1 if firstStop is before secondStop, 1 if secondStop is before 
   *  firstStop, or 0 if both stops are the same stop
   */
  public int compareStops(Stop firstStop, Stop secondStop) throws IllegalArgumentException {
    // Throw exception unless both Stops are within Path
    if (!(includesStop(firstStop) && includesStop(secondStop))) {
      Stop missingStop = !includesStop(firstStop) ? firstStop : secondStop;
      String msg = "stop " + missingStop + " is not on path";
      throw new IllegalArgumentException(msg);
    }

    // Compare indices of both Stops
    int firstStopIndex = stopIndex(firstStop);
    int secondStopIndex = stopIndex(secondStop);

    if (firstStopIndex > secondStopIndex) {
      return 1;
    } else if (firstStopIndex < secondStopIndex) {
      return -1;
    } else {
      return 0;
    }
  }

	/**
	 * Check whether a stop is included in a path.
	 *
	 * @param stop the stop that checked on inclusion.
	 *
	 * @return true if the stop is included in a path, else return false.
	 */
	public boolean includesStop(Stop stop){
		for (int i = 0; i < getStops().size(); i++) {
			Stop thisStop = getStops().get(i);
			if(stop == thisStop){
				return true;
			}
		}
		return false;
	}

  /**
   * Find the index of a Stop's location within path.
   *
   * Starting from 0, this method determines the Stop's location on a path:
   * 0 is the first stop, 1 is the second stop, etc.
   * 
   * @param stop the Stop for which to get index
   * @return index of the desired stop
   */
  public int stopIndex(Stop stop) {
    return stops.indexOf(stop);
  }

  /** 
   * Check if path already exists within the system.
   *
   * @param p Path object to check against
   * @return true if Path already exists, else false.
   */
  private static boolean exists(Path p) {
    for (Path thisPath : getAllPaths()) {
      if (p.equals(thisPath)) {
        return true;
      }
    }
    return false;
  }

}
