package main;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

public class CapacityDataStore {

    private static File dataStore = new File("data/dataStore.csv");

    public static enum ColumnHeaderNames {
        WRITE_DATE, WRITE_TIME, BUS_ID, ROUTE_ID, ROUTE_DESCRIPTION, ROUTETIMETABLE_IDENTIFIER, STOP_IDENTIFIER,
        STOP_DESCRIPTION, PASSENGERS_EXITED, PASSENGERS_BOARDED, TOTAL_PASSENGERS, OCCUPATION_RATE
    }

    private static boolean fileLocked;

    // private constructor
    private CapacityDataStore(){
    }

    public static void writeBusStateChange(Bus bus){
        try {
            if (!dataStore.exists()) {
                dataStore.getParentFile().mkdirs();
                dataStore.createNewFile();
            }
        }
        catch (IOException ex){
            System.out.println("Failed to create file" + dataStore.getAbsolutePath());
            ex.printStackTrace();
        }
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dataStore, true));
            BufferedReader reader = new BufferedReader(new FileReader(dataStore));
            setLock();
            if (reader.readLine() == null) {
                for (ColumnHeaderNames columnHeaderName : ColumnHeaderNames.values()) {
                    writer.write(columnHeaderName + ",");
                }
                writer.newLine();
            }
            writer.write(getCurrentDayMonth() + ",");
            writer.write(getCurrentTime() + ",");
            writer.write((bus.getFleetNumber() + ","));
            try {
                writer.write((bus.getRouteTimetable().getRoute().getNumber() + ","));
            }
            catch (NullPointerException ex) {
                System.out.println("Bus has no RouteTimetable associated with it");
            }
            writer.write((bus.getRouteTimetable().getRoute().getDescription() + ","));
            writer.write((bus.getRouteTimetable() + ","));
            try{
                writer.write((bus.getStop() + ","));
            }
            catch (NullPointerException ex) {
                System.out.println("Bus is currently not at a stop");
            }
            writer.write((bus.getStop().getName() + ","));
            writer.write((bus.getNumPassengersExited() + ","));
            writer.write((bus.getNumPassengersBoarded() + ","));
            writer.write((bus.getNumPassengers() + ","));
            writer.write((bus.getOccupationRate() + ","));
            writer.newLine();
            writer.flush();
            writer.close();
            removeLock();
        }
        catch (IOException ex) {
            System.out.println("Failed to save bus state update to" + dataStore.getAbsolutePath());
            ex.printStackTrace();
        }
    }

    /*
    *
    */
    public static List<Double> readHistoricCurrentStopCrowdedness(Date fromDate, Date toDate, RouteTimetable routeTimeTable, Stop requestedStop, Stop currentStop) {

        List<Double> historicCurrentStopCrowdedness = new ArrayList<>();
        List<String> historicCurrentStopCrowdednessDate = new ArrayList<>();
        List<String> historicRequestedStopCrowdednessDate = new ArrayList<>();

        SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss Z");

        //FileReader fileReader = null;

        String[] listOfStringsCurrentStop = new String[] { routeTimeTable.toString(), currentStop.toString()};
        String[] listOfStringsRequestedStop = new String[] { routeTimeTable.toString(), requestedStop.toString()};
            try {
                if(lockStatus()){
                    throw new IOException("File" + dataStore.getAbsolutePath() + "is currently in use!");
                }
                BufferedReader reader = new BufferedReader(new FileReader(dataStore));
                String line = null;
                while((line = reader.readLine()) != null) {
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
                        historicCurrentStopCrowdedness.add(Double.parseDouble(busData[getColumnHeaderPosition(ColumnHeaderNames.OCCUPATION_RATE)]));
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
                historicCurrentStopCrowdedness.remove(i);
            }
        }
    return historicCurrentStopCrowdedness;
    }

    public static List<Double> readHistoricRequestedStopCrowdedness(Date fromDate, Date toDate, RouteTimetable routeTimeTable, Stop requestedStop) {

        List<Double> historicRequestedStopCrowdedness = new ArrayList<>();

        SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss Z");

        String[] listOfStringsRequestedStop = new String[] { routeTimeTable.toString(), requestedStop.toString()};
        try {
            if(lockStatus()){
                throw new IOException("File" + dataStore.getAbsolutePath() + "is currently in use!");
            }
            BufferedReader reader = new BufferedReader(new FileReader(dataStore));
            String line = null;
            while((line = reader.readLine()) != null) {
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
                        historicRequestedStopCrowdedness.add(Double.parseDouble(busData[getColumnHeaderPosition(ColumnHeaderNames.OCCUPATION_RATE)]));
                    }
                }
            }
            reader.close();
        }
        catch (IOException ex) {
            System.out.println("Failed to read from file" + dataStore.getAbsolutePath());
            ex.printStackTrace();
        }
        return historicRequestedStopCrowdedness;
    }

    public static int getColumnHeaderPosition(ColumnHeaderNames columnHeaderName) {
        int indexPos = 0;
        try {
            if(lockStatus()){
                throw new IOException("File" + dataStore.getAbsolutePath() + "is currently in use!");
            }
            LineNumberReader reader = new LineNumberReader(new FileReader(dataStore));
            String result;

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

    public static String getCurrentDayMonth(){
        Date date = new Date();
        SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd/MM/yyyy");
        return dayMonthYear.format(date);
    }

    public static String getCurrentTime(){
        Date date = new Date();
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss Z");
        return time.format(date);
    }

    public static void setLock(){
        fileLocked = true;
    }

    public static void removeLock(){
        fileLocked = false;
    }

    public static boolean lockStatus(){
        return fileLocked;
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
