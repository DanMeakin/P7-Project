package main;

import java.util.ArrayList;
import java.util.List;

/**
 * The Walk class represents a (short) walk between bus stops.
 *
 * It exists to permit connections between otherwise unconnected stops.
 */
public class Walk extends Path {

  private static final int WALKING_SPEED = 5_000; // 5km/h walking speed

  public Walk(Stop walkStart, Stop walkEnd) {
    addStop(walkStart);
    addStop(walkEnd);
  }

  /**
   * Add stop to walk.
   *
   * This method overrides the superclass addStop method, to test whether there
   * are already two stops on the walk. If there are, no more stops can be
   * added.
   *
   * @param stop the stop to add
   */
  @Override
  public void addStop(Stop stop) throws IllegalArgumentException {
    if (getStops().size() >= 2) {
      String msg = "unable to add more than two stops to walk";
      throw new IllegalArgumentException(msg);
    }
    super.addStop(stop);
  }

  /**
  * Get inverted walk.
  *
  * This method will determine whether there is another walk which has the
  * same starting and ending points but in the opposite order.
  *
  * @return inverted walk (if it exists), or null if it does not
  */
  public Walk inverted() {
    for (Walk w : getAllWalks()) {
      if (w.getOrigin() == getDestination() &&
          w.getDestination() == getOrigin()) {
        return w;
      }
    }   
    return null;
  }

  /**
   * Get list of all existing walks.
   *
   * @return list of all current walks.
   */
  public static List<Walk> getAllWalks() {
    List<Walk> allWalks = new ArrayList<>();
    for (Path p : getAllPaths()) {
      if (p instanceof Walk) {
        allWalks.add((Walk) p);
      }
    }
    return allWalks;
  }
  public String getDescription() {
    return getStops().get(0).getName() + " - " + getStops().get(1).getName();
  }

  /**
   * Calculate the journey time between two stops.
   *
   * A walk only contains two stops, so this method can only be invoked using
   * the two stops on the walk in the correct order. isRushHour is ignored as
   * rush hour has no influence on walk journey time.
   *
   * @param origin the stop at which to begin journey
   * @param destination the stop at which to end journey
   * @param isRushHour calculate rush hour timing or not
   * @return time in minutes to travel from origin to destination
   */
  @Override
  public int journeyTimeBetweenStops(Stop origin, Stop destination, boolean isRushHour) throws IllegalArgumentException {
    if (compareStops(origin, destination) > 0) {
      String msg = "this path does not travel from " + origin + " to " + destination;
      throw new IllegalArgumentException(msg);
    }
    return walkingTime();
  }

  /**
   * Calculate the total walking time.
   *
   * This method simply divides distance between stops by the average walking
   * speed defined on Walk.
   *
   * @return walking time between stops in minutes
   */
  public int walkingTime() {
    return (int) getOrigin().distanceTo(getDestination()) / WALKING_SPEED * 60;
  }

  /**
   * Check if two Walk instances are to be considered the same.
   *
   * @param otherWalk the walk against which to compare.
   * @return true if both walks are equal, else false.
   */
  public boolean equals(Walk otherWalk) {
    return (otherWalk.getOrigin() == getOrigin() &&
            otherWalk.getDestination() == getDestination());
  }

  /**
   * Check if two Path instances are to be considered the same.
   *
   * @param otherPath the path against which to compare.
   * @return true if other path is a route instance and satisfies equals(Route)
   *  method are equal, else false. See {@link Walk#equals(Walk)}.
   */
  @Override
  public boolean equals(Path otherPath) {
    if (otherPath instanceof Walk) {
      return equals((Walk) otherPath);
    } else {
      return false;
    }
  }
}
