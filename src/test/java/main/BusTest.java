package main;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import main.Bus;
import main.BusType;
import main.RouteTimetable;
import main.Stop;

/**
 * BusTest class contains a series of unit tests for the Bus class.
 *
 * The Bus class represents a bus owned and operated by the local transit
 * company. The following tests ensure that the required behaviour is exposed
 * by the Bus class.
 */
public class BusTest {
  private static Bus bus;
  private static int fleetNumber;
  private static Date acquisitionDate;
  private static int initialPassengers;

  private static Bus stoppedBus;
  private static int stoppedFleetNumber;
  private static Date stoppedAcquisitionDate;
  private static int stoppedInitialPassengers;

  private static BusType mockedBusType;
  private static int busTypeSeatedCapacity;
  private static int busTypeStandingCapacity;
  private static String busTypeMake;
  private static String busTypeModel;

  private static RouteTimetable mockedRouteTimetable;

  private static Stop mockedStop;

  @BeforeClass
  public static void setUpClass() {
    fleetNumber = 100;
    acquisitionDate = new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime();
    initialPassengers = 10;
      
    stoppedFleetNumber = 665;
    stoppedAcquisitionDate = new GregorianCalendar(2013, Calendar.APRIL, 23).getTime();
    stoppedInitialPassengers = 20;

    mockedBusType = mock(BusType.class);
    busTypeSeatedCapacity = 50;
    busTypeStandingCapacity = 30;
    busTypeMake = "Volvo";
    busTypeModel = "7700";
    when(mockedBusType.getMake()).thenReturn(busTypeMake);
    when(mockedBusType.getModel()).thenReturn(busTypeModel);
    when(mockedBusType.getSeatedCapacity()).thenReturn(busTypeSeatedCapacity);
    when(mockedBusType.getStandingCapacity()).thenReturn(busTypeStandingCapacity);

    mockedStop = mock(Stop.class);
    mockedRouteTimetable = mock(RouteTimetable.class);
  }

  @Before
  public void setUp() {
    bus = new Bus(fleetNumber, mockedBusType, acquisitionDate);
    bus.startRoute(mockedRouteTimetable);
    bus.arrivesAtStop(mockedStop);
    bus.passengersBoard(initialPassengers);
    bus.leavesStop();
    
    stoppedBus = new Bus(stoppedFleetNumber, mockedBusType, stoppedAcquisitionDate);
    stoppedBus.startRoute(mockedRouteTimetable);
    stoppedBus.arrivesAtStop(mockedStop);
    stoppedBus.passengersBoard(stoppedInitialPassengers);
  }

  @After
  public void tearDown() {
    Bus.removeBus(bus);
    Bus.removeBus(stoppedBus);
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * testCreateBus() tests the creation of a bus instance.
   *
   * A bus instance requires a number of parameters to be passed before it can
   * be created. The bus must be created with:-
   *
   *  * an acquisition date;
   *  * an associated bus type; and
   *  * a fleet number.
   *
   * This method ensures that a bus is instantiated properly.
   */
  @Test
  public void testCreateBus() {
    try {
      new Bus(123, mockedBusType, acquisitionDate);
    } catch (IllegalArgumentException e) {
      fail("unable to create bus: " + e);
    }
  }

  /**
   * testCreateDuplicateBus() tests the creation of a duplicate bus instance.
   *
   * It should not be possible to create duplicate buses. This test ensures
   * that this criterion is respected.
   *
   */
  @Test
  public void testCreateDuplicateBus() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Bus with fleet number " + fleetNumber + " already exists");
    new Bus(fleetNumber, mockedBusType, acquisitionDate);
  }

  /**
   * testCreateBusWithDuplicateFleetNumber() tests to ensure that a bus
   * with duplicate fleet number cannot be created.
   *
   * It should not be possible to create a bus which has the same fleet number
   * as another. This method tests for this condition.
   */
  @Test
  public void testCreateBusWithDuplicateID() throws IllegalArgumentException {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Bus with fleet number " + fleetNumber + " already exists");
    BusType thisMockedBusType = mock(BusType.class);
    Date thisAcquisitionDate = new GregorianCalendar(2011, Calendar.MARCH, 22).getTime();
    new Bus(fleetNumber, thisMockedBusType, thisAcquisitionDate);
  }

  /**
   * testArrivesAtStop method.
   *
   * The arrivesAtStop method ensures that the state and location of a Bus is
   * properly noted throughout the journey. A stopping event will involve 
   * passengers getting on and off the bus.
   */
  @Test
  public void testArrivesAtStop() {
    bus.arrivesAtStop(mockedStop);
    assertTrue(bus.isAtStop());
    assertEquals(bus.getStop(), mockedStop);
  }

  /**
   * testArrivesAtStop method when already at stop.
   *
   * A bus cannot arrive at a stop when already at one. Attempting to call
   * this method in such circumstances should result in an exception being
   * thrown.
   */
  @Test
  public void testArrivesAtStopWhenAtStop() {
    String msg = "bus is already at a stop";
    thrown.expect(UnsupportedOperationException.class);
    thrown.expectMessage(msg);
    bus.arrivesAtStop(mockedStop);
    bus.arrivesAtStop(mockedStop);
  }

  /**
   * testGetNumPassengersBoarded method.
   *
   * The getNumPassengersBoarded method is a getter method which returns the
   * number of passengers who boarded the bus at the last Stop.
   */
  @Test
  public void testGetNumPassengersBoarded() {
    bus.arrivesAtStop(mockedStop);
    bus.passengersBoard(20);
    bus.leavesStop();
    assertEquals(bus.getNumPassengersBoarded(), 20);
    bus.arrivesAtStop(mockedStop);
    bus.leavesStop();
    assertEquals(bus.getNumPassengersBoarded(), 0);
  }

  /**
   * testGetNumPassengersExited method.
   *
   * The getNumPassengersExited method is a getter method which returns the
   * number of passengers who exited the bus at the last Stop.
   */
  @Test
  public void testGetNumPassengersExited() {
    bus.arrivesAtStop(mockedStop);
    bus.passengersExit(4);
    bus.leavesStop();
    assertEquals(bus.getNumPassengersExited(), 4);
    bus.arrivesAtStop(mockedStop);
    bus.leavesStop();
    assertEquals(bus.getNumPassengersExited(), 0);
  }

  /**
   * testGetBusType method.
   *
   * This tests the getBusType getter method.
   */
  @Test
  public void testGetBusType() {
    assertEquals(bus.getBusType(), mockedBusType);
  }

  /**
   * testGetModel method.
   *
   * This tests the getModel getter method.
   */
  @Test
  public void testGetModel() {
    assertEquals(bus.getModel(), busTypeModel);
  }

  /**
   * testPassengersBoard method.
   *
   * The passengersBoard method is used where passengers board the vehicle. It
   * takes the number of passengers entering the bus at a stop as its argument.
   */
  @Test
  public void testPassengersBoard() {
    stoppedBus.passengersBoard(5);
    assertEquals(stoppedBus.getNumPassengersBoarded(), 5);
    assertEquals(stoppedInitialPassengers + 5, stoppedBus.getNumPassengers());
  }

  /**
   * testPassengersBoard method when bus not at stop.
   *
   * Passengers may only board when bus is at a stop. Calling the
   * passengersBoard method when this is not the case should cause an exception
   * to be thrown.
   */
  @Test
  public void testPassengersBoardWhenNotAtStop() {
    String msg = "passengers can only board at a stop; bus is currently between stops";
    thrown.expect(UnsupportedOperationException.class);
    thrown.expectMessage(msg);
    bus.passengersBoard(5);
  }

  /**
   * testPassengersExit method.
   *
   * The passengersExit method is used where passengers exit the vehicle. It
   * takes the number of passengers exiting the bus at a stop as its argument.
   */
  @Test
  public void testPassengersExit() {
    stoppedBus.passengersExit(7);
    assertEquals(stoppedBus.getNumPassengersExited(), 7);
    assertEquals(stoppedInitialPassengers - 7, stoppedBus.getNumPassengers());
  }

  /**
   * testPassengersExit method with an invalid number of passengers.
   *
   * This test ensures that the passengersExit method fails where a number of
   * passengers exits the bus which is greater than the current number of
   * passengers.
   */
  @Test
  public void testInvalidPassengersExit() {
    String msg = "more passengers exiting than are currently on bus (" + stoppedBus.getNumPassengers() + " passengers on bus, 100 exiting)";
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage(msg);
    stoppedBus.passengersExit(100);
  }

  /* testPassengersExit method when bus is not at a stop.
   *
   * Calling passengersExit when a bus is not at a stop should result in an
   * exception being thrown. This test ensures that this is the case.
   */
  @Test
  public void testPassengersExitWhenNotAtStop() {
    String msg = "passengers can only exit at a stop; bus is currently between stops";
    thrown.expect(UnsupportedOperationException.class);
    thrown.expectMessage(msg);
    bus.passengersExit(1);
  }
  /**
   * testLeavesStop method.
   *
   * The leavesStop method ensures that the state and location of a Bus is
   * properly noted throughout the journey. A stopping event will involve 
   * passengers getting on and off the bus.
   */
  @Test
  public void testLeavesStop() {
    stoppedBus.leavesStop();
    assertFalse(stoppedBus.isAtStop());
    assertNull(stoppedBus.getStop());
  }

  /**
   * testLeavesStop method when not at stop.
   *
   * A bus cannot leave a stop when it is not at a stop. If the leavesStop
   * method is called in such circumstances, an exception should be thrown.
   */
  @Test
  public void testLeaveStopWhenNotAtStop() {
    String msg = "bus is not at a stop";
    thrown.expect(UnsupportedOperationException.class);
    thrown.expectMessage(msg);
    bus.leavesStop();
  }
  /**
   * testEndRoute method.
   *
   * The endRoute method terminates the bus's operation on a route. It can
   * only be called when the bus is at a stop and empty.
   */
  @Test
  public void testEndRoute() {
    stoppedBus.passengersExit(stoppedInitialPassengers);
    stoppedBus.endRoute();
    assertFalse(stoppedBus.isOnRoute());
  }

  /**
   * testEndRoute method when called with passengers on board.
   *
   * The endRoute method should fail when the bus currently has passengers
   * or is not at a stop. This tests this behaviour.
   */
  @Test
  public void testEndRouteWithPassengersOnboard() {
    String msg = "unable to end route between stops or with passengers on board";
    thrown.expect(UnsupportedOperationException.class);
    thrown.expectMessage(msg);
    stoppedBus.endRoute();
  }

  /**
   * testEndRoute method when called between stops.
   *
   * The endRoute method should fail when the bus currently has passengers
   * or is not at a stop. This tests this behaviour.
   */
  @Test
  public void testEndRouteBetweenStops() {
    String msg = "unable to end route between stops or with passengers on board";
    thrown.expect(UnsupportedOperationException.class);
    thrown.expectMessage(msg);
    stoppedBus.passengersExit(stoppedInitialPassengers);
    stoppedBus.leavesStop();
    stoppedBus.endRoute();
  }

  /**
   * testGetAcquisitionDate() tests the existence of a getter method.
   */
  @Test
  public void testGetAcquisitionDate() {
    Date thisAcquisitionDate = bus.getAcquisitionDate();
    assertEquals(thisAcquisitionDate, acquisitionDate);
  }

  /**
   * testGetFleetNumber() tests the existence of a getter method.
   */
  @Test
  public void testGetFleetNumber() {
    int thisFleetNumber = bus.getFleetNumber();
    assertEquals(thisFleetNumber, fleetNumber);
  }

  /**
   * testGetNumPassengers() tests the existence of a getter method.
   */
  @Test
  public void testGetNumPassengers() {
    int numPassengers = bus.getNumPassengers();
    assertEquals(numPassengers, 10); // Should be 10, as bus has 10 passengers at start
  }

  /**
   * testGetSeatedCapacity() tests that the bus returns total seated capacity.
   *
   * Each Bus is aggregated to a BusType. This method tests that a Bus obtains 
   * seated capacity details from its BusType.
   */
  @Test
  public void testGetSeatedCapacity() {
    int seatedCapacity = bus.getSeatedCapacity();
    assertEquals(seatedCapacity, busTypeSeatedCapacity);
  }

  /** 
   * testGetStandingCapacity() tests that the bus returns total standing capacity.
   *
   * Each Bus is aggregated to a BusType. This method tests that a Bus obtains 
   * standing capacity details from its BusType.
   */
  @Test
  public void testGetStandingCapacity() {
    int standingCapacity = bus.getStandingCapacity();
    assertEquals(standingCapacity, busTypeStandingCapacity);
  }

  /**
   * testGetTotalCapacity() tests that the bus returns total capacity.
   *
   * Each Bus is aggregated to a BusType. This method tests that a Bus obtains
   * total capacity details from its BusType.
   */
  @Test
  public void testGetTotalCapacity() {
    int totalCapacity = bus.getTotalCapacity();
    assertEquals(totalCapacity, busTypeSeatedCapacity + busTypeStandingCapacity);
  }

  /**
   * testGetMake() tests that the bus returns its make.
   */
  @Test
  public void testGetMake() {
    String thisMake = bus.getMake();
    assertEquals(thisMake, busTypeMake);
  }

}
