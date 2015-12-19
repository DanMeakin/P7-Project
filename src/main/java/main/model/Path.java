package main.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Path abstract class represents a path between two or more stops.
 *
 * It is designed to be inherited by bus routes and by walking routes.
 */
public abstract class Path {

  public abstract Path findInverted();
  public abstract String getDescription();
  public abstract boolean equals(Path otherPath);
  public abstract int journeyTimeBetweenStops(Stop origin, Stop destination, boolean isRushHour);
  public abstract Stop getOrigin();
  public abstract Stop getDestination();
  public abstract List<Stop> getStops();

  public static List<Path> allPaths = new ArrayList<>();

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
   * Remove path from list of all paths.
   */
  public void remove() {
    allPaths.remove(this);
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
    return getStops().indexOf(stop);
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
