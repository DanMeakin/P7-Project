package main.routeplanner;

import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.time.*;

import main.capacitytracker.CapacityCalculator;
import main.model.*;

/**
 * This class contains the unit tests to test the Itinerary class.
 */
public class ItineraryTest {

  private Itinerary itinerary1;
  private Itinerary itinerary2;
  private List<ItineraryLeg> legs1;
  private List<ItineraryLeg> legs2;
  private LocalDate date1;
  private LocalDate date2;

  /**
   * Sets-up objects before running each test.
   */
  @Before
  public void setUpClass() {
    legs1 = new ArrayList<>();
    legs2 = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      ItineraryLeg il1 = mock(ItineraryLeg.class);
      ItineraryLeg il2 = mock(ItineraryLeg.class);
      when(il1.equals(il1)).thenReturn(true);
      when(il2.equals(il2)).thenReturn(true);
      when(il1.getStartTime()).thenReturn(60*(i+6));
      when(il1.getEndTime()).thenReturn(60*(i+6) + 55);
      when(il2.getStartTime()).thenReturn(30*(i+13));
      when(il2.getEndTime()).thenReturn(30*(i+13) + 25);
      Stop origin1 = mock(Stop.class);
      Stop destination1 = mock(Stop.class);
      Stop origin2 = mock(Stop.class);
      Stop destination2 = mock(Stop.class);
      when(origin1.toString()).thenReturn("L1 Origin");
      when(destination1.toString()).thenReturn("L1 Destination");
      when(origin2.toString()).thenReturn("L2 Origin");
      when(destination2.toString()).thenReturn("L2 Destination");
      when(il1.getOrigin()).thenReturn(origin1);
      when(il1.getDestination()).thenReturn(destination1);
      when(il2.getOrigin()).thenReturn(origin2);
      when(il2.getDestination()).thenReturn(destination2);
      legs1.add(il1);
      legs2.add(il2);
    }
    date1 = LocalDate.of(2015, Month.JUNE, 14);
    date2 = LocalDate.of(2007, Month.NOVEMBER, 29);

    itinerary1 = new Itinerary(date1, legs1);
    itinerary2 = new Itinerary(date2, legs2);
  }

  /**
   * Tests the equals method.
   *
   * Two itineraries should be equal only when they share the same date and
   * same list of legs.
   */
  @Test
  public void testEquals() {
    assertNotEquals(itinerary1, itinerary2);
    assertNotEquals(itinerary2, itinerary1);

    Itinerary dupItinerary1 = new Itinerary(date1, legs1);
    Itinerary dupItinerary2 = new Itinerary(date2, legs2);
    Itinerary itinerary3 = new Itinerary(date1, legs2);
    assertEquals(itinerary1, dupItinerary1);
    assertEquals(itinerary2, dupItinerary2);
    assertNotEquals(itinerary1, itinerary3);
    assertNotEquals(itinerary2, itinerary3);

    Object o = new Object();
    assertNotEquals(itinerary1, o);
    assertNotEquals(itinerary2, o);
    assertNotEquals(itinerary3, o);
  }

  /**
   * Tests the toString method.
   */
  @Test
  public void testToString() {
    String expected = "Itinerary: " + date1;
    for (ItineraryLeg il : itinerary1.getLegs()) {
      expected += ", " + il.getOrigin() + " -> " + il.getDestination();
    }
    assertEquals(expected, itinerary1.toString());
  }

  /**
   * Tests the crowdedness method.
   *
   * This method returns the crowdedness value for an itinerary using the
   * CapacityCalculator class.
   */
  @Test
  public void testDetermineCrowdedness() {
    // Each itinerary has 10 legs, so we must mock crowdedness results for
    // each leg of each itinerary.
    CapacityCalculator.CrowdednessIndicator[] crowdedness1 = new CapacityCalculator.CrowdednessIndicator[] { 
      CapacityCalculator.CrowdednessIndicator.GREEN, 
      CapacityCalculator.CrowdednessIndicator.GREEN, 
      CapacityCalculator.CrowdednessIndicator.GREEN, 
      CapacityCalculator.CrowdednessIndicator.ORANGE, 
      CapacityCalculator.CrowdednessIndicator.RED, 
      CapacityCalculator.CrowdednessIndicator.GREEN, 
      CapacityCalculator.CrowdednessIndicator.GREEN, 
      CapacityCalculator.CrowdednessIndicator.ORANGE, 
      CapacityCalculator.CrowdednessIndicator.GREEN,
      CapacityCalculator.CrowdednessIndicator.GREEN 
    };
    CapacityCalculator.CrowdednessIndicator[] crowdedness2 = new CapacityCalculator.CrowdednessIndicator[] { 
      CapacityCalculator.CrowdednessIndicator.ORANGE,
      CapacityCalculator.CrowdednessIndicator.ORANGE, 
      CapacityCalculator.CrowdednessIndicator.ORANGE, 
      CapacityCalculator.CrowdednessIndicator.GREEN, 
      CapacityCalculator.CrowdednessIndicator.GREEN, 
      CapacityCalculator.CrowdednessIndicator.ORANGE, 
      CapacityCalculator.CrowdednessIndicator.ORANGE, 
      CapacityCalculator.CrowdednessIndicator.GREEN,
      CapacityCalculator.CrowdednessIndicator.GREEN, 
      CapacityCalculator.CrowdednessIndicator.RED
    };
    for (int i = 0; i < 10; i++) {
      when(legs1.get(i).crowdedness()).thenReturn(crowdedness1[i]);
      when(legs2.get(i).crowdedness()).thenReturn(crowdedness2[i]);
      // Only process the first nine legs for crowdedness purposes. Ignore the
      // last entry
      if (i < 9) {
        when(legs1.get(i).isBus()).thenReturn(true);
        when(legs2.get(i).isBus()).thenReturn(true);
      }
    }
    assertEquals(CapacityCalculator.CrowdednessIndicator.RED, itinerary1.crowdedness());
    assertEquals(CapacityCalculator.CrowdednessIndicator.ORANGE, itinerary2.crowdedness());
  }

  /**
   * Test totalDuration method.
   */
  @Test
  public void testTotalDuration() {
    assertEquals(60*9 + 55, itinerary1.totalDuration());
    assertEquals(30*9 + 25, itinerary2.totalDuration());
  }
}
