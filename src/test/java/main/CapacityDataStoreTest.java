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
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

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

    final Date date = Mockito.mock(Date.class);

    final SimpleDateFormat yearMonth = Mockito.mock(SimpleDateFormat.class);

    @Before
    public void setUp() {
        mockedBus = mock(Bus.class);

        Mockito.when(date.getTime()).thenReturn(30L);
        Mockito.when(mon)

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
        when(anotherMockedBus.getStop().getID()).thenReturn(anotherMockedStopID);
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
        when(thirdMockedBus.getStop().getID()).thenReturn(thirdMockedStopID);
        when(thirdMockedBus.getNumPassengersExited()).thenReturn(thirdMockedNumOfPassengersExited);
        when(thirdMockedBus.getNumPassengersBoarded()).thenReturn(thirdMockedNumOfPassengersBoarded);
        when(thirdMockedBus.getNumPassengers()).thenReturn(thirdMockedNumOfPassengers);
        when(thirdMockedBus.getRouteTimetable().getRoute().getNumber()).thenReturn(thirdMockedRouteNumber);
        when(thirdMockedBus.getRouteTimetable().getRoute().getDescription()).thenReturn(thirdMockedRouteDescription);

        when(thirdMockedRoute.getNumber()).thenReturn(thirdMockedRouteNumber);
        when(thirdMockedRoute.getDescription()).thenReturn(thirdMockedRouteDescription);

        when(thirdMockedStop.getID()).thenReturn(thirdMockedStopID);
    }

    /*
     * start mess
     *
    @test
    public void mockedWriteBusStateChange(){
        CapacityDataStore.writeBusStateChange(mockedBus);
        CapacityDataStore.writeBusStateChange(anotherMockedBus);
        CapacityDataStore.writeBusStateChange(thirdMockedBus);
            try
            {
                PrintWriter writer = new PrintWriter(new StringWriter());
                Bus[] busList = {mockedBus, anotherMockedBus, thirdMockedBus};
                for (int i = 0; i < busList.length; i++) {
                    writer.write((arbitraryDate.toString()) + ", ");
                    writer.write(Integer.toString(busList[i].getFleetNumber()) + ", ");
                    writer.write((busList[i].getRouteTimetable().getRoute().getNumber()) + ", ");
                    writer.write((busList[i].getRouteTimetable().getRoute().getDescription()) + ", ");
                    writer.write(Integer.toString(busList[i].getStop().getID()) + ", ");
                    writer.write(Integer.toString(busList[i].getNumPassengersExited()) + ", ");
                    writer.write(Integer.toString(busList[i].getNumPassengersBoarded()) + ", ");
                    writer.write(Integer.toString(busList[i].getNumPassengers()) + "\n");
                    writer.flush();
                    writer.close();
                }
            }
            catch(IOException ex) {
                System.out.println("Failed to save bus state update to");
                ex.printStackTrace();
            }
        assertEquals("busStatemocked.txt", "busState.txt");
    }

    @test
    public void testGetDataToStore(){
    }

    @test
    public void testWriteChangedStateData() {
        CapacityDataStore.writeBusStateChange(mockedBus);
        CapacityDataStore.writeBusStateChange(anotherMockedBus);
        CapacityDataStore.writeBusStateChange(thirdMockedBus);
        try {
            String busState;
            BufferedWriter mockedWriter = Mockito.mock(BufferedWriter.class);
            Bus[] busList = {mockedBus, anotherMockedBus, thirdMockedBus};
                for (int i = 0; i < busList.length; i++) {
                Mockito.when(mockedWriter.write()).toString(busState).append(arbitraryDate.toString() + ", " + busList[i].getRouteTimetable().getRoute() + ", " + busList[i].getStop() + "\n"));
                }
        }
        catch (IOException ex) {
            System.out.println("Failed to save bus state update to");
            ex.printStackTrace();
        }
    }

    @test
    public void testReadHistoricData() {
        CapacityDataStore.readHistoricData(arbitraryDate, mockedRoute, mockedStop);
        try {
            BufferedReader mockedReader = Mockito.mock(BufferedReader.class);
            Mockito.when(mockedReader.readLine()).thenReturn(arbitraryDate.toString() + ", " + mockedBus.getRouteTimetable().getRoute() + ", " + mockedBus.getStop() + "\n");
        } catch (IOException ex) {
            System.out.println("Failed to save bus state update to");
            ex.printStackTrace();
        }
        assertEquals("output to be determined", "busState.txt");
    }
    *
    * end mess
    */
}
