package main;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ivo on 14-12-2015.
 */
public class CapacityDataStoreReader {

    private static File dataStore = new File("data/dataStore.csv");

    private CapacityDataStoreReader(){
    }

    public static List<String> filterHistoricStopData(Date fromDate, Date toDate, RouteTimetable routeTimetable, Stop stop, CapacityDataStoreWriter.ColumnHeaderNames columnHeaderName){

        SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss Z");

        List<String> sourceData = readHistoricStopCrowdedness(routeTimetable, stop);
        List<String> filteredBusData = new ArrayList<>();

        for(int i = 0; i < sourceData.size(); i++){
            String[] splittedLine = readHistoricStopCrowdedness(routeTimetable, stop).get(i).split(",");
            if(convertSimpleYearMonth(splittedLine[getColumnHeaderPosition(CapacityDataStoreWriter.ColumnHeaderNames.WRITE_DATE)]).after(fromDate)
                    && convertSimpleYearMonth(splittedLine[getColumnHeaderPosition(CapacityDataStoreWriter.ColumnHeaderNames.WRITE_DATE)]).before(toDate)) {
                filteredBusData.add(splittedLine[getColumnHeaderPosition(columnHeaderName)]);
            }
        }
        return filteredBusData;
    }

    public static List<String> readHistoricStopCrowdedness(RouteTimetable routeTimetable, Stop stop) {

        List<String> busData = new ArrayList<>();

        String[] dataToMatch = new String[] { Integer.toString(routeTimetable.getStartTime()), routeTimetable.getSchedule().getOperatingDay().toString(), stop.toString() };
        try {
            if(CapacityDataStoreWriter.getLockStatus()){
                throw new IOException("File" + dataStore.getAbsolutePath() + "is currently in use!");
            }
            BufferedReader reader = new BufferedReader(new FileReader(dataStore));
            String line = reader.readLine();
            while(line != null) {
                boolean conditionsMet = true;
                for (int i = 1; i < dataToMatch.length; i++) {
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
        }
        catch (IOException ex) {
            System.out.println("Failed to read from file" + dataStore.getAbsolutePath());
            ex.printStackTrace();
        }
        return busData;
    }

    public static int getColumnHeaderPosition(CapacityDataStoreWriter.ColumnHeaderNames columnHeaderName) {
        int indexPos = 0;
        try {
            if(CapacityDataStoreWriter.getLockStatus()){
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
}