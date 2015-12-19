package main.routeplanner;

import java.util.HashMap;

import main.model.*;
import main.misc.StopPair;

/**
 * The CostEstimator class is used to generate and query a static costs table
 * used in determining the cost of travelling between two specified stops.
 *
 * The class generates a static costs table when first instantiated. This
 * consists of a large number of calculations which may take some time. The
 * methods provided within the class are not useable until after completion
 * of the generation of the table.
 */
class CostEstimator {

  private final Stop endNode;

  /**
   * Value to be used for stops not connected on one single Route
   */
  public static final int UNCONNECTED = 1_000_000;

  private static HashMap<StopPair, Integer> costsTable = new HashMap<StopPair, Integer>();

  // Flag whether costs table has been initalized, and whether it has been
  // fully populated.
  private static boolean costsTableInitialized = false; 
  private static boolean costsTablePopulated = false;

  /**
   * Create a new CostEstimator instance.
   *
   * The constructor accepts a endNode argument which is used in determining
   * the value of h'(ni). h'(ni) is used in the ItineraryFinder to estimate
   * the overall cost value of a trip between a starting point and the given
   * endNode.
   *
   * @param endNode the ending node used for determining h'(ni)
   */
  public CostEstimator(Stop endNode) {
    this.endNode = endNode;
    if (!isCostsTableInitialized()) {
      generateCostsTable();  
    }
  }

  /**
   * Determine the value of h'(ni).
   *
   * The h'(ni) value is used in calculating an optimal itinerary. This method
   * returns the value of h'(ni) for the passed endNode.
   *
   * @param ni the node for which to get h' value
   * @return value of h'(ni)
   */
  public int hPrime(Stop ni) throws UnsupportedOperationException {
    if (!isCostsTablePopulated()) {
      String msg = "costs table is not yet populated; please wait";
      throw new UnsupportedOperationException(msg);
    }
    return getH(ni, endNode);
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
  private static void setCostsTableInitialized() {
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
  private static void setCostsTablePopulated() {
    costsTablePopulated = true;
  }

  /**
   * Retrieve the H value for stops s1 & s2.
   *
   * @return H value for path between s1 & s2
   */
  private static int getH(Stop s1, Stop s2) {
    StopPair pair = new StopPair(s1, s2);
    if (costsTable.get(pair) == null) {
      return UNCONNECTED;
    } else {
      return costsTable.get(pair);
    }
  }

  /**
   * Set the H value for stops s1 & s2.
   */
  private static void setH(Stop s1, Stop s2, int cost) {
    StopPair key = new StopPair(s1, s2);
    costsTable.put(key, cost);
  }

  /**
   * Generate costs table for journeys between stops.
   *
   * The calculation iterates through each available Route and determines the
   * time it takes to travel from one stop to the next. Where no single-route
   * path connects two stops, the value assigned to the estimated time for that
   * journey is set to the value of UNCONNECTED.
   */
  private static void generateCostsTable() {
    setCostsTableInitialized();
    // Initialize SUSPT network
    for (Path p : Path.getAllPaths()) {
      for (Stop s1 : p.getStops()) {
        for (Stop s2 : p.getStops()) {
          int t = 0;
          try {
            if (!s1.equals(s2)) {
              t = p.journeyTimeBetweenStops(s1, s2, false);
            }
          } catch (IllegalArgumentException e) {
            // IllegalArgumentException thrown if no route between stops.
            t = UNCONNECTED;
          }
          // Only set new value if lower than existing
          if (t < getH(s1, s2)) {
            setH(s1, s2, t);
          }
        }
      }
    }

    // Calculate costs of all-to-all shortest paths
    for (Stop sK : Stop.getAllStops()) {
      for (Stop sI : Stop.getAllStops()) {
        for (Stop sJ : Stop.getAllStops()) {
          if (!sI.equals(sJ) && getH(sI, sJ) > getH(sI, sK) + getH(sK, sJ)) {
            setH(sI, sJ, getH(sI, sK) + getH(sK, sJ));
          }
        }
      }
    }
    setCostsTablePopulated();
  }

}
