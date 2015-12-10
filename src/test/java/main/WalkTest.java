package main;

import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Arrays;

import main.Stop;

public class WalkTest {

  private static Walk walk;
  private static Stop walkStart;
  private static Stop walkEnd;
  private static Stop otherStop1;
  private static Stop otherStop2;
  private static int walkDistance;

  private static Walk invertedWalk;
  private static Walk walkWithSameOrigin;
  private static Walk walkWithSameDestination;
  private static Walk anotherWalk;

  /**
   * Set-up before testing begins.
   */
  @BeforeClass
  public static void setUpClass() {
    walkStart = mock(Stop.class);
    walkEnd = mock(Stop.class);
    otherStop1 = mock(Stop.class);
    otherStop2 = mock(Stop.class);

    walkDistance = 5000; // 5km distance
    when(walkStart.distanceTo(walkEnd)).thenReturn(walkDistance);
    when(walkEnd.distanceTo(walkStart)).thenReturn(walkDistance);
  }

  /**
   * Set-up before each test.
   */
  @Before
  public void setUp() {
    walk = new Walk(walkStart, walkEnd);
    invertedWalk = new Walk(walkEnd, walkStart);
    walkWithSameOrigin = new Walk(walkStart, otherStop1);
    walkWithSameDestination = new Walk(otherStop2, walkEnd);
    anotherWalk = new Walk(otherStop1, otherStop2);
  }

  /**
   * Tear-down after each test.
   */
  @After
  public void tearDown() {
    for (Walk w : Walk.getAllWalks()) {
      w.remove();
    }
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * Test create duplicate Walk.
   *
   * The Walk class should prevent duplicates from being created. This test
   * ensures this behaviour occurs.
   */
  @Test
  public void testCreateDuplicateWalk() {
    thrown.expect(IllegalArgumentException.class);
    String msg = "Path " + walk.getDescription() + " already exists";
    thrown.expectMessage(msg);
    new Walk(walkStart, walkEnd);
  }

  /**
   * Test getAllWalks static method.
   *
   * getAllWalks should return every instance of walk present within the system.
   * This test ensures this behaviour.
   */
  @Test
  public void testGetAllWalks() {
    List<Walk> expected = Arrays.asList(
        walk,
        invertedWalk, 
        walkWithSameOrigin, 
        walkWithSameDestination,
        anotherWalk)
      ;
    List<Walk> actual = Walk.getAllWalks();
    assertEquals(expected, actual);
  }

  /**
   * Test removePath static method.
   *
   * The removePath static method is found in the Path parent class. As this
   * is an abstract class, this method is tested within the WalkTest suite.
   */
  @Test
  public void testRemovePath() {
    Path p = new Walk(mock(Stop.class), mock(Stop.class));
    assertTrue(Path.getAllPaths().contains(p));
    int sizeBefore = Path.getAllPaths().size();
    Path.removePath(p);
    assertFalse(Path.getAllPaths().contains(p));
    int sizeAfter = Path.getAllPaths().size();
    assertEquals(sizeBefore, sizeAfter + 1);
  }

  /**
   * Test findPathsIncludingStop static method.
   *
   * The findPathsIncludingStop static method is found in the Path parent class.
   * As this is an abstract class, this method is tested within the WalkTest
   * suite.
   */
  @Test
  public void testFindPathsIncludingStop() {
    // Create non-walk Path objects
    Path p1 = mock(Path.class);
    Path.addPath(p1);
    when(p1.includesStop(otherStop1)).thenReturn(true);
    Path p2 = mock(Path.class);
    Path.addPath(p2);
    when(p2.includesStop(otherStop2)).thenReturn(true);

    List<Path> actual = Path.findPathsIncludingStop(otherStop1);
    List<Path> expected = Arrays.asList(walkWithSameOrigin, anotherWalk, p1);
    assertEquals(expected, actual);

    actual = Path.findPathsIncludingStop(otherStop2);
    expected = Arrays.asList(walkWithSameDestination, anotherWalk, p2);
    assertEquals(expected, actual);
  }

  /**
   * Test findInverted method.
   *
   * The findInverted method should return the inverse of a given walk, i.e. that
   * walk but in reverse. If no such walk exists, the method should return null.
   */
  @Test
  public void testFindInverted() {
    assertEquals(walk.findInverted(), invertedWalk);
    assertEquals(walkWithSameOrigin.findInverted(), null);
    assertEquals(walkWithSameDestination.findInverted(), null);
  }

  /**
   * Test the journeyTimeBetweenStops method.
   *
   * This method calculates the time it takes to travel from one stop to the
   * next on a particular Walk. As there are only two stops within a Walk
   * object, this method only accepts two stops in the correct order.
   *
   * The journey time is calculated with reference to the walkingTime method,
   * and this is reflected in this test. It should ignore the isRushHour flag
   * value.
   */
  @Test
  public void testJourneyTimeBetweenStops() {
    assertEquals(
        walk.journeyTimeBetweenStops(walkStart, walkEnd, false),
        walk.walkingTime()
        );
    assertEquals(
        walk.journeyTimeBetweenStops(walkStart, walkEnd, false),
        walk.journeyTimeBetweenStops(walkStart, walkEnd, true)
        );
  }

  /**
   * Test the journeyTimeBetweenStops method with invalid stop.
   *
   * The journeyTimeBetweenStops method should throw an IllegalArgumentException
   * if passed a stop which does not exist within the walk.
   */
  @Test
  public void testJourneyTimeBetweenStopsWithInvalidStop() {
    Stop invalidStop = mock(Stop.class);
    thrown.expect(IllegalArgumentException.class);
    String msg = "stop " + invalidStop + " is not on path";
    thrown.expectMessage(msg);
    walk.journeyTimeBetweenStops(invalidStop, walkEnd, false);
  }

  /**
   * Test the journeyTimeBetweenStops method with stops in wrong order.
   *
   * The journeyTimeBetweenStops method should throw an IllegalArgumentException
   * if passed stops in the wrong order.
   */
  @Test
  public void testJourneyTimeBetweenStopsWithStopsInWrongOrder() {
    thrown.expect(IllegalArgumentException.class);
    String msg = "this path does not travel from " + walkEnd + " to " + walkStart;
    thrown.expectMessage(msg);
    walk.journeyTimeBetweenStops(walkEnd, walkStart, false);
  }

  /**
   * Test the walkingTime method.
   *
   * This method calculates the time it takes to travel from one stop to the
   * next on a particular Walk.
   *
   * It should calculate the walking time with reference to the distance
   * between the start and end stops.
   */
  @Test
  public void testWalkingTime() {
    // Calculate time in minutes using t = d / v
    int expected = walkStart.distanceTo(walkEnd) / Walk.getWalkingSpeed() * 60;
    int actual = walk.walkingTime();
    assertEquals(expected, actual);
  }

  /**
   * Test the equals method.
   *
   * Two walks are equal only when they start and end in the same place. A walk
   * with the same path as a route is not equal to it.
   */
  @Test
  public void testEquals() {
    // Create non-walk path
    Path p = mock(Path.class);
    assertTrue(walk.equals(walk));
    assertFalse(walk.equals(walkWithSameOrigin));
    assertFalse(walk.equals(walkWithSameDestination));
    assertFalse(walk.equals(p));
  }
}
