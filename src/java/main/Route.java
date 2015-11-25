package main;

import java.util.ArrayList;
import java.util.List;

public class Route {
	private final String routeNumber;
	private final String routeDescription;
	private List<Stop> stops = new ArrayList<Stop>();
	private List<Integer> timeBetweenStops = new ArrayList<Integer>();
	private List<Integer> rushHourTimeBetweenStops = new ArrayList<Integer>();

	public Route(String routeNumber, String routeDescription, Stop routeStart) {
		stops.add(routeStart);
		timeBetweenStops.add(0);
		rushHourTimeBetweenStops.add(0);
		this.routeNumber = routeNumber;
		this.routeDescription = routeDescription;
	}

	public void addStop(Stop stop, int time, int rushHourTime) {
		stops.add(stop);
		timeBetweenStops.add(time);
		rushHourTimeBetweenStops.add(rushHourTime);
		//For testing whether entries are stored in ArrayList stops
		for (int i = 0; i < stops.size(); i++) {
			System.out.print(stops.get(i).getID() + " ");
			System.out.print(stops.get(i).getName() + " ");
			System.out.println(stops.get(i).getLocation() + " ");
		}
		System.out.println("End of list");
		System.out.println("number of entries in ArrayList route for this route: " + stops.size());
		System.out.println("");
		//end test
	}

	public String getNumber() {
		return routeNumber;
	}

	public String getDescription() {
		return routeDescription;
	}

	public List<Stop> getStops() {
		return stops;
	}

	//public HashMap<Stop, Integer> viewStopTimes(){
	//	return timeBetweenStops;
	//}
	//public HashMap<Stop, Integer> viewRushHourStopTimes(){
	//	return rushHourTimeBetweenStops;
	//}

	public List<Integer> getStopTiming(boolean isRushHour, boolean isCumulative) {
		List<Integer> stopTiming = new ArrayList<>();
		List<Integer> sourceTiming;

		if (isRushHour) {
			sourceTiming = rushHourTimeBetweenStops;
		} else {
			sourceTiming = timeBetweenStops;
		}

		int accumulator = 0;
		for (int i = 0; i < stops.size(); i++) {
			int currentTiming = sourceTiming.get(i);
			if (isCumulative) {
				accumulator = accumulator + currentTiming;
				stopTiming.add(accumulator);
			} else {
				stopTiming.add(currentTiming);
			}
		}

		return stopTiming;
	}
	public boolean includesStop(Stop stop){
		for (int i = 0; i < stops.size(); i++) {
			Stop thisStop = stops.get(i);
			if(stop == thisStop){
				return true;
				
			}
			
		}
		return false;
	}

	public List<Integer> getNonRushHourTiming() {
		return getStopTiming(false, false);
	}

	public List<Integer> getRushHourTiming() {
		return getStopTiming(true, false);
	}

	public List<Integer> getCumulativeNonRushHourTiming() {
		return getStopTiming(false, true);
	}

	public List<Integer> getCumulativeRushHourTiming() {
		return getStopTiming(true, true);
	}


}

