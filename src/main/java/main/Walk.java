package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Walk class represents a (short) walk between bus stops.
 *
 * It exists to permit connections between otherwise unconnected stops.
 */
public class Walk extends Path {

  private static final int WALKING_SPEED = 5_000; // 5km/h walking speed
  private static final int WALKING_DISTANCE = 500; // 500m walking distance

  private final Stop walkStart;
  private final Stop walkEnd;

  /**
   * Get WALKING_SPEED value.
   *
   * @return value of WALKING_SPEED constant in metres per hour
   */
  public static int getWalkingSpeed() {
    return WALKING_SPEED;
  }

  /**
   * Get WALKING_DISTANCE value.
   *
   * The WALKING_DISTANCE value is used in determining whether to create a new
   * Walk when a new Stop is created. New Walks between Stops within walking
   * distance of one another will be generated upon creation of the new Stop.
   *
   * @return value of WALKING_DISTANCE constant in metres
   */
  public static int getWalkingDistance() {
    return WALKING_DISTANCE;
  }

  /**
   * Creates a Walk instance.
   *
   * @param walkStart the stop from which this walk begins
   * @param walkEnd   the stop at which this walk ends
   */
  public Walk(Stop walkStart, Stop walkEnd) {
    this.walkStart = walkStart;
    this.walkEnd = walkEnd;
    addPath(this); // Must complete by storing Walk in list of all Paths
  }

  /**
   * Signals to the Walk class that a new stop has been created, and creates
   * new Walks if appropriate.
   *
   * Walk instances should be created when a new Stop is created if this stop
   * is within walking distance of another stop. This method carries out
   * the creation of these Walks if required.
   *
   * @param newStop the newly created Stop
   */
  public static void stopAdded(Stop newStop) {
    for (Stop s : Stop.getAllStops()) {
      if (!newStop.equals(s) && newStop.distanceTo(s) <= getWalkingDistance()) {
        new Walk(newStop, s);
        new Walk(s, newStop);
      }
    }
  }

  /**
   * Get value of walkStart.
   *
   * @return stop representing the starting point of the walk
   */
  public Stop getOrigin() {
    return walkStart;
  }

  /**
   * Get value of walkEnd.
   *
   * @return stop representing the end point of the walk
   */
  public Stop getDestination() {
    return walkEnd;
  }

  /**
   * Gets list of all Stops on walk.
   *
   * As a walk consists only of an origin stop and a destination stop, this
   * list will always contain exactly two entries.
   *
   * @return list of all stops on walk
   */
  public List<Stop> getStops() {
    return Arrays.asList(getOrigin(), getDestination());
  }

  /**
  * Get inverted walk.
  *
  * This method will determine whether there is another walk which has the
  * same starting and ending points but in the opposite order.
  *
  * @return inverted walk (if it exists), or null if it does not
  */
  @Override
  public Walk findInverted() {
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
    return getOrigin().getName() + " - " + getDestination().getName();
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
