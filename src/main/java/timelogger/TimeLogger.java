package timelogger;

import java.util.ArrayList;
import java.util.List;
import timelogger.exceptions.*;

/**
 * Tracks added tasks through months and days.
 */
public class TimeLogger {
    private List<WorkMonth> months;

    public TimeLogger() {
        months = new ArrayList<>();
    }
    
    /**
     * Check all workmonths in the timelogger and check 
     * if any of their date's equal the specified workmonth date. If yes,
     * then the specified workmonth is not new.
     * @param workMonth The month to check.
     * @return boolean Whether the specified workmonth is new or not.
     */
    public boolean isNewMonth(WorkMonth workMonth) {
        boolean isNotNewMonth = months.stream()
                .anyMatch(checkable -> workMonth.getDate()
                        .equals(checkable.getDate())
                );
        return !isNotNewMonth;
    }
    
    /**
     * Add new workmonth to the timelogger.
     * @param workMonth Month to add.
     * @exception NotNewMonthException Specified month's date is already in the timelogger.
     */
    public void addMonth(WorkMonth workMonth) {
        if (this.isNewMonth(workMonth)) {
            months.add(workMonth);
        } else {
            throw new NotNewMonthException();
        }
    }
    
    public WorkMonth getMonth(int index) {
        return months.get(index);
    }

    public List<WorkMonth> getMonths() {
        return new ArrayList<>(months);
    }
    
    /**
     * Print out this timelogger's months line-by-line.
     * Formatting is the following: {index}. {date}
     */
    public void printMonths() {
        for (int i = 0; i < months.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, months.get(i).getDate());
        }
    }
}
