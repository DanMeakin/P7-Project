package main;

import java.util.*;

import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 * The CapacityCaluclator class calculates the
 */
public class CapacityCalculator {

    private RouteTimetable routeTimetable;
    private Stop requestedStop;
    private Stop currentStop;

    private List<String> historicRequestedStopSeatedOccupation = CapacityDataStoreReader.filterHistoricData(routeTimetable, requestedStop, CapacityDataStoreWriter.ColumnHeaderNames.SEATED_OCCUPATION_RATE);
    private List<String> historicRequestedStopTotalOccupation = CapacityDataStoreReader.filterHistoricData(routeTimetable, requestedStop, CapacityDataStoreWriter.ColumnHeaderNames.TOTAL_OCCUPATION_RATE);

    private List<String> historicCurrentStopSeatedOccupation = CapacityDataStoreReader.filterHistoricData(routeTimetable, currentStop, CapacityDataStoreWriter.ColumnHeaderNames.SEATED_OCCUPATION_RATE);
    private List<String> historicCurrentStopTotalOccupation = CapacityDataStoreReader.filterHistoricData(routeTimetable, currentStop, CapacityDataStoreWriter.ColumnHeaderNames.TOTAL_OCCUPATION_RATE);

    private double currentSeatedOccupation = routeTimetable.getAllocatedBus().getSeatedOccupationRate();
    private double currentTotalOccupation = routeTimetable.getAllocatedBus().getTotalOccupationRate();

    //should be the same through runtime, breaks comparison otherwise.
    private static double crowdednessFactor = 1;

    public static enum crowdednessIndicator {
        GREEN, ORANGE, RED
    }

    public CapacityCalculator(RouteTimetable routeTimetable, Stop requestedStop) {
        this.routeTimetable = routeTimetable;
        this.requestedStop = requestedStop;
    }

    public crowdednessIndicator getCrowdednessIndicator() {
            if(pickCalculator().get(0) <= 0) {
                return crowdednessIndicator.GREEN;
            }
            if(pickCalculator().get(1) <= 0) {
                return crowdednessIndicator.ORANGE;
            }
        return crowdednessIndicator.RED;
    }

    private List<Double> pickCalculator(){
        List<Double> crowdednessPrediction = new ArrayList<>();
        if(routeTimetable.getAllocatedBus().getLastStop() != null && routeTimetable.getAllocatedBus().getLastStop() != requestedStop){
            currentStop = routeTimetable.getAllocatedBus().getLastStop();
            crowdednessPrediction.add(calculateCrowdedness(true, routeTimetable, currentStop, requestedStop));
            crowdednessPrediction.add(calculateCrowdedness(false, routeTimetable, currentStop, requestedStop));
            return crowdednessPrediction;
        }
        crowdednessPrediction.add(calculateCrowdedness(true, routeTimetable, requestedStop));
        crowdednessPrediction.add(calculateCrowdedness(false, routeTimetable, requestedStop));
        return crowdednessPrediction;
    }

    /*
    * Relies on org.apache.commons.math3
    */
    private Double calculateCrowdedness(boolean occupationSeated, RouteTimetable routeTimetable, Stop currentStop, Stop requestedStop) throws IllegalArgumentException {

        SimpleRegression simpleRegression = new SimpleRegression();

        List<Double> historicRequestedStopOccupationDouble = new ArrayList<>();
        List<Double> historicCurrentStopOccupationDouble = new ArrayList<>();

        if (occupationSeated) {
            compareListSize(historicCurrentStopSeatedOccupation);
            for (int i = 0; i < historicRequestedStopSeatedOccupation.size(); i++) {
                historicRequestedStopOccupationDouble.add(Double.parseDouble(historicRequestedStopSeatedOccupation.get(i)));
                historicCurrentStopOccupationDouble.add(Double.parseDouble(historicCurrentStopSeatedOccupation.get(i)));
            }
            for (int i = 0; i < historicRequestedStopOccupationDouble.size(); i++) {
                simpleRegression.addData(historicRequestedStopOccupationDouble.get(i), historicCurrentStopOccupationDouble.get(i));
            }
            return (Math.round((simpleRegression.predict(currentSeatedOccupation)) * crowdednessFactor) * 100) / 100d;
        }
        compareListSize(historicCurrentStopTotalOccupation);
        for (int i = 0; i < historicRequestedStopTotalOccupation.size(); i++) {
            historicRequestedStopOccupationDouble.add(Double.parseDouble(historicRequestedStopTotalOccupation.get(i)));
            historicCurrentStopOccupationDouble.add(Double.parseDouble(historicCurrentStopTotalOccupation.get(i)));
        }

        for (int i = 0; i < historicRequestedStopOccupationDouble.size(); i++) {
            simpleRegression.addData(historicRequestedStopOccupationDouble.get(i), historicCurrentStopOccupationDouble.get(i));
        }
        return (Math.round((simpleRegression.predict(currentTotalOccupation)) * crowdednessFactor) * 100) / 100d;
    }


    public Double calculateCrowdedness(boolean occupationSeated, RouteTimetable routeTimetable, Stop RequestedStop) {

        List<Double> historicRequestedStopOccupationDouble = new ArrayList<>();
        double averageCrowdedness = 0;

        if (occupationSeated && !historicRequestedStopSeatedOccupation.isEmpty()) {
            for (int i = 0; i < historicRequestedStopSeatedOccupation.size(); i++) {
                historicRequestedStopOccupationDouble.add(Double.parseDouble(historicRequestedStopSeatedOccupation.get(i)));
            }
            for (Double dataPoint : historicRequestedStopOccupationDouble) {
                averageCrowdedness += dataPoint;
            }
            return (Math.round(averageCrowdedness / historicRequestedStopOccupationDouble.size() * crowdednessFactor) * 100) / 100d;
        }
        for (int i = 0; i < historicRequestedStopTotalOccupation.size(); i++) {
            historicRequestedStopOccupationDouble.add(Double.parseDouble(historicRequestedStopTotalOccupation.get(i)));
        }
        for (Double dataPoint : historicRequestedStopOccupationDouble) {
            averageCrowdedness += dataPoint;
        }
        return (Math.round(averageCrowdedness / historicRequestedStopOccupationDouble.size() * crowdednessFactor) * 100) / 100d;
    }

    public void compareListSize(List<String> currentStopList) {
        for (int i = 0; i < currentStopList.size(); i++) {
            if (!CapacityDataStoreReader.filterHistoricData(routeTimetable, requestedStop, CapacityDataStoreWriter.ColumnHeaderNames.WRITE_DATE).get(i).
                    equals(CapacityDataStoreReader.filterHistoricData(routeTimetable, currentStop, CapacityDataStoreWriter.ColumnHeaderNames.WRITE_DATE).get(i))) {
                currentStopList.remove(i);
            }
        }
    }

    public void setCrowdednessFactor(double cf){
        cf = crowdednessFactor;
    }

    public double getCrowdednessFactor(){
        return crowdednessFactor;
    }

}