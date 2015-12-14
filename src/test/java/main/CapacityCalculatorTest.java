package main;

/**
 * @author Ivo on 7-12-2015.
 */
/*
public class CapacityCalculatorTest {

    private static Bus mockedBus;
    private static Bus anotherMockedBus;
    private static Bus thirdMockedBus;

    private static Route mockedRoute;
    private static Route anotherMockedRoute;
    private static Route thirdMockedRoute;

    private static RouteTimetable mockedRouteTimetable;
    private static RouteTimetable anotherMockedRouteTimetable;
    private static RouteTimetable thirdMockedRouteTimetable;

    private static Stop mockedStop;
    private static Stop anotherMockedStop;
    private static Stop thirdMockedStop;

    private BusType mockedBusType;
    private BusType anotherMockedBusType;

    //long date = System.currentTimeMillis();
    //Date arbitraryDate = new Date(date);
    //SimpleDateFormat form = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
    //String str = form.format(arbitraryDate);
    //DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    private int mockedBusTypeTotalCapacity = 110;
    private int anotherMockedBusTypeTotalCapacity = 98;

    private int mockedFleetNumber = 143;
    private String mockedRouteNumber = "225";
    private String mockedRouteDescription = "North-South";
    private int mockedStopID = 121;
    private String mockedStopName = "Universtet";
    private int mockedNumOfPassengersExited = 12;
    private int mockedNumOfPassengersBoarded = 20;
    private int mockedNumOfPassengers = 12;

    private int anotherMockedFleetNumber = 97;
    private String anotherMockedRouteNumber = "103";
    private String anotherMockedRouteDescription = "East-West";
    private int anotherMockedStopID = 33;
    private String anotherMockedStopName = "Nytorv";
    private int anotherMockedNumOfPassengersExited = 10;
    private int anotherMockedNumOfPassengersBoarded = 15;
    private int anotherMockedNumOfPassengers = 43;

    private int thirdMockedFleetNumber = 90;
    private String thirdMockedRouteNumber = "125";
    private String thirdMockedRouteDescription = "East-West";
    private int thirdMockedStopID = 205;
    private String thirdMockedStopName = "Gigantium";
    private int thirdMockedNumOfPassengersExited = 1;
    private int thirdMockedNumOfPassengersBoarded = 9;
    private int thirdMockedNumOfPassengers = 20;

    private double occupationRate = (this.mockedBus.getNumPassengers() / this.mockedBus.getBusType().getTotalCapacity());

    final Date date = Mockito.mock(Date.class);

    final SimpleDateFormat dayMonthYear = Mockito.mock(SimpleDateFormat.class);

    @Before
    public void setUp() {
        mockedBus = mock(Bus.class);
        mockedRouteTimetable = mock(RouteTimetable.class);
        anotherMockedRouteTimetable = mock(RouteTimetable.class);
        thirdMockedRouteTimetable = mock(RouteTimetable.class);

        Mockito.when(date.getTime()).thenReturn(30L);
        Mockito.when(dayMonthYear.toPattern()).thenReturn("MM/dd/yyyy");

        when(mockedBus.getFleetNumber()).thenReturn(mockedFleetNumber);
        when(mockedBus.getRouteTimetable().getRoute().getNumber()).thenReturn(mockedRouteNumber);
        when(mockedBus.getRouteTimetable().getRoute().getDescription()).thenReturn(mockedRouteDescription);
        when(mockedBus.getRouteTimetable()).thenReturn(mockedRouteTimetable);
        when(mockedBus.getStop().getID()).thenReturn(mockedStopID);
        when(mockedBus.getStop().getName()).thenReturn(mockedStopName);
        when(mockedBus.getNumPassengersExited()).thenReturn(mockedNumOfPassengersExited);
        when(mockedBus.getNumPassengersBoarded()).thenReturn(mockedNumOfPassengersBoarded);
        when(mockedBus.getNumPassengers()).thenReturn(mockedNumOfPassengers);
        when(mockedBus.getOccupationRate()).thenReturn(occupationRate);

        //when(mockedBus.getBusType().getTotalCapacity()).thenReturn(mockedBusTypeTotalCapacity);
        //when(mockedRoute.getNumber()).thenReturn(mockedRouteNumber);
        //when(mockedRoute.getDescription()).thenReturn(mockedRouteDescription);
        //when(mockedStop.getID()).thenReturn(mockedStopID);

        anotherMockedBus = mock(Bus.class);
        when(anotherMockedBus.getFleetNumber()).thenReturn(anotherMockedFleetNumber);
        when(anotherMockedBus.getRouteTimetable().getRoute().getNumber()).thenReturn(anotherMockedRouteNumber);
        when(anotherMockedBus.getRouteTimetable().getRoute().getDescription()).thenReturn(anotherMockedRouteDescription);
        when(anotherMockedBus.getRouteTimetable()).thenReturn(anotherMockedRouteTimetable);
        when(anotherMockedBus.getStop().getID()).thenReturn(anotherMockedStopID);
        when(anotherMockedBus.getStop().getName()).thenReturn(anotherMockedStopName);
        when(anotherMockedBus.getNumPassengersExited()).thenReturn(anotherMockedNumOfPassengersExited);
        when(anotherMockedBus.getNumPassengersBoarded()).thenReturn(anotherMockedNumOfPassengersBoarded);
        when(anotherMockedBus.getNumPassengers()).thenReturn(anotherMockedNumOfPassengers);
        when(anotherMockedBus.getOccupationRate()).thenReturn(occupationRate);

        //when(anotherMockedRoute.getNumber()).thenReturn(anotherMockedRouteNumber);
        //when(anotherMockedRoute.getDescription()).thenReturn(anotherMockedRouteDescription);

        when(anotherMockedStop.getID()).thenReturn(anotherMockedStopID);

        thirdMockedBus = mock(Bus.class);
        when(thirdMockedBus.getFleetNumber()).thenReturn(thirdMockedFleetNumber);
        when(thirdMockedBus.getRouteTimetable().getRoute().getNumber()).thenReturn(thirdMockedRouteNumber);
        when(thirdMockedBus.getRouteTimetable().getRoute().getDescription()).thenReturn(thirdMockedRouteDescription);
        when(thirdMockedBus.getRouteTimetable()).thenReturn(thirdMockedRouteTimetable);
        when(thirdMockedBus.getStop().getID()).thenReturn(thirdMockedStopID);
        when(thirdMockedBus.getStop().getName()).thenReturn(thirdMockedStopName);
        when(thirdMockedBus.getNumPassengersExited()).thenReturn(thirdMockedNumOfPassengersExited);
        when(thirdMockedBus.getNumPassengersBoarded()).thenReturn(thirdMockedNumOfPassengersBoarded);
        when(thirdMockedBus.getNumPassengers()).thenReturn(thirdMockedNumOfPassengers);
        when(thirdMockedBus.getOccupationRate()).thenReturn(occupationRate);


        when(thirdMockedRoute.getNumber()).thenReturn(thirdMockedRouteNumber);
        when(thirdMockedRoute.getDescription()).thenReturn(thirdMockedRouteDescription);

        when(thirdMockedStop.getID()).thenReturn(thirdMockedStopID);
    }


    public double testCalculateCrowdedness(int currentCrowdedness, List<double>);
    CapacityDataStoreWriter.writeBusStateChange(mockedBus);

    public int testCalculateCrowdedness(){
        assertEquals()
    }

}
*/