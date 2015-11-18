package main;

public class Bus {
	private final int fleetNumber;
	private final BusType busType;
	private int numOfPassengers;
	private RouteTimetable route;
	private Stop stop;
	private boolean onRoute;
	private boolean atStop;
	
	public Bus(int fleetnumber, BusType bustype) {
		this.fleetNumber = fleetnumber;
		this.busType = bustype;
	}
	
	public void arrivesAtStop(Stop stop) {
		this.stop = stop;
		this.atStop = true;
	}
	
	public void leavesStop() {
		this.stop = null;
		this.atStop = false;
	}
	
	public void startRoute(RouteTimetable route) {
		this.route = route;
		this.onRoute = true;
	}
	
	public void endRoute() {
		this.route = null;
		this.onRoute = false;
	}
	
	/*
	//Not in use
	public void allocateType(BusType bustype) {
		this.busType = bustype;
	}
	*/
}
