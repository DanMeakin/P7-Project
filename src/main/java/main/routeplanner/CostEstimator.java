package main.routeplanner;

import java.util.HashMap;

import main.Route;
import main.Stop;

public class CostEstimator {

  // Value to be used for stops not connected on one single Route
  public static final int UNCONNECTED = 1_000_000;

  private static HashMap<StopTuple, Integer> costsTable;

  // Flag whether costs table has been initalized, and whether it has been
  // fully populated.
  private static boolean costsTableInitialized = false; 
  private static boolean costsTablePopulated = false;

  /**
   * The StopTuple class defines a 2-tuple of Stops.
   *
   * This is solely used as a key in costsTable.
   */
  private static class StopTuple {
    
    private Stop s1;
    private Stop s2;

    public StopTuple(Stop s1, Stop s2) {
      this.s1 = s1;
      this.s2 = s2;
    }

    /**
     * Create array containing both stops.
     *
     * @return array of length 2 containing s1 & s2 respectively
     */
    public Stop[] toArray() {
      return new Stop[] {s1, s2};
    }

    /**
     * Get value of s1.
     *
     * @return s1
     */
    public Stop getS1() {
      return s1;
    }

    /**
     * Get value of s2.
     *
     * @return s2
     */

    public Stop getS2() {
      return s2;
    }

    /**
     * Determine StopTuple equality.
     *
     * @return true if S1 == otherS1 and S2 == otherS2, else false
     */
    public boolean equals(StopTuple otherStopTuple) {
      return (getS1() == otherStopTuple.getS1() &&
              getS2() == otherStopTuple.getS2());
    }

    /**
     * Override hashCode method to ensure equivalent StopTuples are treated
     * as such as keys in HashMap.
     *
     * This method creates a number based on the ID# of s1 and the ID# of s2.
     * It assumes that the ID# will never exceed 9999, which would be a huge
     * number of stops for a system of this nature.
     *
     * @return hash value of this StopTuple
     */
    @Override
    public int hashCode() {
      return s1.getID() * 10_000 + s2.getID();
    }
  }

  public CostEstimator() {
    if (!isCostsTableInitialized()) {
      generateCostsTable();  
    }
  }

  /**
   * Determine if populating costs table has commenced.
   *
   * Populating the table must be done before the CostEstimator can be used.
   * The costsTableInitialized flag ensures that initialization of the
   * populating process only happens once.
   *
   * @return true if initialization has commenced, else false
   */
  public static boolean isCostsTableInitialized() {
    return costsTableInitialized;
  }

  /**
   * Set the costsTableInitialized flag to true.
   */
  public static void setCostsTableInitialized() {
    costsTableInitialized = true;
  }

  /**
   * Determine if populating costs table has completed.
   *
   * Populating the table must be done before the CostEstimator can be used.
   * The costsTablePopulated field flags whether populating the table has
   * completed.
   *
   * @return true if initialization has completed, else false
   */
  public static boolean isCostsTablePopulated() {
    return costsTablePopulated;
  }

  /**
   * Set the costsTablePopulated flag to true.
   */
  public static void setCostsTablePopulated() {
    costsTablePopulated = true;
  }

  /**
   * Retrieve the H value for stops s1 & s2.
   *
   * @return H value for path between s1 & s2
   */
  public static int getH(Stop s1, Stop s2) {
    return costsTable.get(new StopTuple(s1, s2));
  }

  /**
   * Set the H value for stops s1 & s2.
   */
  public static void setH(Stop s1, Stop s2, int cost) {
    costsTable.put(new StopTuple(s1, s2), cost);
  }

  /**
   * Generate costs table for journeys between stops.
   *
   * This is an adaptation of algorithm 2 contained within the paper
   * Yu, Zhang, Jiafu, Tang, Shimeng, Lv, Xinggang, Luo. (2014). Floyd-A∗ 
   * Algorithm Solving the Least-Time Itinerary Planning Problem in Urban 
   * Scheduled Public Transport Network. Mathematical Problems in Engineering,
   * 2014. http://dx.doi.org/10.1155/2014/185383.
   *
   * The calculation iterates through each available Route and determines the
   * time it takes to travel from one stop to the next. Where no single-route
   * path connects two stops, the value assigned to the estimated time for that
   * journey is set to the value of UNCONNECTED.
   */
  private static void generateCostsTable() {
    // Initialize SUSPT network
    for (Route r : Route.getAllRoutes()) {
      for (Stop s1 : r.getStops()) {
        for (Stop s2 : r.getStops()) {
          try {
            int t = r.journeyTimeBetweenStops(s1, s2, false);
            costsTable.put(new CostEstimator.StopTuple(s1, s2), t);
          } catch (IllegalArgumentException e) {
            costsTable.put(new CostEstimator.StopTuple(s1, s2), UNCONNECTED);
          }
        }
      }
    }

    // Calculate costs of all-to-all shortest paths
    for (Stop sK : Stop.getAllStops()) {
      for (Stop sI : Stop.getAllStops()) {
        for (Stop sJ : Stop.getAllStops()) {
          if (getH(sI, sJ) > getH(sI, sK) + getH(sK, sJ)) {
            setH(sI, sJ, getH(sI, sK) + getH(sK, sJ));
          }
        }
      }
    }
  }
}
