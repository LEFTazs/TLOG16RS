package timelogger;

public class Service {
    public static WorkMonth WorkMonthRBToWorkMonth(WorkMonthRB workmonthRB) {
        WorkMonth workmonth = new WorkMonth(
                        workmonthRB.getYear(), 
                        workmonthRB.getMonth());
        return workmonth;
    }
    
    public static WorkDay WorkDayRBToWorkDay(WorkDayRB workdayRB) {
        WorkDay workday = new WorkDay(workdayRB.getRequiredHours() * 60, 
                        workdayRB.getYear(), 
                        workdayRB.getMonth(), 
                        workdayRB.getDay());
        return workday;
    }
    
    public static Task StartTaskRBToTask(StartTaskRB startTaskRB) {
        Task task = new Task(startTaskRB.getTaskId(),
                        startTaskRB.getComment(),
                        startTaskRB.getStartTime(),
                        startTaskRB.getStartTime());
        
        return task;
    }
    
    public static Task FinishingTaskRBToTask(FinishingTaskRB finishingTaskRB) {
        Task task = new Task(finishingTaskRB.getTaskId());
        task.setStartTime(finishingTaskRB.getStartTime());
        task.setEndTime(finishingTaskRB.getEndTime());
        
        return task;
    }
    
    public static Task ModifyTaskRBToTask(ModifyTaskRB modifyTaskRB) {
        Task task = new Task(modifyTaskRB.getTaskId());
        task.setStartTime(modifyTaskRB.getStartTime());
        task.setEndTime(modifyTaskRB.getStartTime());
        
        return task;
    }
    
    
    
    public static void addWorkMonthToTimeLogger(
            TimeLogger timelogger, WorkMonth workmonth) {
        timelogger.addMonth(workmonth);
    }
    
    public static void addWorkDayToTimeLogger(
            TimeLogger timelogger, WorkDay workday) {
        int yearToAddTo = workday.getActualDay().getYear();
        int monthToAddTo = workday.getActualDay().getMonthValue();
        WorkMonth foundWorkmonth = Service.getSpecifiedWorkMonthFromTimeLogger(
                timelogger, yearToAddTo, monthToAddTo);
        
        foundWorkmonth = Service.createWorkMonthInTimeLoggerIfNull(
                foundWorkmonth, yearToAddTo, monthToAddTo, timelogger);
        
        foundWorkmonth.addWorkDay(workday);
    }
    
    public static WorkMonth getSpecifiedWorkMonthFromTimeLogger(
            TimeLogger timelogger, int year, int month) {
        WorkMonth foundWorkMonth = timelogger.getMonths().stream()
                        .filter(workMonth -> Service.isWorkMonthDateSame(
                                workMonth, year, month)
                        )
                        .findFirst()
                        .orElse(null);
        return foundWorkMonth;
    }
    
    public static boolean isWorkMonthDateSame(
            WorkMonth monthToCheck, int year, int month) {
        int workMonthYear = monthToCheck.getDate().getYear();
        int workMonthMonth = monthToCheck.getDate().getMonthValue();
        
        return year == workMonthYear && month == workMonthMonth;
    }
    
    public static WorkMonth createWorkMonthInTimeLoggerIfNull(
            WorkMonth workmonthToCheck, int year, int month, 
            TimeLogger timelogger) {
        if (workmonthToCheck == null) {
            workmonthToCheck = new WorkMonth(year, month);
            timelogger.addMonth(workmonthToCheck);
        }
        return workmonthToCheck;
    }
    
    public static void addTaskToTimeLogger(
            TimeLogger timelogger, Task task, 
            int year, int month, int day) {
        WorkMonth foundWorkmonth = Service.getSpecifiedWorkMonthFromTimeLogger(
                timelogger, year, month);
        
        foundWorkmonth = Service.createWorkMonthInTimeLoggerIfNull(
                foundWorkmonth, year, month, timelogger);
        
        WorkDay foundWorkDay = 
                Service.getSpecifiedWorkDayFromWorkMonth(foundWorkmonth, day);
        
        foundWorkDay = Service.createWorkDayInWorkMonthIfNull(
                foundWorkDay, year, month, day, foundWorkmonth);
                
        foundWorkDay.addTask(task);
    }
    
    public static WorkDay getSpecifiedWorkDayFromWorkMonth(
            WorkMonth workmonthToSearch, int day) {
        WorkDay foundWorkDay = workmonthToSearch.getDays().stream()
                        .filter(workday -> 
                                workday.getActualDay().getDayOfMonth() == day
                        )
                        .findFirst()
                        .orElse(null);
        return foundWorkDay;
    }
    
    public static WorkDay createWorkDayInWorkMonthIfNull(
            WorkDay workdayToCheck, int year, int month, int day,
            WorkMonth workmonth) {
        if (workdayToCheck == null) {
            workdayToCheck = new WorkDay(year, month, day);
            workmonth.addWorkDay(workdayToCheck);
        }
        return workdayToCheck;
    }
}
