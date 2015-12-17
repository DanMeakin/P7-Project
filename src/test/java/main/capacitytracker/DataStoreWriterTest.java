package main.capacitytracker;

import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Method;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import main.Bus;
import main.RouteTimetable;
import main.Stop;

public class DataStoreWriterTest {

  private static DataStoreWriter dataStoreWriter;
  
  private static List<Bus> buses = new ArrayList<>();
  private static List<String> fleetNumbers = new ArrayList<>();
  private static List<String> rtIDs = new ArrayList<>();
  private static List<String> routeNumbers = new ArrayList<>();
  private static List<String> routeDescriptions = new ArrayList<>();
  private static List<String> startingTimes = new ArrayList<>();
  private static List<String> operatingDays = new ArrayList<>();
  private static List<String> stopIDs = new ArrayList<>();
  private static List<String> passengersOnArrival = new ArrayList<>();
  private static List<String> passengersExiting = new ArrayList<>();
  private static List<String> passengersBoarding = new ArrayList<>();
  private static List<String> passengersOnDeparture = new ArrayList<>();
  private static List<String> seatedCapacities = new ArrayList<>();
  private static List<String> standingCapacities = new ArrayList<>();
  private static List<String> totalCapacities = new ArrayList<>();
  private static List<String> seatedOccupancyLevels = new ArrayList<>();
  private static List<String> standingOccupancyLevels = new ArrayList<>();
  private static List<String> totalOccupancyLevels = new ArrayList<>();

  @BeforeClass
  public static void setUpClass() {
    dataStoreWriter = new DataStoreWriter("data/mock");

    for (int i = 0; i < 10; i++) {
      int passengersBoarded = (int) Math.random() * 20;
      int passengersExited = (int) Math.random() * 20;
      int passengersOnArrival = (int) Math.random() * 30;
      int passengersOnDeparture = passengersOnArrival + passengersBoarded - passengersExited;
      int seatedCapacity = (int) Math.random() * 60;
      int standingCapacity = (int) Math.random() * 40; 
      int totalCapacity = seatedCapacity + standingCapacity;
      double seatedOccupancyLevel = Math.random();
      double standingOccupancyLevel = Math.random();
      double totalOccupancyLevel = Math.random();

      int fleetNumber = (int) Math.random() * 1_000;
      int rtID = (int) Math.random() * 10_000;
      String routeNumber = Integer.toString((i + 1) * 5);
      String routeDescription = "Test Description";
      int startingTime = (int) (Math.random() * 24 * 60 + Math.random() * 60);
      String operatingDay = "WEEKDAYS";
      int stopID = (int) Math.random() * 10_000_000;

      Bus bus = mock(Bus.class);
      when(bus.getFleetNumber()).thenReturn(fleetNumber);
      when(bus.getRouteTimetable().getID()).thenReturn(rtID);
      when(bus.getRouteTimetable().getRoute().getNumber()).thenReturn(routeNumber);
      when(bus.getRouteTimetable().getRoute().getDescription()).thenReturn(routeDescription);
      when(bus.getRouteTimetable().getStartTime()).thenReturn(startingTime);
      when(bus.getRouteTimetable().getSchedule().getOperatingDay().toString()).thenReturn(operatingDay);
      when(bus.getStop().getID()).thenReturn(stopID);
      when(bus.getNumPassengersBoarded()).thenReturn(passengersBoarded);
      when(bus.getNumPassengersExited()).thenReturn(passengersExited);
      when(bus.getNumPassengers()).thenReturn(passengersOnDeparture);
      when(bus.getSeatedCapacity()).thenReturn(seatedCapacity);
      when(bus.getStandingCapacity()).thenReturn(standingCapacity);
      when(bus.getTotalCapacity()).thenReturn(totalCapacity);
      when(bus.getSeatedOccupancyLevel()).thenReturn(seatedOccupancyLevel);
      when(bus.getStandingOccupancyLevel()).thenReturn(standingOccupancyLevel);
      when(bus.getTotalOccupancyLevel()).thenReturn(totalOccupancyLevel);

      DataStoreWriterTest.buses.add(bus);
      DataStoreWriterTest.fleetNumbers.add(Integer.toString(fleetNumber));
      DataStoreWriterTest.rtIDs.add(Integer.toString(rtID));
      DataStoreWriterTest.routeNumbers.add(routeNumber);
      DataStoreWriterTest.routeDescriptions.add(routeDescription);
      DataStoreWriterTest.startingTimes.add(String.format("%2d:%2d", startingTime / 60, startingTime % 60));
      DataStoreWriterTest.operatingDays.add(operatingDay);
      DataStoreWriterTest.stopIDs.add(Integer.toString(stopID));
      DataStoreWriterTest.passengersOnArrival.add(Integer.toString(passengersOnArrival));
      DataStoreWriterTest.passengersExiting.add(Integer.toString(passengersExited));
      DataStoreWriterTest.passengersBoarding.add(Integer.toString(passengersBoarded));
      DataStoreWriterTest.passengersOnDeparture.add(Integer.toString(passengersOnDeparture));
      DataStoreWriterTest.seatedCapacities.add(Integer.toString(seatedCapacity));
      DataStoreWriterTest.standingCapacities.add(Integer.toString(standingCapacity));
      DataStoreWriterTest.totalCapacities.add(Integer.toString(totalCapacity));
      DataStoreWriterTest.seatedOccupancyLevels.add(Double.toString(seatedOccupancyLevel));
      DataStoreWriterTest.standingOccupancyLevels.add(Double.toString(standingOccupancyLevel));
      DataStoreWriterTest.totalOccupancyLevels.add(Double.toString(totalOccupancyLevel));
    }
  }

  @Test
  public void testGenerateRecord() {
    Class<?>[] params = new Class[1];
    params[0] = Bus.class;
    Method method = null;
    try {
      method = dataStoreWriter.getClass().getDeclaredMethod("generateRecord", params);
      method.setAccessible(true);
    } catch (NoSuchMethodException e) {
      fail("unable to set generateRecord accessible");
    }
    for (int i = 0; i < buses.size(); i++) {
      List<String> expectedRecord = Arrays.asList(
          LocalDateTime.now().toString(),
          fleetNumbers.get(i),
          rtIDs.get(i),
          routeNumbers.get(i),
          routeDescriptions.get(i),
          startingTimes.get(i),
          operatingDays.get(i),
          stopIDs.get(i),
          passengersOnArrival.get(i),
          passengersExiting.get(i),
          passengersBoarding.get(i),
          passengersOnDeparture.get(i),
          seatedCapacities.get(i),
          standingCapacities.get(i),
          totalCapacities.get(i),
          seatedOccupancyLevels.get(i),
          standingOccupancyLevels.get(i),
          totalOccupancyLevels.get(i)
          );
      List<String> actualRecord = null;
      try {
        Object bs = method.invoke(buses.get(i));
        if (bs instanceof List<?>) {
          actualRecord = (List<String>) bs;
        }
      } catch (Exception e) {
        fail("unable to access method");
      }
      assertEquals(expectedRecord, actualRecord);
    }
  }

}
