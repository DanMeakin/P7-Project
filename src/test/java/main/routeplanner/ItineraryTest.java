package main.routeplanner;

import org.junit.*;

import main.CapacityCalculator;
import main.Stop;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.time.*;

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
   * Tests the determineCrowdedness method.
   *
   * This method returns the crowdedness value for an itinerary using the
   * CapacityCalculator class.
   */
  @Test
  public void testDetermineCrowdedness() {
    // Each itinerary has 10 legs, so we must mock crowdedness results for
    // each leg of each itinerary.
    CapacityCalculator.crowdednessIndicator[] crowdedness1 = new CapacityCalculator.crowdednessIndicator[] { 
      CapacityCalculator.crowdednessIndicator.GREEN, 
      CapacityCalculator.crowdednessIndicator.GREEN, 
      CapacityCalculator.crowdednessIndicator.GREEN, 
      CapacityCalculator.crowdednessIndicator.ORANGE, 
      CapacityCalculator.crowdednessIndicator.RED, 
      CapacityCalculator.crowdednessIndicator.GREEN, 
      CapacityCalculator.crowdednessIndicator.GREEN, 
      CapacityCalculator.crowdednessIndicator.ORANGE, 
      CapacityCalculator.crowdednessIndicator.GREEN,
      CapacityCalculator.crowdednessIndicator.GREEN 
    };
    CapacityCalculator.crowdednessIndicator[] crowdedness2 = new CapacityCalculator.crowdednessIndicator[] { 
      CapacityCalculator.crowdednessIndicator.ORANGE,
      CapacityCalculator.crowdednessIndicator.ORANGE, 
      CapacityCalculator.crowdednessIndicator.ORANGE, 
      CapacityCalculator.crowdednessIndicator.GREEN, 
      CapacityCalculator.crowdednessIndicator.GREEN, 
      CapacityCalculator.crowdednessIndicator.ORANGE, 
      CapacityCalculator.crowdednessIndicator.ORANGE, 
      CapacityCalculator.crowdednessIndicator.GREEN,
      CapacityCalculator.crowdednessIndicator.GREEN, 
      CapacityCalculator.crowdednessIndicator.RED
    };
    for (int i = 0; i < 10; i++) {
      when(legs1.get(i).calculateCrowdedness()).thenReturn(crowdedness1[i]);
      when(legs2.get(i).calculateCrowdedness()).thenReturn(crowdedness2[i]);
      // Only process the first nine legs for crowdedness purposes. Ignore the
      // last entry
      if (i < 9) {
        when(legs1.get(i).isBus()).thenReturn(true);
        when(legs2.get(i).isBus()).thenReturn(true);
      }
    }
    assertEquals(CapacityCalculator.crowdednessIndicator.RED, itinerary1.determineCrowdedness());
    assertEquals(CapacityCalculator.crowdednessIndicator.ORANGE, itinerary2.determineCrowdedness());
  }
}
