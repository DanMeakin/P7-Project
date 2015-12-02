package main;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.Date;
import main.CapacityDataStore;
import main.Schedule;
import java.io.InputStream;


import main.Stop;
import main.Schedule;

public class CapacityDataStoreTest {

    private static Schedule mockedSchedule;
    private static Date scheduleStart;
    private static Date scheduleEnd;

    private static Stop mockedStop;
    private static Bus mockedBus;
    private static Route mockedRoute;

    private static Stop anotherMockedStop;
    private static Bus anotherMockedBus;
    private static Route anotherMockedRoute;

    private static Stop thirdMockedStop;
    private static Bus thirdMockedBus;
    private static Route thirdMockedRoute;

    @Before
    public void setUp() {
        mockedRoute = mock(Route.class);
        mockedBus = mock(Bus.class);
        mockedStop = mock(Stop.class);
        when(mockedStop.getID()).thenReturn(121);
        when(mockedBus.getFleetNumber()).thenReturn(143);
        when(mockedBus.isAtStop()).thenReturn(true);
        when(mockedBus.getNumPassengersBoarded()).thenReturn(20);
        when(mockedBus.getNumPassengersExited()).thenReturn(12);
        when(mockedBus.getRouteTimetable().getRoute().getNumber());
        when(mockedBus.equals(mockedBus)).thenReturn(true);
        when(mockedRoute.getNumber()).thenReturn("225");

        anotherMockedBus = mock(Bus.class);
        anotherMockedStop = mock(Stop.class);
        when(anotherMockedStop.getID()).thenReturn(122);
        when(anotherMockedBus.getFleetNumber()).thenReturn(144);
        when(anotherMockedBus.isAtStop()).thenReturn(true);
        when(anotherMockedBus.getNumPassengersBoarded()).thenReturn(15);
        when(anotherMockedBus.getNumPassengersExited()).thenReturn(4);
        when(anotherMockedBus.equals(anotherMockedBus)).thenReturn(true);
        anotherMockedRoute = mock(Route.class);
        when(mockedRoute.getNumber()).thenReturn("226");

        thirdMockedBus = mock(Bus.class);
        thirdMockedStop = mock(Stop.class);
        when(thirdMockedStop.getID()).thenReturn(123);
        when(thirdMockedBus.getFleetNumber()).thenReturn(145);
        when(thirdMockedBus.isAtStop()).thenReturn(true);
        thirdMockedBus.arrivesAtStop(thirdMockedStop))
        when(thirdMockedBus.getNumPassengersBoarded()).thenReturn(9);
        when(thirdMockedBus.getNumPassengersExited()).thenReturn(2);
        when(thirdMockedBus.equals(thirdMockedBus)).thenReturn(true);
        thirdMockedRoute = mock(Route.class);
        when(mockedRoute.getNumber()).thenReturn("227");

        scheduleStart = new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime();
    }

    /*
    private Date expectedDateTime;
    private int expectedFleetnumber;
    private int expectedRouteID;
    private int expectedStopID;
    private int expectedNumOfPassengersExited;
    private int expectedNumOfPassengersBoarded;
    */

    @Test
    public void testReadRecordReadFleetnumber() {


        int expectedFleetnumber = mockedBus.getFleetNumber();
        assertEquals(CapacityDataStore.readRecordFleetnumber(), expectedFleetnumber);
    }

    @Test
    public void testReadReacordRouteID() {
        assertEquals(CapacityDataStore.readRecordRouteID(), expectedRouteID);
    }

    @Test
    public void testReadRecordStopID() {
        assertEquals(CapacityDataStore.readRecordStopID(), expectedStopID);
    }

    @Test
    public void testReadRecordNumOfPassengersExited() {
        assertEquals(CapacityDataStore.readRecordNumOfPassengersExited(), expectedNumOfPassengersExited);
    }

    @Test
    public void testReadRecordNumOfPassengersBoarded() {
        assertEquals(CapacityDataStore.readRecordNumOfPassengersBoarded(), expectedNumOfPassengersBoarded);
    }

    @Test
    public void testWriteBusStateChange(){
        assertEquals(CapacityDataStore.readRecordNumOfPassengersBoarded(), expectedNumOfPassengersBoarded);
    }

    @Test
    public void lookUpRecord(){
       assertEquals(CapacityDataStore.readRecordDateTime());
    }


    // read records
    // insert a new record
    // write records

    // check inserted record
    // check updated record
    // check deleted record


}