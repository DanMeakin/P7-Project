package java.main;


import main.Route;

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
        List<Integer> actualTime = new ArrayList<>();
        for(int i = 0; i < this.route.getStopTiming(this.isRushHour, true).size(); i++){
            int cummulativeStopTiming = this.route.getStopTiming(this.isRushHour, true).get(i) + this.startTime;
            actualTime.add(i, cummulativeStopTiming);
        }
        return actualTime;

    }
    public List<Stop> getStops(){
        return this.route.getStops();

    }

}

