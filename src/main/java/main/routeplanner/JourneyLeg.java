package main.routeplanner;

import main.RouteTimetable;
import main.Stop;
import main.Walk;

/**
 * The JourneyLeg is a class used to represent one leg of a journey.
 *
 * This is a class used simply to represent one leg of a journey specified
 * by the itinerary finder. It consists of a RouteTimetable, representing the
 * specific bus journey on which the passenger is to travel, and the start
 * and end stops of this leg of the journey.
 */
public class JourneyLeg {

  // Only one of these two fields is populated at one time
  private RouteTimetable routeTimetable;
  private Walk walk;

  private Stop origin;
  private Stop destination;
  private int startTime;
  private int endTime;

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
    this.startTime = rt.timeAtStop(origin);
    this.endTime = rt.timeAtStop(destination);
  }

  /**
   * Constructor for JourneyLeg class.
   *
   * This creates an instance of JourneyLeg without a RouteTimetable. Instead,
   * this constructor is used to construct a walking leg of a journey between
   * the origin and destination.
   */
  public JourneyLeg(Walk walk, int startTime) {
    this.walk = walk;
    this.origin = walk.getOrigin();
    this.destination = walk.getDestination();
    this.startTime = startTime;
    this.endTime = startTime + walk.walkingTime();
  }
  
  public String toString() {
    String s;
    if (walk != null) {
      s = "JourneyLeg: walk from " + 
        getOrigin() + " to " + getDestination() +
      " at " + getStartTime();
    } else {
      s = "JourneyLeg: bus " + getRouteTimetable().getRoute() + " from " + 
        getOrigin() + " to " + getDestination() + 
        " at " + getRouteTimetable().timeAtStop(getOrigin());
    }
    return s;
  }

  public String journeyLegType() {
    if (isWalk()) {
      return "walk";
    } else if (isBus()) {
      return "bus";
    } else {
      return "unknown";
    }
  }

  /**
   * Equals method to compare JourneyLeg instances.
   *
   * Two JourneyLeg instances are equal only if they are of the same type, they
   * are from the same origin and to the same destination at the same time.
   *
   * @return true if equal, else false
   */
  public boolean equals(JourneyLeg otherJourneyLeg) {
    boolean sameJourneyType = journeyLegType().equals(otherJourneyLeg.journeyLegType());
    boolean sameOrigin = getOrigin().equals(otherJourneyLeg.getOrigin());
    boolean sameDestination = getDestination().equals(otherJourneyLeg.getDestination());
    boolean sameStartTime = getStartTime() == otherJourneyLeg.getStartTime();
    return (sameJourneyType && sameOrigin && sameDestination && sameStartTime);
  }

  /**
   * Equals method to compare JourneyLeg instance with other Object.
   *
   * @return true if equal, else false
   */
  @Override
  public boolean equals(Object o) {
    return (o instanceof JourneyLeg && equals((JourneyLeg) o));
  }

  public boolean isWalk() {
    return (walk != null);
  }

  public boolean isBus() {
    return (routeTimetable != null);
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

  public Walk getWalk() {
    return walk;
  }

  public int getStartTime() {
    return startTime;
  }

  public int getEndTime() {
    return endTime;
  }

}


