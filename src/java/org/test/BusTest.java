package org.test;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import main.Bus;
import main.BusType;
import main.Schedule;

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

  private static BusType mockedBusType;
  private static int busTypeSeatedCapacity;
  private static int busTypeStandingCapacity;
  private static String busTypeMake;
  private static String busTypeModel;

  private static Schedule mockedSchedule;

  @BeforeClass
  public static void setUpClass() {
    fleetNumber = 100;
    acquisitionDate = new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime();

    bus = new Bus(fleetNumber, mockedBusType, acquisitionDate);

    mockedBusType = mock(BusType.class);
    busTypeSeatedCapacity = 50;
    busTypeStandingCapacity = 30;
    busTypeMake = "Volvo";
    busTypeModel = "7700";
    when(mockedBusType.getMake()).thenReturn(busTypeMake);
    when(mockedBusType.getModel()).thenReturn(busTypeModel);
    when(mockedBusType.getSeatedCapacity()).thenReturn(busTypeSeatedCapacity);
    when(mockedBusType.getStandingCapacity()).thenReturn(busTypeStandingCapacity);
  }

  @Before
  public void setUp() {

  }

  /**
   * Tests are incomplete.
   *
   * This is a dummy test to flag up the fact tests are incomplete and must be
   * finished.
   */
  @Test
  public void testsIncomplete() {
    // Please make sure additional tests are added as required.
    fail("Bus unit tests are not yet complete!!");
  }

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

  @Rule
  public ExpectedException thrown = ExpectedException.none();

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
  public void testCreateStopWithDuplicateID() throws IllegalArgumentException {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Bus with fleet number " + fleetNumber + " already exists");
    BusType thisMockedBusType = mock(BusType.class);
    Date thisAcquisitionDate = new GregorianCalendar(2011, Calendar.MARCH, 22).getTime();
    new Bus(fleetNumber, thisMockedBusType, thisAcquisitionDate);
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
    assertEquals(numPassengers, 0); // Should be 0, as nobody is on the bus yet
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

  /**
   * testGetMake() tests that the bus returns its make.
   */
  @Test
  public void testGetModel() {
    String thisModel = bus.getModel();
    assertEquals(thisModel, busTypeModel);
  }

  /**
   * testGetType() tests that the bus properly returns its type.
   */
  @Test
  public void testGetType() {
    BusType thisType = bus.getType();
    assertEquals(thisType, mockedBusType);
  }
}
