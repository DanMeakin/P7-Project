package main.routeplanner;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.time.*;

import main.Path;
import main.Schedule;
import main.Route;
import main.RouteTimetable;
import main.Stop;
import main.Walk;
import main.capacitytracker.CapacityCalculator;

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

  private static ItineraryFinder itineraryFinder;

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
    // Creating stops creates walks, so remove all walks before
    // continuing - walks are manually created for testing purposes
    for (Path p : new ArrayList<>(Path.getAllPaths())) {
      Path.removePath(p);
    }

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
        LocalDate.of(2015, Month.JANUARY, 1),
        LocalDate.of(2015, Month.DECEMBER, 31),
        Schedule.DayOption.WEEKDAYS
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
    
    // Create itinerary finder between N1 -> N5,
    // at 6:02am on Weds 2nd December 2015
    itineraryFinder = new ItineraryFinder(nodes[0], nodes[4], LocalDateTime.of(2015, Month.DECEMBER, 2, 6, 2, 0));
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
    assertEquals("expected " + services.size() + ", actual " + Path.getAllPaths().size(), services.size(), Path.getAllPaths().size());
    assertEquals(services, Path.getAllPaths());
    Itinerary expected = new Itinerary(
        LocalDate.of(2015, Month.DECEMBER, 2),
        Arrays.asList(
          new ItineraryLeg(
            LocalDate.of(2015, Month.DECEMBER, 2),
            schedule.nextDepartureRouteTimetable(60 * 6 + 2, nodes[0], routes[0]),
            nodes[0],
            nodes[2]
            ),
          new ItineraryLeg(
            LocalDate.of(2015, Month.DECEMBER, 2),
            walks[2], 
            60 * 6 + 25
            )
          )
        );
    Itinerary actual = itineraryFinder.findBestItinerary();
    assertEquals(expected, actual);
  }

  /**
   * Test the findBestItineraries method.
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
   *  Option 3c
   *  ---------
   *  L5 from N1 at 6:12am - N7 at 6:24am;
   *  walk from N7 at 6:24am - N4 at 6:26am;
   *  L3 from N4 at 6:26am - N5 at 6:36am
   *
   * It can be seen that Option 1 is the best option, being 3 minutes faster
   * than Option 3. It is therefore expected that this method will return an 
   * itinerary describing Option 1, followed by Option 3, 3a, 3b & 3c.
   *
   * It should be noted that Option 1a is superior to Option 3c. However due
   * to the way in which the findBestItineraries method is implemented, where
   * two itineraries will arrive at the same time, the shortest route (by
   * travelling time) will be selected. The next route to be selected will be
   * the shortest route departing after this route. As a result, any route
   * departing before the previous route will be disregarded, even if it would
   * arrive before the route actually returned.
   */ 
  @Test
  public void testFindBestItineraries() {
    assertEquals("expected " + services.size() + ", actual " + Path.getAllPaths().size(), services.size(), Path.getAllPaths().size());
    assertEquals(services, Path.getAllPaths());
    LocalDate date = LocalDate.of(2015, Month.DECEMBER, 2);
    List<ItineraryLeg> expected1 = Arrays.asList(
        new ItineraryLeg(
          date,
          schedule.nextDepartureRouteTimetable(60 * 6 + 2, nodes[0], routes[0]),
          nodes[0],
          nodes[2]
          ),
        new ItineraryLeg(
          date, 
          walks[2], 
          60 * 6 + 25
          )
        );
    List<ItineraryLeg> expected2 = Arrays.asList(
        new ItineraryLeg(
          date,
          schedule.nextDepartureRouteTimetable(60 * 6 + 3, nodes[0], routes[4]),
          nodes[0],
          nodes[6]
          ),
        new ItineraryLeg(
          date, 
          walks[1],
          6 * 60 + 15
          ),
        new ItineraryLeg(
          date,
          schedule.nextDepartureRouteTimetable(60 * 6 + 20, nodes[3], routes[2]),
          nodes[3],
          nodes[4]
          )
        );
    List<ItineraryLeg> expected3 = Arrays.asList(
        new ItineraryLeg(
          date,
          schedule.nextDepartureRouteTimetable(60 * 6 + 4, nodes[0], routes[4]),
          nodes[0],
          nodes[6]
          ),
        new ItineraryLeg(
          date, 
          walks[1],
          6 * 60 + 18
          ),
        new ItineraryLeg(
          date,
          schedule.nextDepartureRouteTimetable(60 * 6 + 20, nodes[3], routes[2]),
          nodes[3],
          nodes[4]
          )
        );
    List<ItineraryLeg> expected4 = Arrays.asList(
        new ItineraryLeg(
          date,
          schedule.nextDepartureRouteTimetable(60 * 6 + 7, nodes[0], routes[4]),
          nodes[0],
          nodes[6]
          ),
        new ItineraryLeg(
          date,
          walks[1],
          6 * 60 + 21
          ),
        new ItineraryLeg(
          date,
          schedule.nextDepartureRouteTimetable(60 * 6 + 23, nodes[3], routes[2]),
          nodes[3],
          nodes[4]
          )
        );
    List<ItineraryLeg> expected5 = Arrays.asList(
        new ItineraryLeg(
          date,
          schedule.nextDepartureRouteTimetable(60 * 6 + 10, nodes[0], routes[4]),
          nodes[0],
          nodes[6]
          ),
        new ItineraryLeg(
          date,
          walks[1], 
          6 * 60 + 24
          ),
        new ItineraryLeg(
          date,
          schedule.nextDepartureRouteTimetable(60 * 6 + 26, nodes[3], routes[2]),
          nodes[3],
          nodes[4]
          )
        );
     
    List<Itinerary> expected = Arrays.asList(
        new Itinerary(date, expected1), 
        new Itinerary(date, expected2),
        new Itinerary(date, expected3),
        new Itinerary(date, expected4), 
        new Itinerary(date, expected5)
        );
    List<Itinerary> actual = itineraryFinder.findBestItineraries(5);
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(expected.get(i), actual.get(i));
    }
  }

  /**
   * Test setFilter method.
   */
  @Test
  public void testSetFilter() {
    assertEquals(CapacityCalculator.CrowdednessIndicator.RED, itineraryFinder.getFilter());
    itineraryFinder.setFilter(CapacityCalculator.CrowdednessIndicator.ORANGE);
    assertEquals(CapacityCalculator.CrowdednessIndicator.ORANGE, itineraryFinder.getFilter());
  }

  /**
   * Test the getDate method.
   */
  @Test
  public void testGetDate() {
    assertEquals(LocalDate.of(2015, Month.DECEMBER, 2), itineraryFinder.getDate());
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
    assertTrue(a.equals(a2));
    assertTrue(b.equals(b2));
    assertFalse(a.equals(b));
    assertFalse(a.equals(c));
    assertFalse(a.equals(d));
    assertFalse(a.equals(e));
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

  /**
   * Test the TArc#departureTime method.
   *
   * This method should return the departure time of the next service leaving
   * the starting node after time.
   *
   * It should return the value of CostEstimator.UNCONNECTED if no further 
   * departures are available.
   */
  @Test
  public void testDepartureTime() {
    ItineraryFinder.TArc tArc = itineraryFinder.new TArc(nodes[0], nodes[2], routes[0], 60 * 6);
    ItineraryFinder.TArc lateTArc = itineraryFinder.new TArc(nodes[0], nodes[2], routes[0], 60 * 20);

    assertEquals(60 * 6 + 2, tArc.departureTime());
    assertEquals(CostEstimator.UNCONNECTED, lateTArc.departureTime());
  }

  /**
   * Test the TArc#journeyTime method.
   *
   * This method should return the journey time of the next service leaving
   * the starting node after time.
   *
   * It should return the value of CostEstimator.UNCONNECTED if no further 
   * departures are available.
   */
  @Test
  public void testJourneyTime() {
    RouteTimetable rushHourRT = new RouteTimetable(routes[0], schedule, 60 * 10 + 15, true);

    ItineraryFinder.TArc tArc = itineraryFinder.new TArc(nodes[0], nodes[2], routes[0], 60 * 6);
    ItineraryFinder.TArc lateTArc = itineraryFinder.new TArc(nodes[0], nodes[2], routes[0], 60 * 20);
    ItineraryFinder.TArc walkTArc = itineraryFinder.new TArc(nodes[3], nodes[6], walks[0], 60 * 10);
    ItineraryFinder.TArc rushHourTArc = itineraryFinder.new TArc(nodes[0], nodes[2], routes[0], 60 * 10);


    assertEquals(23, tArc.journeyTime());
    assertEquals(CostEstimator.UNCONNECTED, lateTArc.journeyTime());
    assertEquals(1, walkTArc.journeyTime());
    assertEquals(23, rushHourTArc.journeyTime());
  }

  /**
   * Test the TArc#pi method.
   */
  @Test
  public void testPi() {
    Walk mockWalk = mock(Walk.class);
    when(mockWalk.walkingTime()).thenReturn(20);
    ItineraryFinder.TArc tArc = itineraryFinder.new TArc(mock(Stop.class), mock(Stop.class), mockWalk, 60 * 10);
    ItineraryFinder.TArc midnightTArc = itineraryFinder.new TArc(mock(Stop.class), mock(Stop.class), mockWalk, 60 * 24 - 1);
    assertEquals(20, tArc.pi());
    assertEquals(20, midnightTArc.pi());
  }
}
