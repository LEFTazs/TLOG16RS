package timelogger;

import java.time.LocalTime;
import java.time.DayOfWeek;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.ArrayList;
import java.util.List;
import timelogger.exceptions.*;

/**
 * Utility functions for the timelogger package.
 */
public class Util {
    /**
     * Round the given interval, so that it's length is a multiple of a quarter hour.
     * The starttime will not be rounded, only the endtime.
     * @param startTime Interval's starttime.
     * @param endTime Interval's endtime.
     * @return LocalTime The given endtime, rounded.
     */
    public static LocalTime roundToMultipleQuarterHour(
            LocalTime startTime, LocalTime endTime) {
        long minuteDuration = MINUTES.between(startTime, endTime);
        long remainder = minuteDuration % 15;
        long roundedMinuteDuration;
        if (remainder < 15 / 2) {
            roundedMinuteDuration = minuteDuration - remainder;
        } else {
            roundedMinuteDuration = minuteDuration - remainder + 15;
        }
        return startTime.plusMinutes(roundedMinuteDuration);
    }
    
    /**
     * Task t is seperated from the tasks list, if it's time interval doesn't
     * overlay any of the list's time intervals. The t task is also not seperated 
     * if starttime equals with any of the list's starttimes.
     * @param tasks List of tasks to check against.
     * @param t Task to check if it's time is seperated.
     * @return boolean Whether task t seperated or not.
     */
    public static boolean isSeperatedTime(
            List<Task> tasks, Task t) {
        List<Task> tasks_ = new ArrayList<>(tasks);
        boolean isNotSeperatedTime;
        if (t.isEndTimeSet()) {
            isNotSeperatedTime = tasks_.stream()
                    .filter(task -> task.isEndTimeSet())
                    .anyMatch(checkable -> 
                            (t.getEndTime().isAfter(checkable.getStartTime()) && 
                            t.getStartTime().isBefore(checkable.getEndTime()))
                            || t.getStartTime().equals(checkable.getStartTime())
                    );
        } else {
            isNotSeperatedTime = tasks_.stream()
                    .filter(task -> task.isEndTimeSet())
                    .anyMatch(checkable -> 
                            t.getStartTime().isBefore(checkable.getEndTime())
                    );
        }
        return !isNotSeperatedTime;
    }
    
    /**
     * Check if the specified workday's date is a weekday.
     * @param workDay Day to check.
     * @return Whether the specified day is on a weekday.
     */
    public static boolean isWeekday(WorkDay workDay) {
        DayOfWeek dayOfWeek = workDay.getActualDay().getDayOfWeek();
        boolean isWeekendDay = dayOfWeek == DayOfWeek.SATURDAY || 
                dayOfWeek == DayOfWeek.SUNDAY;
        return !isWeekendDay;
    }

    /**
     * Check if the specified time interval's length is a multiple of a quarter hour.
     * @param startTime Interval's starttime.
     * @param endTime Interval's endtime.
     * @return Whether the specified interval's length is a multiple of a quarter hour or not.
     * @exception EmptyTimeFieldException One of the given LocalTimes is a null.
     * @exception NotExpectedTimeOrderException The starttime is after endtime.
     */
    public static boolean isMultipleQuarterHour(
            LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null)
            throw new EmptyTimeFieldException();
        if (startTime.isAfter(endTime))
            throw new NotExpectedTimeOrderException();
        
        long timeInterval = MINUTES.between(startTime, endTime);
        boolean isMultiple = timeInterval % 15 == 0;
        return isMultiple;
    }

}
