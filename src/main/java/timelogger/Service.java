package timelogger;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Service extends ServiceBase {
    
    public static List<WorkMonth> listWorkmonths(TimeLogger timelogger) {
        return timelogger.getMonths();
    }
    
    
    public static List<WorkDay> listWorkmonthDays(
            TimeLogger timelogger, int year, int month) {
        try {
            return Service.tryListWorkmonthDays(timelogger, year, month);
        } catch (Exception e) {
            log.error(e.toString());
            return new ArrayList<>();
        }
    }
    
    public static List<WorkDay> tryListWorkmonthDays(
            TimeLogger timelogger, int year, int month) {
        WorkMonth foundWorkMonth = Service.findWorkMonthOrCreateNew(
                timelogger, year, month);
        return foundWorkMonth.getDays();
    }
    
    
    public static List<Task> listWorkDayTasks(
            TimeLogger timelogger, int year,  int month, int day) {
        try {
            return Service.tryListWorkDayTasks(timelogger, year, month, day);
        } catch (Exception e) {
            log.error(e.toString());
            return new ArrayList<>();
        }
    }
    
    public static List<Task> tryListWorkDayTasks(
            TimeLogger timelogger, int year,  int month, int day) {
        WorkMonth foundWorkmonth = Service.findWorkMonthOrCreateNew(
                timelogger, year, month);

        WorkDay foundWorkDay = Service.findWorkDayOrCreateNew(
                foundWorkmonth, day);

        return foundWorkDay.getTasks();
    }
    
    
    public static WorkMonth addWorkMonth(
            TimeLogger timelogger, WorkMonthRB newWorkmonth) {
        try {
            return Service.tryAddWorkMonth(timelogger, newWorkmonth);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }
    
    public static WorkMonth tryAddWorkMonth(
            TimeLogger timelogger, WorkMonthRB newWorkmonth) {
        WorkMonth workmonth = Service.WorkMonthRBToWorkMonth(newWorkmonth);

        timelogger.addMonth(workmonth);

        return workmonth;
    }
    
    
    public static WorkDay addWorkDay(
            TimeLogger timelogger, WorkDayRB newWorkday) {
        try {
            return Service.tryAddWorkDay(timelogger, newWorkday);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }
    
    public static WorkDay tryAddWorkDay(
            TimeLogger timelogger, WorkDayRB newWorkday) {
        WorkDay workday = Service.WorkDayRBToWorkDay(newWorkday);

        int yearToAddTo = newWorkday.getYear();
        int monthToAddTo = newWorkday.getMonth();
        WorkMonth workmonthToAddTo = Service.findWorkMonthOrCreateNew(
                timelogger, yearToAddTo, monthToAddTo);

        workmonthToAddTo.addWorkDay(workday);

        return workday;
    }
    
    
    public static Task addTaskStart(
            TimeLogger timelogger, StartTaskRB newStartTask) {
        try {
            return Service.tryAddTaskStart(timelogger, newStartTask);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }
    
    public static Task tryAddTaskStart(
            TimeLogger timelogger, StartTaskRB newStartTask) {
        Task task = Service.StartTaskRBToTask(newStartTask);

        int yearToAddTo = newStartTask.getYear();
        int monthToAddTo = newStartTask.getMonth();
        int dayToAddTo = newStartTask.getDay();
        WorkMonth workmonthToAddTo = Service.findWorkMonthOrCreateNew(
                timelogger, yearToAddTo, monthToAddTo);
        WorkDay workdayToAddTo = Service.findWorkDayOrCreateNew(
                workmonthToAddTo, dayToAddTo);

        workdayToAddTo.addTask(task);

        return task;
    }
    
    
    public static Task addTaskFinish(
            TimeLogger timelogger, FinishingTaskRB finishingTask) {
        try {
            return Service.tryAddTaskFinish(timelogger, finishingTask);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }
    
    public static Task tryAddTaskFinish(
            TimeLogger timelogger, FinishingTaskRB finishingTask) {
        Task taskInfo = Service.FinishingTaskRBToTask(finishingTask);

        int yearToAddTo = finishingTask.getYear();
        int monthToAddTo = finishingTask.getMonth();
        int dayToAddTo = finishingTask.getDay();
        WorkMonth workmonthToSearch = Service.findWorkMonthOrCreateNew(
                timelogger, yearToAddTo, monthToAddTo);
        WorkDay workdayToSearch = Service.findWorkDayOrCreateNew(
                workmonthToSearch, dayToAddTo);
        Task taskToChange = Service.findTaskOrCreateNew(
                workdayToSearch, taskInfo);

        taskToChange.setEndTime(finishingTask.getEndTime());

        return taskToChange;
    }
    
    
    public static Task modifyTask(
            TimeLogger timelogger,  ModifyTaskRB modifyTask) {
        try {
            return Service.tryModifyTask(timelogger, modifyTask);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }
    
    public static Task tryModifyTask(
            TimeLogger timelogger,  ModifyTaskRB modifyTask) {
        Task taskInfo = Service.ModifyTaskRBToTask(modifyTask);
        Task newTask = Service.ModifyTaskRBToNewTask(modifyTask);

        int yearToAddTo = modifyTask.getYear();
        int monthToAddTo = modifyTask.getMonth();
        int dayToAddTo = modifyTask.getDay();
        WorkMonth workmonthToSearch = Service.findWorkMonthOrCreateNew(
                timelogger, yearToAddTo, monthToAddTo);
        WorkDay workdayToSearch = Service.findWorkDayOrCreateNew(
                workmonthToSearch, dayToAddTo);
        Task taskToChange = Service.findTaskOrCreateNew(
                workdayToSearch, taskInfo);

        taskToChange.setTaskId(newTask.getTaskId());
        taskToChange.setComment(newTask.getComment());
        taskToChange.setTimes(newTask.getStartTime(), 
                newTask.getEndTime());

        return taskToChange;
    }
    
    
    public static void deleteTask(
            TimeLogger timelogger, DeleteTaskRB deleteTask) {
        try {
            Service.tryDeleteTask(timelogger, deleteTask);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }
    
    public static void tryDeleteTask(
            TimeLogger timelogger, DeleteTaskRB deleteTask) {
        int yearToSearch = deleteTask.getYear();
        int monthToSearch = deleteTask.getMonth();
        int dayToSearch = deleteTask.getDay();
        LocalTime startTimeToSearch = LocalTime.parse(deleteTask.getStartTime());
        String taskIdToSearch = deleteTask.getTaskId();
        WorkMonth workmonthToSearch = Service.findWorkMonth(
                timelogger, yearToSearch, monthToSearch);
        if (workmonthToSearch == null)
            return;
        WorkDay workdayToSearch = Service.findWorkDay(
                workmonthToSearch, dayToSearch);
        if (workdayToSearch == null)
            return;
        Task taskToDelete = Service.findTask(
                workdayToSearch, startTimeToSearch, taskIdToSearch);
        if (taskToDelete == null)
            return;

        workdayToSearch.deleteTask(taskToDelete);
    }
    
    
    public static void deleteAll(TimeLogger timelogger) {
        timelogger.deleteMonths();
    }
}
