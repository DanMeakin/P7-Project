package main;
import java.io.*;
import java.util.*;
import java.util.GregorianCalendar;
import java.util.Date;

public class Bus {
	private final int fleetNumber;
	private final BusType busType;
	private final Date acquisitionDate;
	private int numOfPassengers;
	private int numOfPassengersEntered;
	private int numOfPassengersExited;
	private RouteTimetable route;
	private Stop stop;
	private boolean onRoute;
	private boolean atStop;

	public Bus(int fleetnumber, BusType bustype, Date acquisitionDate) {
		this.fleetNumber = fleetnumber;
		this.busType = bustype;
		this.acquisitionDate = acquisitionDate;
		//this.saveToFile();
	}

	/*
	public void saveToFile() {

		File f = new File("busstate.txt");
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));
			writer.write(this.getFleetNumber() + "/");
			writer.write(this.getBusType().toString() + "/");
			writer.write(this.getRouteTimetable().toString() + "/");
			writer.write(this.getStop().toString()  + "/");
			writer.write(this.getNumOfPassengers() + "\n");
			System.out.println(f.getAbsolutePath());
		}
		catch(IOException ex)
		{
			System.out.println("Failed to save bus state update to" + f.getAbsolutePath());
			ex.printStackTrace();
		}
	}
	*/

	public void arrivesAtStop(Stop stop) {
		this.stop = stop;
		this.atStop = true;
	}

	public void leavesStop() {
		this.stop = null;
		this.atStop = false;
		//this.saveToFile();
	}
	
	public void startRoute(RouteTimetable route) {
		this.route = route;
		this.onRoute = true;
	}
	
	public void endRoute() {
		this.route = null;
		this.onRoute = false;
	}

	public void setNumOfPassengersEntered(int numOfPassengersEntered){
		this.numOfPassengers = (numOfPassengers + numOfPassengersEntered);
	}

	public int getNumOfPassengersEntered(){
		return this.numOfPassengersEntered;
	}

	public void setNumOfPassengersExited(int numOfPassengersExited){
		this.numOfPassengers = (numOfPassengers - numOfPassengersExited);
	}

	public int getNumOfPassengersExited(){
		return this.numOfPassengersExited;
	}

	public int getNumOfPassengers(){
		return this.numOfPassengers;
	}

	public int getFleetNumber(){
		return this.fleetNumber;
	}

	public BusType getBusType(){
		return this.busType;
	}

	public Date getAcquisitionDate() {
		return this.acquisitionDate;
	}

	public int getSeatedCapacity(){
		return this.busType.getSeatedCapacity();
	}

	public int getStandingCapacity(){
		return this.busType.getStandingCapacity();
	}

	public int getTotalCapacity(){
		return this.busType.getSeatedCapacity() + this.busType.getStandingCapacity();
	}

	public String getMake(){
		return this.busType.getMake();
	}

	public String getModel(){
		return this.busType.getModel();
	}

	public RouteTimetable getRouteTimetable(){
		return this.route;
	}

	public Stop getStop(){
		return this.stop;
	}
}
