package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ivo on 14-12-2015.
 */
public class CapacityDataStoreReader {

    private List<Double> historicCurrentStopSeatedOccupationRate;
    private List<Double> historicCurrentStopTotalOccupationRate;
    private List<Double> historicRequestedStopSeatedOccupationRate;
    private List<Double> historicRequestedStopTotalOccupationRate;

    public CapacityDataStoreReader(RouteTimetable routeTimeTable, Stop requestedStop, Stop currentStop){
        this.historicCurrentStopSeatedOccupationRate = readHistoricCurrentStopCrowdedness();
        this.historicTotalOccupationRate = readHistoricRequestedStopCrowdedness();
        this.usw
    }

    public CapacityDataStoreReader(RouteTimetable routeTimeTable, Stop requestedStop){
        this.historicSeatedOccupationRate = readHistoricCurrentStopCrowdedness();
        this.historicTotalOccupationRate = readHistoricRequestedStopCrowdedness()
    }


    public static List<List<Double>> readHistoricCurrentStopCrowdedness(Date fromDate, Date toDate, RouteTimetable routeTimeTable, Stop requestedStop, Stop currentStop) {

        List<List<Double>> historicCurrentStopCrowdedness = new ArrayList<>();
        List<Double> historicSeatedCurrentStopCrowdedness = new ArrayList<>();
        List<Double> historicTotalCurrentStopCrowdedness = new ArrayList<>();

        List<String> historicCurrentStopCrowdednessDate = new ArrayList<>();
        List<String> historicRequestedStopCrowdednessDate = new ArrayList<>();

        SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss Z");

        String[] listOfStringsCurrentStop = new String[] { Integer.toString(routeTimeTable.getStartTime()), routeTimeTable.getSchedule().getOperatingDay().toString(), currentStop.toString() };
        String[] listOfStringsRequestedStop = new String[] { Integer.toString(routeTimeTable.getStartTime()), routeTimeTable.getSchedule().getOperatingDay().toString(), requestedStop.toString() };
        try {
            if(lockStatus()){
                throw new IOException("File" + dataStore.getAbsolutePath() + "is currently in use!");
            }
            BufferedReader reader = new BufferedReader(new FileReader(dataStore));
            String line = reader.readLine();
            while(line != null) {
                boolean conditionsCurrentMet = true;
                boolean conditionsRequestedMet = true;
                for (int i = 1; i < listOfStringsCurrentStop.length; i++) {
                    conditionsCurrentMet = line.contains(listOfStringsCurrentStop[i]);
                    conditionsRequestedMet = line.contains(listOfStringsRequestedStop[i]);
                    if (!conditionsCurrentMet || !conditionsRequestedMet) {
                        break;
                    }
                }
                if (conditionsCurrentMet) {
                    String[] busData = line.split(",");
                    if(convertSimpleYearMonth(busData[getColumnHeaderPosition(ColumnHeaderNames.WRITE_DATE)]).after(fromDate)
                            && convertSimpleYearMonth(busData[getColumnHeaderPosition(ColumnHeaderNames.WRITE_DATE)]).before(toDate)){
                        historicSeatedCurrentStopCrowdedness.add(Double.parseDouble(busData[getColumnHeaderPosition(ColumnHeaderNames.SEATED_OCCUPATION_RATE)]));
                        historicTotalCurrentStopCrowdedness.add(Double.parseDouble(busData[getColumnHeaderPosition(ColumnHeaderNames.TOTAL_OCCUPATION_RATE)]));
                        historicCurrentStopCrowdednessDate.add(busData[getColumnHeaderPosition(ColumnHeaderNames.WRITE_DATE)]);
                    }
                }
                if (conditionsRequestedMet) {
                    String[] busData = line.split(",");
                    if(convertSimpleYearMonth(busData[getColumnHeaderPosition(ColumnHeaderNames.WRITE_DATE)]).after(fromDate)
                            && convertSimpleYearMonth(busData[getColumnHeaderPosition(ColumnHeaderNames.WRITE_DATE)]).before(toDate)){
                        historicRequestedStopCrowdednessDate.add(busData[getColumnHeaderPosition(ColumnHeaderNames.WRITE_DATE)]);
                    }
                }
            }
            reader.close();
        }
        catch (IOException ex) {
            System.out.println("Failed to read from file" + dataStore.getAbsolutePath());
            ex.printStackTrace();
        }
        for(int i = 0; i <  historicCurrentStopCrowdednessDate.size(); i++){
            if(!historicCurrentStopCrowdednessDate.get(i).equals(historicRequestedStopCrowdednessDate.get(i))) {
                historicSeatedCurrentStopCrowdedness.remove(i);
                historicTotalCurrentStopCrowdedness.remove(i);
            }
            historicCurrentStopCrowdedness.add(historicSeatedCurrentStopCrowdedness);
            historicCurrentStopCrowdedness.add(historicTotalCurrentStopCrowdedness);
        }
        return historicCurrentStopCrowdedness;
    }

    public static List<List<Double>> readHistoricRequestedStopCrowdedness(Date fromDate, Date toDate, RouteTimetable routeTimeTable, Stop requestedStop) {

        List<List<Double>> historicRequestedStopCrowdedness = new ArrayList<>();
        List<Double> historicSeatedRequestedStopCrowdedness = new ArrayList<>();
        List<Double> historicTotalRequestedStopCrowdedness = new ArrayList<>();

        SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss Z");

        String[] listOfStringsRequestedStop = new String[] { routeTimeTable.toString(), requestedStop.toString()};
        try {
            if(lockStatus()){
                throw new IOException("File" + dataStore.getAbsolutePath() + "is currently in use!");
            }
            BufferedReader reader = new BufferedReader(new FileReader(dataStore));
            String line = reader.readLine();
            while(line != null) {
                boolean conditionsRequestedMet = true;
                for (int i = 1; i < listOfStringsRequestedStop.length; i++) {
                    conditionsRequestedMet = line.contains(listOfStringsRequestedStop[i]);
                    if (!conditionsRequestedMet) {
                        break;
                    }
                }
                if (conditionsRequestedMet) {
                    String[] busData = line.split(",");
                    if(convertSimpleYearMonth(busData[getColumnHeaderPosition(ColumnHeaderNames.WRITE_DATE)]).after(fromDate)
                            && convertSimpleYearMonth(busData[getColumnHeaderPosition(ColumnHeaderNames.WRITE_DATE)]).before(toDate)){
                        historicSeatedRequestedStopCrowdedness.add(Double.parseDouble(busData[getColumnHeaderPosition(ColumnHeaderNames.SEATED_OCCUPATION_RATE)]));
                        historicTotalRequestedStopCrowdedness.add(Double.parseDouble(busData[getColumnHeaderPosition(ColumnHeaderNames.TOTAL_OCCUPATION_RATE)]));
                    }
                }
            }
            reader.close();
        }
        catch (IOException ex) {
            System.out.println("Failed to read from file" + dataStore.getAbsolutePath());
            ex.printStackTrace();
        }
        historicRequestedStopCrowdedness.add(historicSeatedRequestedStopCrowdedness);
        historicRequestedStopCrowdedness.add(historicTotalRequestedStopCrowdedness);
        return historicRequestedStopCrowdedness;
    }

    public static int getColumnHeaderPosition(ColumnHeaderNames columnHeaderName) {
        int indexPos = 0;
        try {
            if(lockStatus()){
                throw new IOException("File" + dataStore.getAbsolutePath() + "is currently in use!");
            }
            LineNumberReader reader = new LineNumberReader(new FileReader(dataStore));

            reader.setLineNumber(0);
            String[] headers = reader.readLine().split(",");
            reader.close();

            for (int i = 1; i < headers.length; i++) {
                if(headers[i].equals(columnHeaderName.toString())) {
                    indexPos = i;
                }
            }
        }
        catch (IOException ex) {
            System.out.println("Failed to read from file" + dataStore.getAbsolutePath());
            ex.printStackTrace();
        }
        return indexPos;
    }
}


