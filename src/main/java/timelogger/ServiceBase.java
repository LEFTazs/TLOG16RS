package timelogger;

import java.time.LocalTime;

public class ServiceBase {
    
    protected ServiceBase() {
      throw new IllegalStateException("Service class");
    }
    
    protected static WorkMonth workMonthRBToWorkMonth(WorkMonthRB workmonthRB) {
        return new WorkMonth(
                workmonthRB.getYear(), 
                workmonthRB.getMonth());
    }

    protected static WorkDay workDayRBToWorkDay(WorkDayRB workdayRB) {
        return new WorkDay(
                (long)workdayRB.getRequiredHours() * 60, 
                workdayRB.getYear(), 
                workdayRB.getMonth(), 
                workdayRB.getDay());
    }

    protected static Task startTaskRBToTask(StartTaskRB startTaskRB) {
        return new Task(
                startTaskRB.getTaskId(), 
                startTaskRB.getComment(), 
                startTaskRB.getStartTime(), 
                startTaskRB.getStartTime());
    }

    protected static Task finishingTaskRBToTask(FinishingTaskRB finishingTaskRB) {
        Task task = new Task(finishingTaskRB.getTaskId());
        task.setTimes(finishingTaskRB.getStartTime(), 
                finishingTaskRB.getEndTime());
        return task;
    }

    protected static Task modifyTaskRBToTask(ModifyTaskRB modifyTaskRB) {
        Task task = new Task(modifyTaskRB.getTaskId());
        task.setTimes(modifyTaskRB.getStartTime(), modifyTaskRB.getStartTime());
        return task;
    }
    
    protected static Task modifyTaskRBToNewTask(ModifyTaskRB modifyTaskRB) {
        return new Task(modifyTaskRB.getNewTaskId(), 
                modifyTaskRB.getNewComment(), 
                modifyTaskRB.getNewStartTime(), 
                modifyTaskRB.getNewEndTime());
    }

    protected static WorkMonth findWorkMonthOrCreateNew(
            TimeLogger timeloggerToSearch, int yearToFind, int monthToFind) {
        WorkMonth foundWorkmonth = Service.findWorkMonth(
                timeloggerToSearch, yearToFind, monthToFind);
        foundWorkmonth = Service.createWorkMonthIfNull(
                foundWorkmonth, yearToFind, monthToFind, timeloggerToSearch);
        return foundWorkmonth;
    }

    protected static WorkMonth findWorkMonth(
            TimeLogger timelogger, int yearToFind, int monthToFind) {
        return timelogger.getMonths().stream()
                .filter(workMonth -> 
                        Service.isWorkMonthDateSame(
                                workMonth, yearToFind, monthToFind))
                .findFirst()
                .orElse(null);
    }

    protected static WorkMonth createWorkMonthIfNull(
            WorkMonth workmonthToCheck, int year, int month, 
            TimeLogger timelogger) {
        if (workmonthToCheck == null) {
            workmonthToCheck = new WorkMonth(year, month);
            timelogger.addMonth(workmonthToCheck);
        }
        return workmonthToCheck;
    }

    protected static WorkDay findWorkDayOrCreateNew(
            WorkMonth workmonthToSearch, int dayToFind) {
        WorkDay foundWorkday = Service.findWorkDay(
                workmonthToSearch, dayToFind);
        int workmonthYear = workmonthToSearch.getDate().getYear();
        int workmonthMonth = workmonthToSearch.getDate().getMonthValue();
        foundWorkday = Service.createWorkDayIfNull(
                foundWorkday, workmonthYear, workmonthMonth, dayToFind, 
                workmonthToSearch);
        return foundWorkday;
    }

    protected static WorkDay findWorkDay(
            WorkMonth workmonthToSearch, int dayToFind) {
        return workmonthToSearch.getDays().stream()
                .filter(workday -> 
                        workday.getActualDay().getDayOfMonth() == dayToFind)
                .findFirst()
                .orElse(null);
    }

    protected static WorkDay createWorkDayIfNull(
            WorkDay workdayToCheck, int year, int month, int day, 
            WorkMonth workmonth) {
        if (workdayToCheck == null) {
            workdayToCheck = new WorkDay(year, month, day);
            workmonth.addWorkDay(workdayToCheck);
        }
        return workdayToCheck;
    }

    protected static Task findTaskOrCreateNew(
            WorkDay workdayToSearch, Task taskInfo) {
        Task foundTask = Service.findTask(
                workdayToSearch, taskInfo.getStartTime(), taskInfo.getTaskId());
        foundTask = Service.addTaskIfNull(
                foundTask, taskInfo, workdayToSearch);
        return foundTask;
    }

    protected static Task findTask(
            WorkDay workdayToSearch, LocalTime startTime, String taskId) {
        return workdayToSearch.getTasks().stream()
                .filter(task -> 
                        task.getStartTime().equals(startTime) && 
                                task.getTaskId().equals(taskId))
                .findFirst()
                .orElse(null);
    }

    protected static Task addTaskIfNull(
            Task taskToCheck, Task taskToAddIfNull, WorkDay workday) {
        if (taskToCheck == null) {
            workday.addTask(taskToAddIfNull);
            return taskToAddIfNull;
        }
        return taskToCheck;
    }

    protected static boolean isWorkMonthDateSame(
            WorkMonth workmonthToCheck, int year, int month) {
        int workMonthYear = workmonthToCheck.getDate().getYear();
        int workMonthMonth = workmonthToCheck.getDate().getMonthValue();
        return year == workMonthYear && month == workMonthMonth;
    }
}
