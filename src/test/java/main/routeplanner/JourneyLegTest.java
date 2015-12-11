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
 * JourneyLegTest class contains a series of unit tests for the JourneyLeg class.
 *
 * The JourneyLeg represents one leg of a journey for use elsewhere in the system.
 */
public class JourneyLegTest {

  @Test
  public void testEquals() {
    Stop origin1 = mock(Stop.class);
    when(origin1.equals(origin1)).thenReturn(true);
    Stop origin2 = mock(Stop.class);
    when(origin2.equals(origin2)).thenReturn(true);
    Stop destination1 = mock(Stop.class);
    when(destination1.equals(destination1)).thenReturn(true);
    Stop destination2 = mock(Stop.class);
    when(destination2.equals(destination2)).thenReturn(true);

    RouteTimetable rt1 = mock(RouteTimetable.class);
    RouteTimetable rt2 = mock(RouteTimetable.class);
    when(rt1.timeAtStop(origin1)).thenReturn(10*60);
    when(rt1.timeAtStop(origin2)).thenReturn(11*60);
    when(rt1.timeAtStop(destination1)).thenReturn(10*60 + 45);
    when(rt1.timeAtStop(destination2)).thenReturn(11*60 + 45);
    when(rt2.timeAtStop(origin1)).thenReturn(6*60);
    when(rt2.timeAtStop(origin2)).thenReturn(7*60);
    when(rt2.timeAtStop(destination1)).thenReturn(6*60 + 30);
    when(rt2.timeAtStop(destination2)).thenReturn(7*60 + 30);

    Walk w1 = mock(Walk.class);
    when(w1.getOrigin()).thenReturn(origin1);
    when(w1.getDestination()).thenReturn(destination1);
    Walk w2 = mock(Walk.class);
    when(w2.getOrigin()).thenReturn(origin2);
    when(w2.getDestination()).thenReturn(destination2);
    int time1 = 10*60 + 15;
    int time2 = 10*60 + 16;
    JourneyLeg jl1 = new JourneyLeg(rt1, origin1, destination1);
    JourneyLeg jl2 = new JourneyLeg(rt1, origin1, destination2);
    JourneyLeg jl3 = new JourneyLeg(rt1, origin2, destination1);
    JourneyLeg jl4 = new JourneyLeg(rt1, origin2, destination2);
    JourneyLeg jl1Duplicate = new JourneyLeg(rt1, origin1, destination1);

    assertTrue(jl1.equals(jl1Duplicate));
    for (JourneyLeg jl : new JourneyLeg[] {jl2, jl3, jl4}) {
      assertFalse(jl1.equals(jl));
    }
  }

}
