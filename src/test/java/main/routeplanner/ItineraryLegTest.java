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
import main.routeplanner.ItineraryLeg.ItineraryLegType;

/**
 * ItineraryLegTest class contains a series of unit tests for the ItineraryLeg class.
 *
 * The ItineraryLeg represents one leg of a journey for use elsewhere in the system.
 */
public class ItineraryLegTest {

  private static Stop origin1;
  private static Stop origin2;
  private static Stop destination1;
  private static Stop destination2;

  private static RouteTimetable rt1;
  private static RouteTimetable rt2;

  private static Walk w1;
  private static Walk w2;

  private static List<ItineraryLeg> legs;

  private static int time1;
  private static int time2;

  /**
   * Set-up before testing.
   */
  @BeforeClass
  public static void setUpClass() {
    origin1 = mock(Stop.class);
    when(origin1.equals(origin1)).thenReturn(true);
    origin2 = mock(Stop.class);
    when(origin2.equals(origin2)).thenReturn(true);
    destination1 = mock(Stop.class);
    when(destination1.equals(destination1)).thenReturn(true);
    destination2 = mock(Stop.class);
    when(destination2.equals(destination2)).thenReturn(true);

    rt1 = mock(RouteTimetable.class);
    rt2 = mock(RouteTimetable.class);
    when(rt1.timeAtStop(origin1)).thenReturn(6*60 + 45);
    when(rt1.timeAtStop(origin2)).thenReturn(7*60);
    when(rt1.timeAtStop(destination1)).thenReturn(10*60 + 45);
    when(rt1.timeAtStop(destination2)).thenReturn(11*60 + 45);
    when(rt2.timeAtStop(origin1)).thenReturn(6*60);
    when(rt2.timeAtStop(origin2)).thenReturn(7*60);
    when(rt2.timeAtStop(destination1)).thenReturn(7*60 + 30);
    when(rt2.timeAtStop(destination2)).thenReturn(8*60 + 30);

    w1 = mock(Walk.class);
    when(w1.getOrigin()).thenReturn(origin1);
    when(w1.getDestination()).thenReturn(destination1);
    w2 = mock(Walk.class);
    when(w2.getOrigin()).thenReturn(origin2);
    when(w2.getDestination()).thenReturn(destination2);
    when(w1.equals(w1)).thenReturn(true);
    when(w2.equals(w2)).thenReturn(true);

    int time1 = 10*60 + 15;
    int time2 = 10*60 + 16;

    legs = new ArrayList<>();
    legs.add(new ItineraryLeg(rt1, origin1, destination1));
    legs.add(new ItineraryLeg(rt1, origin1, destination2));
    legs.add(new ItineraryLeg(rt1, origin2, destination1));
    legs.add(new ItineraryLeg(rt1, origin2, destination2));
    legs.add(new ItineraryLeg(rt2, origin1, destination1));
    legs.add(new ItineraryLeg(rt2, origin1, destination2));
    legs.add(new ItineraryLeg(rt2, origin2, destination1));
    legs.add(new ItineraryLeg(rt2, origin2, destination2));
    legs.add(new ItineraryLeg(w1, 6*60 + 15));
    legs.add(new ItineraryLeg(w1, 7*60 + 2));
    legs.add(new ItineraryLeg(w2, 6*60 + 40));
    legs.add(new ItineraryLeg(w2, 8*60 + 9));
  }

  /**
   * Test the ItineraryLegType enum.
   */
  @Test
  public void testItineraryLegTypeEnum() {
    assertEquals(new ItineraryLegType[]{ItineraryLegType.WALK, ItineraryLegType.BUS}, ItineraryLegType.values());
    assertEquals(ItineraryLegType.WALK, ItineraryLegType.valueOf("WALK"));
    assertEquals(ItineraryLegType.BUS, ItineraryLegType.valueOf("BUS"));
  }

  @Test
  public void testEquals() {
    ItineraryLeg leg1Duplicate = new ItineraryLeg(rt1, origin1, destination1);
    ItineraryLeg leg2Duplicate = new ItineraryLeg(w1, 7*60 + 2);
    Object o = new Object();

    assertTrue(legs.get(0).equals(leg1Duplicate));
    assertTrue(legs.get(9).equals(leg2Duplicate));
    for (ItineraryLeg leg : legs) {
      List<ItineraryLeg> otherItineraryLegs = new ArrayList<>(legs);
      otherItineraryLegs.remove(leg);
      for (ItineraryLeg otherleg : otherItineraryLegs) {
        assertFalse(leg.equals(otherleg));
        assertFalse(leg.equals(o));
      }
    }
  }

  /**
   * Test isBus method.
   *
   * The first 8 entries in legs are bus-based journeys, so the method
   * should return true for these and these alone.
   */
  @Test
  public void testIsBus() {
    for (ItineraryLeg leg : legs) {
      if (legs.indexOf(leg) < 8) {
        assertTrue(leg.isBus());
      } else {
        assertFalse(leg.isBus());
      }
    }
  } 

  /**
   * Test isWalk method.
   *
   * The first 8 entries in legs are bus-based journeys, so the method
   * should return false for these and true for the remaining legs.
   */
  @Test
  public void testIsWalk() {
    for (ItineraryLeg leg : legs) {
      if (legs.indexOf(leg) < 8) {
        assertFalse(leg.isWalk());
      } else {
        assertTrue(leg.isWalk());
      }
    }
  } 
  /**
   * Test getRouteTimetable method.
   */
  @Test
  public void testGetRouteTimetable() {
    for (ItineraryLeg leg : legs) {
      RouteTimetable thisRT;
      if (legs.indexOf(leg) < 4) {
        thisRT = rt1;
      } else if (legs.indexOf(leg) < 8) {
        thisRT = rt2;
      } else {
        thisRT = null;
      }
      assertEquals(thisRT, leg.getRouteTimetable());
    }
  }

  /**
   * Test getWalk method.
   */
  @Test
  public void testGetWalk() {
    for (ItineraryLeg leg : legs) {
      Walk thisWalk;
      if (legs.indexOf(leg) < 8) {
        thisWalk = null;
      } else if (legs.indexOf(leg) < 10) {
        thisWalk = w1;
      } else {
        thisWalk = w2;
      }
      assertEquals(thisWalk, leg.getWalk());
    }
  }

  /**
   * Test getStartTime method.
   */
  @Test
  public void testGetStartTime() {
    assertEquals(6*60 + 45, legs.get(0).getStartTime());
  }

  /**
   * Test getEndTime method.
   */
  @Test
  public void testGetEndTime() {
    assertEquals(10*60 + 45, legs.get(0).getEndTime());
  }
}
