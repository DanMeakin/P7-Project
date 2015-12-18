package main.capacitytracker;

import java.util.*;
import java.time.LocalDate;

import main.RouteTimetable;
import main.Stop;

import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 * The CapacityCalculator class calculates the
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
    if (crowdedness.get("seatedOccupancy") < 1) {
      return CrowdednessIndicator.GREEN;
    } else if (crowdedness.get("totalOccupancy") < 1) {
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
    List<DataStoreRecord> data = getDataStore().read();

    List<Number> seatedOccupancy = new ArrayList<>();
    List<Number> totalOccupancy = new ArrayList<>();

    for (int i = 0; i < data.size(); i++) {
      DataStoreRecord record = data.get(i);
      seatedOccupancy.add(record.getSeatedOccupancyRate());
      totalOccupancy.add(record.getTotalOccupancyRate());
    }

    crowdedness.put("seatedOccupancy", average(seatedOccupancy));
    crowdedness.put("totalOccupancy", average(totalOccupancy));

    return crowdedness;
  }

  /**
   * Calculates the crowdedness of a currently-operating bus based on the
   * average linear regression value of the desired stop.
   *
   * This method creates linear regressions for all stops (S) which has already
   * been visited today on RouteTimetable and plots the historic capacities of
   * buses operating at each stop in S against the historic capacities of the
   * same bus operating at the desired stop.
   *
   * Each linear regression is then used to make a prediction as to how crowded
   * a bus arriving at the desired stop will be. The average (mean) is then
   * taken as the prediction of crowdedness of the RouteTimetable when it
   * arrives at stop.
   *
   * @return a map containing keys "seatedOccupancy" and "totalOccupancy" with
   *         values being the predicted seatedOccupancy rate and the predicted
   *         totalOccupancy rate for RouteTimetable at Stop
   */
  private Map<String, Double> realTimePredictedCrowdedness() {
    Stop busCurrentLocation = findBusCurrentLocation();
    boolean busAfterDesiredStop = 
      getRouteTimetable().getRoute().compareStops(busCurrentLocation, getStop()) > 0;
    if (busCurrentLocation == null) {
      String msg = "bus is not currently on route; unable to continue";
      throw new UnsupportedOperationException(msg);
    } else if (busAfterDesiredStop) {
      String msg = "bus has already passed the requested stop";
      throw new UnsupportedOperationException(msg);
    }
    List<Number> totalPredictions = new ArrayList<>();
    List<Number> seatedPredictions = new ArrayList<>();

    for (Stop s : getRouteTimetable().getStops()) {
      // If Stop s is before bus current location then get crowdedness
      if (getRouteTimetable().getRoute().compareStops(s, busCurrentLocation) < 0) {
        double currentTotalOccupancyRate = -1;
        double currentSeatedOccupancyRate = -1;
        SimpleRegression totalRegression = new SimpleRegression();
        SimpleRegression seatedRegression = new SimpleRegression();
        DataStoreReader ds = new DataStoreReader("data", s, getRouteTimetable());
        for (DataStoreRecord r : ds.read()) {
          LocalDate rDate = r.getTimestamp().toLocalDate();
          if (rDate.equals(LocalDate.now())) {
            currentTotalOccupancyRate = r.getTotalOccupancyRate();
            currentSeatedOccupancyRate = r.getSeatedOccupancyRate();
          } else {
            DataStoreRecord desiredStopRecord = getDataStore().selectRecordForDate(rDate);
            if (desiredStopRecord != null) {
              totalRegression.addData(
                  r.getTotalOccupancyRate(), 
                  desiredStopRecord.getTotalOccupancyRate()
                  );
              seatedRegression.addData(
                  r.getSeatedOccupancyRate(),
                  desiredStopRecord.getSeatedOccupancyRate()
                  );
            }
          }
        }
        double predictedTotalRate = totalRegression.predict(currentTotalOccupancyRate);
        double predictedSeatedRate = seatedRegression.predict(currentSeatedOccupancyRate);
        totalPredictions.add(predictedTotalRate);
        seatedPredictions.add(predictedSeatedRate);
      }
    }   

    Map<String, Double> result = new HashMap<>();
    result.put("seatedOccupancy", (double) average(seatedPredictions));
    result.put("totalOccupancy", (double) average(totalPredictions));
    return result;
  }

  /**
   * Find the bus running this RouteTimetable's current location.
   *
   * If this RouteTimetable is currently in service and being run by a bus,
   * this method will return the last Stop for which data is currently
   * available. If the RouteTimetable is not being run then returns null.
   *
   * @return last Stop for which data is currently available for the bus
   *         currently operating RouteTimetable, or null if the RT is not
   *         currently in operation
   */
  private Stop findBusCurrentLocation() {
    // List all stops in reverse, then go backwards through them until
    // the last stop for which data exists is found
    List<Stop> allStops = getRouteTimetable().getStops();
    Collections.reverse(allStops);
    for (Stop s : allStops) {
      DataStoreReader ds = new DataStoreReader("data", s, getRouteTimetable());
      if (ds.selectRecordForDate(LocalDate.now()) != null) {
        return s;
      }  
    }
    return null;
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
   * Gets stop dataStore field.
   *
   * @return dataStore instance for specific stop and route timetable
   */
  public DataStoreReader getDataStore() {
    return dataStore;
  }

  /**
   * Gets routeTimetable associated with this.
   *
   * @return routeTimetable associated with this
   */
  public RouteTimetable getRouteTimetable() {
    return routeTimetable;
  }

  /**
   * Gets stop associated with this.
   *
   * @return stop associated with this
   */
  public Stop getStop() {
    return stop;
  }

}
