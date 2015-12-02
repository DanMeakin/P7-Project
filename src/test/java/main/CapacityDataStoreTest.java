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

    private static Bus mockedBus;
    private static Bus anotherMockedBus;
    private static Bus thirdMockedBus;

    @Before
    public void setUp() {
        mockedBus = mock(Bus.class);
        when(mockedBus.getFleetNumber()).thenReturn(143);
        when(mockedBus.getStop().getID()).thenReturn(121);
        when(mockedBus.getNumPassengersBoarded()).thenReturn(20);
        when(mockedBus.getNumPassengersExited()).thenReturn(12);
        when(mockedBus.getRouteTimetable().getRoute().getNumber()).thenReturn("225");
        when(mockedBus.equals(mockedBus)).thenReturn(true);

        anotherMockedBus = mock(Bus.class);
        when(anotherMockedBus.getFleetNumber()).thenReturn(144);
        when(mockedBus.getStop().getID()).thenReturn(122);
        when(anotherMockedBus.getNumPassengersBoarded()).thenReturn(15);
        when(anotherMockedBus.getNumPassengersExited()).thenReturn(4);
        when(anotherMockedBus.equals(anotherMockedBus)).thenReturn(true);
        when(anotherMockedBus.getRouteTimetable().getRoute().getNumber()).thenReturn("226");

        thirdMockedBus = mock(Bus.class);
        when(thirdMockedBus.getFleetNumber()).thenReturn(145);
        when(mockedBus.getStop().getID()).thenReturn(123);
        when(thirdMockedBus.getNumPassengersBoarded()).thenReturn(9);
        when(thirdMockedBus.getNumPassengersExited()).thenReturn(2);
        when(thirdMockedBus.equals(thirdMockedBus)).thenReturn(true);
        when(thirdMockedBus.getRouteTimetable().getRoute().getNumber()).thenReturn("227");

    }


    private Date expectedDateTime;
    private int expectedFleetnumber;
    private int expectedRouteID;
    private int expectedStopID;
    private int expectedNumOfPassengersExited;
    private int expectedNumOfPassengersBoarded;


    @Test
    public void testWriteBusStateChange(){

        assertEquals(CapacityDataStore.readRecordNumOfPassengersBoarded(), expectedNumOfPassengersBoarded);
    }



    public void testReadRecordReadFleetnumber() {
    int expectedFleetnumber = mockedBus.getFleetNumber();
    CapacityDataStore.readRecordFleetnumber();
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

    /*
    @Test
    public void lookUpRecord(){
       assertEquals(CapacityDataStore.readRecordDateTime());
    }
    */


    // read records
    // insert a new record
    // write records

    // check inserted record
    // check updated record
    // check deleted record


}