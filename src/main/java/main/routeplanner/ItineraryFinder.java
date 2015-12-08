package main.routeplanner;

import main.RouteTimetable;
import main.Schedule;
import main.Stop;

import java.time.*;
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

  private CostEstimator costEstimator = new CostEstimator();

  // Fields for the calculation of least time itinerary
  private List<Stop> open;
  private List<Stop> closed;
  private HashMap<Stop, Integer> fs;
  private HashMap<Stop, Integer> gs;
  private HashMap<Stop, Integer> hs;
  private HashMap<Stop, List<Node>> pres;

  /** 
   * The Node inner class is used to represent one node within an itinerary.
   *
   * The class is very minimal and is used simply to represent one node used
   * within the route finding algorithm.
   */
  private class Node {
    
    private Stop startNode;
    private Stop endNode;
    private RouteTimetable service;

    public Node(Stop ni, Stop nj, RouteTimetable si) {
      setStartNode(ni);
      setEndNode(ni);
      setService(si);
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

    public RouteTimetable getService() {
      return service;
    }

    private void setService(RouteTimetable s) {
      service = s;
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
  public void setTime(LocalDateTime time) {
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
  public void setDate(LocalDateTime date) {
    this.date = date.toLocalDate();
  }

  /**
   * Find and set schedule based on instance's date.
   */
  private void setSchedule() {
    this.schedule = Schedule.findSchedule(Date.from(this.date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
  }

  /**
   * Initialize the itinerary finder.
   *
   * A number of variables must be initialized, and some pre-calculations
   * undertaken before the itinerary finder can be used to calculate optimal
   * routes. This method ensures these actions are undertaken when called.
   */
  private void initializeFinder() {
    open.add(getStartingStop());
    gs.put(getStartingStop(), 0);
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
  private List<Node> calculateLeastTimePath() {
    return null;
  }
}
