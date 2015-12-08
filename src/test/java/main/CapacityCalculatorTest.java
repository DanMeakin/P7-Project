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

/**
 * @author Ivo on 7-12-2015.
 */
public class CapacityCalculatorTest {

    private static Bus mockedBus;
    private static Bus anotherMockedBus;
    private static Bus thirdMockedBus;

    private static Route mockedRoute;
    private static Route anotherMockedRoute;
    private static Route thirdMockedRoute;

    private static Stop mockedStop;
    private static Stop anotherMockedStop;
    private static Stop thirdMockedStop;

    long date = System.currentTimeMillis();
    Date arbitraryDate = new Date(date);
    SimpleDateFormat form = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
    String str = form.format(arbitraryDate);
    //DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    private int mockedFleetNumber = 143;
    private String mockedRouteNumber = "225";
    private String mockedRouteDescription = "North-South";
    private int mockedStopID = 121;
    private int mockedNumOfPassengersExited = 12;
    private int mockedNumOfPassengersBoarded = 20;
    private int mockedNumOfPassengers = mockedNumOfPassengersBoarded - mockedNumOfPassengersExited;

    private int anotherMockedFleetNumber = 97;
    private String anotherMockedRouteNumber = "103";
    private String anotherMockedRouteDescription = "East-West";
    private int anotherMockedStopID = 33;
    private int anotherMockedNumOfPassengersExited = 10;
    private int anotherMockedNumOfPassengersBoarded = 15;
    private int anotherMockedNumOfPassengers = anotherMockedNumOfPassengersBoarded - anotherMockedNumOfPassengersExited;

    private int thirdMockedFleetNumber = 90;
    private String thirdMockedRouteNumber = "125";
    private String thirdMockedRouteDescription = "East-West";
    private int thirdMockedStopID = 205;
    private int thirdMockedNumOfPassengersExited = 1;
    private int thirdMockedNumOfPassengersBoarded = 9;
    private int thirdMockedNumOfPassengers = thirdMockedNumOfPassengersBoarded - thirdMockedNumOfPassengersExited;


    @Before
    public void setUp() {
        mockedBus = mock(Bus.class);
        when(mockedBus.getFleetNumber()).thenReturn(mockedFleetNumber);
        when(mockedBus.getStop().getID()).thenReturn(mockedStopID);
        when(mockedBus.getNumPassengersExited()).thenReturn(mockedNumOfPassengersExited);
        when(mockedBus.getNumPassengersBoarded()).thenReturn(mockedNumOfPassengersBoarded);
        when(mockedBus.getNumPassengers()).thenReturn(mockedNumOfPassengers);
        when(mockedBus.getRouteTimetable().getRoute().getNumber()).thenReturn(mockedRouteNumber);
        when(mockedBus.getRouteTimetable().getRoute().getDescription()).thenReturn(mockedRouteDescription);

        when(mockedRoute.getNumber()).thenReturn(mockedRouteNumber);
        when(mockedRoute.getDescription()).thenReturn(mockedRouteDescription);

        when(mockedStop.getID()).thenReturn(mockedStopID);

        anotherMockedBus = mock(Bus.class);
        when(anotherMockedBus.getFleetNumber()).thenReturn(anotherMockedFleetNumber);
        when(anotherMockedBus.getStop().getID()).thenReturn(mockedStopID);
        when(anotherMockedBus.getNumPassengersExited()).thenReturn(anotherMockedNumOfPassengersExited);
        when(anotherMockedBus.getNumPassengersBoarded()).thenReturn(anotherMockedNumOfPassengersBoarded);
        when(anotherMockedBus.getNumPassengers()).thenReturn(anotherMockedNumOfPassengers);
        when(anotherMockedBus.getRouteTimetable().getRoute().getNumber()).thenReturn(anotherMockedRouteNumber);
        when(anotherMockedBus.getRouteTimetable().getRoute().getDescription()).thenReturn(anotherMockedRouteDescription);

        when(anotherMockedRoute.getNumber()).thenReturn(anotherMockedRouteNumber);
        when(anotherMockedRoute.getDescription()).thenReturn(anotherMockedRouteDescription);

        when(anotherMockedStop.getID()).thenReturn(anotherMockedStopID);

        thirdMockedBus = mock(Bus.class);
        when(thirdMockedBus.getFleetNumber()).thenReturn(thirdMockedFleetNumber);
        when(thirdMockedBus.getStop().getID()).thenReturn(mockedStopID);
        when(thirdMockedBus.getNumPassengersExited()).thenReturn(thirdMockedNumOfPassengersExited);
        when(thirdMockedBus.getNumPassengersBoarded()).thenReturn(thirdMockedNumOfPassengersBoarded);
        when(thirdMockedBus.getNumPassengers()).thenReturn(thirdMockedNumOfPassengers);
        when(thirdMockedBus.getRouteTimetable().getRoute().getNumber()).thenReturn(thirdMockedRouteNumber);
        when(thirdMockedBus.getRouteTimetable().getRoute().getDescription()).thenReturn(thirdMockedRouteDescription);

        when(thirdMockedRoute.getNumber()).thenReturn(thirdMockedRouteNumber);
        when(thirdMockedRoute.getDescription()).thenReturn(thirdMockedRouteDescription);

        when(thirdMockedStop.getID()).thenReturn(thirdMockedStopID);
    }


    public double testCalculateCrowdedness(int currentCrowdedness, List<double>)

    public int testCalculateCrowdedness(){
        assertEquals()
    }

}
