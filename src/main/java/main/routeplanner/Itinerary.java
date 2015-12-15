package main.routeplanner;

import java.util.List;

import main.CapacityCalculator;

import java.time.LocalDate;

/**
 * This class defines one itinerary for a journey on foot or by bus from a
 * specified origin to a specified destination.
 *
 * Instances of Itinerary are generated by the ItineraryFinder class, and are
 * to be used to obtain and display full details on a particular journey.
 *
 * @see ItineraryFinder
 */
class Itinerary {

  private final LocalDate date;
  private final List<ItineraryLeg> legs;

  /**
   * Creates an Itinerary instance containing a date and a series of legs of
   * the journey.
   *
   * @param date the date of the itinerary
   * @param legs the legs of the journey to which the itinerary relates
   */
  public Itinerary(LocalDate date, List<ItineraryLeg> legs) {
    this.date = date;
    this.legs = legs;
  }

  /**
   * Compares this against another object for equality.
   *
   * An Itinerary is considered equal to another object if that object is also
   * an Itinerary, and it is for the same date and contains exactly the same
   * legs of the journey.
   *
   * @param o the other object against which to compare this
   * @return true, if o is equal to this, else false
   */
  public boolean equals(Object o) {
    return (o instanceof Itinerary && equals((Itinerary) o));
  }

  /**
   * Compares this against another itinerary for equality.
   *
   * An Itinerary is considered equal to another itinerary if it is for the 
   * same date and contains exactly the same legs of the journey.
   *
   * @param otherItinerary the other itinerary against which to compare this
   * @return true, if o is equal to this, else false
   */
  public boolean equals(Itinerary otherItinerary) {
    return (
        getDate().equals(otherItinerary.getDate()) &&
        getLegs().equals(otherItinerary.getLegs())
        );
  }

  /**
   * Gets date of itinerary.
   *
   * @return the date to which this itinerary relates
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Get list of legs of the journey for this itinerary.
   *
   * @return the list of legs of the journey for this itinerary
   */
  public List<ItineraryLeg> getLegs() {
    return legs;
  }
  
  /** 
   * Determines the crowdedness level of this itinerary.
   *
   * Crowdedness is calculated within the CapacityCalculator class, and other
   * associated classes. This method uses the result of this calculation to
   * ascertain how crowded this itinerary is.
   *
   * The crowdedness of an itinerary is based on the crowdedness of each of its
   * legs. Any walk legs are ignored, as crowdedness is not relevant for such
   * a journey. Each bus leg obtains an estimated crowdedness value from the
   * CapacityCalculator, and the maximum value of crowdedness is returned as
   * the crowdedness measure of this itinerary.
   *
   * @return crowdedness level of this itinerary
   */
  public CapacityCalculator.crowdednessIndicator determineCrowdedness() {
    CapacityCalculator.crowdednessIndicator crowdedness = 
      CapacityCalculator.crowdednessIndicator.GREEN;
    for (ItineraryLeg leg : getLegs()) {
      if (leg.isBus()) {
        CapacityCalculator.crowdednessIndicator legCrowdedness = leg.calculateCrowdedness();
        // Unless legCrowdedness is GREEN, set crowdedness to this value. If it
        // is GREEN then this will not change the value of crowdedness. If it
        // is ORANGE or RED then it must be changed to this value. If it turns
        // to RED then this terminates the loop.
        if (!legCrowdedness.equals(CapacityCalculator.crowdednessIndicator.GREEN)) {
          crowdedness = legCrowdedness;
        }
      }
      if (crowdedness.equals(CapacityCalculator.crowdednessIndicator.RED)) {
        break;
      }
    }
    return crowdedness;
  }
}