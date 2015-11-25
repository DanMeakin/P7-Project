package main;
import java.io.*;

public class Bus implements java.io.Serializable {
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
		this.saveToFile();
	}

	public void saveToFile() {
		File f = new File("busstate.txt");
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));
			writer.write(this.getFleetNumber() + "/");
			//writer.write(this.getBusType().toString() + "/");
			//writer.write(this.getRouteTimeTable().toString() + "/");
			//writer.write(this.getStopAsString() + "/");
			writer.write(this.getNumOfPassengers() + "\n");
			System.out.println(f.getAbsolutePath());
		}
		catch(IOException ex)
		{
			System.out.println("Failed to save bus state update to" + f.getAbsolutePath());
			ex.printStackTrace();
		}
	}

	public void arrivesAtStop(Stop stop) {
		this.stop = stop;
		this.atStop = true;
	}
	
	public void leavesStop() {
		this.stop = null;
		this.atStop = false;
		this.saveToFile();
	}
	
	public void startRoute(RouteTimetable route) {
		this.route = route;
		this.onRoute = true;
	}
	
	public void endRoute() {
		this.route = null;
		this.onRoute = false;
	}

	public void passengersEntered(int numOfPassengersEntered){
		this.numOfPassengers = (numOfPassengers + numOfPassengersEntered);
	}

	public void passengersExited(int numOfPassengersExited){
		this.numOfPassengers = (numOfPassengers - numOfPassengersExited);
	}

	public int getFleetNumber(){
		return this.fleetNumber;
	}

	public BusType getBusType(){
		return this.busType;
	}

	public int getNumOfPassengers(){
		return this.numOfPassengers;
	}

	public RouteTimetable getRouteTimeTable(){
		return this.route;
	}

	public String getStopAsString(){
		return this.stop.toString();
	}

	public Stop getStop(){
		return this.stop;
	}
	/*
	//Not in use
	public void allocateType(BusType bustype) {
		this.busType = bustype;
	}
	*/
}
