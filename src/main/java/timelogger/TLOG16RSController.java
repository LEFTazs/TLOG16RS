package timelogger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TLOG16RSController {
	TimeLogger timelogger = new TimeLogger();

	@GetMapping("/timelogger/workmonths")
	public String listWorkmonths() {
		String output = "";
		for (WorkMonth workmonth : timelogger.getMonths()) {
			output += workmonth.toString() + "\n"; //TODO: placeholder
		}
		return output;
	}
	
	@PostMapping("/timelogger/workmonths")
	public WorkMonth addWorkMonth(@RequestBody WorkMonthRB newWorkmonth) {
		WorkMonth workmonth = new WorkMonth(newWorkmonth.getYear(), newWorkmonth.getMonth());
		timelogger.addMonth(workmonth);
		return workmonth;
	}
}