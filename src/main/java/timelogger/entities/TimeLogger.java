package timelogger.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import timelogger.exceptions.*;

/**
 * Tracks added tasks through months and days.
 */
@Entity
public class TimeLogger {
    @lombok.Getter @lombok.Setter
    @Id int id;
    
    @lombok.Getter @lombok.Setter
    private String name;
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<WorkMonth> months;
    
    public TimeLogger(String name) {
        months = new ArrayList<>();
        this.name = name;
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
    
    public void deleteMonths() {
        months.clear();
    }
    
}
