package main.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Route class defines route type objects that hold
 * characteristics of the route routeNumber, routeDescription
 * and implements several domain specific methods such as
 * addStop, includesStop etc.
 */
public class Route extends Path {

  // the route number of this route
  private final String routeNumber;
  // the description of this route
  private final String routeDescription;
  // a data structure wherin the non-rush hour time between stops for this route are stored
  private List<Integer> timeBetweenStops = new ArrayList<Integer>();
  // a data structure wherin the rush hour time between stops for this route are stored
  private List<Integer> rushHourTimeBetweenStops = new ArrayList<Integer>();

  // a data structure in which all stops associated with this path are stored
  private List<Stop> stops = new ArrayList<Stop>();

  /**
   * Creates a route, add the first stop to list stops and create entries with value 0
   * in lists timeBetweenStops and rushHourTimeBetweenStops.
   *
   * @param routeNumber the route number associated with this route.
   * @param routeDescription the description of this route.
   * @param routeStart the stop where this route starts.
   */
  public Route(String routeNumber, String routeDescription, Stop routeStart) {
    addStop(routeStart, 0, 0);
    this.routeNumber = routeNumber;
    this.routeDescription = routeDescription;
    addPath(this); // Must complete by storing Route in list of all Paths
  }

  /**
   * Get list of all existing routes.
   *
   * @return list of all current routes
   */
  public static List<Route> getAllRoutes() {
    List<Route> allRoutes = new ArrayList<>();
    for (Path p : getAllPaths()) {
      if (p instanceof Route) {
        allRoutes.add((Route) p);
      }
    }
    return allRoutes;
  }

  /**
   * Gets a list if all routes matching number.
   *
   * @param number the route number to match against
   * @return list of all routes matching number
   */
  public static List<Route> findRouteByNumber(String number) {
    List<Route> routes = new ArrayList<>();
    for (Route r : getAllRoutes()) {
      if (r.getNumber().equals(number)) {
        routes.add(r);
      }
    }
    return routes;
  }

  /**
   * Get list of all routes including a given stop.
   *
   * @param s stop for which to test inclusion within routes
   * @return list of all routes including stop
   */
  public static List<Route> findRoutesIncludingStop(Stop s) {
    List<Route> routesIncludingStop = new ArrayList<>();
    for (Route r : getAllRoutes()) {
      if (r.includesStop(s)) {
        routesIncludingStop.add(r);
      }
    }
    return routesIncludingStop;
  }

  /**
   * Get inverted route.
   *
   * This method will determine whether there is another route which has the
   * same number and route description but in the opposite order.
   *
   * This assumes that a route description is named "start - end"
   *
   * @return inverted route (if it exists), or null if it does not
   */
  public Route findInverted() {
    String thisOrigin = getDescription().split(" - ")[0];
    String thisDestination = getDescription().split(" - ")[1];
    String reverseRouteName = thisDestination + " - " + thisOrigin;
    for (Route r : getAllRoutes()) {
      if (r.getNumber().equals(getNumber()) &&
          r.getDescription().equals(reverseRouteName)) {
        return r;
      }
    }
    return null;
  }

  /**
   * Add a stop to a route.
   *
   * @param stop the stop to add to the path
   * @param time the non-rush hour time between the last and this stop
   * @param rushHourTime the rush hour time between the last and this stop
   */
  public void addStop(Stop stop, int time, int rushHourTime) {
    stops.add(stop);
    timeBetweenStops.add(time);
    rushHourTimeBetweenStops.add(rushHourTime);
  }

  /**
   * Get origin stop.
   *
   * @return the stop at the beginning of the route
   */
  public Stop getOrigin() {
    return getStops().get(0);
  }

  /**
   * Get destination stop.
   *
   * @return the stop at the end of the route
   */
  public Stop getDestination() {
    return getStops().get(getStops().size()-1);
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
   * Get the route number for a route.
   *
   * @return routeNumber the route number for this route.
   */
  public String getNumber() {
    return routeNumber;
  }

  /**
   * Get the discription for a route.
   *
   * @return routeDescroption the route description for this route.
   */
  public String getDescription() {
    return routeDescription;
  }

  /**
   * Get the time between stops for a route.
   *
   * @param isRushHour set the stop times to be returned to rush hour.
   * @param isCumulative set the stop times to be returned to be cumulative.
   *
   * @return stopTiming a list of times between stops.
   */
  public List<Integer> getStopTiming(boolean isRushHour, boolean isCumulative) {
    List<Integer> stopTiming = new ArrayList<>();
    List<Integer> sourceTiming;

    if (isRushHour) {
      sourceTiming = rushHourTimeBetweenStops;
    } else {
      sourceTiming = timeBetweenStops;
    }

    int accumulator = 0;
    for (int i = 0; i < getStops().size(); i++) {
      int currentTiming = sourceTiming.get(i);
      if (isCumulative) {
        accumulator = accumulator + currentTiming;
        stopTiming.add(accumulator);
      } else {
        stopTiming.add(currentTiming);
      }
    }

    return stopTiming;
  }

  /**
   * Get the non-rush hour, non cumulative timings for a route.
   *
   * @return getStopTiming(false, false) the non-rush hour, non cumulative
   * result of getStopTiming, a list of non-rush hour,
   * non cumulative timings for a route.
   */
  public List<Integer> getNonRushHourTiming() {
    return getStopTiming(false, false);
  }

  /**
   * Get the rush hour, non cumulative timings for a route.
   *
   * @return getStopTiming(false, false) the non-rush hour, non cumulative
   * result of getStopTiming, a list of rush hour,
   * non cumulative timings for a route.
   */
  public List<Integer> getRushHourTiming() {
    return getStopTiming(true, false);
  }

  /**
   * Get the non-rush hour, cumulative timings for a route.
   *
   * @return getStopTiming(false, false) the non-rush hour, cumulative
   * result of getStopTiming, a list of non-rush hour,
   * cumulative timings for a route.
   */
  public List<Integer> getCumulativeNonRushHourTiming() {
    return getStopTiming(false, true);
  }

  /**
   * Get the rush hour, cumulative timings for a route.
   *
   * @return getStopTiming(false, false) the rush hour, cumulative
   * result of getStopTiming, a list of rush hour,
   * cumulative timings for a route.
   */
  public List<Integer> getCumulativeRushHourTiming() {
    return getStopTiming(true, true);
  }

  /**
   * Calculate the journey time between two stops.
   *
   * @param origin the stop at which to begin journey
   * @param destination the stop at which to end journey
   * @param isRushHour calculate rush hour timing or not
   * @return time in minutes to travel from origin to destination
   */
  public int journeyTimeBetweenStops(Stop origin, Stop destination, boolean isRushHour) throws IllegalArgumentException {
    if (compareStops(origin, destination) > 0) {
      String msg = "this path does not travel from " + origin + " to " + destination;
      throw new IllegalArgumentException(msg);
    }
    int originIndex = stopIndex(origin);
    int destinationIndex = stopIndex(destination);
    return getStopTiming(isRushHour, true).get(destinationIndex) - getStopTiming(isRushHour, true).get(originIndex);
  }

  /**
   * Check if two Route instances are to be considered the same.
   *
   * @param otherRoute the route against which to compare.
   * @return true if both routes are equal, else false.
   */
  public boolean equals(Route otherRoute) {
    return (getNumber() == otherRoute.getNumber() &&
            getDescription() == otherRoute.getDescription());
  }

  /**
   * Check if two Path instances are to be considered the same.
   *
   * @param otherPath the path against which to compare.
   * @return true if other path is a route instance and satisfies equals(Route)
   *  method are equal, else false. See {@link Route#equals(Route)}.
   */
  @Override
  public boolean equals(Path otherPath) {
    if (otherPath instanceof Route) {
      return equals((Route) otherPath);
    } else {
      return false;
    }
  }

}

