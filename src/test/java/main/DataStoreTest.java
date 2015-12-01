package main;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.Date;
import main.DataStore;
import main.Schedule;


import main.Stop;

public class DataStoreTest {

    private Date currentTimeDate;
    private int fleetnumber;
    private int currentRouteID;
    private int currentStopID;
    private int numOfPassengersExited;
    private int numOfPassengersBoarded;

    @Test
    public void testReadRecordCurrentTimeDate() {
        assertEquals(readRecordDateTime());
    }

    // read records
    // insert a new record
    // write records

    // check inserted record
    // check updated record
    // check deleted record


}