package main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CapacityDataStore {

    private static File f = new File("test.txt");

    public static enum ColumnHeaderNames {
        WRITE_DATE, WRITE_TIME, BUS_ID, ROUTE_ID, ROUTE_DESCRIPTION, ROUTETIMETABLE_IDENTIFIER, STOP_IDENTIFIER,
        STOP_DESCRIPTION, PASSENGERS_EXITED, PASSENGERS_BOARDED, TOTAL_PASSENGERS, OCCUPATION_RATE
    }

    //creates a new CapacityDataStore
    private CapacityDataStore(){
    }

    public static void writeBusStateChange(Bus bus){
        Date date = new Date();
        SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss Z");
        dayMonthYear.format(date);
        time.format(date);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));
            BufferedReader reader = new BufferedReader(new FileReader(f));
            if (reader.readLine() == null) {
                for (ColumnHeaderNames columnHeaderName : ColumnHeaderNames.values()) {
                    writer.write(columnHeaderName + ",");
                }
                writer.newLine();
            }
            writer.write(dayMonthYear.format(date) + ",");
            writer.write(time.format(date) + ",");
            writer.write((bus.getFleetNumber() + ","));
            writer.write((bus.getRouteTimetable().getRoute().getNumber() + ","));
            writer.write((bus.getRouteTimetable().getRoute().getDescription() + ","));
            writer.write((bus.getRouteTimetable() + ","));
            writer.write((bus.getStop() + ","));
            writer.write((bus.getStop().getName() + ","));
            writer.write((bus.getNumPassengersExited() + ","));
            writer.write((bus.getNumPassengersBoarded() + ","));
            writer.write((bus.getNumPassengers() + ","));
            writer.write((bus.getOccupationRate() + ","));
            writer.newLine();
            writer.flush();
            writer.close();
        }
        catch (IOException ex) {
            System.out.println("Failed to save bus state update to" + f.getAbsolutePath());
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

        Date date = new Date();
        SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss Z");
        dayMonthYear.format(date);
        time.format(date);

        //FileReader fileReader = null;

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
                        historicCurrentStopCrowdedness.add(Double.parseDouble(busData[getColumnHeaderPosition(ColumnHeaderNames.OCCUPATION_RATE)]));
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

    public static List<Double> readHistoricRequestedStopCrowdedness(Date fromDate, Date toDate, RouteTimetable routeTimeTable, Stop requestedStop) {
        List<Double> historicRequestedStopCrowdedness = new ArrayList<>();

        Date date = new Date();
        SimpleDateFormat dayMonthYear = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss Z");
        dayMonthYear.format(date);
        time.format(date);

        //FileReader fileReader = null;

        String[] listOfStringsRequestedStop = new String[] { dayMonthYear.format(fromDate), dayMonthYear.format(toDate), routeTimeTable.toString(), requestedStop.toString()};

        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line = null;
            while((line = reader.readLine()) != null) {
                boolean conditionsRequestedMet = true;
                for (int i = 1; i < listOfStringsRequestedStop.length; i++) {
                    conditionsRequestedMet = line.contains(listOfStringsRequestedStop[i]) && date.after(fromDate) && date.before(toDate);
                    if (!conditionsRequestedMet || !date.after(fromDate) && !date.before(toDate)) {
                        break;
                    }
                }
                if (conditionsRequestedMet) {
                    String[] busData = line.split(",");
                    historicRequestedStopCrowdedness.add(Double.parseDouble(busData[getColumnHeaderPosition(ColumnHeaderNames.OCCUPATION_RATE)]));
                }
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return historicRequestedStopCrowdedness;
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
