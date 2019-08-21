package timelogger;

import java.time.LocalTime;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TLOG16RSController {
	TimeLogger timelogger = new TimeLogger();

	@GetMapping("/timelogger/workmonths")
	public List<WorkMonth> listWorkmonths() {
		return timelogger.getMonths();
	}
        
        @GetMapping("/timelogger/workmonths/{year}/{month}")
	public WorkMonth listWorkmonth(
                @PathVariable int year, @PathVariable int month) {
                WorkMonth workmonthToList = Service.findWorkMonthOrCreateNew(
                        timelogger, year, month);
		return workmonthToList;
	}
        
        @GetMapping("/timelogger/workmonths/{year}/{month}/{day}")
	public WorkDay listWorkDay(
                @PathVariable int year, @PathVariable int month, 
                @PathVariable int day) {
                WorkMonth foundWorkmonth = Service.findWorkMonthOrCreateNew(
                        timelogger, year, month);

                WorkDay foundWorkDay = Service.findWorkDayOrCreateNew(
                        foundWorkmonth, day);
                
		return foundWorkDay;
	}
	
	@PostMapping("/timelogger/workmonths")
	public WorkMonth addWorkMonth(@RequestBody WorkMonthRB newWorkmonth) {
		WorkMonth workmonth = Service.WorkMonthRBToWorkMonth(newWorkmonth);
                
		timelogger.addMonth(workmonth);
                
		return workmonth;
	}
        
        @PostMapping("/timelogger/workmonths/workdays")
	public WorkDay addWorkDay(@RequestBody WorkDayRB newWorkday) {
		WorkDay workday = Service.WorkDayRBToWorkDay(newWorkday);
		
                int yearToAddTo = newWorkday.getYear();
                int monthToAddTo = newWorkday.getMonth();
                WorkMonth workmonthToAddTo = Service.findWorkMonthOrCreateNew(
                        timelogger, yearToAddTo, monthToAddTo);
                
                workmonthToAddTo.addWorkDay(workday);
                
		return workday;
	}
        
        @PostMapping("/timelogger/workmonths/workdays/tasks/start")
	public Task addTaskStart(@RequestBody StartTaskRB newStartTask) {
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
        
        @PutMapping("/timelogger/workmonths/workdays/tasks/finish")
        public Task addTaskFinish(@RequestBody FinishingTaskRB finishingTask) {
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
        
        @PutMapping("/timelogger/workmonths/workdays/tasks/modify")
        public Task modifyTask(@RequestBody ModifyTaskRB modifyTask) {
            Task taskInfo = Service.ModifyTaskRBToTask(modifyTask);
            
            int yearToAddTo = modifyTask.getYear();
            int monthToAddTo = modifyTask.getMonth();
            int dayToAddTo = modifyTask.getDay();
            WorkMonth workmonthToSearch = Service.findWorkMonthOrCreateNew(
                    timelogger, yearToAddTo, monthToAddTo);
            WorkDay workdayToSearch = Service.findWorkDayOrCreateNew(
                    workmonthToSearch, dayToAddTo);
            Task taskToChange = Service.findTaskOrCreateNew(
                    workdayToSearch, taskInfo);
            
            taskToChange.setTaskId(modifyTask.getNewTaskId());
            taskToChange.setComment(modifyTask.getNewComment());
            taskToChange.setTimes(modifyTask.getNewStartTime(), 
                    modifyTask.getNewEndTime());
            
            return taskToChange;
        }
        
        @DeleteMapping("/timelogger/workmonths/workdays/tasks/delete")
        public void deleteTask(@RequestBody DeleteTaskRB deleteTask) {
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
}