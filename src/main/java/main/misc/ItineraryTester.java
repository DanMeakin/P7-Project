package main.misc;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import main.model.*;
import main.routeplanner.*;

public class ItineraryTester {
  
  public static void main(String[] args) {
      new DataLoader("data");
      System.out.println("Got " + Stop.getAllStops().size() + " Stops; got " + Route.getAllRoutes().size() + " Routes; got " + Walk.getAllWalks().size() + " Walks!");
      System.out.println("Created " + (RouteTimetable.getCounterValue() - 10_000) + " RouteTimetables; " + Bus.getAllBuses().size() + " buses");
      long startTime = System.nanoTime();
      ItineraryFinder itf = new ItineraryFinder(null, null, LocalDateTime.now());
      long endTime = System.nanoTime();
      System.out.println("Took " + (endTime - startTime) / 1_000_000_000 + "s to run CostEstimator");
      double counter = 0;
      double max = 0;
      for (int i = 0; i < 100; i++) {
        int numStops = Stop.getAllStops().size();
        int startIndex = (int) (Math.random() * numStops);
        int endIndex = (int) (Math.random() * numStops);
        Stop start = Stop.getAllStops().get(startIndex);
        Stop end = Stop.getAllStops().get(endIndex);
        System.out.print("Running Query #" + i + "\r");
        startTime = System.nanoTime();
        itf = new ItineraryFinder(start, end, LocalDateTime.of(2015, Month.DECEMBER, 1, 10, 0, 0));
        itf.findBestItineraries(3);
        endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000_000;
        counter += duration;
        max = Math.max(max, duration);
      }
      System.out.println("100 queries made in " + counter + "s. Longest query took " + max);
    }

}
