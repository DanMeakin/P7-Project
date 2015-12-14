package main;

import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

public class CapacityDataStoreWriterTest {

    private static File dataStore = new File("data/dataStore.csv");

    private static Random randomInt;

    private static Schedule schedule;
    private static Date scheduleStart;
    private static Date scheduleEnd;
    private static Date fromDate;
    private static Date toDate;

    private static List<BusType> busTypes;
    private static List<String> busMakes;
    private static List<String> busModels;
    private static List<Integer> busSeatingCapacities;
    private static List<Integer> busStandingCapacities;

    private static List<Bus> buses;
    private static List<Integer> busFleetNumbers;
    private static Date busAcquisitionDate;

    private static List<Stop> stopsRoute0;
    private static List<Integer> stopIDsRoute0;
    private static List<String> stopNamesRoute0;

    private static List<Stop> stopsRoute1;
    private static List<Integer> stopIDsRoute1;
    private static List<String> stopNamesRoute1;

    private static List<Stop> stopsRoute2;
    private static List<Integer> stopIDsRoute2;
    private static List<String> stopNamesRoute2;

    private static double stopLatitudes;
    private static double stopLongitudes;

    private static List<Route> busRoutes;
    private static List<String> routeNumbers;
    private static List<String> routeDescriptions;

    private static List<RouteTimetable> routeTimetables;
    private static List<Route> routeTimetableRoutes;

    private static Bus mockedBus0;
    private static Bus mockedBus1;
    private static Bus mockedBus2;

    private static List<RouteTimetable> busRouteTimetables;
    private static RouteTimetable mockedRouteTimetable;

    List<Bus> mockedBuses = new ArrayList<>();

    @Before
    public void setUp() {

        fromDate = new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime();
        toDate = new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 31).getTime();

        busMakes = Arrays.asList("Volvo", "Daf", "Mercedes-Benz");
        busModels = Arrays.asList("7700", "E65", "Citaro");
        busSeatingCapacities = Arrays.asList(64, 58, 61, 45, 62);
        busStandingCapacities = Arrays.asList(24, 28, 31, 39, 25);
        busTypes = new ArrayList<BusType>();
        for (int i = 0; i < busMakes.size(); i++) {
            busTypes.add(
                    new BusType(
                            busMakes.get(i),
                            busModels.get(i),
                            busSeatingCapacities.get(i),
                            busStandingCapacities.get(i)
                    )
            );
        }

        busFleetNumbers = Arrays.asList(100, 101, 102);
        buses = new ArrayList<Bus>();
        for (int i = 0; i < busFleetNumbers.size(); i++) {
            buses.add(
                    new Bus(
                            busFleetNumbers.get(i),
                            busTypes.get(i),
                            busAcquisitionDate = new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 10).getTime()
                    )
            );
        }

        stopIDsRoute0 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        stopNamesRoute0 = Arrays.asList("Ritavej", "AAU Busterminal", "Boulevarden", "Nytorv", "Universitet", "Vesterbro", "Kastetvej",
                "Hadsundvej");
        stopsRoute0 = new ArrayList<Stop>();
        for (int i = 0; i < stopIDsRoute0.size(); i++) {
            stopsRoute0.add(
                    new Stop(
                            stopIDsRoute0.get(i),
                            stopNamesRoute0.get(i),
                            stopLatitudes = 50.2,
                            stopLongitudes = 50.3
                    )
            );
        }

        stopIDsRoute1 = Arrays.asList(9, 10, 11, 12, 13, 14, 15);
        stopNamesRoute1 = Arrays.asList("Humlebakken", "Østre Alle", "Bornholmsgade", "Nyhavnsgade", "City Sud", "Prinsengade", "Danmarksgade",
                "Istedgade", "Hasserisvej", "Hobrovej", "Kong Christians Alle", "Ny Kærvej", "Van Boetzelaerstraat");
        stopsRoute1 = new ArrayList<Stop>();
        for (int i = 0; i < stopIDsRoute1.size(); i++) {
            stopsRoute1.add(
                    new Stop(
                            stopIDsRoute1.get(i),
                            stopNamesRoute1.get(i),
                            stopLatitudes = 50.6,
                            stopLongitudes = 50.7
                    )
            );
        }

        stopIDsRoute2 = Arrays.asList(16, 17, 18, 19, 20, 999);
        stopNamesRoute2 = Arrays.asList("Istedgade", "Hasserisvej", "Hobrovej", "Kong Christians Alle", "Ny Kærvej", "Van Boetzelaerstraat");
        stopsRoute2 = new ArrayList<Stop>();
        for (int i = 0; i < stopIDsRoute2.size(); i++) {
            stopsRoute2.add(
                    new Stop(
                            stopIDsRoute2.get(i),
                            stopNamesRoute2.get(i),
                            stopLatitudes = 50.1,
                            stopLongitudes = 49.8
                    )
            );
        }

        Route route0 = new Route("4", "Ritavej-Somewhere", stopsRoute0.get(0));
        Route route1 = new Route("81X", "Humlebakken-Somewhere else", stopsRoute1.get(0));
        Route route2 = new Route("21", "Istedgade-Somewhere else", stopsRoute2.get(0));

        busRoutes = Arrays.asList(route0, route1, route2);

        scheduleStart = new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime();
        scheduleEnd = new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 31).getTime();
        schedule = new Schedule(
                scheduleStart,
                scheduleEnd,
                Schedule.DayOptions.WEEKDAYS
        );

        for (int i = 0; i < stopsRoute0.size()-1; i++) {
            busRoutes.get(0).addStop(
                    stopsRoute0.get(i+1),
                    4,
                    5
            );
        }

        for (int i = 0; i < stopsRoute1.size()-1; i++) {
            busRoutes.get(0).addStop(
                    stopsRoute1.get(i+1),
                    3,
                    4
            );
        }

        for (int i = 0; i < stopsRoute2.size()-1; i++) {
            busRoutes.get(0).addStop(
                    stopsRoute2.get(i+1),
                    6,
                    7
            );
        }

        routeTimetableRoutes = Arrays.asList(busRoutes.get(0), busRoutes.get(1), busRoutes.get(2));
        routeTimetables = new ArrayList<RouteTimetable>();
        for (int i = 0; i < routeTimetableRoutes.size(); i++) {
            routeTimetables.add(
                    new RouteTimetable(
                            routeTimetableRoutes.get(i),
                            schedule,
                            (10 * 60 + 30) * (i * 10),
                            true
                    )
            );
        }

        for (int i = 0; i < routeTimetables.size(); i++) {
            schedule.addRouteTimetable(routeTimetables.get(i), buses.get(i));
            buses.get(i).startRoute(routeTimetables.get(i));
        }


        for(int i = 0; i < buses.size(); i++){
            mockedBuses.add(mock(Bus.class));
        }

        /*
        when(mockedBuses.get(0).getFleetNumber()).thenReturn(buses.get(0).getFleetNumber());
        when(mockedBuses.get(0).getRouteTimetable().getRoute().getNumber()).thenReturn(buses.get(0).getRouteTimetable().getRoute().getNumber());
        when(mockedBuses.get(0).getRouteTimetable().getRoute().getDescription()).thenReturn(buses.get(0).getRouteTimetable().getRoute().getDescription());
        when(mockedBuses.get(0).getRouteTimetable().getRouteTimetableID()).thenReturn(buses.get(0).getRouteTimetable().getRouteTimetableID());
        when(mockedBuses.get(0).getStop().getID()).thenReturn(buses.get(0).getStop().getID());
        when(mockedBuses.get(0).getStop().getName()).thenReturn(buses.get(0).getStop().getName());
        when(mockedBuses.get(0).getNumPassengersExited()).thenReturn(buses.get(0).getNumPassengersExited());
        when(mockedBuses.get(0).getNumPassengersBoarded()).thenReturn(buses.get(0).getNumPassengersBoarded());
        when(mockedBuses.get(0).getNumPassengers()).thenReturn(buses.get(0).getNumPassengers());
        when(mockedBuses.get(0).getOccupationRate()).thenReturn(buses.get(0).getOccupationRate());
        
        when(mockedBuses.get(1).getFleetNumber()).thenReturn(buses.get(1).getFleetNumber());
        when(mockedBuses.get(1).getRouteTimetable().getRoute().getNumber()).thenReturn(buses.get(1).getRouteTimetable().getRoute().getNumber());
        when(mockedBuses.get(1).getRouteTimetable().getRoute().getDescription()).thenReturn(buses.get(1).getRouteTimetable().getRoute().getDescription());
        when(mockedBuses.get(1).getRouteTimetable().getRouteTimetableID()).thenReturn(buses.get(1).getRouteTimetable().getRouteTimetableID());
        when(mockedBuses.get(1).getStop().getID()).thenReturn(buses.get(1).getStop().getID());
        when(mockedBuses.get(1).getStop().getName()).thenReturn(buses.get(1).getStop().getName());
        when(mockedBuses.get(1).getNumPassengersExited()).thenReturn(buses.get(1).getNumPassengersExited());
        when(mockedBuses.get(1).getNumPassengersBoarded()).thenReturn(buses.get(1).getNumPassengersBoarded());
        when(mockedBuses.get(1).getNumPassengers()).thenReturn(buses.get(1).getNumPassengers());
        when(mockedBuses.get(1).getOccupationRate()).thenReturn(buses.get(1).getOccupationRate());
        when(mockedBuses.get(2).getFleetNumber()).thenReturn(buses.get(2).getFleetNumber());
        when(mockedBuses.get(2).getRouteTimetable().getRoute().getNumber()).thenReturn(buses.get(2).getRouteTimetable().getRoute().getNumber());
        when(mockedBuses.get(2).getRouteTimetable().getRoute().getDescription()).thenReturn(buses.get(2).getRouteTimetable().getRoute().getDescription());
        when(mockedBuses.get(2).getRouteTimetable().getRouteTimetableID()).thenReturn(buses.get(2).getRouteTimetable().getRouteTimetableID());
        when(mockedBuses.get(2).getStop().getID()).thenReturn(buses.get(2).getStop().getID());
        when(mockedBuses.get(2).getStop().getName()).thenReturn(buses.get(2).getStop().getName());
        when(mockedBuses.get(2).getNumPassengersExited()).thenReturn(buses.get(2).getNumPassengersExited());
        when(mockedBuses.get(2).getNumPassengersBoarded()).thenReturn(buses.get(2).getNumPassengersBoarded());
        when(mockedBuses.get(2).getNumPassengers()).thenReturn(buses.get(2).getNumPassengers());
        when(mockedBuses.get(2).getOccupationRate()).thenReturn(buses.get(2).getOccupationRate());
        */

    }
    @Test
    public void testWriteBusStateChange() {
        String[] expectedBusData = new String[buses.size()];
        for (int i = 0; i < buses.size(); i++) {
            buses.get(i).arrivesAtStop(buses.get(i).getRouteTimetable().getRoute().getStops().get(0));
            buses.get(i).passengersBoard(21);
            buses.get(i).passengersExit(8);
            CapacityDataStoreWriter.writeBusStateChange(buses.get(i));
            expectedBusData[i] = (CapacityDataStoreWriter.getCurrentDayMonth() + "," + CapacityDataStoreWriter.getCurrentTime() + "," + buses.get(i).getFleetNumber() + "," +
                    buses.get(i).getRouteTimetable().getRoute().getNumber() + "," + buses.get(i).getRouteTimetable().getRoute().getDescription() + "," +
                    buses.get(i).getRouteTimetable().getID() + "," + buses.get(i).getLastStop().getID() + "," + buses.get(i).getLastStop().getName() + "," +
                    buses.get(i).getNumPassengersExited() + "," + buses.get(i).getNumPassengersBoarded() + "," + buses.get(i).getNumPassengers() + "," +
                    buses.get(i).getSeatedOccupationRate() + "," + buses.get(i).getTotalOccupationRate() + ",");

            buses.get(i).leavesStop();
        }

        List<String> actualBusData = new ArrayList<>();
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(dataStore));
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                actualBusData.add(line);
            }
        } catch (IOException ex) {
            System.out.println("Failed to read from file" + dataStore.getAbsolutePath());
            ex.printStackTrace();
        }

        for (int i = 0; i < buses.size(); i++) {
            System.out.println(expectedBusData[i] + " entry " + i + " From expectedBusData");
            System.out.println(actualBusData.get(i) + " entry " + i + " From actualBusData");
        }

        for (int i = 0; i < actualBusData.size(); i++) {
            assertEquals(expectedBusData[i], actualBusData.get(i));
        }
    }

    @After
    public void fileDeleted(){

    }





    //System.out.println(CapacityDataStoreWriter.readHistoricRequestedStopCrowdedness(fromDate, toDate, routeTimetables.get(0), stopsRoute0.get(0)).size());
    //System.out.println(CapacityDataStoreWriter.readHistoricCurrentStopCrowdedness(fromDate, toDate, routeTimetables.get(0), stopsRoute0.get(0), stopsRoute0.get(1)).get(20));
    //System.out.println(CapacityDataStoreWriter.getColumnHeaderPosition(CapacityDataStoreWriter.ColumnHeaderNames.BUS_ID));
    //CapacityCalculator c = new CapacityCalculator();
    //System.out.println(c.calculateCrowdedness(1, fromDate, toDate, routeTimetables.get(0), stopsRoute0.get(0)));
    //System.out.println(c.calculateCrowdedness(1, fromDate, toDate, 4, routeTimetables.get(0), stopsRoute0.get(0), stopsRoute0.get(1)));


}

    /*
    @Test
    public void testReadHistoricCrowdedness(){
        System.out.println(CapacityDataStoreWriter.readHistoricRequestedStopCrowdedness(fromDate, toDate, routeTimetables.get(0), stopsRoute0.get(1)));
    }
    */


        /*
        // Create 6 mocked routeTimetables, with the first three on route1 and
        // the second three on route 2.
        busRouteTimetables = new ArrayList<RouteTimetable>();
        for (int i = 0; i < 6; i++) {
            RouteTimetable rt = mock(RouteTimetable.class);
            if (i < 3) {
                when(rt.getRoute()).thenReturn(mockedRoute1);
            } else {
                when(rt.getRoute()).thenReturn(mockedRoute2);
            }
            busRouteTimetables.add(rt);
        }
        anotherMockedRouteTimetable = mock(RouteTimetable.class);
        when(anotherMockedRouteTimetable.getRoute()).thenReturn(mockedRoute2);
        anotherMockedBus = mock(Bus.class);
        when(anotherMockedBus.equals(anotherMockedBus)).thenReturn(true);
    }
    @After
    public void tearDown() {
        // This must be done to allow schedule to be recreated in next tests
        Schedule.removeSchedule(schedule);
        Schedule.removeSchedule(weekdaySchedule);
        Schedule.removeSchedule(saturdaySchedule);
        Schedule.removeSchedule(sundaySchedule);
    }
    */