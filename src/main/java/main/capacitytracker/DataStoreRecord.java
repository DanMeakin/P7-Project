package main.capacitytracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import main.model.*;

/**
 * This class defines one record in the datastore.
 */
class DataStoreRecord {

  private static DateTimeFormatter timestampFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private final LocalDateTime timestamp;
  private final Bus bus;
  private final RouteTimetable routeTimetable;
  private final Stop stop;
  private final int numPassengersOnArrival;
  private final int numPassengersExited;
  private final int numPassengersBoarded;
  private final int numPassengersOnDeparture;
  private final double occupancyLevel;

  /**
   * Instantiates a DataStoreRecord instance.
   *
   * @param record a CSVRecord representing one row in the dataStore file
   */
  public DataStoreRecord(CSVRecord record) {
    System.out.println(record);
    this.timestamp = LocalDateTime.parse(record.get("timestamp"), timestampFormat);
    this.bus = Bus.findBus(Integer.parseInt(record.get("busFleetNumber")));
    this.routeTimetable = findRouteTimetable(
        record.get("routeNumber"), 
        record.get("routeDescription"),
        timestamp.toLocalDate(),
        record.get("routeTimetableStartTime")
        );
    this.stop = Stop.findStop(Integer.parseInt(record.get("stopID")));
    this.numPassengersOnArrival = Integer.parseInt(record.get("numberPassengersOnArrival"));
    this.numPassengersOnDeparture = Integer.parseInt(record.get("numberPassengersOnDeparture"));
    this.numPassengersExited = Integer.parseInt(record.get("numberPassengersExited"));
    this.numPassengersBoarded = Integer.parseInt(record.get("numberPassengersBoarded"));
    this.occupancyLevel = Double.parseDouble(record.get("occupancyLevel"));
    System.out.println("Passengers on Arrival: " + numPassengersOnArrival);
    System.out.println("Passengers on Departure: " + numPassengersOnDeparture);
  }

  /**
   * Overrides equality testing method.
   */
  @Override
  public boolean equals(Object o) {
    return (o instanceof DataStoreRecord && equals((DataStoreRecord) o));
  }

  /**
   * Determines if two DataStoreRecord instances are identical.
   */
  public boolean equals(DataStoreRecord otherDSR) {
    return (getTimestamp().equals(otherDSR.getTimestamp()) &&
            getRouteTimetable().equals(otherDSR.getRouteTimetable()));
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public Bus getBus() {
    return bus;
  }

  public RouteTimetable getRouteTimetable() {
    return routeTimetable;
  }

  public Stop getStop() {
    return stop;
  }

  public int getNumPassengersOnArrival() {
    return numPassengersOnArrival;
  }
  
  public int getNumPassengersExited() {
    return numPassengersExited;
  }

  public int getNumPassengersBoarded() {
    return numPassengersBoarded;
  }

  public int getNumPassengersOnDeparture() {
    return numPassengersOnDeparture;
  }

  public double getOccupancyLevel() {
    return occupancyLevel;
  }

  public int getMaxSeatedCapacity() {
    return getBus().getSeatedCapacity();
  }

  public int getMaxStandingCapacity() {
    return getBus().getStandingCapacity();
  }

  public int getMaxTotalCapacity() {
    return getMaxStandingCapacity() + getMaxSeatedCapacity();
  }

  /**
   * Calculates the percentage of seated capacity occupied on the bus, as a
   * decimal.
   *
   * @return percentage (as a decimal) of seated capacity occupied on bus
   */
  public double getSeatedOccupancyRate() {
    return (double) getNumPassengersOnDeparture() / getMaxSeatedCapacity();
  }

  /**
   * Calculates the percentage of total capacity occupied on the bus, as a
   * decimal.
   *
   * @return percentage (as a decimal) of total capacity occupied on bus
   */
  public double getTotalOccupancyRate() {
    return (double) getNumPassengersOnDeparture() / getMaxTotalCapacity();
  }

  /**
   * Finds route matching number and description.
   *
   * @param number      the route number to match
   * @param description the route description to match
   * @return route matching number and description
   */
  private Route findRoute(String number, String description) {
    List<Route> rs = Route.findRouteByNumber(number);
    for (Route r : rs) {
      if (r.getDescription().equals(description)) {
        return r;
      }
    }
    return null;
  }

  /**
   * Finds route timetable matching this record.
   *
   * @return route timetable matching this record
   */
  private RouteTimetable findRouteTimetable(String routeNumber, String routeDescription, LocalDate date, String startTime) {
    System.out.println(routeNumber + ", " + routeDescription + ", " + date + ", " + startTime);
    String[] stComps = startTime.split(":");
    int time = Integer.parseInt(stComps[0]) * 60 + Integer.parseInt(stComps[1]);
    Route r = findRoute(routeNumber, routeDescription);
    Schedule s = Schedule.findSchedule(date);
    System.out.println("Route: " + r);
    System.out.println(Route.getAllRoutes());
    System.out.println("Schedule: " + s);
    return s.nextDepartureRouteTimetable(time, r.getStops().get(0), r);
  }
}
