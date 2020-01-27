package timelogger;

import com.avaje.ebean.Ebean;
import timelogger.entities.WorkDay;
import timelogger.entities.WorkMonth;
import timelogger.entities.Task;
import timelogger.entities.TimeLogger;
import java.time.LocalTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public class Service extends ServiceBase {
    
    public static ResponseEntity<Object> listWorkmonths(TimeLogger timelogger) {
        List<WorkMonth> workMonthList = timelogger.getMonths();
        return ResponseCreator.createOkResponse(workMonthList);
    }
    
    
    public static ResponseEntity<Object> listWorkmonthDays(
            TimeLogger timelogger, int year, int month) {
        try {
            List<WorkDay> workMonthDays = 
                    Service.tryListWorkmonthDays(timelogger, year, month);
            return ResponseCreator.createOkResponse(workMonthDays);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseCreator.createExceptionResponse(e);
        }
    }
    
    public static List<WorkDay> tryListWorkmonthDays(
            TimeLogger timelogger, int year, int month) {
        WorkMonth foundWorkMonth = Service.findWorkMonthOrCreateNew(
                timelogger, year, month);
        return foundWorkMonth.getDays();
    }
    
    
    public static ResponseEntity<Object> listWorkDayTasks(
            TimeLogger timelogger, int year,  int month, int day) {
        try {
            List<Task> workDayTasks =
                    Service.tryListWorkDayTasks(timelogger, year, month, day);
            return ResponseCreator.createOkResponse(workDayTasks);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseCreator.createExceptionResponse(e);
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
    
    
    public static ResponseEntity<Object> addWorkMonth(
            TimeLogger timelogger, WorkMonthRB newWorkmonth) {
        try {
            WorkMonth addedMonth = 
                    Service.tryAddWorkMonth(timelogger, newWorkmonth);
            return ResponseCreator.createOkResponse(addedMonth);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseCreator.createExceptionResponse(e);
        }
    }
    
    public static WorkMonth tryAddWorkMonth(
            TimeLogger timelogger, WorkMonthRB newWorkmonth) {
        WorkMonth workmonth = Service.workMonthRBToWorkMonth(newWorkmonth);

        timelogger.addMonth(workmonth);

        return workmonth;
    }
    
    
    public static ResponseEntity<Object> addWorkDay(
            TimeLogger timelogger, WorkDayRB newWorkday) {
        try {
            WorkDay addedWorkDay  = 
                    Service.tryAddWorkDay(timelogger, newWorkday);
            return ResponseCreator.createOkResponse(addedWorkDay);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseCreator.createExceptionResponse(e);
        }
    }
    
    public static WorkDay tryAddWorkDay(
            TimeLogger timelogger, WorkDayRB newWorkday) {
        WorkDay workday = Service.workDayRBToWorkDay(newWorkday);

        int yearToAddTo = newWorkday.getYear();
        int monthToAddTo = newWorkday.getMonth();
        WorkMonth workmonthToAddTo = Service.findWorkMonthOrCreateNew(
                timelogger, yearToAddTo, monthToAddTo);

        workmonthToAddTo.addWorkDay(workday);

        return workday;
    }
    
    
    public static ResponseEntity<Object> addTaskStart(
            TimeLogger timelogger, StartTaskRB newStartTask) {
        try {
            Task addedTask = 
                    Service.tryAddTaskStart(timelogger, newStartTask);
            return ResponseCreator.createOkResponse(addedTask);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseCreator.createExceptionResponse(e);
        }
    }
    
    public static Task tryAddTaskStart(
            TimeLogger timelogger, StartTaskRB newStartTask) {
        Task task = Service.startTaskRBToTask(newStartTask);

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
    
    
    public static ResponseEntity<Object> addTaskFinish(
            TimeLogger timelogger, FinishingTaskRB finishingTask) {
        try {
            Task addedTask = 
                    Service.tryAddTaskFinish(timelogger, finishingTask);
            return ResponseCreator.createOkResponse(addedTask);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseCreator.createExceptionResponse(e);
        }
    }
    
    public static Task tryAddTaskFinish(
            TimeLogger timelogger, FinishingTaskRB finishingTask) {
        Task taskInfo = Service.finishingTaskRBToTask(finishingTask);

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
    
    
    public static ResponseEntity<Object> modifyTask(
            TimeLogger timelogger,  ModifyTaskRB modifyTask) {
        try {
            Task modifiedTask = 
                    Service.tryModifyTask(timelogger, modifyTask);
            return ResponseCreator.createOkResponse(modifiedTask);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseCreator.createExceptionResponse(e);
        }
    }
    
    public static Task tryModifyTask(
            TimeLogger timelogger,  ModifyTaskRB modifyTask) {
        Task taskInfo = Service.modifyTaskRBToTask(modifyTask);
        Task newTask = Service.modifyTaskRBToNewTask(modifyTask);

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

        Ebean.delete(taskToDelete);
    }
    
    
    public static void deleteAll(TimeLogger timelogger) {
        timelogger.deleteMonths();
    }
}
