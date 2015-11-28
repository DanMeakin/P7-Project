package main;
import java.util.*;

public class RouteTimetable {
    private final Route route;
    private int startTime;
    private boolean isRushHour;
    private Schedule schedule;

    public RouteTimetable(Route route, Schedule schedule,int starttime, boolean isrushhour) {
        this.startTime = starttime;
        this.schedule = schedule;
        this.isRushHour = isrushhour;
        this.route = route;
    }

    public int getStartTime(){
        return this.startTime;
    }

    public boolean isRushHour(){
        return this.isRushHour;
    }

    public List<Integer> getStopTimes(){
        List<Integer> actualTiming = new ArrayList<>();
        for(int timing : route.getStopTiming(this.isRushHour, true)){
            actualTiming.add(timing + this.startTime);
        }
        return actualTiming;
    }

    public List<Stop> getStops(){
        return this.route.getStops();
    }

    public Bus getAllocatedBus() {
        return schedule.getAllocatedBus(this);
    }

}
