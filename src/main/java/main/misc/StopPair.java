package main.misc;

import java.util.ArrayList;
import java.util.List;

import main.model.Stop;

/**
 * The StopPair class defines a 2-tuple of Stops.
 *
 * This is solely used as keys in HashMaps requiring the comparison of some
 * attribute of two Stops.
 */
public class StopPair {
  
  private Stop s1;
  private Stop s2;

  /**
   * Create a StopPair instance.
   *
   * StopPairs are used as keys in the CostEstimator HashMap.
   *
   * @param s1 first stop in pair
   * @param s2 second stop in pair
   */
  public StopPair(Stop s1, Stop s2) {
    if (s1 == null || s2 == null) {
      String msg = "cannot pass a null stop into StopPair - received null value for ";
      List<String> nulls = new ArrayList<>();
      if (s1 == null) {
        nulls.add("s1");
      }
      if (s2 == null) {
        nulls.add("s2");
      }
      msg += String.join(" & ", nulls);
      throw new IllegalArgumentException(msg);
    }
    this.s1 = s1;
    this.s2 = s2;
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
   * Determine StopPair equality.
   *
   * Two StopPairs are equal only if the first stop is equal to the first,
   * and the second is equal to the second.
   *
   * @param otherStopPair the other instance to compare this against
   * @return true if S1 equals otherS1 and S2 equals otherS2, else false
   */
  public boolean equals(StopPair otherStopPair) {
    return (getS1().equals(otherStopPair.getS1()) &&
            getS2().equals(otherStopPair.getS2()));
  }

  public boolean equals(Object o) {
    if (o instanceof StopPair) {
      return equals((StopPair) o);
    } else {
      return false;
    }
  }

  /**
   * Override hashCode method to ensure equivalent StopPairs are treated
   * as such as keys in HashMap.
   *
   * @return hash value of this StopPair
   */
  @Override
  public int hashCode() {
    return getS1().getID() * 10_000 + getS2().getID();
  }

}


