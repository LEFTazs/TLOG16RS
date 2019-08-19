package timelogger;

import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.MINUTES;
import timelogger.exceptions.*;

/**
 * A task is a duration of time, with an assigned indentifier.
 * Additionally, a comment can be added for the task.
 */
@lombok.Getter
@lombok.Setter
public class Task {
    private String taskId;
    private String comment;
    private LocalTime startTime;
    private LocalTime endTime;
    
    public Task(String taskId, String comment, 
                int startHour, int startMin,
                int endHour, int endMin) {
        this.taskId = taskId;
        this.comment = comment;
        this.startTime = LocalTime.of(startHour, startMin);
        this.endTime = LocalTime.of(endHour, endMin);
        throwExceptionIfInvalidTaskId();
        throwExceptionIfWrongTimeOrder();
        roundEndTime();
    }
    
    public Task(String taskId, String comment,
                String startTime, String endTime) {
        this.taskId = taskId;
        this.comment = comment;
        if (startTime == null || endTime == null)
            throw new EmptyTimeFieldException();
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
        throwExceptionIfInvalidTaskId();
        throwExceptionIfWrongTimeOrder();
        roundEndTime();
    }
    
    public Task(String taskId) {
        this.taskId = taskId;
        this.comment = "";
        throwExceptionIfInvalidTaskId();
    }
    
    
    /**
     * Calculate the minutes between the start and endtime of the task.
     * @return long Minutes between start- and endtime.
     * @exception EmptyTimeFieldException The starttime or endtime was not defined.
     */
    public long getMinPerTask()  {
        throwExceptionIfStartTimeNotSet();
        throwExceptionIfEndTimeNotSet();
        long minPerTask = MINUTES.between(startTime, endTime);
        return minPerTask;
    }
    
    /**
     * Check if the task's id is a valid Redmine id or a valid LT id.
     * @return boolean Whether the task id is valid or not.
     */
    public boolean isValidTaskId() {
        return isValidRedmineTaskId() || isValidLTTaskId();
    }
    
    private boolean isValidRedmineTaskId() {
        String regex = "\\d{4}";
        boolean isValid = taskId.matches(regex);
        return isValid;
    }
    
    private boolean isValidLTTaskId() {
        String regex = "LT-\\d{4}";
        boolean isValid = taskId.matches(regex);
        return isValid;
    }
    
    private void roundEndTime() {
        if (!Util.isMultipleQuarterHour(this.startTime, this.endTime)) {
            LocalTime roundedEndTime = Util.roundToMultipleQuarterHour(
                    this.startTime, this.endTime);
            this.endTime = roundedEndTime;
        }
    }
    
    
    private void throwExceptionIfInvalidTaskId() {
        if (this.taskId == null)
            throw new NoTaskIdException();
        if (!isValidTaskId())
            throw new InvalidTaskIdException();
    }
        
    private void throwExceptionIfStartTimeNotSet() {
        if (!isStartTimeSet())
            throw new EmptyTimeFieldException();
    }
    
    private void throwExceptionIfEndTimeNotSet() {
        if (!isEndTimeSet())
            throw new EmptyTimeFieldException();
    }
    
    private void throwExceptionIfWrongTimeOrder() {
        if (this.endTime.isBefore(this.startTime))
            throw new NotExpectedTimeOrderException();
    }
    
    
    public boolean isStartTimeSet() {
        return this.startTime != null;
    }
    
    public boolean isEndTimeSet() {
        return this.endTime != null;
    }
    
    
    public void setTaskId(String taskId) {
        this.taskId = taskId;
        throwExceptionIfInvalidTaskId();
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        if (isEndTimeSet())
            roundEndTime();
        throwExceptionIfWrongTimeOrder();
    }

    public void setStartTime(int hour, int min) {
        this.startTime = LocalTime.of(hour, min);
        if (isEndTimeSet())
            roundEndTime();
        throwExceptionIfWrongTimeOrder();
    }
    
    public void setStartTime(String startTime) {
        this.startTime = LocalTime.parse(startTime);
        if (isEndTimeSet())
            roundEndTime();
        throwExceptionIfWrongTimeOrder();
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        roundEndTime();
        throwExceptionIfWrongTimeOrder();
    }

    public void setEndTime(int hour, int min) {
        this.endTime = LocalTime.of(hour, min);
        roundEndTime();
        throwExceptionIfWrongTimeOrder();
    }
    
    public void setEndTime(String endTime) {
        this.endTime = LocalTime.parse(endTime);
        roundEndTime();
        throwExceptionIfWrongTimeOrder();
    }

    @Override
    public String toString() {
        String stringForm = "Task ID: " + taskId;
        if (comment != null) {
            stringForm += ", Comment: " + comment;
        }
        if (startTime != null) {
            stringForm += ", Start time: " + startTime;
        }
        if (endTime != null) {
            stringForm += ", End time: " + endTime;
        }
        return stringForm;
    }

}
