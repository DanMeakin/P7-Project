package main;

public class Bus {
	private int fleetNumber;
	private int numOfPassengers;
	private BusType busType;
	private RouteTimetable route;
	private BusStop stop;
	private boolean onRoute;
	private boolean atStop;
	
	public Bus(int fleetnumber, BusType bustype) {
		this.fleetNumber = fleetnumber;
		this.busType = bustype;
	}
	
	public void arrivesAtStop(BusStop stop) {
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
	
	public void allocateType(BusType bustype) {
		this.busType = bustype;
	}
	
	
}