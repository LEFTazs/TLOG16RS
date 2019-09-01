package timelogger.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import timelogger.Util;
import timelogger.exceptions.*;

/**
 * Tracks tasks. Contains statistics of these tasks.
 */
@Entity
@lombok.Getter
public class WorkDay {
    @lombok.Setter @Id private int id;
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<Task> tasks;
    private long requiredMinPerDay;
    private LocalDate actualDay;
    private long sumPerDay;
    
    public WorkDay(long requiredMinPerDay, LocalDate actualDay) {
        if (requiredMinPerDay < 0)
            throw new NegativeMinutesOfWorkException();
        
        this.requiredMinPerDay = requiredMinPerDay;
        this.actualDay = actualDay;
        this.tasks = new ArrayList<>();
        
        if (this.actualDay.isAfter(LocalDate.now()))
            throw new FutureWorkException();
    }
    
    public WorkDay(long requiredMinPerDay,
                    int actualYear, int actualMonth, int actualDay) {
        this(requiredMinPerDay, 
                LocalDate.of(actualYear, actualMonth, actualDay));
    }
    
    public WorkDay(long requiredMinPerDay) {
        this(requiredMinPerDay, LocalDate.now());
    }
    
    public WorkDay(int actualYear, int actualMonth, int actualDay) {
        this(450, actualYear, actualMonth, actualDay);
    }
    
    public WorkDay() {
        this(450, LocalDate.now());
    }
    

    
    /**
     * How many extra minutes are there for this day.
     * @return long Extra minutes for this day.
     */
    public long getExtraMinPerDay() {
        updateSumPerDay();
        return sumPerDay - requiredMinPerDay;
    }
    
    /**
     * Add a task to this days's tasks.
     * @param t Task to be added.
     * @exception NotSeparatedTimesException The given task's time interval 
     * is not seperated from the day's tasks.
     */
    public void addTask(Task t) {
        if (Util.isSeperatedTime(tasks, t)) {
            tasks.add(t);
        } else {
            throw new NotSeparatedTimesException();
        }
    }
    
    /**
     * Find the latest task based on it's endtime.
     * @return LocalTime The latest task's endtime. 
     * If there are no tasks, null is returned.
     */
    protected LocalTime getLatestTaskEndTime() {
        Task latestTask = tasks.stream()
                .filter(Task::isEndTimeSet)
                .max(Comparator.comparing(
                        task -> localTimeToLong(task.getEndTime()))
                )
                .orElse(null);
        if (latestTask == null) {
            return null;
        } else {
            return latestTask.getEndTime();
        }
    }
    
    private long localTimeToLong(LocalTime localTime) {
        long hours = (long)localTime.getHour();
        long mins = (long)localTime.getMinute();
        long seconds = (long)localTime.getSecond();
        
        return hours * 60 * 60 + mins * 60 + seconds;
    } 
    
    public boolean hasTask() {
        return !tasks.isEmpty();
    }
    
    
    public Task getTask(int index) {
        return tasks.get(index);
    }
    
    public Task setTask(int index, Task newTask) {
        Task modifiedTask = tasks.set(index, newTask);
        return modifiedTask;
    }
    
    public void deleteTask(int index) {
        tasks.remove(index);
    }
    
    public void deleteTask(Task task) {
        tasks.remove(task);
    }
    
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }
    
    protected Task getUnfinishedTask(int index) {
        return getUnfinishedTasks().get(index);
    }
    
    protected List<Task> getUnfinishedTasks() {
        return tasks.stream()
                .filter(task -> !task.isEndTimeSet())
                .collect(Collectors.toList()
                );
    }
    
    public void setRequiredMinPerDay(long requiredMinPerDay) {
        if (requiredMinPerDay < 0)
            throw new NegativeMinutesOfWorkException();
        
        this.requiredMinPerDay = requiredMinPerDay;
    }
    
    public void setActualDay(int year, int month, int day) {        
        this.actualDay = LocalDate.of(year, month, day);
        
        if (this.actualDay.isAfter(LocalDate.now()))
            throw new FutureWorkException();
    }

    /**
     * Update the sumPerDay field and return it.
     * Sum per day is calculated by summing this workday's task's minPerTask.
     * @return long Sum per day.
     */
    public long getSumPerDay() {
        updateSumPerDay();
        return sumPerDay;
    }
    
    private void updateSumPerDay() {
        sumPerDay = tasks.stream()
                .mapToLong(Task::getMinPerTask)
                .sum();
    }
    
}
