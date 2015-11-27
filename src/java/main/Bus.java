package main;

import java.io.*;
import java.util.*;
import java.util.GregorianCalendar;
import java.util.Date;

public class Bus {
	private final int fleetNumber;
	private final BusType busType;
	private final Date acquisitionDate;
	private int numPassengers;
	private int numPassengersBoarded;
	private int numPassengersExited;
	private RouteTimetable route;
	private Stop stop;
	private boolean onRoute;
	private boolean atStop;
	private static List<Bus> allBuses = new ArrayList<>();

	public Bus(int fleetnumber, BusType bustype, Date acquisitionDate) {
		this.fleetNumber = fleetnumber;
		this.busType = bustype;
		this.acquisitionDate = acquisitionDate;
		addBus(this);
	}

  /**
   * Add a Bus to the list of all buses.
   *
   * @param bus The bus object to add to the list
   */
	public static void addBus(Bus bus) throws IllegalArgumentException {
    if (busExists(bus)) {
      String msg = "Bus with fleet number " + bus.getFleetNumber() + " already exists";
      throw new IllegalArgumentException(msg);
    }
		allBuses.add(bus);
	}

  /**
   * Remove a Bus from the list of all buses.
   *
   * @param bus the bus object to remove
   */
  public static void removeBus(Bus bus) {
    allBuses.remove(bus);
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

	public void arrivesAtStop(Stop stop) throws UnsupportedOperationException {
    if (isAtStop()) {
      String msg = "bus is already at a stop";
      throw new UnsupportedOperationException(msg);
    }
		this.stop = stop;
		this.atStop = true;
	}

	public void leavesStop() throws UnsupportedOperationException {
    if (!isAtStop()) {
      String msg = "bus is not at a stop";
      throw new UnsupportedOperationException(msg);
    }
		this.stop = null;
		this.atStop = false;
		//this.saveToFile();
	}
	
  /**
   * Start operating on a route.
   *
   * This method commences the operation of a Bus for collecting and dropping
   * off passengers.
   *
   * @param route the routeTimetable the bus is to be operating
   */
	public void startRoute(RouteTimetable route) {
		this.route = route;
		this.onRoute = true;
	}
	
  /**
   * End operating on a route.
   *
   * This method ceases the operation of a Bus for collecting and dropping off
   * passengers.
   *
   * The bus can only stop operating once it is at a stop and empty of 
   * passengers.
   */
	public void endRoute() {
    if (!isAtStop() || getNumPassengers() > 0) {
      String msg = "unable to end route between stops or with passengers on board";
      throw new UnsupportedOperationException(msg);
    }
		this.route = null;
		this.onRoute = false;
	}

  /**
   * passengersBoard tracks passengers boarding a bus at a stop.
   *
   * @param num the number of passengers boarding
   */
	public void passengersBoard(int num) throws UnsupportedOperationException {
    if (!isAtStop()) {
      String msg = "passengers can only board at a stop; bus is currently between stops";
      throw new UnsupportedOperationException(msg);
    }
    this.numPassengersBoarded = num;
		this.numPassengers += num;
	}

   /**
   * passengersExit tracks passengers exiting a bus at a stop.
   *
   * @param num the number of passengers exiting
   */
	public void passengersExit(int num) throws UnsupportedOperationException, IllegalArgumentException {
    if (!isAtStop()) {
      String msg = "passengers can only exit at a stop; bus is currently between stops";
      throw new UnsupportedOperationException(msg);
    } else if (num > getNumPassengers()) {
      String msg = "more passengers exiting than are currently on bus (" + getNumPassengers() + " passengers on bus, " + num + " exiting)";
      throw new IllegalArgumentException(msg);
    }
    this.numPassengersExited = num;
		this.numPassengers -= num;
	}

	public int getNumPassengersBoarded(){
		return this.numPassengersBoarded;
	}

	public int getNumPassengersExited(){
		return this.numPassengersExited;
	}

	public int getNumPassengers(){
		return this.numPassengers;
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
		return getSeatedCapacity() + getStandingCapacity();
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

	public Stop getCurrentStop(){
		return this.stop;
	}

  public boolean isAtStop() {
    return this.atStop;
  }

  public boolean isOnRoute() {
    return this.onRoute;
  }

	public static int getNumOfBusesPerType(BusType type){
		HashMap<BusType, List<Bus>> numOfBusesPerType = new HashMap<BusType, List<Bus>>();
		for(Bus o : allBuses){
			List<Bus> temp = numOfBusesPerType.get(o.getBusType());
			if(temp == null){
				temp = new ArrayList<Bus>();
				numOfBusesPerType.put(o.getBusType(), temp);
			}
			temp.add(o);
		}
		return numOfBusesPerType.get(type).size();
	}

  /**
   * Check if two Bus instances are to be considered the same.
   *
   * @param otherBus the bus against which to compare
   * @return true if both buses are equal, else false
   */
  public boolean equals(Bus otherBus) {
    return (getFleetNumber() == otherBus.getFleetNumber());
  }

  /** 
   * Check if Bus already exists within the system.
   *
   * @param bus Bus object to check against
   * @return true if Bus already exists, else false.
   */
  private static boolean busExists(Bus bus) {
    for (Bus b : allBuses) {
      if (b.equals(bus)) {
        return true;
      }
    }
    return false;
  }
}
