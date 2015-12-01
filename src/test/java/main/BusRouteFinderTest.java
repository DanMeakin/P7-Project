package main;

import org.junit.*;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import main.*

public class BusRouteFinderTest {

    /**
     * BusRouteFinderTest class contains a series of unit tests for the BusRouteFinder class.
     *
     * The BusRouteFinder class enables a user to find the best (quickest or least crowded)
     * way of transport from a to b.
     */
        private static Schedule mockedSchedule;
        private static Date scheduleStart;
        private static Date scheduleEnd;

        private static Bus mockedBus;
        private static List<RouteTimetable> busRouteTimetables;
        private static RouteTimetable mockedRouteTimetable;

        private static Bus anotherMockedBus;
        private static RouteTimetable anotherMockedRouteTimetable;

        private static Bus thirdMockedBus;
        private static RouteTimetable thirdMockedRouteTimetable;

        @Before
        public void setUp() {
            mockedBus = mock(Bus.class);
            when(mockedBus.getFleetNumber()).thenReturn(143);
            when(mockedBus.equals(mockedBus)).thenReturn(true);
            mockedRouteTimetable = mock(RouteTimetable.class);
            busRouteTimetables = Arrays.asList(new RouteTimetable[] {
                    mock(RouteTimetable.class),
                    mock(RouteTimetable.class),
                    mock(RouteTimetable.class),
                    mock(RouteTimetable.class),
                    mock(RouteTimetable.class),
                    mock(RouteTimetable.class),
            });

            anotherMockedRouteTimetable = mock(RouteTimetable.class);
            anotherMockedBus = mock(Bus.class);
            when(anotherMockedBus.amIaBus()).thenReturn(44645);

            thirdMockedRouteTimetable = mock(RouteTimetable.class);
            thirdMockedBus = mock(Bus.class);
            when(thirdMockedBus.equals(thirdMockedBus)).thenReturn(true);

            scheduleStart = new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime();
            scheduleEnd = new GregorianCalendar(2015, GregorianCalendar.DECEMBER, 31).getTime();
            schedule = new Schedule(
                    scheduleStart,
                    scheduleEnd,
                    DayOptions.SATURDAY
            );
        }

    @Test
    public void testBusRouteFinder() {
    }
}