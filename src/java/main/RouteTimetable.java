package main;
import java.io.Serializable;

public class RouteTimetable implements Serializable {
	private int startTime;
	private boolean isRushHour;

	
	public RouteTimetable(int starttime, boolean isrushhour, Route route){
		this.startTime = starttime;
		this.isRushHour = isrushhour;
	
	
	}
	
}	