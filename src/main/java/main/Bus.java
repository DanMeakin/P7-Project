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
   * Get list of all buses within system.
   *
   * @return list of all buses within system
   */
  public static List<Bus> getAllBuses() {
    return allBuses;
  }

  /**
   * Find a Bus from the list of all buses.
   *
   * @param fleetNumber the fleet number of the desired bus
   * @return the bus matching with fleet number matching the fleetNumber
   *         argument
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
    notifyObservers(); // Notify observers after leaving stop
    setStop(null);
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
   * Set a bus to be at a Stop.
   *
   * @param stop the Stop the bus is set to be at.
   */
  private void setStop(Stop stop) {
    this.stop = stop;
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
   * Get the percentage of bus capacity currently in use.
   *
   * This method calculates the percentage of bus capacity currently
   * in use by dividing the number of passengers by the total capacity
   * of the bus.
   *
   * @return occupancy level as a decimal
   */
  public double getTotalOccupancyLevel(){
    return (double) getNumPassengers() / (double) getTotalCapacity();
  }

  /**
   * Get the percentage of seating capacity currently in use.
   *
   * This method calculates the percentage of seated capacity currently
   * in use by dividing the number of passengers by the total seated capacity
   * of the bus.
   *
   * If the value is more than 1, this indicates that all seated capacity
   * is filled, so seated occupancy is returned as 1.
   *
   * @return seated occupancy level as a decimal
   */
  public double getSeatedOccupancyLevel() {
    return Math.min(1, (double) getNumPassengers() / (double) getSeatedCapacity());
  }

  /**
   * Get the percentage of standing capacity currently in use.
   *
   * This method calculates the percentage of standing capacity currently
   * in use by disregarding the number of passengers (likely) sitting down,
   * and then dividing the remaining number by the total standing capacity
   * of the bus.
   *
   * If the value is less than 0, this indicates that not all seated capacity
   * is filled, so standing occupancy is returned as 0.
   *
   * @return standing occupancy level as a decimal
   */
  public double getStandingOccupancyLevel() {
    return Math.max(0, (double) getNumPassengers() - getSeatedCapacity() / (double) getStandingCapacity());
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
