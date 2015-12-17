package main;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Ivo on 14-12-2015.
 */
public class CapacityDataStoreReader {

    RouteTimetable routeTimetable;
    Stop stop;
    CapacityDataStoreWriter.ColumnHeaderNames columnHeaderName;

    private static Calendar calendarFrom;
    private static Calendar calendarTo;
    private static int numOfDaysBeforeCurrentForFromDate = -90;

    public CapacityDataStoreReader(RouteTimetable routeTimetable, Stop stop, CapacityDataStoreWriter.ColumnHeaderNames columnHeaderName){
        this.routeTimetable = routeTimetable;
        this.stop = stop;
        this.columnHeaderName = columnHeaderName;
    }

    public List<String> filterHistoricData(){

        calendarFrom = Calendar.getInstance();
        calendarTo = Calendar.getInstance();
        calendarTo.add(Calendar.DATE, numOfDaysBeforeCurrentForFromDate);

        Date fromDate = calendarFrom.getTime();
        Date toDate = calendarTo.getTime();

        SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss Z");

        List<String> sourceData = readHistoricStopCrowdedness();
        List<String> filteredBusData = new ArrayList<>();

        for(int i = 0; i < sourceData.size(); i++){
            String[] splitLine = sourceData.get(i).split(",");
            if(convertSimpleYearMonth(splitLine[getColumnHeaderPosition(CapacityDataStoreWriter.ColumnHeaderNames.WRITE_DATE)]).after(fromDate)
                    && convertSimpleYearMonth(splitLine[getColumnHeaderPosition(CapacityDataStoreWriter.ColumnHeaderNames.WRITE_DATE)]).before(toDate)) {
                filteredBusData.add(splitLine[getColumnHeaderPosition(columnHeaderName)]);
            }
        }
        return filteredBusData;
    }

    private List<String> readHistoricStopCrowdedness() {

        List<String> busData = new ArrayList<>();

        String[] dataToMatch = new String[] { Integer.toString(routeTimetable.getID()), Integer.toString(routeTimetable.getStartTime()), routeTimetable.getSchedule().getOperatingDay().toString(), Integer.toString(stop.getID()) };

        try {
            if (CapacityDataStoreWriter.getLockStatus()) {
                throw new IOException("File" + CapacityDataStoreWriter.dataStore.getAbsolutePath() + "is currently in use!");
            }
            BufferedReader reader = new BufferedReader(new FileReader(CapacityDataStoreWriter.dataStore));
            reader.readLine(); //skip first line
            String line = null;
            while ((line = reader.readLine()) != null) {
                boolean conditionsMet = true;
                for (int i = 0; i < dataToMatch.length; i++) {
                    conditionsMet = line.contains(dataToMatch[i]);
                    if (!conditionsMet) {
                        break;
                    }
                }
                if (conditionsMet) {
                    busData.add(line);
                }
            }
            reader.close();
        } catch (IOException ex) {
            System.out.println("Failed to read from file" + CapacityDataStoreWriter.dataStore.getAbsolutePath());
            ex.printStackTrace();
        }
        return busData;
    }

    private static int getColumnHeaderPosition(CapacityDataStoreWriter.ColumnHeaderNames columnHeaderName) {
        int indexPos = 0;
        try {
            if(CapacityDataStoreWriter.getLockStatus()){
                throw new IOException("File" + CapacityDataStoreWriter.dataStore.getAbsolutePath() + "is currently in use!");
            }
            LineNumberReader reader = new LineNumberReader(new FileReader(CapacityDataStoreWriter.dataStore));

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
            System.out.println("Failed to read from file" + CapacityDataStoreWriter.dataStore.getAbsolutePath());
            ex.printStackTrace();
        }
        return indexPos;
    }

    private static Date convertSimpleYearMonth(String simpleDate) {
        String expectedPattern = "dd/MM/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        Date dayMonthYear = null;
        try {
            dayMonthYear = formatter.parse(simpleDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return dayMonthYear;
    }

    private static Date convertSimpleTime(String simpleDate) {
        String expectedPattern = "HH:mm:ss Z";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        Date time = null;
        try {
            time = formatter.parse(simpleDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return time;
    }

    public static Date getFromDate(){
        return calendarFrom.getTime();
    }

    public static Date getToDate(){
        return calendarTo.getTime();
    }

    public static void setNumOfDayBeforeCurrentForFromDate(int days){
        numOfDaysBeforeCurrentForFromDate = days;
    }

    public static int getNumOfDayBeforeCurrentForFromDate(){
        return numOfDaysBeforeCurrentForFromDate;
    }
}