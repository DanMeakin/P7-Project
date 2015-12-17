package main.capacitytracker;

import java.util.*;

import main.RouteTimetable;
import main.Stop;

import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 * The CapacityCaluclator class calculates the
 */
public class CapacityCalculator {

    private RouteTimetable routeTimetable;
    private Stop requestedStop;
    private Stop currentStop;

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
        if(routeTimetable.getAllocatedBus().getStop() != null && routeTimetable.getAllocatedBus().getStop() != requestedStop){
            currentStop = routeTimetable.getAllocatedBus().getStop();
            crowdednessPrediction.add(calculateCrowdedness(true, routeTimetable, currentStop, requestedStop));
            crowdednessPrediction.add(calculateCrowdedness(false, routeTimetable, currentStop, requestedStop));
            return crowdednessPrediction;
        }
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

        double currentSeatedOccupation = this.routeTimetable.getAllocatedBus().getSeatedOccupationRate();
        double currentTotalOccupation = this.routeTimetable.getAllocatedBus().getTotalOccupationRate();

        CapacityDataStoreReader cdswRequestedSeated = new CapacityDataStoreReader(routeTimetable, requestedStop, CapacityDataStoreWriter.ColumnHeaderNames.SEATED_OCCUPATION_RATE);
        CapacityDataStoreReader cdswRequestedTotal = new CapacityDataStoreReader(routeTimetable, requestedStop, CapacityDataStoreWriter.ColumnHeaderNames.TOTAL_OCCUPATION_RATE);

        CapacityDataStoreReader cdswCurrentSeated = new CapacityDataStoreReader(routeTimetable, currentStop, CapacityDataStoreWriter.ColumnHeaderNames.SEATED_OCCUPATION_RATE);
        CapacityDataStoreReader cdswCurrentTotal = new CapacityDataStoreReader(routeTimetable, currentStop, CapacityDataStoreWriter.ColumnHeaderNames.TOTAL_OCCUPATION_RATE);

        SimpleRegression simpleRegression = new SimpleRegression();

        List<Double> historicRequestedStopOccupationDouble = new ArrayList<>();
        List<Double> historicCurrentStopOccupationDouble = new ArrayList<>();

        if (occupationSeated) {
            compareListSize(cdswCurrentSeated.filterHistoricData());
            for (int i = 0; i < cdswRequestedSeated.filterHistoricData().size(); i++) {
                historicRequestedStopOccupationDouble.add(Double.parseDouble(cdswRequestedSeated.filterHistoricData().get(i)));
                historicCurrentStopOccupationDouble.add(Double.parseDouble(cdswCurrentSeated.filterHistoricData().get(i)));
            }
            for (int i = 0; i < historicRequestedStopOccupationDouble.size(); i++) {
                simpleRegression.addData(historicRequestedStopOccupationDouble.get(i), historicCurrentStopOccupationDouble.get(i));
            }
            return (Math.round((simpleRegression.predict(currentSeatedOccupation)) * crowdednessFactor) * 100) / 100d;
        }
        compareListSize(cdswCurrentTotal.filterHistoricData());
        for (int i = 0; i < cdswRequestedTotal.filterHistoricData().size(); i++) {
            historicRequestedStopOccupationDouble.add(Double.parseDouble(cdswRequestedTotal.filterHistoricData().get(i)));
            historicCurrentStopOccupationDouble.add(Double.parseDouble(cdswCurrentTotal.filterHistoricData().get(i)));
        }

        for (int i = 0; i < historicRequestedStopOccupationDouble.size(); i++) {
            simpleRegression.addData(historicRequestedStopOccupationDouble.get(i), historicCurrentStopOccupationDouble.get(i));
        }
        return (Math.round((simpleRegression.predict(currentTotalOccupation)) * crowdednessFactor) * 100) / 100d;
    }

    private Double calculateCrowdedness(boolean occupationSeated, RouteTimetable routeTimetable, Stop RequestedStop) {

        CapacityDataStoreReader cdswRequestedSeated = new CapacityDataStoreReader(routeTimetable, requestedStop, CapacityDataStoreWriter.ColumnHeaderNames.SEATED_OCCUPATION_RATE);
        CapacityDataStoreReader cdswRequestedTotal = new CapacityDataStoreReader(routeTimetable, requestedStop, CapacityDataStoreWriter.ColumnHeaderNames.TOTAL_OCCUPATION_RATE);

        List<Double> historicRequestedStopOccupationDouble = new ArrayList<>();
        double averageCrowdedness = 0;

        if (occupationSeated && !cdswRequestedSeated.filterHistoricData().isEmpty()) {
            for (int i = 0; i < cdswRequestedSeated.filterHistoricData().size(); i++) {
                historicRequestedStopOccupationDouble.add(Double.parseDouble(cdswRequestedSeated.filterHistoricData().get(i)));
            }
            for (Double dataPoint : historicRequestedStopOccupationDouble) {
                averageCrowdedness += dataPoint;
            }
            return (Math.round(averageCrowdedness / historicRequestedStopOccupationDouble.size() * crowdednessFactor) * 100) / 100d;
        }
        for (int i = 0; i < cdswRequestedTotal.filterHistoricData().size(); i++) {
            historicRequestedStopOccupationDouble.add(Double.parseDouble(cdswRequestedTotal.filterHistoricData().get(i)));
        }
        for (Double dataPoint : historicRequestedStopOccupationDouble) {
            averageCrowdedness += dataPoint;
        }
        return (Math.round(averageCrowdedness / historicRequestedStopOccupationDouble.size() * crowdednessFactor) * 100) / 100d;
    }

    private void compareListSize(List<String> currentStopList) {

        CapacityDataStoreReader cdswReqDate = new CapacityDataStoreReader(routeTimetable, requestedStop, CapacityDataStoreWriter.ColumnHeaderNames.WRITE_DATE);
        CapacityDataStoreReader cdswCurrDate = new CapacityDataStoreReader(routeTimetable, currentStop, CapacityDataStoreWriter.ColumnHeaderNames.WRITE_DATE);

        for (int i = 0; i < currentStopList.size(); i++) {
            if (!cdswReqDate.filterHistoricData().get(i).equals(cdswCurrDate.filterHistoricData().get(i))) {
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
