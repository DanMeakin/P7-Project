package main;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Ivo on 14-12-2015.
 */
public class CapacityDataStoreReader {

    private static Calendar calendar = GregorianCalendar.getInstance();
    private static int numOfDaysBeforeCurrentForFromDate = -90;

    private CapacityDataStoreReader(){
    }

    public static List<String> filterHistoricData(RouteTimetable routeTimetable, Stop stop, CapacityDataStoreWriter.ColumnHeaderNames columnHeaderName){

        Date toDate = getToDate();
        Date fromDate = getFromDate();

        SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss Z");

        List<String> sourceData = readHistoricStopCrowdedness(routeTimetable, stop);
        List<String> filteredBusData = new ArrayList<>();

        for(int i = 0; i < sourceData.size(); i++){
            String[] splitLine = sourceData.get(i).split(",");
            System.out.println("SourceData: " + sourceData.get(i));
            //System.out.println("SplitData: " + splitLine[12]);
            System.out.println(dayMonthYear.format(fromDate) + " " + dayMonthYear.format(toDate));
            if(convertSimpleYearMonth(splitLine[getColumnHeaderPosition(CapacityDataStoreWriter.ColumnHeaderNames.WRITE_DATE)]).after(fromDate)
                    && convertSimpleYearMonth(splitLine[getColumnHeaderPosition(CapacityDataStoreWriter.ColumnHeaderNames.WRITE_DATE)]).before(toDate)) {
                filteredBusData.add(splitLine[getColumnHeaderPosition(columnHeaderName)]);
            }
        }
        for(int i = 0; i < filteredBusData.size(); i ++) {
            System.out.println(filteredBusData.get(0));
        }
        return filteredBusData;
    }

    public static List<String> readHistoricStopCrowdedness(RouteTimetable routeTimetable, Stop stop) {

        List<String> busData = new ArrayList<>();

        //String[] dataToMatch = new String[] { "10000", "0", "WEEKDAYS", "1" };
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

    public static int getColumnHeaderPosition(CapacityDataStoreWriter.ColumnHeaderNames columnHeaderName) {
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

    public static Date convertSimpleYearMonth(String simpleDate) {
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

    public static Date convertSimpleTime(String simpleDate) {
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

    private static Date getToDate(){
        return calendar.getTime();
    }

    public static Date getFromDate(){
        calendar.add(Calendar.DATE, numOfDaysBeforeCurrentForFromDate);
        return calendar.getTime();
    }

    public static void setNumOfDayBeforeCurrentForFromDate(int days){
        numOfDaysBeforeCurrentForFromDate = days;
    }

    public static int getNumOfDayBeforeCurrentForFromDate(){
        return numOfDaysBeforeCurrentForFromDate;
    }
}