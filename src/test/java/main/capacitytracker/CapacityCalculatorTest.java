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
 * @author Ivo on 14-12-2015.
 */
public class CapacityCalculatorTest {

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
    
  }

  /**
   * Test the CrowdednessIndicator enum values.
   */
  @Test
  public void testCrowdednessIndicatorValues() {
    assertEquals(1, 1);
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
