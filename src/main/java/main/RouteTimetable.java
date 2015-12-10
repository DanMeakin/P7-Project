package main;
import java.util.*;

/**
 * The RouteTimetable class defines route timtetable type objects that
 * couples routes with timings.
 * @authors Ivo Hendriks, Janus Avbæk Larsen, Helle Hyllested Larsen, Dan Meakin.
 */
public class RouteTimetable {
    /** counter for generating unique ID's for each route timetable  */
    private static int routeTimetableCounter = 0;
    /** the unique ID number for this route timetable  */
    public final int routeTimetableID;
    /** the route associated with the route timetable  */
    private final Route route;
    /** the start time for the route timetable  */
    private int startTime;
    /** defines wether a route timetable will use rush hour timings or not  */
    private boolean isRushHour;
    /** the schedule the route timetable is associated with  */
    private Schedule schedule;

    /**
     * Creates a route timetable and add it to the stops list.
     *
     * @param starttime the start time of the route timetable.
     * @param schedule the schedule the route timetable will be associated with.
     * @param isrushhour defines wether a route timetable will use rush hour time between stops.
     * @param route the route the route timetable will be based on.
     */
    public RouteTimetable(Route route, Schedule schedule, int starttime, boolean isrushhour) {
        this.startTime = starttime;
        this.schedule = schedule;
        this.isRushHour = isrushhour;
        this.route = route;
        this.routeTimetableID = routeTimetableCounter++;
    }

    /**
     * Get the start time for a route timetable.
     *
     * @return start time the start time for the route timetable.
     */
    public int getStartTime(){
        return this.startTime;
    }

    /**
     * Checks wether a route timetable uses rush hour timings or not.
     *
     * @return true if the route timetable uses rush hour time between stops, else false.
     */
    public boolean isRushHour(){
        return this.isRushHour;
    }

    /**
     * Get route for a route timetable.
     *
     * @return route the route for the route timetable.
     */
    public Route getRoute(){
        return this.route;
    }

    /**
     * Get ID for a route timetable.
     *
     * @return routeTimetableID the ID for the route timetable.
     */
    public int getRouteTimetableID(){
        return this.routeTimetableID;
    }

    /**
     * Get the timing for a particular stop on the RouteTimetable.
     *
     * @param stop The stop for which to obtain timing
     * @return time in minutes from midnight at which a bus running this RT is
     *  due to arrive at the passed stop
     */
    public int timeAtStop(Stop stop) throws IllegalArgumentException {
      if (!getRoute().includesStop(stop)) {
        throw new IllegalArgumentException("RouteTimetable does not include stop " + stop);
      }
      return getStopTimes().get(getStops().indexOf(stop));
    }

    /**
     * Get the time between stops for a route timetable.
     *
     * @return true if the route timetable uses rush hour timings, else false.
     */
    public List<Integer> getStopTimes(){
        List<Integer> actualTiming = new ArrayList<>();
        for(int timing : route.getStopTiming(this.isRushHour, true)){
            actualTiming.add(timing + this.startTime);
        }
        return actualTiming;
    }

    /**
     * Get the stops associated with a route timetbale.
     *
     * @return route.getStops a list containing stops associated with the route timetable.
     */
    public List<Stop> getStops(){
        return this.route.getStops();
    }

    /**
     * Get the bus associated with a route timetable through the schedule.
     *
     * @return schedule.getAllocatedBus the bus associated with a route timetable
     * through the schedule. Will display null if no bus is associated.
     */
    public Bus getAllocatedBus() {
        return schedule.getAllocatedBus(this);
    }

}
