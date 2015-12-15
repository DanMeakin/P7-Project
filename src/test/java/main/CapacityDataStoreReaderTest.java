package main;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * @author Ivo on 14-12-2015.
 */
public class CapacityDataStoreReaderTest {

    /*
    //private static File dataStore = new File("data/dataStore.csv");
    private static File test = new File("data/test.csv");

    private static Random randomInt;

    private static Schedule schedule;
    private static Date scheduleStart;
    private static Date scheduleEnd;
    private static Date fromDate;
    private static Date toDate = new Date();


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

        for (int i = 0; i < stopsRoute0.size() - 1; i++) {
            busRoutes.get(0).addStop(
                    stopsRoute0.get(i + 1),
                    4,
                    5
            );
        }

        for (int i = 0; i < stopsRoute1.size() - 1; i++) {
            busRoutes.get(0).addStop(
                    stopsRoute1.get(i + 1),
                    3,
                    4
            );
        }

        for (int i = 0; i < stopsRoute2.size() - 1; i++) {
            busRoutes.get(0).addStop(
                    stopsRoute2.get(i + 1),
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


        for (int i = 0; i < buses.size(); i++) {
            mockedBuses.add(mock(Bus.class));
        }
    }

    @Test
    public void testFilterHistoricData() {
        CapacityDataStoreReader.filterHistoricData(routeTimetables.get(0), stopsRoute0.get(0), CapacityDataStoreWriter.ColumnHeaderNames.SEATED_OCCUPATION_RATE).get(0);
        for(int i = 0; i < CapacityDataStoreReader.filterHistoricData(routeTimetables.get(0), stopsRoute0.get(0)
    , CapacityDataStoreWriter.ColumnHeaderNames.SEATED_OCCUPATION_RATE).size(); i++){
            System.out.println(CapacityDataStoreReader.filterHistoricData(routeTimetables.get(0), stopsRoute0.get(0)
                    , CapacityDataStoreWriter.ColumnHeaderNames.SEATED_OCCUPATION_RATE).get(0));
        }
    }

    @Test
    public void testGetColumnHeaderData(){
        System.out.println("Position is: " + CapacityDataStoreReader.getColumnHeaderPosition(CapacityDataStoreWriter.ColumnHeaderNames.SEATED_OCCUPATION_RATE));
    }

    @Test
    public void testReadHistoricStopCrowdedness() {
    //   System.out.println((Integer.toString(routeTimetables.get(0).getID()) + " " + Integer.toString(routeTimetables.get(0).getStartTime()) + " " + Integer.toString(stopsRoute0.get(0).getID()) + " " + routeTimetables.get(0).getSchedule().getOperatingDay().toString()));
        CapacityDataStoreReader.readHistoricStopCrowdedness(routeTimetables.get(0), stopsRoute0.get(0));
    }

    public void testgetNumOfDayBeforeCurrentForFromDate(){
        assertEquals(CapacityDataStoreReader.getNumOfDayBeforeCurrentForFromDate(), -90);
    }

    public void testSetNumOfDayBeforeCurrentForFromDate(){
        CapacityDataStoreReader.setNumOfDayBeforeCurrentForFromDate(-60);
        assertEquals(CapacityDataStoreReader.getNumOfDayBeforeCurrentForFromDate(), -60);
    }
    */
}