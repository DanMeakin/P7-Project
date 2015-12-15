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
 * ItineraryFinderTest class contains a series of unit tests for the ItineraryFinder class.
 *
 * The ItineraryFinder class enables a user to find the best (quickest or least crowded)
 * way of transport from a to b.
 *
 */
public class ItineraryFinderTest {

  private static Schedule schedule;
  private static Stop[] nodes;
  private static List<Path> services;
  private static Route[] routes;
  private static Walk[] walks;

  private ItineraryFinder itineraryFinder;

  /**
   * Set-up before any testing begins.
   *
   * This method creates a weekday schedule for the period 1st January 2015 to 
   * 31st December 2015 with the following entries:-
   *
   * Route L1
   * --------
   *   N1    N2    N3
   *  6:02  6:14  6:25
   *  6:08  6:20  6:31
   *  6:13  6:25  6:36
   *  6:18  6:30  6:41
   *  6:22  6:34  6:45
   *
   * Route L2
   * --------
   *   N4    N1    N2
   *  6:05  6:18  6:30
   *  6:15  6:28  6:40
   *  6:25  6:38  6:50
   *  6:33  6:46  6:58
   *  6:41  6:54  7:06
   *
   * Route L3
   * --------
   *   N4    N5
   *  6:05  6:15
   *  6:10  6:20
   *  6:15  6:25
   *  6:20  6:30
   *  6:23  6:33
   *  6:26  6:36
   *
   * Route L4
   * --------
   *   N6    N7
   *  6:02  6:16
   *  6:10  6:24
   *  6:18  6:32
   *  6:26  6:40
   *  6:30  6:44
   *
   * Route L5
   * --------
   *   N1    N7
   *  6:00  6:12
   *  6:03  6:15
   *  6:06  6:18
   *  6:09  6:21
   *  6:12  6:24
   *
   * Walking paths are also created between:
   *
   *  N4 - N7: 1 minute (walks 0, 1)
   *  N3 - N5: 2 minutes (walks 2, 3)
   *  N3 - N6: 3 minutes (walks 4, 5)
   *  N5 - N6: 1 minute (walks 6, 7)
   */
  @BeforeClass
  public static void setUpClass() {
    // Clear all existing Stops, Paths & Schedules
    for (Schedule s : new ArrayList<Schedule>(Schedule.getAllSchedules())) {
      Schedule.removeSchedule(s);
    }
    for (Stop s : new ArrayList<Stop>(Stop.getAllStops())) {
      Stop.removeStop(s);
    }
    for (Path p : new ArrayList<Path>(Path.getAllPaths())) {
      Path.removePath(p);
    }
    // Define stops/nodes
    nodes = new Stop[] {
      new Stop(1, "N1", 0, 0),
      new Stop(2, "N2", 0, 0),
      new Stop(3, "N3", 0, 0),
      new Stop(4, "N4", 0, 0),
      new Stop(5, "N5", 0, 0),
      new Stop(6, "N6", 0, 0),
      new Stop(7, "N7", 0, 0)
    };
    routes = new Route[5];

    routes[0] = new Route("L1", "N1 - N3", nodes[0]);
    routes[0].addStop(nodes[1], 12, 12);
    routes[0].addStop(nodes[2], 11, 11);

    routes[1] = new Route("L2", "N4 - N2", nodes[3]);
    routes[1].addStop(nodes[0], 13, 13);
    routes[1].addStop(nodes[1], 12, 12);

    routes[2] = new Route("L3", "N4 - N5", nodes[3]);
    routes[2].addStop(nodes[4], 10, 10);

    routes[3] = new Route("L4", "N6 - N7", nodes[5]);
    routes[3].addStop(nodes[6], 14, 14);

    routes[4] = new Route("L5", "N1 - N7", nodes[0]);
    routes[4].addStop(nodes[6], 12, 12);

    walks = new Walk[8];

    // Create walking paths
    walks[0] = new Walk(nodes[3], nodes[6]) {
      @Override
      public int walkingTime() {
        return 1;
      }
    };
    walks[1] = new Walk(nodes[6], nodes[3]) {
      @Override
      public int walkingTime() {
        return 1;
      }
    };
    walks[2] = new Walk(nodes[2], nodes[4]) {
      @Override
      public int walkingTime() {
        return 2;
      }
    };
    walks[3] = new Walk(nodes[4], nodes[2]) {
      @Override
      public int walkingTime() {
        return 2;
      }
    };
    walks[4] = new Walk(nodes[2], nodes[5]) {
      @Override
      public int walkingTime() {
        return 3;
      }
    };
    walks[5] = new Walk(nodes[5], nodes[2]) {
      @Override
      public int walkingTime() {
        return 3;
      }
    };
    walks[6] = new Walk(nodes[4], nodes[5]) {
      @Override
      public int walkingTime() {
        return 1;
      }
    };
    walks[7] = new Walk(nodes[5], nodes[4]) {
      @Override
      public int walkingTime() {
        return 1;
      }
    };

    services = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      services.add(routes[i]);
    }
    for (int i = 0; i < 8; i++) {
      services.add(walks[i]);
    }

    schedule = new Schedule(
        new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime(),
        new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 31).getTime(),
        Schedule.DayOptions.WEEKDAYS
        );

    RouteTimetable rt;
    Route r;

    r = routes[0];
    rt = new RouteTimetable(r, schedule, 6 * 60 + 2, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 8, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 13, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 18, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 22, false);

    r = routes[1];
    rt = new RouteTimetable(r, schedule, 6 * 60 + 5, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 15, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 25, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 33, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 41, false);

    r = routes[2];
    rt = new RouteTimetable(r, schedule, 6 * 60 + 5, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 10, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 15, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 20, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 23, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 26, false);

    r = routes[3];
    rt = new RouteTimetable(r, schedule, 6 * 60 + 2, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 10, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 18, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 26, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 30, false);

    r = routes[4];
    rt = new RouteTimetable(r, schedule, 6 * 60, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 3, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 6, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 9, false);
    rt = new RouteTimetable(r, schedule, 6 * 60 + 12, false);
  }

  @AfterClass
  public static void tearDownClass() {
    for (Stop s : new ArrayList<Stop>(Stop.getAllStops())) {
      Stop.removeStop(s);
    }
    for (Schedule s : new ArrayList<Schedule>(Schedule.getAllSchedules())) {
      Schedule.removeSchedule(s);
    }
    for (Path p : new ArrayList<Path>(Path.getAllPaths())) {
      Path.removePath(p);
    }
  }

  /**
   * Test the findBestItinerary method.
   *
   * This test finds the best itinerary for a journey between N1 and N5 at 
   * 6:02am on Weds 2nd December 2015.
   *
   * There are a number of itineraries which could be used to make the trip:
   *
   *  Option 1
   *  --------
   *  L1 from N1 at 6:02am - N3 at 6:25am;
   *  walk from N3 at 6:25am - N5 at 6:27am
   *
   *  Option 1a
   *  ---------
   *  L1 from N1 at 6:08am - N3 at 6:31am;
   *  walk from N3 at 6:31am - N5 at 6:33am
   *
   *  Option 2
   *  --------
   *  L2 from N1 at 6:18am - N2 at 6:30am;
   *  L1 from N2 at 6:34am - N3 at 6:45am;
   *  walk from N3 at 6:45am - N5 at 6:47am
   *
   *  Option 3
   *  --------
   *  L5 from N1 at 6:03am - N7 at 6:15am;
   *  walk from N7 at 6:15am - N4 at 6:16am;
   *  L3 from N4 at 6:20am - N5 at 6:30am
   *
   *  Option 3a
   *  ---------
   *  L5 from N1 at 6:06am - N7 at 6:18am;
   *  walk from N7 at 6:18am - N4 at 6:19am;
   *  L3 from N4 at 6:20am - N5 at 6:30am
   *
   * It can be seen that Option 1 is the best option, being 3 minutes faster
   * than Option 3. It is therefore expected that this method will return an 
   * itinerary describing Option 1.
   */
  @Test
  public void testFindBestItinerary() {
    // Create itinerary finder between N1 -> N5,
    // at 6:02am on Weds 2nd December 2015
    itineraryFinder = new ItineraryFinder(nodes[0], nodes[4], LocalDateTime.of(2015, Month.DECEMBER, 2, 6, 2, 0));
    assertEquals("expected " + services.size() + ", actual " + Path.getAllPaths().size(), services.size(), Path.getAllPaths().size());
    assertEquals(services, Path.getAllPaths());
    List<JourneyLeg> expected = Arrays.asList(
        new JourneyLeg(
          schedule.nextDepartureRouteTimetable(60 * 6 + 2, nodes[0], routes[0]),
          nodes[0],
          nodes[2]
          ),
        new JourneyLeg(walks[2], 60 * 6 + 25)
        );
    List<JourneyLeg> actual = itineraryFinder.findBestItinerary();
    assertEquals(expected, actual);
  }

  /**
   * Test the findKBestItineraries method.
   *
    *  Option 1
   *  --------
   *  L1 from N1 at 6:02am - N3 at 6:25am;
   *  walk from N3 at 6:25am - N5 at 6:27am
   *
   *  Option 1a
   *  ---------
   *  L1 from N1 at 6:08am - N3 at 6:31am;
   *  walk from N3 at 6:31am - N5 at 6:33am
   *
   *  Option 2
   *  --------
   *  L2 from N1 at 6:18am - N2 at 6:30am;
   *  L1 from N2 at 6:34am - N3 at 6:45am;
   *  walk from N3 at 6:45am - N5 at 6:47am
   *
   *  Option 3
   *  --------
   *  L5 from N1 at 6:03am - N7 at 6:15am;
   *  walk from N7 at 6:15am - N4 at 6:16am;
   *  L3 from N4 at 6:20am - N5 at 6:30am
   *
   *  Option 3a
   *  ---------
   *  L5 from N1 at 6:06am - N7 at 6:18am;
   *  walk from N7 at 6:18am - N4 at 6:19am;
   *  L3 from N4 at 6:20am - N5 at 6:30am
   *
   *  Option 3b
   *  ---------
   *  L5 from N1 at 6:09am - N7 at 6:21am;
   *  walk from N7 at 6:21am - N4 at 6:22am;
   *  L3 from N4 at 6:23am - N5 at 6:33am
   *
   * It can be seen that Option 1 is the best option, being 3 minutes faster
   * than Option 3. It is therefore expected that this method will return an 
   * itinerary describing Option 1.
   */ 
  @Test
  public void testCalculateKLeastTimePaths() {
    // Create itinerary finder between N1 -> N5
    // at 6:02am on Weds 2nd December 2015
    itineraryFinder = new ItineraryFinder(nodes[0], nodes[4], LocalDateTime.of(2015, Month.DECEMBER, 2, 6, 2, 0));
    assertEquals("expected " + services.size() + ", actual " + Path.getAllPaths().size(), services.size(), Path.getAllPaths().size());
    assertEquals(services, Path.getAllPaths());
    List<JourneyLeg> expected1 = Arrays.asList(
        new JourneyLeg(
          schedule.nextDepartureRouteTimetable(60 * 6 + 2, nodes[0], routes[0]),
          nodes[0],
          nodes[2]
          ),
        new JourneyLeg(walks[2], 60 * 6 + 25)
        );
    List<JourneyLeg> expected2 = Arrays.asList(
        new JourneyLeg(
          schedule.nextDepartureRouteTimetable(60 * 6 + 3, nodes[0], routes[4]),
          nodes[0],
          nodes[6]
          ),
        new JourneyLeg(walks[1], 6 * 60 + 15),
        new JourneyLeg(
          schedule.nextDepartureRouteTimetable(60 * 6 + 20, nodes[3], routes[2]),
          nodes[3],
          nodes[4]
          )
        );
    List<JourneyLeg> expected3 = Arrays.asList(
        new JourneyLeg(
          schedule.nextDepartureRouteTimetable(60 * 6 + 4, nodes[0], routes[4]),
          nodes[0],
          nodes[6]
          ),
        new JourneyLeg(walks[1], 6 * 60 + 18),
        new JourneyLeg(
          schedule.nextDepartureRouteTimetable(60 * 6 + 20, nodes[3], routes[2]),
          nodes[3],
          nodes[4]
          )
        );
    List<JourneyLeg> expected4 = Arrays.asList(
        new JourneyLeg(
          schedule.nextDepartureRouteTimetable(60 * 6 + 7, nodes[0], routes[4]),
          nodes[0],
          nodes[6]
          ),
        new JourneyLeg(walks[1], 6 * 60 + 21),
        new JourneyLeg(
          schedule.nextDepartureRouteTimetable(60 * 6 + 23, nodes[3], routes[2]),
          nodes[3],
          nodes[4]
          )
        );
    List<JourneyLeg> expected5 = Arrays.asList(
        new JourneyLeg(
          schedule.nextDepartureRouteTimetable(60 * 6 + 10, nodes[0], routes[4]),
          nodes[0],
          nodes[6]
          ),
        new JourneyLeg(walks[1], 6 * 60 + 24),
        new JourneyLeg(
          schedule.nextDepartureRouteTimetable(60 * 6 + 26, nodes[3], routes[2]),
          nodes[3],
          nodes[4]
          )
        );
    List<List<JourneyLeg>> expected = Arrays.asList(expected1, expected2, expected3, expected4, expected5);
    List<List<JourneyLeg>> actual = itineraryFinder.findBestItineraries(5);
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(expected.get(i), actual.get(i));
    }
  }

  /**
   * Test the TArc#equals method.
   */
  @Test
  public void testTArcEquals() {
    itineraryFinder = new ItineraryFinder(nodes[0], nodes[4], LocalDateTime.of(2015, Month.DECEMBER, 2, 6, 2, 0));
    Stop s1 = new Stop(101, "S101", 0, 0);
    Stop s2 = new Stop(102, "S102", 0, 0);
    Stop s3 = new Stop(103, "S103", 0, 0);
    
    Path p1 = mock(Path.class);
    Path p2 = mock(Path.class);
    when(p1.equals(p2)).thenReturn(false);
    when(p1.equals(p1)).thenReturn(true);
    when(p2.equals(p1)).thenReturn(false);
    when(p2.equals(p2)).thenReturn(true);

    int t1 = 100;
    int t2 = 200;

    ItineraryFinder.TArc a = itineraryFinder.new TArc(s1, s2, p1, t1);
    ItineraryFinder.TArc b = itineraryFinder.new TArc(s1, s3, p1, t1);
    ItineraryFinder.TArc c = itineraryFinder.new TArc(s2, s1, p1, t1);
    ItineraryFinder.TArc d = itineraryFinder.new TArc(s2, s1, p2, t1);
    ItineraryFinder.TArc e = itineraryFinder.new TArc(s2, s1, p1, t2);
    ItineraryFinder.TArc a2 = itineraryFinder.new TArc(s1, s2, p1, t1);
    ItineraryFinder.TArc b2 = itineraryFinder.new TArc(s1, s3, p1, t1);
    ItineraryFinder.TArc nullTArc = null;
    Object o = new Object();
    assertTrue(a.equals(a2));
    assertTrue(b.equals(b2));
    assertFalse(a.equals(b));
    assertFalse(a.equals(c));
    assertFalse(a.equals(d));
    assertFalse(a.equals(e));
    assertFalse(a.equals(o));
    assertFalse(b.equals(a));
    assertFalse(b.equals(c));
    assertFalse(b.equals(d));
    assertFalse(b.equals(e));
    assertFalse(c.equals(a));
    assertFalse(c.equals(b));
    assertFalse(c.equals(d));
    assertFalse(c.equals(e));
    assertFalse(d.equals(a));
    assertFalse(d.equals(b));
    assertFalse(d.equals(c));
    assertFalse(d.equals(e));
    assertFalse(e.equals(a));
    assertFalse(e.equals(b));
    assertFalse(e.equals(c));
    assertFalse(e.equals(d));
    assertFalse(a.equals(nullTArc));

  }
}
