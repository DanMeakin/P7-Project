package main.model;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * BusTypeTest class contains a series of unit tests for the BusType class.
 *
 * The BusType class represents a particular type of Bus. Each object will have
 * aggregated to it a number of buses.
 */
public class BusTypeTest {

  private static List<BusType> busTypes;
  private static List<String> makes;
  private static List<String> models;
  private static List<Integer> seatedCapacities;
  private static List<Integer> standingCapacities;

  /**
   * Set-up before testing.
   *
   * A BusType has aggregated to it a number of buses. We need to create some
   * mock buses to test the BusType functionality. We also need some example
   * BusTypes for testing purposes.
   */
  @BeforeClass
  public static void setUpClass() {
    makes = Arrays.asList("Volvo", "Mercedes", "Volvo", "Scania");
    models = Arrays.asList("7700", "Sprinter", "3010B", "X-103");
    seatedCapacities = Arrays.asList(50, 30, 60, 45);
    standingCapacities = Arrays.asList(20, 5, 22, 16);
    busTypes = new ArrayList<BusType>();
    for (int i = 0; i < makes.size(); i++) {
      busTypes.add(
          new BusType(
            makes.get(i),
            models.get(i),
            seatedCapacities.get(i),
            standingCapacities.get(i)
            )
          );
    }
  }

  /**
   * Test BusType make getter method.
   */
  @Test
  public void testGetMake() {
    for (int i = 0; i < busTypes.size(); i++) {
      assertEquals(busTypes.get(i).getMake(), makes.get(i));
    }
  }

  /**
   * Test BusType model getter method.
   */
  @Test
  public void testGetModel() {
    for (int i = 0; i < busTypes.size(); i++) {
      assertEquals(busTypes.get(i).getModel(), models.get(i));
    }
  }

  /**
   * Test BusType standingCapacity getter method.
   */
  @Test
  public void testGetStandingCapacity() {
    for (int i = 0; i < busTypes.size(); i++) {
      assertEquals(busTypes.get(i).getStandingCapacity(), (int) standingCapacities.get(i));
    }
  }

  /**
   * Test BusType seatedCapacity getter method.
   */
  @Test 
  public void testGetSeatedCapacity() {
    for (int i = 0; i < busTypes.size(); i++) {
      assertEquals(busTypes.get(i).getSeatedCapacity(), (int) seatedCapacities.get(i));
    }
  }

  /**
   * Test BusType totalCapacity getter method.
   */
  @Test 
  public void testGetTotalCapacity() {
    for (int i = 0; i < busTypes.size(); i++) {
      int expectedTotalCapacity = standingCapacities.get(i) + seatedCapacities.get(i);
      assertEquals(busTypes.get(i).getTotalCapacity(), expectedTotalCapacity); 
    }
  }
}
