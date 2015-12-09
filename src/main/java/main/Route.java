package main;

import java.util.ArrayList;
import java.util.List;

/**
 * The Route class defines route type objects that hold
 * characteristics of the route routeNumber, routeDescription
 * and implements several domain specific methods such as
 * addStop, includesStop etc.
 * @authors Ivo Hendriks, Janus Avbæk Larsen, Helle Hyllested Larsen, Dan Meakin.
 */
public class Route {

  // list of all existing Routes
  private static List<Route> allRoutes;

	// the route number of this route
	private final String routeNumber;
	// the description of this route
	private final String routeDescription;
	// a data structure wherin all stops associated with this route are stored
	private List<Stop> stops = new ArrayList<Stop>();
	// a data structure wherin the non-rush hour time between stops for this route are stored
	private List<Integer> timeBetweenStops = new ArrayList<Integer>();
	// a data structure wherin the rush hour time between stops for this route are stored
	private List<Integer> rushHourTimeBetweenStops = new ArrayList<Integer>();

	/**
	 * Creates a route, add the first stop to list stops and create entries with value 0
	 * in lists timeBetweenStops and rushHourTimeBetweenStops.
	 *
	 * @param routeNumber the route number associated with this route.
	 * @param routeDescription the description of this route.
	 * @param routeStart the stop where this route starts.
	 */
	public Route(String routeNumber, String routeDescription, Stop routeStart) {
		stops.add(routeStart);
		timeBetweenStops.add(0);
		rushHourTimeBetweenStops.add(0);
		this.routeNumber = routeNumber;
		this.routeDescription = routeDescription;
	}

  /**
   * Add a Route to the list of all routes.
   *
   * @param route the route to add to the list
   */
  private static void addRoute(Route route) throws IllegalArgumentException {
    if (routeExists(route)) {
      String msg = "Route " + route.getNumber() + " " + route.getDescription() + " already exists";
      throw new IllegalArgumentException(msg);
    }
    allRoutes.add(route);
  }

  /**
   * Remove a route from the list of all routes.
   *
   * @param route the route object to remove
   */
  public static void removeRoute(Route route) {
    allRoutes.remove(route);
  }

  /**
   * Get list of all existing routes.
   *
   * @return list of all current routes
   */
  public static List<Route> getAllRoutes() {
    return allRoutes;
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
   * This assumes that a route description is named <start> - <end>.
   *
   * @return inverted route (if it exists), or null if it does not
   */
  public Route invertedRoute() {
    String thisOrigin = getDescription().split(" - ")[0];
    String thisDestination = getDescription().split(" - ")[1];
    String reverseRouteName = thisDestination + " - " + thisOrigin;
    for (Route r : getAllRoutes()) {
      if (r.getNumber() == getNumber() && 
          r.getDescription() == reverseRouteName) {
        return r;
      }
    }
    return null;
  }

	/**
	 * Add a stop to a route.
	 *
	 * @param stop the stop to add to the route
	 * @param time the non-rush hour time between the last and this stop
	 * @param rushHourTime the rush hour time between the last and this stop
	 */
	public void addStop(Stop stop, int time, int rushHourTime) {
		stops.add(stop);
		timeBetweenStops.add(time);
		rushHourTimeBetweenStops.add(rushHourTime);
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
	 * Get the stops associated with a route.
	 *
	 * @return stops the list of stops associated with this route.
	 */
	public List<Stop> getStops() {
		return stops;
	}

	//public HashMap<Stop, Integer> viewStopTimes(){
	//	return timeBetweenStops;
	//}
	//public HashMap<Stop, Integer> viewRushHourStopTimes(){
	//	return rushHourTimeBetweenStops;
	//}

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
		for (int i = 0; i < stops.size(); i++) {
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
	 * Check wether a stop is included in a route.
	 *
	 * @param stop the stop that checked on inclusion.
	 *
	 * @return true if the stop is included in a route, else return false.
	 */
	public boolean includesStop(Stop stop){
		for (int i = 0; i < stops.size(); i++) {
			Stop thisStop = stops.get(i);
			if(stop == thisStop){
				return true;
			}
		}
		return false;
	}

  /**
   * Compare stops to determine which of two stops comes first in route.
   *
   * Throws IllegalArgumentException if a Stop is passed which is not on this
   * route.
   *
   * @param firstStop the first stop to compare
   * @param secondStop the second stop to compare
   * @return -1 if firstStop is before secondStop, 1 if secondStop is before 
   *  firstStop, or 0 if both stops are the same stop
   */
  public int compareStops(Stop firstStop, Stop secondStop) throws IllegalArgumentException {
    // Throw exception unless both Stops are within Route
    if (!(includesStop(firstStop) && includesStop(secondStop))) {
      Stop missingStop = !includesStop(firstStop) ? firstStop : secondStop;
      String msg = "stop " + missingStop + " is not on Route";
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
   * Calculate the journey time between two stops.
   *
   * @param origin the stop at which to begin journey
   * @param destination the stop at which to end journey
   * @param isRushHour calculate rush hour timing or not
   * @return time in minutes to travel from origin to destination
   */
  public int journeyTimeBetweenStops(Stop origin, Stop destination, boolean isRushHour) throws IllegalArgumentException {
    if (compareStops(origin, destination) > 0) {
      String msg = "this route does not travel from " + origin + " to " + destination;
      throw new IllegalArgumentException(msg);
    }
    int originIndex = stopIndex(origin);
    int destinationIndex = stopIndex(destination);
    return getStopTiming(isRushHour, true).get(destinationIndex) - getStopTiming(isRushHour, true).get(originIndex);
  }

  /**
   * Find the index of a Stop's location within route.
   *
   * Starting from 0, this method determines the Stop's location on a route:
   * 0 is the first stop, 1 is the second stop, etc.
   * 
   * @param stop the Stop for which to get index
   * @return index of the desired stop
   */
  private int stopIndex(Stop stop) {
    return stops.indexOf(stop);
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
   * Check if Route already exists within the system.
   *
   * @param route Route object to check against
   * @return true if Route already exists, else false.
   */
  private static boolean routeExists(Route route) {
    for (Route r : allRoutes) {
      if (r.equals(route)) {
        return true;
      }
    }
    return false;
  }
}

