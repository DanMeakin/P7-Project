package main.misc;

import main.model.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Date;
import java.util.Scanner;

import main.capacitytracker.*;
import main.misc.DataLoader;

/**
 * This class is used to simulate travel over a route.
 */
public class Traveller {

  private RouteTimetable routeTimetable;
  private Bus bus;

  public Traveller(RouteTimetable routeTimetable, Bus bus) { 
    this.routeTimetable = routeTimetable;
    this.bus = bus;
  }

  public Bus getBus() {
    return bus;
  }

  public RouteTimetable getRouteTimetable() {
    return routeTimetable;
  }

  /**
   * Carries-out the journey of the specified RouteTimetable.
   *
   * This method simulates a journey of a bus on a specified RouteTimetable,
   * requesting timing and capacity data at each stop.
   */
  public void travel() {
    Scanner reader = new Scanner(System.in);  // Reading from System.in
    getBus().startRoute(getRouteTimetable());
    List<Stop> stops = getRouteTimetable().getRoute().getStops();
    Stop firstStop = stops.get(0);
    Stop lastStop  = stops.get(stops.size()-1);
    System.out.println("Route: " + firstStop + " -> " + lastStop);
    System.out.println("RT #" + getRouteTimetable().getID());
    System.out.println("Details: ");

    for (Stop s : routeTimetable.getStops()) {
      bus.arrivesAtStop(s);
      System.out.println("== Stop " + s + " ==");
      System.out.println("Currently " + getBus().getNumPassengers() + " passengers");
      System.out.println("Please enter number of passengers departing: ");
      int exiting = reader.nextInt();
      bus.passengersExit(exiting);
      System.out.println("Please enter number of passengers boarding: ");
      int boarding = reader.nextInt();
      bus.passengersBoard(boarding);
      System.out.println(s);
      bus.leavesStop();
    }
    reader.close();
  }

  public static void main(String[] args) {
    DataLoader dataLoader = new DataLoader("data");
    DataStoreWriter ds = new DataStoreWriter("data");
    BusType bt = new BusType("Volvo", "100", 50, 30);
    Calendar cal = new GregorianCalendar(2015, 1, 1);
    Bus bus = new Bus(100, bt, cal.getTime());
    bus.addObserver(ds);

    LocalDate localDate = getDateInput();
    Route route = getRouteInput();
    Stop stop = getStopInput();
    int time = getTimeInput();
    createTraveller(localDate, bus, route, stop, time);
  }

  public static LocalDate getDateInput() {
    Scanner reader = new Scanner(System.in);
    System.out.println("Please enter date (using format dd/mm/yyyy):");
    DateTimeFormatter fmtr = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String date = reader.next();
    try {
      return LocalDate.parse(date, fmtr);
    } catch (Exception e) {
      throw new RuntimeException("unable to parse date " + date);
    }
  }

  public static Route getRouteInput() {
    Scanner reader = new Scanner(System.in);
    System.out.println("Please enter Route Number:");
    String routeNo = reader.next();
    List<Route> routes = Route.findRouteByNumber(routeNo);
    if (routes.size() == 0) {
      throw new RuntimeException("unable to find route number " + routeNo);
    }
    System.out.println("Please select desired route:");
    for (int i = 0; i < routes.size(); i++) {
      System.out.println((i+1) + ": " + routes.get(i).getNumber() + " " + routes.get(i).getDescription());
    }
    int selection = reader.nextInt() - 1;
    if (selection >= routes.size()) {
      throw new RuntimeException("selection value too large!");
    }
    return routes.get(selection);
  }

  public static Stop getStopInput() {
    Scanner reader = new Scanner(System.in);
    System.out.println("Please enter Stop name:");
    String stopName = reader.next();
    List<Stop> stops = Stop.findStop(stopName);
    if (stops.size() == 0) {
      throw new RuntimeException("unable to find stop " + stopName);
    }
    System.out.println("Please select desired stop:");
    for (int i = 0; i < stops.size(); i++) {
      System.out.println((i+1) + ": " + stops.get(i).getName());
    }
    int selection = reader.nextInt() - 1;
    if (selection >= stops.size()) {
      throw new RuntimeException("selection value too large!");
    }
    return stops.get(selection);
  }

  public static int getTimeInput() {
    Scanner reader = new Scanner(System.in);
    System.out.println("Please enter hour of day (using 24 hour clock): ");
    int hour = reader.nextInt();
    System.out.println("Please enter minute of hour: ");
    int minute = reader.nextInt();
    return (hour * 60) + minute;
  }

  public static void createTraveller(LocalDate ld, Bus b, Route r, Stop s, int time) {
    Schedule sched = Schedule.findSchedule(ld);
    RouteTimetable rt = sched.nextDepartureRouteTimetable(time, s, r);
    sched.allocateBus(rt, b);
    new Traveller(rt, b).travel();
  }

}
