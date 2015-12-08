package main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CapacityDataStore {

    private static File f = new File("test.txt");

    private static final SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss Z");

    public static enum ColumnHeaderNames {
        WRITE_DATE, WRITE_TIME, BUS_ID, ROUTE_ID, ROUTE_DESCRIPTION, ROUTETIMETABLE_IDENTIFIER, STOP_IDENTIFIER,
        STOP_DESCRIPTION, PASSENGERS_EXITED, PASSENGERS_BOARDED, TOTAL_PASSENGERS, PERC_CAPACITY
    }

    /*
    private static final String WRITE_DATE = "date";
    private static final String WRITE_TIME = "time";
    private static final String BUS_ID = "bus id";
    private static final String ROUTE_ID = "route id";
    private static final String ROUTE_DESCRIPTION = "route description";
    private static final String ROUTETIMETABLE_IDENTIFIER = "routeTimetable identifier";
    private static final String STOP_IDENTIFIER = "stop identifier";
    private static final String STOP_DESCRIPTION = "stop description";
    private static final String PASSENGERS_EXITED = "passengers exited";
    private static final String PASSENGERS_BOARDED = "passengers boarded";
    private static final String TOTAL_PASSENGERS = "total number of passengers";
    private static final String PERC_CAPACITY = "occupied percentage of capacity";


    private static final String [] FILE_HEADER_MAPPING = {"date","time","bus id","route id","route description","routeTimetable identifier","stop identifier","stop description",
            "Passengers exited","passengers boarded","total number of passengers","occupied percentage of capacity"};
    */

    //creates a new CapacityDataStore
    private CapacityDataStore(){
    }

    public static void writeBusStateChange(){
        Date date = new Date();
        dayMonthYear.format(date);
        time.format(date);
    }

    public static List<Double> readHistoricRequestedStopCrowdedness(RouteTimetable rtt, Stop requestedStop){
        List<Double> historicRequestedStopCrowdedness = new ArrayList<>();
        return historicRequestedStopCrowdedness;
    }
    /*
    * relies on org.apache.commons.csv
    */
    public static List<Double> readHistoricCurrentStopCrowdedness(Date fromDate, Date toDate, RouteTimetable routeTimeTable, Stop requestedStop, Stop currentStop) throws NullPointerException {
        List<Double> historicCurrentStopCrowdedness = new ArrayList<>();
        List<String> historicCurrentStopCrowdednessDate = new ArrayList<>();
        List<String> historicRequestedStopCrowdednessDate = new ArrayList<>();

        Date date = new Date();

        FileReader fileReader = null;

        String[] listOfStringsCurrentStop = new String[] { dayMonthYear.format(fromDate), dayMonthYear.format(toDate), routeTimeTable.toString(), currentStop.toString()};

        String[] listOfStringsRequestedStop = new String[] { dayMonthYear.format(fromDate), dayMonthYear.format(toDate), routeTimeTable.toString(), requestedStop.toString()};

        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line = null;
            while((line = reader.readLine()) != null) {
                boolean conditionsCurrentMet = true;
                boolean conditionsRequestedMet = true;
                for (int i = 1; i < listOfStringsCurrentStop.length; i++) {
                    conditionsCurrentMet = line.contains(listOfStringsCurrentStop[i]) && date.after(fromDate) && date.before(toDate);
                    conditionsRequestedMet = line.contains(listOfStringsRequestedStop[i]) && date.after(fromDate) && date.before(toDate);
                    if (!conditionsCurrentMet && !conditionsRequestedMet || !date.after(fromDate) && !date.before(toDate)) {
                        break;
                    }
                }
                if (conditionsCurrentMet) {
                    String[] busData = line.split(",");
                    historicCurrentStopCrowdedness.add(Double.parseDouble(busData[getColumnHeaderPosition(ColumnHeaderNames.PERC_CAPACITY)]));
                    historicCurrentStopCrowdednessDate.add(busData[getColumnHeaderPosition(ColumnHeaderNames.WRITE_DATE)]);
                }
                if (conditionsRequestedMet) {
                    String[] busData = line.split(",");
                    historicRequestedStopCrowdednessDate.add(busData[getColumnHeaderPosition(ColumnHeaderNames.WRITE_DATE)]);
                }
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i <  historicCurrentStopCrowdednessDate.size(); i++){
            if(!historicCurrentStopCrowdednessDate.get(i).equals(historicRequestedStopCrowdednessDate.get(i)));
            historicCurrentStopCrowdedness.remove(i);
        }
    return historicCurrentStopCrowdedness;
    }

    public static int getColumnHeaderPosition(ColumnHeaderNames columnHeaderName) {
        int indexPos = 0;
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(f));
            String result;

            reader.setLineNumber(0);
            String[] headers = reader.readLine().split(",");
            reader.close();

            for (int i = 1; i < headers.length; i++) {
                if(headers[i].equals(columnHeaderName)) {
                    indexPos = i;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return indexPos;
    }
}
