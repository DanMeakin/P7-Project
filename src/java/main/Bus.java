package main;

public class Bus {
	private String acquisitionDate;
	private String saleDate;
	private int fleetNumber;
	private int numOfPassengers;
	private BusType busType;
	private boolean onRoute;
	private boolean atStop;
	
	public Bus(String acquisitiondate, int fleetnumber, BusType bustype) {
		this.acquisitionDate = acquisitiondate;
		this.fleetNumber = fleetnumber;
		this.busType = bustype;
	}
	
	public void ArrivesAtStop() {
		this.atStop = true;
	}
	
	public void LeavesStop() {
		this.atStop = false;
	}
	
	public void StartRoute() {
		this.onRoute = true;
	}
	
	public void EndRoute() {
		this.onRoute = false;
	}
	
	public void Sold() {
	}
	
	public void AllocatedType() {
	}
	
	
}