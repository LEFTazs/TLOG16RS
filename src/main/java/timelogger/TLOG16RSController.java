package timelogger;

import timelogger.entities.*;
import com.avaje.ebean.Ebean;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<Object> listWorkmonths() {
            loadTimeLoggerFromDatabase();
            ResponseEntity<Object> response = 
                    Service.listWorkmonths(timelogger);
            updateTimeLoggerInDatabase();
            return response;
	}
        
        @GetMapping("/timelogger/workmonths/{year}/{month}")
	public ResponseEntity<Object> listWorkmonthDays(
                @PathVariable int year, @PathVariable int month) {
            loadTimeLoggerFromDatabase();
            ResponseEntity<Object> response = 
                    Service.listWorkmonthDays(timelogger, year, month);
            updateTimeLoggerInDatabase();
            return response;
	}
        
        @GetMapping("/timelogger/workmonths/{year}/{month}/{day}")
	public ResponseEntity<Object> listWorkDayTasks(
                @PathVariable int year, @PathVariable int month, 
                @PathVariable int day) {
            loadTimeLoggerFromDatabase();
            ResponseEntity<Object> response = 
                    Service.listWorkDayTasks(timelogger, year, month, day);
            updateTimeLoggerInDatabase();
            return response;
	}
	
	@PostMapping("/timelogger/workmonths")
	public ResponseEntity<Object> addWorkMonth(@RequestBody WorkMonthRB newWorkmonth) {
            ResponseEntity<Object> response = 
                    Service.addWorkMonth(timelogger, newWorkmonth);
            updateTimeLoggerInDatabase();
            return response;
	}
        
        @PostMapping("/timelogger/workmonths/workdays")
	public ResponseEntity<Object> addWorkDay(@RequestBody WorkDayRB newWorkday) {
	    ResponseEntity<Object> response = 
                    Service.addWorkDay(timelogger, newWorkday);
            updateTimeLoggerInDatabase();
            return response;
	}
        
        @PostMapping("/timelogger/workmonths/weekendworkdays")
	public ResponseEntity<Object> addWeekendWorkDay(@RequestBody WorkDayRB newWorkday) {
	    ResponseEntity<Object> response = 
                    Service.addWeekendWorkDay(timelogger, newWorkday);
            updateTimeLoggerInDatabase();
            return response;
	}
        
        @PostMapping("/timelogger/workmonths/workdays/tasks/start")
	public ResponseEntity<Object> addTaskStart(@RequestBody StartTaskRB newStartTask) {
	    ResponseEntity<Object> response = 
                    Service.addTaskStart(timelogger, newStartTask);
            updateTimeLoggerInDatabase();
            return response;
	}
        
        @PutMapping("/timelogger/workmonths/workdays/tasks/finish")
        public ResponseEntity<Object> addTaskFinish(@RequestBody FinishingTaskRB finishingTask) {
            ResponseEntity<Object> response = 
                    Service.addTaskFinish(timelogger, finishingTask);
            updateTimeLoggerInDatabase();
            return response;
        }
        
        @PutMapping("/timelogger/workmonths/workdays/tasks/modify")
        public ResponseEntity<Object> modifyTask(@RequestBody ModifyTaskRB modifyTask) {
            ResponseEntity<Object> response = Service.modifyTask(timelogger, modifyTask);
            updateTimeLoggerInDatabase();
            return response;
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