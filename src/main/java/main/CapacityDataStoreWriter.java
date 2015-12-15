package main;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

public class CapacityDataStoreWriter {

    public static File dataStore = new File("data/dataStore.csv");

    public static enum ColumnHeaderNames {
        WRITE_DATE, WRITE_TIME, BUS_ID, ROUTE_TIMETABLE_ID, ROUTE_ID, ROUTE_DESCRIPTION, ROUTETIMETABLE_TIME_START, SCHEDULE_SCHEDULEDAYS, STOP_NUMBER,
        STOP_DESCRIPTION, PASSENGERS_EXITED, PASSENGERS_BOARDED, TOTAL_PASSENGERS, SEATED_OCCUPATION_RATE, TOTAL_OCCUPATION_RATE
    }

    private static boolean fileLocked;

    // private constructor
    private CapacityDataStoreWriter(){
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
                writer.write((bus.getRouteTimetable().getID() + ","));
            }
            catch (NullPointerException ex) {
                System.out.println("Bus has no RouteTimetable associated with it");
            }
            writer.write((bus.getRouteTimetable().getRoute().getNumber() + ","));
            writer.write((bus.getRouteTimetable().getRoute().getDescription() + ","));
            writer.write((bus.getRouteTimetable().getStartTime() + ","));
            writer.write((bus.getRouteTimetable().getSchedule().getOperatingDay() + ","));
            writer.write((bus.getStop().getID() + ","));
            writer.write((bus.getStop().getName() + ","));
            writer.write((bus.getNumPassengersExited() + ","));
            writer.write((bus.getNumPassengersBoarded() + ","));
            writer.write((bus.getNumPassengers() + ","));
            writer.write((bus.getSeatedOccupationRate() + ","));
            writer.write((bus.getTotalOccupationRate() + ","));
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

    public static boolean getLockStatus(){
        return fileLocked;
    }
}
