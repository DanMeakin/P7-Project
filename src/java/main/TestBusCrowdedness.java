package main;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class TestBusCrowdedness implements java.io.Serializable {

	private static Date acquisitionDate;


	public static void main(String[] args) {

		BusType typevolvo = new BusType("Volvo", "B100", 48, 64);
		BusType typescania = new BusType("Scania", "C50", 52, 48);
		BusType typedaf = new BusType("DAF", "A150", 60, 30);

		acquisitionDate = new GregorianCalendar(2015, Calendar.JANUARY, 1).getTime();
		Bus bus1 = new Bus(1234, typevolvo, acquisitionDate);
		Bus bus2 = new Bus(1235, typedaf, acquisitionDate);
		
		Stop stop1 = new Stop(1, "University", 1, 1);
		Stop stop2 = new Stop(2, "City Centre", 2, 2);
		Stop stop3 = new Stop(3, "Stadium", 3,3 );
		Stop stop4 = new Stop(4, "Nordkraft", 4,4);
		
		Route route1 = new Route("1", "University-Stadium", stop1);
		Route route2 = new Route("2", "University-Centre", stop1);
		Route route3 = new Route("3", "University-Nordkraft", stop1);
		
		route1.addStop(stop1, 1, 2);
		route1.addStop(stop4, 1, 2);
		route1.addStop(stop3, 1, 2);
		
		route2.addStop(stop1, 1, 2);
		route2.addStop(stop2, 1, 2);

		RouteTimetable routetimetable1 = new RouteTimetable(12, true, route1);

		bus1.startRoute(routetimetable1);

		bus1.arrivesAtStop(stop1);
		bus1.setNumOfPassengersEntered(10);
		bus1.leavesStop();
		bus1.arrivesAtStop(stop2);
		bus1.setNumOfPassengersEntered(4);
		bus1.setNumOfPassengersExited(6);
		bus1.leavesStop();

		bus1.endRoute();

		String workingDir = System.getProperty("user.dir");
		System.out.println("Current working directory : " + workingDir);

	}

}


