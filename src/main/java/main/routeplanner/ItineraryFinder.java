package main.routeplanner;

import main.Route;
import main.RouteTimetable;
import main.Schedule;
import main.Stop;

import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * This class is used to find an appropriate route and bus to take between
 * two points.
 *
 * The ItineraryFinder uses a starting and ending bus stop and determines which
 * routes travel between these points. It then finds RouteTimetables for the
 * desired time and returns buses which can be taken between the desired stops.
 */
public class ItineraryFinder {

  private Stop startingStop;
  private Stop endingStop;
  private Schedule schedule;
  private LocalDate date;
  private int time;

  private CostEstimator costEstimator = new CostEstimator(endingStop);

  // Fields for the calculation of least time itinerary
  private List<Stop> openNodes;
  private List<Stop> closedNodes;
  private List<Route> usedRoutes; // List stores routes already traversed
  private HashMap<Stop, Integer> gs;
  private HashMap<Stop, TArc> pres;

  private List<TArc> excludedTArcs; // List of excluded t-arcs

  /** 
   * The TArc inner class is used to represent one t-arc within an itinerary.
   *
   * The class is very minimal and is used simply to represent one t-arc used
   * within the route finding algorithm.
   */
  private class TArc {
    
    private Stop startNode;
    private Stop endNode;
    private Route service;
    private int time;

    public TArc(Stop ni, Stop nj, Route si, int time) {
      setStartNode(ni);
      setEndNode(ni);
      setService(si);
      setTime(time);
    }

    /**
     * Convert t-arc into JourneyLeg class.
     *
     * This method converts a t-arc instance into a journey leg instance.
     * The t-arc class uses some methods and representations peculiar to the
     * graph-based nature of the algorithms used in the itinerary finder. The
     * JourneyLeg class uses more familiar objects and representations for the
     * overall itinerary. T-arcs should be converted to JourneyLegs where used
     * outside of the itinerary finder.
     *
     * @return journey leg representation of t-arc
     */
    public JourneyLeg toJourneyLeg() {
      return new JourneyLeg(
          schedule.nextDepartureRouteTimetable(getTime(), getStartNode(), getService()),
          getStartNode(),
          getEndingStop()
          );
    }

    /**
     * Calculate pi value for this t-arc.
     *
     * The pi value is calculated with reference to the time of arrival of the
     * next departing vehicle at the ending stop.
     *
     * The formula to calculate pi is (in words):-
     *
     *  arrival time at endNode using the first departing vehicle on route
     *  service from startNode, minus initial time time.
     *
     * @return value of pi for this t-arc
     */
    public int pi() {
      int arrivalTime = schedule.nextDepartureTime(getTime(), getEndNode(), getService());
      return arrivalTime - getTime();
    }

    public Stop getStartNode() {
      return startNode;
    }

    private void setStartNode(Stop n) {
      startNode = n;
    }

    public Stop getEndNode() {
      return endNode;
    }

    private void setEndNode(Stop n) {
      endNode = n;
    }

    public Route getService() {
      return service;
    }

    private void setService(Route s) {
      service = s;
    }

    public int getTime() {
      return time;
    }

    private void setTime(int t) {
      time = t;
    }

  }

  /** 
   * Creates an instance of ItineraryFinder.
   *
   * The ItineraryFinder is used to calculate optimal routes between two specified
   * bus stops. It is not designed to be used with arbitrary locations, and is
   * not capable of calculating routes which involve any element of non-bus
   * transportation, i.e. walking between stops, using the train, etc.
   */
  public ItineraryFinder(Stop startingStop, Stop endingStop, LocalDateTime searchTime) {
    setStartingStop(startingStop);
    setEndingStop(endingStop);
    setDate(searchTime);
    setTime(searchTime);
    setSchedule();
    initializeFinder();
  }

  /**
   * Find all paths between startingStop and endingStop.
   *
   * This method searches for all possible paths (i.e. journeys involving one
   * or multiple bus routes) between the startingStop and the endingStop.
   *
   * @return a nested list, each entry containing a list of routes representing
   *  a path between startingStop and endingStop
   */
  public List<List<Route>> findPaths() {
    return null;
  }

  /**
   * Determine all timed paths between startingStop and endingStop.
   *
   * This method uses the paths found in the findPaths method to determine
   * the timings of all journeys which cover the desired route.
   *
   * @return a nested list, each entry containing a list of RouteTimetables 
   *  which will cover the desired journey between startingStop and endingStop
   */
  public List<List<RouteTimetable>> findTimedPaths() {
    return null;
  }

  /**
   * Get startingStop object.
   *
   * @return the stop from which journey is to begin
   */
  public Stop getStartingStop() {
    return startingStop;
  }

  /**
   * Set startingStop.
   *
   * This method sets the value of the stop from which the journey is to begin.
   *
   * @param startingStop the Stop instance to set as the startingStop
   */
  private void setStartingStop(Stop startingStop) {
    this.startingStop = startingStop;
  }

  /**
   * Get endingStop object.
   *
   * @return the stop at which journey is to end
   */
  public Stop getEndingStop() {
    return endingStop;
  }

 /**
   * Set endingStop.
   *
   * This method sets the value of the stop at which the journey is to end.
   *
   * @param endingStop the Stop instance to set as the endingStop
   */
  private void setEndingStop(Stop endingStop) {
    this.endingStop = endingStop;
  }

  /**
   * Get time value.
   *
   * This method returns an int representing the number of minuts since
   * midnight as this is the way in which time is stored within ItineraryFinder
   * for ease of calculation.
   * 
   * @return time for which to determine suitable journeys (in minutes since
   *  midnight)
   */
  public int getTime() {
    return time;
  }

  /**
   * Set time field on ItineraryFinder.
   *
   * As time is simply stored as an integer representing the number of minutes
   * since midnight, this method accepts a LocalDateTime object and converts
   * the time element only.
   *
   * @param time the desired time from which to search for journeys
   */
  private void setTime(LocalDateTime time) {
    this.time = time.getHour() * 60 + time.getMinute();
  }

  /**
   * Get date value.
   *
   * @return date for which to determine suitable journeys, in conjunction with
   *  the value of time
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Set date field on ItineraryFinder.
   *
   * Converts a LocalDateTime object to a LocalDate and saved this into the
   * date field on a ItineraryFinder instance.
   *
   * @param date the date on which to search for journeys
   */
  private void setDate(LocalDateTime date) {
    this.date = date.toLocalDate();
  }

  /**
   * Get list of open nodes.
   *
   * @return list of open nodes
   */
  public List<Stop> getOpenNodes() {
    return openNodes;
  }

  /**
   * Change state of new node to open.
   *
   * This method simply adds a node/stop to the list of open nodes.
   *
   * @param n node to add to list of open stops
   */
  private void setOpenNode(Stop n) {
    openNodes.add(n);
  }

  /**
   * Determine whether node n is open.
   *
   * @param n node to test whether open
   * @return true if open, else false
   */
  public boolean isOpen(Stop n) {
    return getOpenNodes().contains(n);
  }

  /**
   * Get list of closed nodes.
   *
   * @return list of closed nodes
   */
  public List<Stop> getClosedNodes() {
    return closedNodes;
  }

  /**
   * change state of open node to closed.
   *
   * This method moves a node/stop from the list of open nodes to the list
   * of closed nodes.
   *
   * @param n node to move from open to closed list
   */
  private void changeOpenToClosed(Stop n) throws IllegalArgumentException {
    if (!openNodes.contains(n)) {
      String msg = "node " + n + " is not an open node";
      throw new IllegalArgumentException(msg);
    }
    openNodes.remove(n);
    closedNodes.add(n);
  }

  /**
   * Determine whether node n is closed.
   *
   * @param n node to test whether closed
   * @return true if closed, else false
   */
  public boolean isClosed(Stop n) {
    return getClosedNodes().contains(n);
  }

  /**
   * Determine whether node n is new.
   *
   * A node is new if it is on neither the open nor the closed list.
   *
   * @param n node to test whether new
   * @return true if new, else false
   */
  public boolean isNew(Stop n) {
    return !(isOpen(n) || isClosed(n));
  }

  /**
   * Set value of pre(n).
   *
   * @param n the node for which to set value of pre
   * @param t the t-arc to store in value of pre
   */
  private void setPre(Stop n, TArc t) {
    pres.put(n, t);
  }

  /**
   * Get value of pre(n).
   *
   * @param n the node for which to get value of pre
   * @return t-arc value of pre(n)
   */
  public TArc getPre(Stop n) {
    return pres.get(n);
  }

  /**
   * Determine whether route has already been traversed.
   *
   * For the purposes of the itinerary finder, certain calculations are made
   * based on whether a route has already been taken. In particular, the 
   * algorithm assumes that a traveller will not be taking the same route or
   * its inverse more than once.
   *
   * @param r route to test whether already traversed
   * @return true if r has already been traversed, else false
   */
  public boolean routeAlreadyTraversed(Route r) {
    return (r != null && usedRoutes.contains(r)); 
  }

  /**
   * Add route to list of already traversed routes.
   *
   * @param r route to add to list of already traversed routes.
   */
  private void setAlreadyTraversed(Route r) {
    usedRoutes.add(r);
  }

  /**
   * Find and set schedule based on instance's date.
   */
  private void setSchedule() {
    this.schedule = Schedule.findSchedule(Date.from(this.date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
  }

  /**
   * Get value of f'(ni).
   *
   * @param ni node for which to get value of f'
   */
  public int fPrime(Stop ni) {
    return gPrime(ni) + hPrime(ni);
  }

  /**
   * Get value of g'(ni).
   *
   * @param ni node for which to get value of g'
   */
  public int gPrime(Stop ni) {
    return gs.get(ni);
  }

  /**
   * Set value of g'(ni).
   *
   * @param ni node for which to set value of g'
   * @param value value to set for g'
   */
  private void setGPrime(Stop ni, int value) {
    gs.put(ni, value);
  }

  /**
   * Get value of h'(ni).
   *
   * @param ni node for which to get value of h'
   */
  public int hPrime(Stop ni) {
    return costEstimator.hPrime(ni);
  }

  /**
   * Initialize the itinerary finder.
   *
   * A number of variables must be initialized, and some pre-calculations
   * undertaken before the itinerary finder can be used to calculate optimal
   * routes. This method ensures these actions are undertaken when called.
   */
  private void initializeFinder() {
    openNodes = new ArrayList<>();
    closedNodes = new ArrayList<>();
    usedRoutes = new ArrayList<>();
    gs = new HashMap<>();
    pres = new HashMap<>();
    excludedTArcs = new ArrayList<>();

    setOpenNode(getStartingStop());
    setGPrime(getStartingStop(), 0);
  }

  /**
   * Calculate the Least Time path for the desired route.
   *
   * This method is based on the algorithm contained within the paper
   * Yu, Zhang, Jiafu, Tang, Shimeng, Lv, Xinggang, Luo. (2014). Floyd-A∗ 
   * Algorithm Solving the Least-Time Itinerary Planning Problem in Urban 
   * Scheduled Public Transport Network. Mathematical Problems in Engineering,
   * 2014. http://dx.doi.org/10.1155/2014/185383.
   *
   * This implementation uses the pre-defined Stop and RouteTimetable classes
   * and attributes thereon.
   *
   * @return a list of nodes defining each point within an itinerary where the
   *  trip begins, ends or an exchange of buses occurs
   */
  private List<TArc> calculateLeastTimePath() {
    while (!getOpenNodes().isEmpty()) {
      // Select open node with minimum value for f'(ni)
      Stop ni = selectMinimumOpenNode();

      // Set the route leading to this node to already traversed, and set ni
      // to the value of initial time plus the value of g'(ni)
      setAlreadyTraversed(getPre(ni).getService());
      int currentTi = getTime() + gPrime(ni); 

      // If this node is equal to the ending node, then we are finished and need
      // simply to reconstruct the itinerary from node list. Otherwise move node
      // from open to closed list, and carry out next step.
      if (ni == getEndingStop()) {
        return reconstructItinerary();
      } else {
        changeOpenToClosed(ni);
      }

      // Get all paths from node ni to all other connected nodes
      for (Route r : Route.findRoutesIncludingStop(ni)) {
        // Get all ni+ nodes - called ni2 here
        for (Stop ni2 : r.getStops()) {
          // Check that stop s does not come before ni - if so, this does not
          // represent a t-arc - there is no connection between s and ni on this
          // route.
          if (r.compareStops(ni, ni2) > 0) {
            continue;
          }

          // Create t-arc and then skip this iteration if the second node is
          // closed (we don't return to closed nodes), if the route has already
          // been traversed, or its inverse (a passenger will not go back over
          // the same route twice), or if this t-arc is excluded from
          // consideration - this will be the case when trying to determine 
          // additional itinerary options.
          TArc tArc = new TArc(ni, ni2, r, currentTi);
          if (isClosed(ni2)) {
            continue;
          }
          if (routeAlreadyTraversed(r) || routeAlreadyTraversed(r.invertedRoute())) {
            continue;
          }
          if (excludedTArc(tArc)) {
            continue;
          }

          int pi = tArc.pi(); // Calculate pi value
          if (gPrime(ni) + pi >= gPrime(ni2)) {
            continue;
          } else if (isNew(ni2)) {
            setOpenNode(ni2);
          }
          setGPrime(ni2, gPrime(ni) + pi);
          setPre(ni2, tArc);
        }
      }
    }
    return null; // This should only be hit if there is no itinerary between stops
  }

  /**
   * Reconstruct least time itinerary.
   *
   * This method reconstructs the least time itinerary by looping through
   * pre(ni) where ni = nd, nd-, ..., no+.
   *
   * @return least time itinerary for trip between origin and destination stops
   */
  private List<TArc> reconstructItinerary() {
    List<TArc> itinerary = new ArrayList<>();
    for (TArc thisPre = getPre(getEndingStop()); thisPre != null ;) {
      itinerary.add(0, thisPre);
    }
    return itinerary;
  }

  /**
   * Determine and select the open node with minimum value of f'(n).
   *
   * This method forms part of the calculateLeastTimePath method, and is used
   * to select the most promising node at the beginning of each iteration.
   *
   * @return the open node with minimum f'(n) value
   */
  private Stop selectMinimumOpenNode() {
    int min = 1_000_000; // Set initial min to high value
    Stop minNode = null;
    for (Stop s : getOpenNodes()) {
      if (fPrime(s) < min) {
        minNode = s;
        min = fPrime(s);
      }
    }
    return minNode;
  }

  /**
   * Flag whether t-arc is excluded from consideration in itinerary finder.
   *
   * A t-arc can be excluded for a number of reasons. Principally it will be
   * excluded where a number of different itinerary options are desired, as
   * excluding, e.g. the last leg of a journey will open up other options to
   * the itinerary planner.
   *
   * @param tArc the t-arc to test for exclusion
   * @return true if t is excluded from consideration, else false
   */
  private boolean excludedTArc(TArc tArc) {
    return excludedTArcs.contains(tArc);
  }

  /**
   * Add t-arc to exclusion list.
   *
   * @param tArc the t-arc to add to exclusion list
   */
  private void excludeTArc(TArc tArc) {
    excludedTArcs.add(tArc);
  }
}
