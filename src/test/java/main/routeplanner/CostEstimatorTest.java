package main.routeplanner;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.time.*;

import main.Path;
import main.Schedule;
import main.Schedule.DayOptions;
import main.Route;
import main.RouteTimetable;
import main.Bus;
import main.Stop;
import main.Walk;

/**
 * CostEstimatorTest class contains a series of unit tests for the CostEstimator class.
 *
 * The CostEstimator class conducts a number of calculations upon which the
 * ItineraryFinder is dependent. This test suite ensures that these calculations
 * are performed correctly.
 */
public class CostEstimatorTest {

  @After
  public void tearDown() {
    for (Stop s : new ArrayList<Stop>(Stop.getAllStops())) {
      Stop.removeStop(s);
    }
  }

  /**
   * Test the StopTuple#equals method.
   *
   * Two StopTuples with identical starting and ending stops should be equal;
   * in no other case should StopTuples be considered equal.
   */
  @Test
  public void testStopTupleEquality() {
    Stop s1 = new Stop(1, "Stop 1", 0, 0);
    Stop s2 = new Stop(2, "Stop 2", 0, 0);
    CostEstimator.StopPair sp1 = new CostEstimator.StopPair(s1, s2);
    CostEstimator.StopPair sp2 = new CostEstimator.StopPair(s2, s1);
    CostEstimator.StopPair sp3 = new CostEstimator.StopPair(s1, s2);
    CostEstimator.StopPair sp4 = new CostEstimator.StopPair(s2, s1);
    CostEstimator.StopPair sp5 = new CostEstimator.StopPair(s1, s1);
    assertTrue(sp1.equals(sp3));
    assertTrue(sp3.equals(sp1));
    assertTrue(sp2.equals(sp4));
    assertTrue(sp4.equals(sp2));
    assertTrue(sp5.equals(sp5));
    assertFalse(sp1.equals(sp2));
    assertFalse(sp2.equals(sp1));
    assertFalse(sp3.equals(sp4));
    assertFalse(sp4.equals(sp3));
    assertFalse(sp5.equals(sp1));
  }

}
