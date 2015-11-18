package main;

public class TestBusCrowdedness {

	public static void main(String[] args) {
		BusType typevolvo = new BusType("Volvo", "B100", 48, 64);
		BusType typescania = new BusType("Scania", "C50", 52, 48);
		BusType typedaf = new BusType("DAF", "A150", 60, 30);
		
		Bus bus1 = new Bus(1234, typevolvo);
		Bus bus2 = new Bus(1235, typedaf);
		
		BusStop stop1 = new BusStop(1, "University", "Uni");
		BusStop stop2 = new BusStop(2, "City Centre", "Centre");
		BusStop stop3 = new BusStop(3, "Stadium", "Stadium");
		BusStop stop4 = new BusStop(4, "Nordkraft", "NK");
		
		Route route1 = new Route(1, "University-Stadium");
		Route route2 = new Route(2, "University-Centre");
		Route route3 = new Route(3, "University-Nordkraft");
		
		route1.addStop(stop1, 1, 2);
		route1.addStop(stop4, 1, 2);
		route1.addStop(stop3, 1, 2);
		
		route2.addStop(stop1, 1, 2);
		route2.addStop(stop2, 1, 2);
	}
}

