package main;

import java.time.*;
import java.util.Date;
import java.util.List;

/**
 * This class is used to find an appropriate route and bus to take between
 * two points.
 *
 * The RouteFinder uses a starting and ending bus stop and determines which
 * routes travel between these points. It then finds RouteTimetables for the
 * desired time and returns buses which can be taken between the desired stops.
 */
public class RouteFinder {

  private Stop startingStop;
  private Stop endingStop;
  private Schedule schedule;
  private LocalDate date;
  private LocalTime time;

  public RouteFinder(Stop startingStop, Stop endingStop, LocalDateTime searchTime) {
    this.startingStop = startingStop;
    this.endingStop = endingStop;
    this.date = searchTime.toLocalDate();
    this.time = searchTime.toLocalTime();
    this.schedule = Schedule.findSchedule(this.date);
  }

  /**
   * Find all paths between startingStop and endingStop.
   *
   * This method searches for all possible paths (i.e. journeys involving one
   * or multiple bus routes) between the startingStop and the endingStop.
   *
   * @return a nested list, each entry containing a list of routes representing
   *  a path between startingStop and endingStop
   */
  public List<List<Route>> findPaths() {
    return null;
  }
}
