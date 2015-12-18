package main.capacitytracker;

import java.util.*;

import main.RouteTimetable;
import main.Stop;

import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 * The CapacityCaluclator class calculates the
 */
public class CapacityCalculator {

  private final RouteTimetable routeTimetable;
  private final Stop stop;
  private final DataStoreReader dataStore;

  public static enum CrowdednessIndicator {

    GREEN, ORANGE, RED;

    public boolean moreCrowdedThan(CrowdednessIndicator otherCI) {
      if (this.equals(RED)) {
        return false;
      } else if (this.equals(ORANGE)) {
        return (otherCI.equals(RED));
      } else {
        return (otherCI.equals(RED) || otherCI.equals(ORANGE));
      }
    }

  }

  /**
   * Initializes a CapacityCalculator instance.
   *
   * @param routeTimetable the route timetable for which to calculate likely
   *                       capacity/crowdedness
   * @param stop           the stop for which to calculate likely crowdedness
   */
  public CapacityCalculator(RouteTimetable routeTimetable, Stop stop) {
    this.routeTimetable = routeTimetable;
    this.stop = stop;
    this.dataStore = new DataStoreReader("data", stop, routeTimetable);
  }

  /**
   * Gets an indicator of likely crowdedness of this RouteTimetable at this
   * Stop.
   *
   * @return GREEN if there is likely seating available; ORANGE if there is 
   *         likely standing room available but no seating available; and
   *         RED if there is likely no room available
   */
  public CrowdednessIndicator getCrowdednessIndicator() {
    return pastCrowdednessIndicator();
  }

  /**
   * Returns an indicator of likely crowdedness of this RouteTimetable at this
   * Stop based on historic data.
   *
   * @return GREEN if there is likely seating available; ORANGE if there is 
   *         likely standing room available but no seating available; and
   *         RED if there is likely no room available
   */
  private CrowdednessIndicator pastCrowdednessIndicator() {
    Map<String, Double> crowdedness = pastAverageCrowdedness();
    if (crowdedness.get("maxSeatedPassengers") - crowdedness.get("numberPassengersOnDeparture") > 0) {
      return CrowdednessIndicator.GREEN;
    } else if (crowdedness.get("maxTotalPassengers") - crowdedness.get("numberPassengersOnDeparture") > 0) {
      return CrowdednessIndicator.ORANGE;
    } else {
      return CrowdednessIndicator.RED;
    }
  }

  /**
   * Calculates the average past crowdedness of the specified RouteTimetable at
   * the specified stop.
   *
   * This method uses a linear regression over all existing crowdedness data
   * for the specified RouteTimetable and Stop.
   *
   * @return average past crowdedness for seating and standing room
   */
  private Map<String, Double> pastAverageCrowdedness() {
    Map<String, Double> crowdedness = new HashMap<>();
    List<Map<String, Integer>> data = getDataStore().getPassengerData();

    List<Number> arrival = new ArrayList<>();
    List<Number> departure = new ArrayList<>();
    List<Number> boarding = new ArrayList<>();
    List<Number> exiting = new ArrayList<>();

    List<Number> standingOccupancy = new ArrayList<>();
    List<Number> seatedOccupancy = new ArrayList<>();
    List<Number> totalOccupancy = new ArrayList<>();

    for (int i = 0; i < data.size(); i++) {
      double standingOcc = (data.get(i).get("numberPassengersOnDeparture") - 
                            data.get(i).get("maxSeatedPassengers")) / data.get(i).get("maxTotalPassengers");
      double seatedOcc = (data.get(i).get("numberPassengersOnDeparture") / data.get(i).get("maxSeatedPassengers"));
      double totalOcc = (data.get(i).get("numberPassengersOnDeparture")) / data.get(i).get("maxTotalPassengers");
      arrival.add(data.get(i).get("numberPassengersOnArrival"));
      departure.add(data.get(i).get("numberPassengersOnDeparture"));
      boarding.add(data.get(i).get("numberPassengersBoarded"));
      exiting.add(data.get(i).get("numberPassengersExited"));
      standingOccupancy.add(standingOcc);
      seatedOccupancy.add(seatedOcc);
      totalOccupancy.add(totalOcc);
    }

    crowdedness.put("numberPassengersOnArrival", average(arrival));
    crowdedness.put("numberPassengersOnDeparture", average(departure));
    crowdedness.put("numberPassengersBoarded", average(boarding));
    crowdedness.put("numberPassengersExited", average(exiting));

    crowdedness.put("standingOccupancy", average(standingOccupancy));
    crowdedness.put("seatedOccupancy", average(seatedOccupancy));
    crowdedness.put("totalOccupancy", average(totalOccupancy));

    crowdedness.put("maxSeatedPassengers", (double) data.get(0).get("maxSeatedPassengers"));
    crowdedness.put("maxStandingPassengers", (double) data.get(0).get("maxStandingPassengers"));
    crowdedness.put("maxTotalPassengers", (double) data.get(0).get("maxTotalPassengers"));

    return crowdedness;
  }

  /**
   * Calculates the average (mean) of all entries in list.
   *
   * @param list a list of numerical values
   * @return average (mean) of all values
   */
  private double average(List<Number> list) {
    double accumulator = 0;
    for (Number o : list) {
      accumulator += (double) o;
    }
    return accumulator / list.size();
  }

  /**
   * Gets dataStore field.
   *
   * @return dataStore instance
   */
  public DataStoreReader getDataStore() {
    return dataStore;
  }

}
