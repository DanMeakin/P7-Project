package main.routeplanner;

import java.time.LocalDate;

import main.capacitytracker.CapacityCalculator;
import main.RouteTimetable;
import main.Stop;
import main.Walk;

/**
 * This class is used to represent one leg of an itinerary.
 *
 * This is a class used simply to represent one leg of a journey specified
 * by the itinerary finder. It consists of a RouteTimetable, representing the
 * specific bus journey on which the passenger is to travel, and the start
 * and end stops of this leg of the itinerary.
 */
public class ItineraryLeg {

  // Only one of these two fields is populated at one time
  private RouteTimetable routeTimetable;
  private Walk walk;

  private final LocalDate date;
  private final Stop origin;
  private final Stop destination;
  private final int startTime;
  private final int endTime;

  private CapacityCalculator capacityCalculator;

  public enum ItineraryLegType { WALK, BUS };

  /**
   * Constructor for ItineraryLeg class.
   *
   * This creates an instance of ItineraryLeg, containing a route timetable,
   * and starting and ending stops.
   */
  public ItineraryLeg(LocalDate date, RouteTimetable rt, Stop origin, Stop destination) {
    this.date = date;
    this.routeTimetable = rt;
    this.origin = origin;
    this.destination = destination;
    this.startTime = rt.timeAtStop(origin);
    this.endTime = rt.timeAtStop(destination);

    // If ItineraryLeg is for today, try getting real-time capacity data
    boolean realTime = date.equals(LocalDate.now());
    this.capacityCalculator = new CapacityCalculator(rt, origin, realTime);
  }

  /**
   * Constructor for ItineraryLeg class.
   *
   * This creates an instance of ItineraryLeg without a RouteTimetable. Instead,
   * this constructor is used to construct a walking leg of a journey between
   * the origin and destination.
   */
  public ItineraryLeg(LocalDate date, Walk walk, int startTime) {
    this.date = date;
    this.walk = walk;
    this.origin = walk.getOrigin();
    this.destination = walk.getDestination();
    this.startTime = startTime;
    this.endTime = startTime + walk.walkingTime();
  }
  
  /**
   * Gets the type of ItineraryLeg.
   *
   * @return WALK if the ItineraryLeg is a walk, else BUS.
   */
  public ItineraryLegType journeyLegType() {
    if (walk != null) {
      return ItineraryLegType.WALK;
    } else {
      return ItineraryLegType.BUS;
    }
  }

  /**
   * Equals method to compare ItineraryLeg with other Object.
   *
   * A ItineraryLeg is equal only to another ItineraryLeg. As such, where a
   * ItineraryLeg is passed, this will call the #equals(ItineraryLeg) method.
   * Otherwise, return false.
   *
   * @param o object with which to compare this
   * @return true if equal, else false
   */
  @Override
  public boolean equals(Object o) {
    return (o instanceof ItineraryLeg && equals((ItineraryLeg) o));
  }

  /**
   * Equals method to compare ItineraryLeg instances.
   *
   * Two ItineraryLeg instances are equal only if they are of the same type, they
   * are from the same origin and to the same destination at the same time.
   *
   * @return true if equal, else false
   */
  public boolean equals(ItineraryLeg otherItineraryLeg) {
    boolean sameJourneyType = journeyLegType().equals(otherItineraryLeg.journeyLegType());
    boolean sameJourney = false;
    if (sameJourneyType) {
      if (isWalk()) {
        sameJourney = getWalk().equals(otherItineraryLeg.getWalk());
      } else {
        sameJourney = getRouteTimetable().equals(otherItineraryLeg.getRouteTimetable());
      }
    }
    boolean sameOrigin = getOrigin().equals(otherItineraryLeg.getOrigin());
    boolean sameDestination = getDestination().equals(otherItineraryLeg.getDestination());
    boolean sameStartTime = getStartTime() == otherItineraryLeg.getStartTime();
    return (sameJourney && sameOrigin && sameDestination && sameStartTime);
  }

  /**
   * Gets whether itinerary leg is a walk or not.
   *
   * @return true if itinerary leg is a walk, else false
   */
  public boolean isWalk() {
    return journeyLegType().equals(ItineraryLegType.WALK);
  }

  /**
   * Gets whether itinerary leg is a bus journey or not.
   *
   * @return true if itinerary leg is a bus journey, else false
   */
  public boolean isBus() {
    return journeyLegType().equals(ItineraryLegType.BUS);
  }

  /**
   * Gets date of ItineraryLeg.
   *
   * @return date of ItineraryLeg
   */
  public LocalDate getDate() {
    return date;
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
   * If this is not a bus type ItineraryLeg, then this method returns null.
   *
   * @return route timetable to which this relates
   */
  public RouteTimetable getRouteTimetable() {
    return routeTimetable;
  }

  /**
   * Gets Walk.
   *
   * If this is not a walk type ItineraryLeg, then this method returns null.
   *
   * @return walk to which this relates
   */
  public Walk getWalk() {
    return walk;
  }

  /**
   * Gets start time.
   *
   * @return start time of itinerary leg (in minutes from midnight)
   */
  public int getStartTime() {
    return startTime;
  }

  /**
   * Gets end time.
   *
   * @return end time of itinerary leg (in minutes from midnight)
   */
  public int getEndTime() {
    return endTime;
  }

  /**
   * Gets the estimated crowdedness of this leg.
   *
   * Uses the CapacityCalculator class.
   *
   * @return enum value of GREEN, ORANGE or RED depending on the capacity of
   *         the bus for this leg
   * @throws IllegalArgumentException if attempting to calculate crowdedness
   *                                  of a walk (only applies to buses)
   * @see CapacityCalculator
   */
  public CapacityCalculator.CrowdednessIndicator calculateCrowdedness() {
    if (isBus()) {
      return capacityCalculator.crowdedness();
    }
    String msg = "unable to calculate crowdedness of a walk leg";
    throw new IllegalArgumentException(msg);
  }

}


