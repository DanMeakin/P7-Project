package main;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date.*;
import main.CapacityDataStore;
import main.Schedule;

import main.Stop;
import main.Schedule;
import org.mockito.Mockito;

public class CapacityDataStoreTest {


    private static Schedule schedule;
    private static Date scheduleStart;
    private static Date scheduleEnd;
    private static Date currentDate;
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

    private static List<Stop> stopsR0;
    private static List<Integer> stopIDsR0;
    private static List<String> stopNamesR0;

    private static List<Stop> stopsR1;
    private static List<Integer> stopIDsR1;
    private static List<String> stopNamesR1;

    private static List<Stop> stopsR2;
    private static List<Integer> stopIDsR2;
    private static List<String> stopNamesR2;

    private static double stopLatitudes;
    private static double stopLongitudes;

    private static List<Route> routes;
    private static List<String> routeNumbers;
    private static List<String> routeDescriptions;

    private static List<RouteTimetable> routeTimetables;
    private static List<Route> routeTimetableRoutes;

    private static Random randomInt;

    private static Schedule weekdaySchedule;
    private static Schedule saturdaySchedule;
    private static Schedule sundaySchedule;

    private static Route mockedRoute1;
    private static Route mockedRoute2;

    private static List<RouteTimetable> busRouteTimetables;
    private static RouteTimetable mockedRouteTimetable;

    private static Bus anotherMockedBus;
    private static RouteTimetable anotherMockedRouteTimetable;

    @Before
    public void setUp() {

        fromDate = new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime();
        toDate = new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 31).getTime();

        busMakes = Arrays.asList("Volvo", "Daf", "Mercedes", "Scania", "MAN");
        busModels = Arrays.asList("7700", "E65", "Citaro", "E8700", "800");
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

        busFleetNumbers = Arrays.asList(100, 101, 102, 103, 104, 105, 106, 107, 108, 109);
        buses = new ArrayList<Bus>();
        for (int i = 0; i < busFleetNumbers.size(); i++) {
            buses.add(
                    new Bus(
                            busFleetNumbers.get(i),
                            busTypes.get(i / 2),
                            busAcquisitionDate = new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 10).getTime()
                    )
            );
        }

        stopIDsR0 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        stopNamesR0 = Arrays.asList("Ritavej", "AAU Busterminal", "Boulevarden", "Nytorv", "Universitet", "Vesterbro", "Kastetvej",
                "Hadsundvej");
        stopsR0 = new ArrayList<Stop>();
        for (int i = 0; i < stopIDsR0.size(); i++) {
            stopsR0.add(
                    new Stop(
                            stopIDsR0.get(i),
                            stopNamesR0.get(i),
                            stopLatitudes = 50.2,
                            stopLongitudes = 50.3
                    )
            );
        }

        stopIDsR1 = Arrays.asList(9, 10, 11, 12, 13, 14, 15);
        stopNamesR1 = Arrays.asList("Humlebakken", "Østre Alle", "Bornholmsgade", "Nyhavnsgade", "City Sud", "Prinsengade", "Danmarksgade",
                "Istedgade", "Hasserisvej", "Hobrovej", "Kong Christians Alle", "Ny Kærvej", "Van Boetzelaerstraat");
        stopsR1 = new ArrayList<Stop>();
        for (int i = 0; i < stopIDsR1.size(); i++) {
            stopsR1.add(
                    new Stop(
                            stopIDsR1.get(i),
                            stopNamesR1.get(i),
                            stopLatitudes = 50.6,
                            stopLongitudes = 50.7
                    )
            );
        }

        stopIDsR2 = Arrays.asList(16, 17, 18, 19, 20, 999);
        stopNamesR2 = Arrays.asList("Istedgade", "Hasserisvej", "Hobrovej", "Kong Christians Alle", "Ny Kærvej", "Van Boetzelaerstraat");
        stopsR2 = new ArrayList<Stop>();
        for (int i = 0; i < stopIDsR2.size(); i++) {
            stopsR2.add(
                    new Stop(
                            stopIDsR2.get(i),
                            stopNamesR2.get(i),
                            stopLatitudes = 50.1,
                            stopLongitudes = 49.8
                    )
            );
        }

        Route route0 = new Route("4", "Ritavej-Somewhere", stopsR0.get(0));
        Route route1 = new Route("81X", "Humlebakken-Somewhere else", stopsR1.get(0));
        Route route2 = new Route("21", "Istedgade-Somewhere else", stopsR2.get(0));

        routes = Arrays.asList(route0, route1, route2);

        scheduleStart = new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime();
        scheduleEnd = new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 31).getTime();
        schedule = new Schedule(
                scheduleStart,
                scheduleEnd,
                Schedule.DayOptions.WEEKDAYS
        );

        //System.out.println(routes.get(0).getNumber());
        //RouteTimetable rtt1 = new RouteTimetable(routeTimetableRoutes.get(0), schedule, (10 * 60 + 30) * (1 * 10), true);
        //System.out.println(rtt1.getRoute().getNumber());

        for (int i = 0; i < stopsR0.size()-1; i++) {
            routes.get(0).addStop(
                    stopsR0.get(i+1),
                    4,
                    5
            );
        }

        for (int i = 0; i < stopsR1.size()-1; i++) {
            routes.get(0).addStop(
                    stopsR1.get(i+1),
                    3,
                    4
            );
        }

        for (int i = 0; i < stopsR2.size()-1; i++) {
            routes.get(0).addStop(
                    stopsR2.get(i+1),
                    6,
                    7
            );
        }

        routeTimetableRoutes = Arrays.asList(routes.get(0), routes.get(0), routes.get(0), routes.get(0), routes.get(1),
                routes.get(1), routes.get(1), routes.get(2), routes.get(2), routes.get(2));
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



    }
    @Test
    public void testWriteBusStateChange(){
        /*
        buses.get(0).arrivesAtStop(buses.get(0).getRouteTimetable().getRoute().getStops().get(0));
        buses.get(0).passengersBoard(100);
        buses.get(0).passengersExit(8);
        CapacityDataStore.writeBusStateChange(buses.get(0));
        buses.get(0).leavesStop();
        buses.get(0).arrivesAtStop(buses.get(0).getRouteTimetable().getRoute().getStops().get(1));
        buses.get(0).passengersBoard(250);
        buses.get(0).passengersExit(6);
        CapacityDataStore.writeBusStateChange(buses.get(0));
        buses.get(0).leavesStop();
        buses.get(0).arrivesAtStop(buses.get(0).getRouteTimetable().getRoute().getStops().get(2));
        buses.get(0).passengersBoard(120);
        buses.get(0).passengersExit(17);
        CapacityDataStore.writeBusStateChange(buses.get(0));
        buses.get(0).leavesStop();
        */
        //System.out.println(CapacityDataStore.readHistoricRequestedStopCrowdedness(fromDate, toDate, routeTimetables.get(0), stopsR0.get(0)).size());
        //System.out.println(CapacityDataStore.readHistoricCurrentStopCrowdedness(fromDate, toDate, routeTimetables.get(0), stopsR0.get(0), stopsR0.get(1)).get(20));
        //System.out.println(CapacityDataStore.getColumnHeaderPosition(CapacityDataStore.ColumnHeaderNames.BUS_ID));
        CapacityCalculator c = new CapacityCalculator();
        //System.out.println(c.calculateCrowdedness(1, fromDate, toDate, routeTimetables.get(0), stopsR0.get(0)));
        System.out.println(c.calculateCrowdedness(1, fromDate, toDate, 4, routeTimetables.get(0), stopsR0.get(0), stopsR0.get(1)));


    }

    /*
    @Test
    public void testReadHistoricCrowdedness(){
        System.out.println(CapacityDataStore.readHistoricRequestedStopCrowdedness(fromDate, toDate, routeTimetables.get(0), stopsR0.get(1)));
    }
    */
}

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

