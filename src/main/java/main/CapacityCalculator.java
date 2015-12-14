package main;

import java.util.*;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class CapacityCalculator {

    private Date toDate;
    private Date fromDate;

    private RouteTimetable routeTimetable;
    private Stop requestedStop;
    private Stop currentStop = routeTimetable.getAllocatedBus().getLastStop();

    private List<String> historicRequestedStopSeatedOccupation = CapacityDataStoreReader.filterHistoricStopData(fromDate, toDate, routeTimetable, requestedStop, CapacityDataStoreWriter.ColumnHeaderNames.SEATED_OCCUPATION_RATE);
    private List<String> historicRequestedStopTotalOccupation = CapacityDataStoreReader.filterHistoricStopData(fromDate, toDate, routeTimetable, requestedStop, CapacityDataStoreWriter.ColumnHeaderNames.TOTAL_OCCUPATION_RATE);

    private List<String> historicCurrentStopSeatedOccupation = CapacityDataStoreReader.filterHistoricStopData(fromDate, toDate, routeTimetable, currentStop, CapacityDataStoreWriter.ColumnHeaderNames.SEATED_OCCUPATION_RATE);
    private List<String> historicCurrentStopTotalOccupation = CapacityDataStoreReader.filterHistoricStopData(fromDate, toDate, routeTimetable, currentStop, CapacityDataStoreWriter.ColumnHeaderNames.TOTAL_OCCUPATION_RATE);

    private double currentSeatedOccupation = routeTimetable.getAllocatedBus().getSeatedOccupationRate();
    private double currentTotalOccupation = routeTimetable.getAllocatedBus().getTotalOccupationRate();

    //should be the same through runtime, breaks comparison otherwise.
    private final double crowdednessFactor;
    private int numOfDaysFromCurrentForFromDate = -90;
    private Calendar calendar = GregorianCalendar.getInstance();

    public static enum crowdednessIndicator {
        GREEN, ORANGE, RED
    }

    public CapacityCalculator(Double crowdednessFactor,  RouteTimetable routeTimetable, Stop requestedStop) {
        this.crowdednessFactor = crowdednessFactor;
        this.routeTimetable = routeTimetable;
        this.requestedStop = requestedStop;

        toDate = getToDate();
        fromDate = getFromDate();
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

    public List<Double> pickCalculator(){
        List<Double> crowdednessPrediction = new ArrayList<>();
        if(routeTimetable.getAllocatedBus() != null){
            crowdednessPrediction.add(calculateCrowdedness(true, fromDate, toDate, routeTimetable, currentStop, requestedStop));
            crowdednessPrediction.add(calculateCrowdedness(false, fromDate, toDate, routeTimetable, currentStop, requestedStop));
            return crowdednessPrediction;
        }
        crowdednessPrediction.add(calculateCrowdedness(true, fromDate, toDate, routeTimetable, requestedStop));
        crowdednessPrediction.add(calculateCrowdedness(false, fromDate, toDate, routeTimetable, requestedStop));
        return crowdednessPrediction;
    }

    /*
    * Relies on org.apache.commons.math3
    */
    public Double calculateCrowdedness(boolean occupationSeated, Date fromDate, Date toDate, RouteTimetable routeTimetable, Stop currentStop, Stop requestedStop) throws IllegalArgumentException {

        SimpleRegression simpleRegression = new SimpleRegression();

        List<Double> historicRequestedStopOccupationDouble = new ArrayList<>();
        List<Double> historicCurrentStopOccupationDouble = new ArrayList<>();

        if (occupationSeated) {
            compareListSizes(historicRequestedStopSeatedOccupation, historicCurrentStopSeatedOccupation);
            for (int i = 0; i < historicRequestedStopSeatedOccupation.size(); i++) {
                historicRequestedStopOccupationDouble.add(Double.parseDouble(historicRequestedStopSeatedOccupation.get(i)));
                historicCurrentStopOccupationDouble.add(Double.parseDouble(historicCurrentStopSeatedOccupation.get(i)));
            }
            for (int i = 0; i < historicRequestedStopOccupationDouble.size(); i++) {
                simpleRegression.addData(historicRequestedStopOccupationDouble.get(i), historicCurrentStopOccupationDouble.get(i));
            }
            return (Math.round((simpleRegression.predict(currentSeatedOccupation)) * crowdednessFactor) * 100) / 100d;
        }
        compareListSizes(historicRequestedStopTotalOccupation, historicCurrentStopTotalOccupation);
        for (int i = 0; i < historicRequestedStopTotalOccupation.size(); i++) {
            historicRequestedStopOccupationDouble.add(Double.parseDouble(historicRequestedStopTotalOccupation.get(i)));
            historicCurrentStopOccupationDouble.add(Double.parseDouble(historicCurrentStopTotalOccupation.get(i)));
        }

        for (int i = 0; i < historicRequestedStopOccupationDouble.size(); i++) {
            simpleRegression.addData(historicRequestedStopOccupationDouble.get(i), historicCurrentStopOccupationDouble.get(i));
        }
        return (Math.round((simpleRegression.predict(currentTotalOccupation)) * crowdednessFactor) * 100) / 100d;
    }


    public Double calculateCrowdedness(boolean occupationSeated, Date fromDate, Date toDate, RouteTimetable routeTimetable, Stop RequestedStop) {

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

    public void compareListSizes(List<String> currentStopList, List<String> requestedStopList) {
        for (int i = 0; i < currentStopList.size(); i++) {
            if (!CapacityDataStoreReader.filterHistoricStopData(fromDate, toDate, routeTimetable, requestedStop, CapacityDataStoreWriter.ColumnHeaderNames.WRITE_DATE).get(i).
                    equals(CapacityDataStoreReader.filterHistoricStopData(fromDate, toDate, routeTimetable, currentStop, CapacityDataStoreWriter.ColumnHeaderNames.WRITE_DATE).get(i))) {
                currentStopList.remove(i);
            }
        }
    }

    public double getCrowdednessFactor(double cf){
        return crowdednessFactor;
    }

    private Date getToDate(){
        return calendar.getTime();
    }

    public Date getFromDate(){
        calendar.add(Calendar.DATE, numOfDaysFromCurrentForFromDate);
        return calendar.getTime();
    }

    private void setNumOfDaysFromNowForFromDate(int days){
        numOfDaysFromCurrentForFromDate = days;
    }

}