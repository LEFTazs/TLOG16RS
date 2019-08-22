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
            return Service.listWorkmonths(timelogger);
	}
        
        @GetMapping("/timelogger/workmonths/{year}/{month}")
	public WorkMonth listWorkmonth(
                @PathVariable int year, @PathVariable int month) {
            return Service.listWorkmonth(timelogger, year, month);
	}
        
        @GetMapping("/timelogger/workmonths/{year}/{month}/{day}")
	public WorkDay listWorkDay(
                @PathVariable int year, @PathVariable int month, 
                @PathVariable int day) {
            return Service.listWorkDay(timelogger, year, month, day);
	}
	
	@PostMapping("/timelogger/workmonths")
	public WorkMonth addWorkMonth(@RequestBody WorkMonthRB newWorkmonth) {
            return Service.addWorkMonth(timelogger, newWorkmonth);
	}
        
        @PostMapping("/timelogger/workmonths/workdays")
	public WorkDay addWorkDay(@RequestBody WorkDayRB newWorkday) {
	    return Service.addWorkDay(timelogger, newWorkday);
	}
        
        @PostMapping("/timelogger/workmonths/workdays/tasks/start")
	public Task addTaskStart(@RequestBody StartTaskRB newStartTask) {
	    return Service.addTaskStart(timelogger, newStartTask);
	}
        
        @PutMapping("/timelogger/workmonths/workdays/tasks/finish")
        public Task addTaskFinish(@RequestBody FinishingTaskRB finishingTask) {
            return Service.addTaskFinish(timelogger, finishingTask);
        }
        
        @PutMapping("/timelogger/workmonths/workdays/tasks/modify")
        public Task modifyTask(@RequestBody ModifyTaskRB modifyTask) {
            return Service.modifyTask(timelogger, modifyTask);
        }
        
        @DeleteMapping("/timelogger/workmonths/workdays/tasks/delete")
        public void deleteTask(@RequestBody DeleteTaskRB deleteTask) {
            Service.deleteTask(timelogger, deleteTask);
        }
}