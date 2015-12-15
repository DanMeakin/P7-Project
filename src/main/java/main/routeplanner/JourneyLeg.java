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

  public enum JourneyLegType { WALK, BUS };

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
  
  /**
   * Gets the type of JourneyLeg.
   *
   * @return WALK if the JourneyLeg is a walk, else BUS.
   */
  public JourneyLegType journeyLegType() {
    if (walk != null) {
      return JourneyLegType.WALK;
    } else {
      return JourneyLegType.BUS;
    }
  }

  /**
   * Equals method to compare JourneyLeg with other Object.
   *
   * A JourneyLeg is equal only to another JourneyLeg. As such, where a
   * JourneyLeg is passed, this will call the #equals(JourneyLeg) method.
   * Otherwise, return false.
   *
   * @param o object with which to compare this
   * @return true if equal, else false
   */
  @Override
  public boolean equals(Object o) {
    return (o instanceof JourneyLeg && equals((JourneyLeg) o));
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
    boolean sameJourney = false;
    if (sameJourneyType) {
      if (isWalk()) {
        sameJourney = getWalk().equals(otherJourneyLeg.getWalk());
      } else {
        sameJourney = getRouteTimetable().equals(otherJourneyLeg.getRouteTimetable());
      }
    }
    boolean sameOrigin = getOrigin().equals(otherJourneyLeg.getOrigin());
    boolean sameDestination = getDestination().equals(otherJourneyLeg.getDestination());
    boolean sameStartTime = getStartTime() == otherJourneyLeg.getStartTime();
    return (sameJourney && sameOrigin && sameDestination && sameStartTime);
  }

  /**
   * Gets whether journey leg is a walk or not.
   *
   * @return true if journey leg is a walk, else false
   */
  public boolean isWalk() {
    return journeyLegType().equals(JourneyLegType.WALK);
  }

  /**
   * Gets whether journey leg is a bus journey or not.
   *
   * @return true if journey leg is a bus journey, else false
   */
  public boolean isBus() {
    return journeyLegType().equals(JourneyLegType.BUS);
  }

  /**
   * Gets origin.
   *
   * @return origin stop
   */
  public Stop getOrigin() {
    return origin;
  }

  /**
   * Gets destination.
   *
   * @return destination stop.
   */
  public Stop getDestination() {
    return destination;
  }

  /**
   * Gets RouteTimetable.
   *
   * If this is not a bus type JourneyLeg, then this method returns null.
   *
   * @return route timetable to which this relates
   */
  public RouteTimetable getRouteTimetable() {
    return routeTimetable;
  }

  /**
   * Gets Walk.
   *
   * If this is not a walk type JourneyLeg, then this method returns null.
   *
   * @return walk to which this relates
   */
  public Walk getWalk() {
    return walk;
  }

  /**
   * Gets start time.
   *
   * @return start time of journey leg (in minutes from midnight)
   */
  public int getStartTime() {
    return startTime;
  }

  /**
   * Gets end time.
   *
   * @return end time of journey leg (in minutes from midnight)
   */
  public int getEndTime() {
    return endTime;
  }

}


