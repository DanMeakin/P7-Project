package main;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.ArrayList;
import main.CapacityDataStore;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import main.CapacityDataStore;

public class CapacityCalculator {

    private double crowdednessFactor;
    private double currentCrowdedness;

    public CapacityCalculator() {
    }

    /*
    * Relies on org.apache.commons.math3.stat.regression.SimpleRegression
    */
    public double calculateCrowdedness(double crowdednessFactor, double currentCrowdedness, RouteTimetable routeTimetable, Stop currentStop, Stop requestedStop) throws IllegalArgumentException {
        this.crowdednessFactor = crowdednessFactor;
        this.currentCrowdedness = currentCrowdedness;


        List<Double> requestedStopHistoricData = new ArrayList<>(CapacityDataStore.readHistoricRequestedStopCrowdedness(routeTimetable, requestedStop));
        List<Double> currentStopHistoricData = new ArrayList<>(CapacityDataStore.readHistoricCurrentStopCrowdedness(routeTimetable, requestedStop, currentStop));

        SimpleRegression simpleRegression = new SimpleRegression();

        if (!requestedStopHistoricData.isEmpty() && !currentStopHistoricData.isEmpty() && requestedStopHistoricData.size() == currentStopHistoricData.size()) {
            for (int i = 0; i < requestedStopHistoricData.size(); i++) {
                simpleRegression.addData(currentStopHistoricData.get(i), requestedStopHistoricData.get(i));
            }
        }
        return (simpleRegression.predict(currentCrowdedness))*crowdednessFactor;
    }

    public double calculateCrowdedness(double crowdednessFactor, RouteTimetable rtt, Stop requestedStop) {
        this.crowdednessFactor = crowdednessFactor;

        List<Double> requestedStopHistoricData = new ArrayList<>(CapacityDataStore.readHistoricRequestedStopCrowdedness(rtt, requestedStop));

        double averageCrowdedness = 0;
        if (!requestedStopHistoricData.isEmpty()) {
            for (Double dataPoint : requestedStopHistoricData) {
                averageCrowdedness += dataPoint;
            }
            return averageCrowdedness / requestedStopHistoricData.size();
        }
        return averageCrowdedness*crowdednessFactor;
    }
}
