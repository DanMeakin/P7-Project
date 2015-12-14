package main;

import java.util.*;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class CapacityCalculator {

    Date toDate;
    Date fromDate;

    private RouteTimetable routeTimetable;
    private Stop requestedStop;

    //should be the same through runtime, breaks comparison otherwise.
    private final double crowdednessFactor;
    private int numOfDaysFromCurrentForFromDate = -90;
    private Calendar calendar = GregorianCalendar.getInstance();

    public static enum crowdednessIndicator {
        GREEN, ORANGE, RED
    }

    public CapacityCalculator(Double crowdednessFactor, Stop requestedStop, RouteTimetable routeTimetable) {
        this.crowdednessFactor = crowdednessFactor;
        this.routeTimetable = routeTimetable;
        this.requestedStop = requestedStop;
        toDate = getToDate();
        fromDate = getFromDate();
    }

    public crowdednessIndicator getCrowdednessIndicator() {
        pickCalculator();
            if(pickCalculator().get(0) <= 0) {
                return crowdednessIndicator.GREEN;
            }
            if(pickCalculator().get(1) <= 0) {
                return crowdednessIndicator.ORANGE;
            }
        return crowdednessIndicator.RED;
    }

    public List<Double> pickCalculator(){
        if(routeTimetable.getAllocatedBus() != null){
            Stop currentStop;
            currentStop = routeTimetable.getAllocatedBus().getStop();
            return calculateCrowdedness(fromDate, toDate, routeTimetable, currentStop, requestedStop);
        }
        return calculateCrowdedness(fromDate, toDate, routeTimetable, requestedStop);
    }

    /*
    * Relies on org.apache.commons.math3
    */
    public List<Double> calculateCrowdedness(Date fromDate, Date toDate, RouteTimetable routeTimetable, Stop currentStop, Stop requestedStop) throws IllegalArgumentException {

        List<List<Double>> requestedStopHistoricData = new ArrayList<>(CapacityDataStoreWriter.readHistoricRequestedStopCrowdedness(fromDate, toDate, routeTimetable, requestedStop));

        List<List<Double>> currentStopHistoricData = new ArrayList<>(CapacityDataStoreWriter.readHistoricCurrentStopCrowdedness(fromDate, toDate, routeTimetable, requestedStop, currentStop));

        SimpleRegression simpleRegression = new SimpleRegression();

        if (!requestedStopHistoricData.isEmpty() && !currentStopHistoricData.isEmpty() && requestedStopHistoricData.size() == currentStopHistoricData.size()) {

            List<List> sourceDataRequested = new ArrayList<>();
            List<Double> sourceDataRequestedContent = new ArrayList();

            List<List> sourceDataCurrent = new ArrayList<>();
            List<Double> sourceDataCurrentContent = new ArrayList();
            List<Double> resultData = new ArrayList<>();
            List<Double> requestedStopHistoricDataSeated = new ArrayList<>();

           forrequestedStopHistoricData.get(0))

                for (int i = 0; i < requestedStopHistoricData) {
                    System.out.println(c.getName());
                }
            }

            Iterator<List<Double>> it = CapacityDataStoreWriter.readHistoricRequestedStopCrowdedness(getFromDate(), getToDate(), routeTimetable, requestedStop).iterator();
                while(it.hasNext())
                {
                    Iterator<Double> itr = it.next().iterator();
                    while(itr.hasNext())
                    {
                        resultData.add(simpleRegression(itr.next(), sourceDataRequested.itr.next();
                    }
                    simpleRegression.clear();
                }

            for(List<String> csv : csvList)
            {
                //dumb logic to place the commas correctly
                if(!csv.isEmpty())
                {
                    System.out.print(csv.get(0));
                    for(int i=1; i < csv.size(); i++)
                    {
                        System.out.print("," + csv.get(i));
                    }
                }
                System.out.print("\n");



            for (int i = 0; i < sourceDataRequested.size(); i++) {
                sourceDataRequested.add(requestedStopHistoricData.get(i));
                sourceDataCurrent.add(currentStopHistoricData.get(i));
                for (int j = 0; j < sourceDataRequested.size(); j++) {
                    sourceDataCurrent.get(i);
                    sourceDataCurrentContent.add(sourceDataCurrent.get(j));
                }
            }
        }
        return (Math.round( (simpleRegression.predict(currentCrowdedness))*crowdednessFactor) *100)/100d;
        //(Math.round( (simpleRegressionTotal.predict(currentCrowdedness))*crowdednessFactor) *100)/100d;
    }

    public List<Double> calculateCrowdedness(Date fromDate, Date toDate, double crowdednessFactor, RouteTimetable routeTimetable, Stop requestedStop) {

        List<Double> requestedStopHistoricData = new ArrayList<Double>(CapacityDataStoreWriter.readHistoricRequestedStopCrowdedness(getFromDate(), getToDate(), routeTimetable, requestedStop));

        double averageCrowdedness = 0;
        if (!requestedStopHistoricData.isEmpty()) {
            for (Double dataPoint : requestedStopHistoricData) {
                averageCrowdedness += dataPoint;
            }
            return averageCrowdedness / requestedStopHistoricData.get(0).size();
        }
        return (Math.round( averageCrowdedness*crowdednessFactor) *100)/100d;
    }

    public double getCrowdednessFactor(double cf){
        return crowdednessFactor;
    }

    private Date getToDate(){
        return calendar.getTime();
    }

    private void setNumOfDaysFromNowForFromDate(int days){
        numOfDaysFromCurrentForFromDate = days;
    }

    public Date getFromDate(){
        calendar.add(Calendar.DATE, numOfDaysFromCurrentForFromDate);
        return calendar.getTime();
    }
}