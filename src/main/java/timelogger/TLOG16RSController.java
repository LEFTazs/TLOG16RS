package timelogger;

import timelogger.entities.*;
import com.avaje.ebean.Ebean;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TLOG16RSController {
	TimeLogger timelogger;
        
        public TLOG16RSController() {
            timelogger = new TimeLogger("Szolar Balazs");
            
            List<TimeLogger> timeloggerEntries = 
                    Ebean.find(TimeLogger.class).findList();
            if (timeloggerEntries.isEmpty())
                saveTimeLoggerToDatabase();
            else
                loadTimeLoggerFromDatabase();
        }
        
        
	@GetMapping("/timelogger/workmonths")
	public List<WorkMonth> listWorkmonths() {
            loadTimeLoggerFromDatabase();
            List<WorkMonth> workmonths = Service.listWorkmonths(timelogger);
            updateTimeLoggerInDatabase();
            return workmonths;
	}
        
        @GetMapping("/timelogger/workmonths/{year}/{month}")
	public List<WorkDay> listWorkmonthDays(
                @PathVariable int year, @PathVariable int month) {
            loadTimeLoggerFromDatabase();
            List<WorkDay> workdays = Service.listWorkmonthDays(timelogger, year, month);
            updateTimeLoggerInDatabase();
            return workdays;
	}
        
        @GetMapping("/timelogger/workmonths/{year}/{month}/{day}")
	public List<Task> listWorkDayTasks(
                @PathVariable int year, @PathVariable int month, 
                @PathVariable int day) {
            loadTimeLoggerFromDatabase();
            List<Task> tasks = Service.listWorkDayTasks(timelogger, year, month, day);
            updateTimeLoggerInDatabase();
            return tasks;
	}
	
	@PostMapping("/timelogger/workmonths")
	public WorkMonth addWorkMonth(@RequestBody WorkMonthRB newWorkmonth) {
            WorkMonth workmonth = Service.addWorkMonth(timelogger, newWorkmonth);
            updateTimeLoggerInDatabase();
            return workmonth;
	}
        
        @PostMapping("/timelogger/workmonths/workdays")
	public WorkDay addWorkDay(@RequestBody WorkDayRB newWorkday) {
	    WorkDay workday = Service.addWorkDay(timelogger, newWorkday);
            updateTimeLoggerInDatabase();
            return workday;
	}
        
        @PostMapping("/timelogger/workmonths/workdays/tasks/start")
	public Task addTaskStart(@RequestBody StartTaskRB newStartTask) {
	    Task task = Service.addTaskStart(timelogger, newStartTask);
            updateTimeLoggerInDatabase();
            return task;
	}
        
        @PutMapping("/timelogger/workmonths/workdays/tasks/finish")
        public Task addTaskFinish(@RequestBody FinishingTaskRB finishingTask) {
            Task task = Service.addTaskFinish(timelogger, finishingTask);
            updateTimeLoggerInDatabase();
            return task;
        }
        
        @PutMapping("/timelogger/workmonths/workdays/tasks/modify")
        public Task modifyTask(@RequestBody ModifyTaskRB modifyTask) {
            Task task = Service.modifyTask(timelogger, modifyTask);
            updateTimeLoggerInDatabase();
            return task;
        }
        
        @DeleteMapping("/timelogger/workmonths/workdays/tasks/delete")
        public void deleteTask(@RequestBody DeleteTaskRB deleteTask) {
            Service.deleteTask(timelogger, deleteTask);
            updateTimeLoggerInDatabase();
        }
        
        @DeleteMapping("/timelogger/workmonths/deleteall")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deleteAll() {
            Service.deleteAll(timelogger);
            updateTimeLoggerInDatabase();
        }
        
        
        
        private TimeLogger saveTimeLoggerToDatabase() {
            Ebean.save(timelogger);
            return timelogger;
        }
        
        private TimeLogger updateTimeLoggerInDatabase() {
            Ebean.update(timelogger);
            return timelogger;
        }
        
        private TimeLogger loadTimeLoggerFromDatabase() {
           List<TimeLogger> databaseEntries = 
                   Ebean.find(TimeLogger.class).findList();
           TimeLogger latestEntry = 
                   databaseEntries.get(databaseEntries.size() - 1);
           this.timelogger = latestEntry;
           this.timelogger.getMonths().stream().forEach(WorkMonth::setDateFromDateString);
           return this.timelogger;
        }
        
}