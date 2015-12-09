package main.routeplanner;

import main.RouteTimetable;
import main.Stop;

/**
 * The JourneyLeg is a class used to represent one leg of a journey.
 *
 * This is a class used simply to represent one leg of a journey specified
 * by the itinerary finder. It consists of a RouteTimetable, representing the
 * specific bus journey on which the passenger is to travel, and the start
 * and end stops of this leg of the journey.
 */
public class JourneyLeg {

  private RouteTimetable routeTimetable;
  private Stop origin;
  private Stop destination;

  /**
   * Constructor for JourneyLeg class.
   *
   * This creates an instance of JourneyLeg, containing a route timetable,
   * and starting and ending stops.
   */
  public JourneyLeg(RouteTimetable rt, Stop origin, Stop destination) {
    this.routeTimetable = rt;
    this.origin = origin;
    this.destination = destination;
  }

  public Stop getOrigin() {
    return origin;
  }

  public Stop getDestination() {
    return destination;
  }

  public RouteTimetable getRouteTimetable() {
    return routeTimetable;
  }

}


