package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.csv.*;

import main.routeplanner.Itinerary;
import main.routeplanner.ItineraryFinder;
import main.routeplanner.ItineraryLeg;

/**
 * This class loads example Stop, Route, RouteTimetable, Bus and BusType data
 * into the system.
 *
 * As this system is not in use, it is necessary to load mock data to
 * demonstrate the functionality of the system.
 */
public class DataLoader {

  private final String dataPath;
  private Schedule weekdaySchedule;
  private Schedule saturdaySchedule;
  private Schedule sundaySchedule;

  public String getDataPath() {
    return dataPath;
  }

  /**
   * Creats a DataLoader instance.
   *
   * @param dataPath the path to the folder containing stop and route CSV files
   */
  public DataLoader(String dataPath) {
    this.dataPath = dataPath;
    createSchedule();
    loadStops();
    loadRoutes();
    createRouteTimetables();

  }
  
  /**
   * Opens and returns stops data file.
   *
   * @return file object for stops data
   */
  private File stopsFile() {
    return new File(getDataPath(), "stops.csv");
  }

  /**
   * Opens and returns routes data file.
   *
   * @return file object for routes data
   */
  private File routesFile() {
    return new File(getDataPath(), "routes.csv");
  }

  /**
   * Opens and returns timings data file.
   *
   * @return file object for timings data
   */
  private File timingsFile() {
    return new File(getDataPath(), "frequencies.csv");
  }

  public static void main(String[] args) {
    new DataLoader("data");
    System.out.println("Got " + Stop.getAllStops().size() + " Stops; got " + Route.getAllRoutes().size() + " Routes; got " + Walk.getAllWalks().size() + " Walks!");
    System.out.println("Created " + (RouteTimetable.getCounterValue() - 10_000) + " RouteTimetables; " + Bus.getAllBuses().size() + " buses");
    long startTime = System.nanoTime();
    ItineraryFinder itf = new ItineraryFinder(null, null, LocalDateTime.now());
    long endTime = System.nanoTime();
    System.out.println("Took " + (endTime - startTime) / 1_000_000_000 + "s");
    Scanner scan = new Scanner(System.in);
    while (true) {
      Stop start;
      Stop end;
      System.out.println("Please enter starting stop: ");
      String s = scan.next();
      if (s.equals("exit")) {
        break;
      }
      List<Stop> starts = Stop.findStop(s);
      if (starts.size() > 0) {
        start = starts.get(0);
      } else {
        continue;
      }
      System.out.println("Got " + start);
      System.out.println("Please enter ending stop: ");
      String e = scan.next();
      List<Stop> ends = Stop.findStop(e);
      if (ends.size() > 0) {
        end = ends.get(0);
      } else {
        continue;
      }
      System.out.println("Got " + end);
      itf = new ItineraryFinder(start, end, LocalDateTime.now());
      Itinerary best = itf.findBestItinerary();
      System.out.println(best);
      System.out.println(best.getLegs().size());
      for (ItineraryLeg leg : best.getLegs()) {
        System.out.println(leg.getOrigin() + " - " + leg.getDestination() + ", " + leg.getStartTime());
      }
    }
    scan.close();
  }

  /**
   * Loads stops data from file.
   */
  private void loadStops() {
    CSVParser parser = createCSVParser(stopsFile());
    for (CSVRecord record : parser) {
      String name = record.get(0);
      int id = Integer.parseInt(record.get(1));
      double latitude = Double.parseDouble(record.get(2)) / 1_000_000;
      double longitude = Double.parseDouble(record.get(3)) / 1_000_000;
      new Stop(id, name, latitude, longitude);
    }
  }

  /**
   * Loads routes data from file.
   */
  private void loadRoutes() {
    CSVParser parser = createCSVParser(routesFile());
    for (CSVRecord record : parser) {
      String number = record.get(0);
      List<Stop> stops = new ArrayList<>();
      // Create list of stops from comma-separated stop ID#s
      for (String stopID : record.get(2).split(",")) {
        stops.add(Stop.findStop(Integer.parseInt(stopID)));
      }
      String description = stops.get(0).getName() + " - " + record.get(1);
      Route thisRoute = new Route(number, description, stops.get(0));
      // Create list of timings from comma-separated timings, but ignore the
      // first as this relates to first stop, which is added when Route is
      // contructed.
      String[] timings = record.get(3).split(",");
      for (int i = 0; i < timings.length; i++) {
        if (i == 0) {
          continue;
        } else {
          thisRoute.addStop(
              stops.get(i), 
              Integer.parseInt(timings[i]), 
              Integer.parseInt(timings[i]) + 1
              );
        }
      }
    }
  }

  /**
   * Creates RouteTimetables from loaded Routes.
   */
  private void createRouteTimetables() {
    BusType genericBusType = new BusType("Generic Manufacturer", "Bus 101", 50, 30);
    Date acquisitionDate = new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime();
    int counter = 1000;
    CSVParser parser = createCSVParser(timingsFile());
    for (CSVRecord record : parser) {
      String number = record.get(0);
      int weekdayFrequency = Integer.parseInt(record.get(1));
      int saturdayFrequency = Integer.parseInt(record.get(2));
      int sundayFrequency = Integer.parseInt(record.get(3));
      int startTime = Integer.parseInt(record.get(4));
      int endTime = Integer.parseInt(record.get(5));
      for (Route r : Route.findRouteByNumber(number)) {
        int currentTime = startTime;
        while (currentTime <= endTime) {
          // Rush hour between 8am and 10am, and 4pm and 6pm
          boolean isRushHour = 
            (startTime >= 8*60 && startTime <= 10*60) ||
            (startTime >= 16*60 && startTime <= 18*60);
          RouteTimetable rt = new RouteTimetable(r, weekdaySchedule, currentTime, isRushHour);
          Bus bus = new Bus(counter, genericBusType, acquisitionDate);
          weekdaySchedule.allocateBus(rt, bus);
          System.out.println("Created RT for Route " + r.getNumber() + " " + r.getDescription() + " at " + currentTime);
          currentTime += weekdayFrequency;
          counter++;
        }
        currentTime = startTime;
        while (currentTime <= endTime) {
          RouteTimetable rt = new RouteTimetable(r, saturdaySchedule, currentTime, false);
          Bus bus = new Bus(counter, genericBusType, acquisitionDate);
          saturdaySchedule.allocateBus(rt, bus);
          System.out.println("Created RT for Route " + r.getNumber() + " " + r.getDescription() + " at " + currentTime);
          currentTime += saturdayFrequency;
          counter++;
        }
        currentTime = startTime;
        while (currentTime <= endTime) {
          RouteTimetable rt = new RouteTimetable(r, sundaySchedule, currentTime, false);
          Bus bus = new Bus(counter, genericBusType, acquisitionDate);
          sundaySchedule.allocateBus(rt, bus);
          System.out.println("Created RT for Route " + r.getNumber() + " " + r.getDescription() + " at " + currentTime);
          currentTime += sundayFrequency;
          counter++;
        }
      }
    }
  }

  /**
   * Creates Schedules.
   */
  private void createSchedule() {
    Date startDate = new GregorianCalendar(2015, GregorianCalendar.JANUARY, 1).getTime();
    Date endDate = new GregorianCalendar(2016, GregorianCalendar.DECEMBER, 31).getTime();
    weekdaySchedule = new Schedule(startDate, endDate, Schedule.DayOption.WEEKDAYS);
    saturdaySchedule = new Schedule(startDate, endDate, Schedule.DayOption.SATURDAY);
    sundaySchedule = new Schedule(startDate, endDate, Schedule.DayOption.SUNDAY);
  }

  /**
   * Creates a CSV parse from File object.
   *
   * @param file the file object from which to create CSVParser
   * @return CSVParser instance
   */
  private CSVParser createCSVParser(File file) {
    try {
      CSVParser parser = CSVParser.parse(file, StandardCharsets.UTF_8, CSVFormat.RFC4180);
      return parser;
    } catch (IOException e) {
      throw new RuntimeException("IOException: " + e.getMessage());
    }
  }
}
