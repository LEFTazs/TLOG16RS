package timelogger;

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
                WorkMonth workmonthToList = 
                        Service.getSpecifiedWorkMonthFromTimeLogger(
                                timelogger, year, month);
                workmonthToList = Service.createWorkMonthInTimeLoggerIfNull(
                        workmonthToList, year, month, timelogger);
		return workmonthToList;
	}
        
        @GetMapping("/timelogger/workmonths/{year}/{month}/{day}")
	public WorkDay listWorkDay(
                @PathVariable int year, @PathVariable int month, 
                @PathVariable int day) {
                WorkMonth foundWorkmonth = 
                        Service.getSpecifiedWorkMonthFromTimeLogger(
                                timelogger, year, month);
        
                foundWorkmonth = Service.createWorkMonthInTimeLoggerIfNull(
                        foundWorkmonth, year, month, timelogger);

                WorkDay foundWorkDay = 
                        Service.getSpecifiedWorkDayFromWorkMonth(foundWorkmonth, day);

                foundWorkDay = Service.createWorkDayInWorkMonthIfNull(
                        foundWorkDay, year, month, day, foundWorkmonth);
                
		return foundWorkDay;
	}
	
	@PostMapping("/timelogger/workmonths")
	public WorkMonth addWorkMonth(@RequestBody WorkMonthRB newWorkmonth) {
		WorkMonth workmonth = Service.WorkMonthRBToWorkMonth(newWorkmonth);
                
		Service.addWorkMonthToTimeLogger(timelogger, workmonth);
                
		return workmonth;
	}
        
        @PostMapping("/timelogger/workmonths/workdays")
	public WorkDay addWorkDay(@RequestBody WorkDayRB newWorkday) {
		WorkDay workday = Service.WorkDayRBToWorkDay(newWorkday);
		
                Service.addWorkDayToTimeLogger(timelogger, workday);
                
		return workday;
	}
        
        @PostMapping("/timelogger/workmonths/workdays/tasks/start")
	public Task addTaskStart(@RequestBody StartTaskRB newStartTask) {
		Task task = Service.StartTaskRBToTask(newStartTask);
		
                Service.addTaskToTimeLogger(
                        timelogger, task, 
                        newStartTask.getYear(), 
                        newStartTask.getMonth(), 
                        newStartTask.getDay()
                );
                
		return task;
	}
        
        @PutMapping("/timelogger/workmonths/workdays/tasks/finish")
        public Task addTaskFinish(@RequestBody FinishingTaskRB finishingTask) {
            Task task = Service.FinishingTaskRBToTask(finishingTask);
            
            //TODO: finish method
            
            return task;
        }
        
        @PutMapping("/timelogger/workmonths/workdays/tasks/modify")
        public Task modifyTask(@RequestBody ModifyTaskRB taskToModify) {
            Task task = Service.ModifyTaskRBToTask(taskToModify);
            
            //TODO: finish method
            
            return task;
        }
        
        @DeleteMapping("/timelogger/workmonths/workdays/tasks/delete")
        public void deleteTask(@RequestBody DeleteTaskRB taskToDelete) {
            //TODO: finish method
        }
}