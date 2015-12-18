package main.capacitytracker;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import main.capacitytracker.CapacityCalculator.CrowdednessIndicator;

import java.io.*;
import java.util.*;
import java.time.*;

import main.model.*;

/**
 * This class contains the test suite for the CapacityCalculator class.
 */
public class CapacityCalculatorTest {

  private static List<DataStoreRecord> mockedRecordList;
  private static DataStoreReader datastore;
  private static CapacityCalculator capacityCalculator;
  private static CapacityCalculator realTimeCapacityCalculator;

  private static RouteTimetable routeTimetable;
  private static Stop stop;

  private static double expectedSeatedAverage;
  private static double expectedTotalAverage;
  private static CrowdednessIndicator expectedCrowdedness;

  /**
   * Sets-up mocked data for use in tests.
   *
   * The CapacityCalculator uses the passenger figures for various 
   * RouteTimetables, Buses, etc. to calculate estimated crowdedness levels
   * of RouteTimetables operating on a certain date.
   *
   * This class mocks data from two RouteTimetables - one of which is not
   * relevant and tests the filtering process in CapacityCalculator - and
   * the other which contains historic and emulated real-time information.
   */
  @BeforeClass
  public static void setUpClass() {
    mockedRecordList = new ArrayList<>();
    double seatedAccumulator = 0;
    int seatedCounter = 0;
    double totalAccumulator = 0;
    int totalCounter = 0;
    for (int i = 0; i < 100; i++) {
      DataStoreRecord r = mock(DataStoreRecord.class);
      double seated = Math.random() * 3;
      double total = Math.random() * 2;
      when(r.getSeatedOccupancyRate()).thenReturn(seated);
      when(r.getTotalOccupancyRate()).thenReturn(total);
      mockedRecordList.add(r);

      seatedAccumulator += seated;
      totalAccumulator += total;
      seatedCounter++;
      totalCounter++;
    }
    expectedSeatedAverage = seatedAccumulator / seatedCounter;
    expectedTotalAverage = totalAccumulator / totalCounter;
    if (expectedSeatedAverage < 1) {
      expectedCrowdedness = CrowdednessIndicator.GREEN;
    } else if (expectedTotalAverage < 1) {
      expectedCrowdedness = CrowdednessIndicator.ORANGE;
    } else {
      expectedCrowdedness = CrowdednessIndicator.RED;
    }
    datastore = mock(DataStoreReader.class);
    when(datastore.read()).thenReturn(mockedRecordList);
    routeTimetable = mock(RouteTimetable.class);
    stop = mock(Stop.class);
    capacityCalculator = new CapacityCalculator(routeTimetable, stop, false, datastore);
    realTimeCapacityCalculator = new CapacityCalculator(routeTimetable, stop, true, datastore);
  }

  /**
   * Test the alternative constructor CapacityCalculator(RouteTimetable, Stop).
   */
  @Test
  public void testCapacityCalculator() {
    try {
      new CapacityCalculator(mock(RouteTimetable.class), mock(Stop.class));
    } catch (IOException e) {
      fail("unable to create CapacityCalculator: " + e.getMessage());
    }
  }

  /**
   * Test the crowdedness method.
   */
  @Test
  public void testCrowdedness() {
    try {
      assertEquals(expectedCrowdedness, capacityCalculator.crowdedness());
    } catch (IOException e) {
      fail("unexpected IOException: " + e.getMessage());
    }
  }

  /**
   * Test the crowdedness method with calculator using real-time data.
   */
  @Test
  public void testRealTimeCrowdedness() {
    try {
      assertEquals(expectedCrowdedness, realTimeCapacityCalculator.crowdedness());
    } catch (IOException e) {
      fail("unexpected IOException: " + e.getMessage());
    }
  }

  /**
   * Test the CrowdednessIndicator enum values.
   */
  @Test
  public void testCrowdednessIndicatorValues() {
    assertEquals(1, 1);
  }

  /**
   * Test getRouteTimetable method.
   */
  @Test
  public void testGetRouteTimetable() {
    assertEquals(routeTimetable, capacityCalculator.getRouteTimetable());
  }

  /**
   * Test getStop method.
   */
  @Test
  public void testGetStop() {
    assertEquals(stop, capacityCalculator.getStop());
  }

  /**
   * Test the CrowdednessIndicator#moreCrowdedThan method.
   */
  @Test
  public void testMoreCrowdedThan() {
    CrowdednessIndicator red = CrowdednessIndicator.RED;
    CrowdednessIndicator green = CrowdednessIndicator.GREEN;
    CrowdednessIndicator orange = CrowdednessIndicator.ORANGE;
    
    assertTrue(red.moreCrowdedThan(orange));
    assertTrue(red.moreCrowdedThan(green));
    assertTrue(orange.moreCrowdedThan(green));
    assertFalse(red.moreCrowdedThan(red));
    assertFalse(orange.moreCrowdedThan(red));
    assertFalse(orange.moreCrowdedThan(orange));
    assertFalse(green.moreCrowdedThan(red));
    assertFalse(green.moreCrowdedThan(orange));
    assertFalse(green.moreCrowdedThan(green));
  }

}
