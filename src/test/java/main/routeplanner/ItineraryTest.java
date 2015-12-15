package main.routeplanner;

import org.junit.*;
import org.junit.rules.ExpectedException;
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
      legs1.add(il1);
      legs2.add(il2);
    }
    date1 = LocalDate.of(2015, Month.JUNE, 14);
    date2 = LocalDate.of(2007, Month.NOVEMBER, 29);

    itinerary1 = new Itinerary(date1, legs1);
    itinerary2 = new Itinerary(date2, legs2);
  }

  /**
   * Tests the #equals(Itinerary) method.
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

}
