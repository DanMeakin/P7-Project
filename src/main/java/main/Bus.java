package main;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Date;
import java.util.Observable;

/**
 * The Bus class defines bus objects that hold several
 * characteristics of the bus such as fleetNmumber,
 * busType, numOfPassengers etc. and implement
 * a number of domain specific methods such as addBus,
 * startRoute, arrivesAtStop, passengersBoarding etc.
 * @authors Ivo Hendriks, Janus Avbæk Larsen, Helle Hyllested Larsen, Dan Meakin.
 */
public class Bus extends Observable {
  /** the fleet number of this bus  */
  private final int fleetNumber;
  /** the type of this bus  */
  private final BusType busType;
  /** the acquisistion date of this bus  */
  private final Date acquisitionDate;
  /** the number of passengers on this bus  */
  private int numPassengers;
  /** the number of passengers boarded this bus at the last stop */
  private int numPassengersBoarded;
  /** the number of passengers exited this bus at the last stop */
  private int numPassengersExited;
  /** the route timetable associated with this bus */
  private RouteTimetable route;
  /** the stop associated with this bus */
  private Stop stop;
  private Stop lastStop;
  /** a data structure containing all busses */
  private static List<Bus> allBuses = new ArrayList<>();

  /**
   * Creates a bus and add it to the allBuses list.
   *
   * @param fleetnumber the fleetnumber associated with this bus.
   * @param bustype the bus type of this bus.
   * @param acquisitionDate the acquisition date of this bus.
   */
  public Bus(int fleetnumber, BusType bustype, Date acquisitionDate) {
    this.fleetNumber = fleetnumber;
    this.busType = bustype;
    this.acquisitionDate = acquisitionDate;
    addBus(this);
  }

  /**
   * Add a Bus to the list of all buses.
   *
   * @param bus the bus object to add to the list
   */
  private static void addBus(Bus bus) throws IllegalArgumentException {
    if (busExists(bus)) {
      String msg = "Bus with fleet number " + bus.getFleetNumber() + " already exists";
      throw new IllegalArgumentException(msg);
    }
    allBuses.add(bus);
  }

  /**
   * Remove a bus from the list of all buses.
   *
   * @param bus the bus object to remove
   */
  public static void removeBus(Bus bus) {
    allBuses.remove(bus);
  }

  /**
   * Find a Bus from the list of all buses.
   *
   * @param fleetNumber the fleet number of the desired bus
   */
  public static Bus findBus(int fleetNumber) {
    for (Bus b : allBuses) {
      if (b.getFleetNumber() == fleetNumber) {
        return b;
      }
    }
    return null;
  }

  /**
   * Method to flag that a bus has arrived at a Stop.
   *
   * This method makes changes to state to reflect that the bus has arrvied at
   * a Stop. It cannot be called when the bus is already at a stop, and throws
   * an exception in that case.
   *
   * @param stop the stop at which the bus has arrived
   */
  public void arrivesAtStop(Stop stop) throws UnsupportedOperationException {
    if (isAtStop()) {
      String msg = "bus is already at a stop";
      throw new UnsupportedOperationException(msg);
    }
    setLastStop(null);
    setStop(stop);
    setNumPassengersBoarded(0);
    setNumPassengersExited(0);
    setChanged(); // Mark as changed for observers
  }

  public void leavesStop() throws UnsupportedOperationException {
    if (!isAtStop()) {
      String msg = "bus is not at a stop";
      throw new UnsupportedOperationException(msg);
    }
    setLastStop(stop);
    setStop(null);
    notifyObservers(); // Notify observers after leaving stop
  }
  
  /**
   * Start operating on a route.
   *
   * This method commences the operation of a Bus for collecting and dropping
   * off passengers.
   *
   * @param routeTimetable the routeTimetable the bus is to be operating
   */
  public void startRoute(RouteTimetable routeTimetable) {
    setRouteTimetable(routeTimetable);
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
    setRouteTimetable(null);
  }

  /**
   * passengersBoard tracks passengers boarding a bus at a stop.
   *
   * @param num the number of passengers boarding.
   */
  public void passengersBoard(int num) throws UnsupportedOperationException {
    if (!isAtStop()) {
      String msg = "passengers can only board at a stop; bus is currently between stops";
      throw new UnsupportedOperationException(msg);
    }
    setNumPassengersBoarded(num);
    setNumPassengers(getNumPassengers() + num);
  }

   /**
   * passengersExit tracks passengers exiting a bus at a stop.
   *
   * @param num the number of passengers exiting.
   */
  public void passengersExit(int num) throws UnsupportedOperationException, IllegalArgumentException {
    if (!isAtStop()) {
      String msg = "passengers can only exit at a stop; bus is currently between stops";
      throw new UnsupportedOperationException(msg);
    } else if (num > getNumPassengers()) {
      String msg = "more passengers exiting than are currently on bus (" + getNumPassengers() + " passengers on bus, " + num + " exiting)";
      throw new IllegalArgumentException(msg);
    }
    setNumPassengersExited(num);
    setNumPassengers(getNumPassengers() - num);
  }

  /**
   * Get the number of passengers on a bus.
   *
   * @return numPassengers the number of passengers on the bus.
   */
  public int getNumPassengers(){
    return this.numPassengers;
  }

  /**
   * Get the number of passengers boarded the bus.
   *
   * @return numPassengersExited the number of passengers boarded the bus.
   */
  public int getNumPassengersBoarded(){
    return this.numPassengersBoarded;
  }

  /**
   * Get the number of passengers extited the bus.
   *
   * @return numPassengersExited the number of passengers exited the bus.
   */
  public int getNumPassengersExited(){
    return this.numPassengersExited;
  }

  /**
   * Set the number of passengers on a bus.
   *
   * @param n the number of passsengers on the bus.
   */
  private void setNumPassengers(int n) {
    this.numPassengers = n;
  }

  /**
   * Set the number of passengers boarded.
   *
   * @param n the number of passsengers boarded.
   */
  private void setNumPassengersBoarded(int n) {
    this.numPassengersBoarded = n;
  }

  /**
   * Set the number of passengers exitited.
   *
   * @param n the number of passsengers exitited.
   */
  private void setNumPassengersExited(int n) {
    this.numPassengersExited = n;
  }

  /**
   * Get the fleet number of a bus.
   *
   * @return fleetnumber the fleet number of the bus.
   */
  public int getFleetNumber(){
    return this.fleetNumber;
  }

  /**
   * Get the type of a bus.
   *
   * @return busType the type of the bus.
   */
  public BusType getBusType(){
    return this.busType;
  }

  /**
   * Get the acquisition date of a bus.
   *
   * @return acquisitionDate the acquisition bate of the bus.
   */
  public Date getAcquisitionDate() {
    return this.acquisitionDate;
  }

  /**
   * Get the seated capacity of a bus.
   *
   * @return busType.getSeatedCapacity the seated capacity of the bus.
   */
  public int getSeatedCapacity(){
    return this.busType.getSeatedCapacity();
  }

  /**
   * Get the standing capacity of a bus.
   *
   * @return busType.getStandingCapacity the standing capacity of the bus.
   */
  public int getStandingCapacity(){
    return this.busType.getStandingCapacity();
  }

  /**
   * Get the total capacity of a bus.
   *
   * @return getSeatedCapacity + getStandingcapacity the total capacity of the bus.
   */
  public int getTotalCapacity(){
    return getSeatedCapacity() + getStandingCapacity();
  }

  /**
   * Get the mke of a bus.
   *
   * @return busType.getMake the make of the bus.
   */
  public String getMake(){
    return this.busType.getMake();
  }

  /**
   * Get the model of a bus.
   *
   * @return busType.getModel the model of the bus.
   */
  public String getModel(){
    return this.busType.getModel();
  }

  /**
   * Get the RouteTimeTable a bus is on.
   *
   * @return route the RouteTimeTable the bus is on.
   */
  public RouteTimetable getRouteTimetable(){
    return this.route;
  }

  /**
   * Set a bus to be on a route timetable.
   *
   * @param rt the route timetable the bus is set to be at.
   */
  private void setRouteTimetable(RouteTimetable rt) {
    this.route = rt;
  }

  /**
   * Get the stop a bus is at.
   *
   * @return stop the Stop the bus is at.
   */
  public Stop getStop(){
    return this.stop;
  }

  /**
   * Get the last stop a bus is at.
   *
   * @return lastStop the last stop the bus was at. If available, return the Stop the bus is at.
   */
  public Stop getLastStop(){
    if (!isAtStop()) {
      return this.lastStop;
    }
    return this.stop;
  }


  /**
   * Set a bus to be at a Stop.
   *
   * @param stop the Stop the bus is set to be at.
   */
  private void setStop(Stop stop) {
    this.stop = stop;
  }


  /**
   * Set the last stop a bus was at.
   *
   * @param stop the Stop the bus was last at.
   */
  private void setLastStop(Stop stop){
    if (!isAtStop()) {
      String msg = "last stop can ony be set when bus is at a stop";
      throw new UnsupportedOperationException(msg);
    }
    this.lastStop = stop;
  }

  /**
   * Check whether a bus in currently at a Stop.
   *
   * @return true if a bus is at a Stop, false if not.
   */
  public boolean isAtStop() {
    return (getStop() != null);
  }

  /**
   * Check whether a bus in currently on a Route.
   *
   * @return true if a bus is one a Route, false if not.
   */
  public boolean isOnRoute() {
    return (getRouteTimetable() != null);
  }

  /**
   * Get the number of passengers divided by the capacity of a bus.
   *
   * @return totalOccupationRate the occupation rate of the bus.
   */
  public double getTotalOccupationRate(){
    DecimalFormat rateFormat = new DecimalFormat("#.00");
    double totalOccupationRate = (Math.round( (double) this.getNumPassengers() / (double) this.getTotalCapacity()*100)/100d);
    return totalOccupationRate;
  }

  /**
   * Get the number of passengers divided by the number of seats on a bus.
   *
   * @return seatedOccupationRate the occupation rate of the bus.
   */
  public double getSeatedOccupationRate(){
    DecimalFormat rateFormat = new DecimalFormat("#.00");
    double seatedOccupationRate = (Math.round( (double) this.getNumPassengers() / (double) this.getSeatedCapacity()*100)/100d);
    return seatedOccupationRate;
  }

  /**
   * Check how many buses exist of a certain type.
   *
   * @param type of Bus to get the number of buses for.
   * @return the number of buses per type.
   */
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
   * @param otherBus the bus against which to compare.
   * @return true if both buses are equal, else false.
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
