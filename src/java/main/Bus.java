package main;

public class Bus {
	private int fleetNumber;
	private int numOfPassengers;
	private BusType busType;
	private Route route;
	private boolean onRoute;
	private boolean atStop;
	
	public Bus(int fleetnumber, BusType bustype) {
		this.fleetNumber = fleetnumber;
		this.busType = bustype;
	}
	
	public void ArrivesAtStop() {
		this.atStop = true;
	}
	
	public void LeavesStop() {
		this.atStop = false;
	}
	
	public void StartRoute(Route route) {
		this.route = route;
		this.onRoute = true;
	}
	
	public void EndRoute() {
		this.route = null;
		this.onRoute = false;
	}
	
	public void AllocateType(BusType bustype) {
		this.busType = bustype;
	}
	
	
}