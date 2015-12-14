package main.routeplanner;

import main.Path;
import main.Route;
import main.RouteTimetable;
import main.Schedule;
import main.Stop;
import main.Walk;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

  public CostEstimator costEstimator;

  // Fields for the calculation of least time itinerary
  private List<Stop> openNodes;
  private List<Stop> closedNodes;
  private List<Path> usedPaths; // List stores paths already traversed
  private HashMap<Stop, Integer> gs;
  private HashMap<Stop, TArc> pres;

  /** 
   * The TArc inner class is used to represent one t-arc within an itinerary.
   *
   * The class is very minimal and is used simply to represent one t-arc used
   * within the route finding algorithm.
   */
  class TArc {
    
    private final Stop startNode;
    private final Stop endNode;
    private final Path service;
    private final int time;

    /**
     * TArc constructor.
     *
     * The constructor requires starting and ending stops/nodes, a service,
     * and the initial time. The ItineraryFinder supports walking between
     * stops; if a t-arc represents a walk, a null Route should be passed.
     */
    public TArc(Stop ni, Stop nj, Path si, int time) {
      this.startNode = ni;
      this.endNode = nj;
      this.service = si;
      this.time = time;
    }

    public String toString() {
      return "TArc(" + 
        getStartNode() + ", " + 
        getEndNode() + ", " + 
        getService() + ", " + 
        getTime() + ")";
    }
    
    @Override
    public boolean equals(Object o) {
      return (o instanceof TArc && equals((TArc) o));
    }

    public boolean equals(TArc otherTArc) {
      return (
          this != null && otherTArc != null &&
          getStartNode().equals(otherTArc.getStartNode()) &&
          getEndNode().equals(otherTArc.getEndNode()) &&
          getService().equals(otherTArc.getService()) &&
          getTime() == otherTArc.getTime()
          );
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
      if (getService() instanceof Walk) {
        return new JourneyLeg(
            (Walk) getService(),
            getTime()
            );
      } else {
        return new JourneyLeg(
            schedule.nextDepartureRouteTimetable(getTime(), getStartNode(), (Route) getService()),
            getStartNode(),
            getEndNode()
            );
      }
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
     *  service from startNode, minus initial time time; or
     *  walking time between nodes, plus the starting time
     *
     * @return value of pi for this t-arc
     */
    public int pi() {
      return arrivalTime() - getTime();
    }

    /**
     * Calculate departure time for this t-arc.
     *
     * If there is no next departure for this service, this method will return
     * the value of CostEstimator.UNCONNECTED to signal that this t-arc should
     * be ignored.
     *
     * @return departure time (in minutes since midnight) from startNode for 
     *         this t-arc
     */
    public int departureTime() {
      try {
        int nextDepartureTime = schedule.nextDepartureTime(
            getTime(), 
            getStartNode(), (Route) getService()
            );
        return nextDepartureTime;
      } catch (UnsupportedOperationException e) {
        // If this is caught, it means there is no next departure time info
        // available. This should return a very large value for the purpose
        // of route planning calculations.
        return CostEstimator.UNCONNECTED;
      }
    }

    /**
     * Calculate journey time between nodes for this t-arc.
     *
     * @return journey time (in minutes) from startNode to endNode
     */
    public int journeyTime() {
      try {
        boolean isRushHour = 
          getService() instanceof Route && 
          schedule.nextDepartureRouteTimetable(getTime(), getStartNode(), (Route) getService()).isRushHour();
        return getService().journeyTimeBetweenStops(getStartNode(), getEndNode(), isRushHour);
      } catch (UnsupportedOperationException e) {
        // If this is caught, it means there is no next departure time info
        // available. This should return a very large value for the purpose
        // of route planning calculations.
        return CostEstimator.UNCONNECTED;
      }
    }

    /**
     * Calculate arrival time for this t-arc.
     *
     * This will be the time of the next departure of the specified service
     * from the endNode of this t-arc, or the walking time for this t-arc,
     * plus the starting time.
     *
     * @return arrival time (in minutes since midnight) for this t-arc
     */
    public int arrivalTime() {
      if (getService() instanceof Walk) {
        return ((Walk) getService()).walkingTime() + getTime();
      } else {
        return departureTime() + journeyTime();
      }
    }

    public Stop getStartNode() {
      return startNode;
    }

    public Stop getEndNode() {
      return endNode;
    }

    public Path getService() {
      return service;
    }

    public int getTime() {
      return time;
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
    costEstimator = new CostEstimator(endingStop);
  }

  /**
   * Find the best itinerary for this journey.
   *
   * This method uses the calculateLeastTimePath private method to calculate
   * the least time path for the desired journey.
   *
   * @return the best itinerary for this journey, represented by a series of
   *         journey legs each representing a RouteTimetable or a walk from an 
   *         origin to a destination
   */
  public List<JourneyLeg> findBestItinerary() {
    return convertPathToItinerary(calculateLeastTimePath());
  }

  /**
   * Finds the k best itineraries for this journey.
   *
   * This method uses the calculateKLeastTimePaths private method to calculate
   * the k least time paths for the desired journey.
   * 
   * @return the k best itineraries for this journey, represented by a nested
   *         list of journey leg lists, each representing a RouteTimetable or
   *         a walk from an origin to a destination
   */ 
  public List<List<JourneyLeg>> findKBestItineraries(int k) {
    List<List<JourneyLeg>> bestItineraries = new ArrayList<>();
    List<List<TArc>> bestPaths = calculateKLeastTimePaths(k);
    for (List<TArc> path : bestPaths) {
      bestItineraries.add(convertPathToItinerary(path));
    }
    return bestItineraries;
  }

  /**
   * Converts a path to an itinerary.
   *
   * A path consists of a list of TArcs. An itinerary consists of a list of
   * JourneyLegs. This method converts the former to the latter.
   *
   * @param path a t-arc list representing a path
   * @return a list of journey legs representing an itinerary
   */
  private List<JourneyLeg> convertPathToItinerary(List<TArc> path) {
    List<JourneyLeg> itinerary = new ArrayList<>();
    for (TArc t : path) {
      itinerary.add(t.toJourneyLeg());
    }
    return itinerary;
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
    setTime(time.getHour() * 60 + time.getMinute());
  }

  /**
   * Set time field on ItineraryFinder.
   *
   * @param time the desired time (in minutes after midnight)
   */
  private void setTime(int time) {
    this.time = time;
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
   * Determine whether path has already been traversed.
   *
   * For the purposes of the itinerary finder, certain calculations are made
   * based on whether a path has already been taken. In particular, the 
   * algorithm assumes that a traveller will not be taking the same path or
   * its inverse more than once.
   *
   * @param p path to test whether already traversed
   * @return true if r has already been traversed, else false
   */
  public boolean pathAlreadyTraversed(Path p) {
    return (p != null && usedPaths.contains(p)); 
  }

  /**
   * Add path to list of already traversed paths.
   *
   * @param p path to add to list of already traversed paths.
   */
  private void setAlreadyTraversed(Path p) {
    usedPaths.add(p);
  }

  /**
   * Determine if the immediately previous leg was walked.
   *
   * @return true if immediately previous leg was walked, else false
   */
  public boolean walkedLastLeg() {
    return (usedPaths.size() > 0 && usedPaths.get(usedPaths.size()-1) instanceof Walk);
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
    Integer g = gs.get(ni);
    if (g == null) {
      return CostEstimator.UNCONNECTED;
    } else {
      return g;
    }
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
    usedPaths = new ArrayList<>();
    gs = new HashMap<>();
    pres = new HashMap<>();

    setOpenNode(getStartingStop());
    setGPrime(getStartingStop(), 0);
  }

  /**
   * Calculates the k least time paths for the desired route.
   *
   * This method augments the procedure in the calculateLeastTimePath method
   * by iteratively excluding individual t-arcs from the least time path to
   * obtain a second-, third-, ... k-th least time path.
   *
   * @param k number of paths to obtain
   * @return a nested list of a list of nodes each containing legs of the k-th
   *         least time path {@see ItineraryFinder#calculateLeastTimePath()}
   */
  private List<List<TArc>> calculateKLeastTimePaths(int k) {
    List<List<TArc>> leastTimePaths = new ArrayList<>();
    leastTimePaths.add(calculateLeastTimePath());
    while (leastTimePaths.size() < k) {
      List<TArc> previousLTP = leastTimePaths.get(leastTimePaths.size()-1);
      int previousLTPDeparture = previousLTP.get(0).departureTime();
      setTime(previousLTPDeparture + 1);
      leastTimePaths.add(calculateLeastTimePath());
    }
    return leastTimePaths;
  }

  /**
   * Calculate the Least Time path for the desired route.
   *
   * This implementation uses the pre-defined Stop and RouteTimetable classes
   * and attributes thereon.
   *
   * @return a list of nodes defining each point within an itinerary where the
   *         trip begins, ends or an exchange of buses occurs
   */
  private List<TArc> calculateLeastTimePath() {
    initializeFinder();
    Stop ni = null;
    while (!getOpenNodes().isEmpty()) {
      // Select open node with minimum value for f'(ni)
      ni = selectMinimumOpenNode();
      selectPathFromNode(ni);
    }
    return reconstructItinerary();
  }

private void selectPathFromNode(Stop ni) {
  // Set the route leading to this node to already traversed, and set ni
  // to the value of initial time plus the value of g'(ni)
  if (getPre(ni) != null) {
    setAlreadyTraversed(getPre(ni).getService());
  }
  int currentTi = getTime() + gPrime(ni); 

  // If this node is equal to the ending node, then we are finished and need
  // simply to reconstruct the itinerary from node list. Otherwise move node
  // from open to closed list, and carry out next step.
  changeOpenToClosed(ni);
  if (ni.equals(getEndingStop())) {
    return;
  } 

  // Get all paths from node ni to all other connected nodes
  for (Path p : Path.findPathsIncludingStop(ni)) {
    // Get all ni+ nodes - called ni2 here
    for (Stop ni2 : p.getStops()) {
      // Check that stop ni2 comes after stop ni - if not, this does not
      // represent a t-arc - there is no connection between s and ni on this
      // route.
      if (p.compareStops(ni, ni2) >= 0) {
        continue;
      }

      // Create t-arc and then skip this iteration if the second node is
      // closed (we don't return to closed nodes), if the route has already
      // been traversed, or its inverse (a passenger will not go back over
      // the same route twice), or if this t-arc is excluded from
      // consideration - this will be the case when trying to determine 
      // additional itinerary options.
      TArc tArc = new TArc(ni, ni2, p, currentTi);
      if (isClosed(ni2)) {
        continue;
      }
      if (p instanceof Walk && walkedLastLeg()) {
        continue;
      }
      if (pathAlreadyTraversed(p) || pathAlreadyTraversed(p.findInverted())) {
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
    TArc thisPre = getPre(getEndingStop());
    while (thisPre != null) {
      itinerary.add(0, thisPre);
      thisPre = getPre(thisPre.getStartNode());
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
    int min = 1_000_000_000; // Set initial min to high value
    Stop minNode = null;
    for (Stop s : getOpenNodes()) {
      if (fPrime(s) < min) {
        minNode = s;
        min = fPrime(s);
      }
    }
    return minNode;
  }
}
