package timelogger.entities;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import timelogger.Util;
import timelogger.exceptions.*;

/**
 * Tracks tasks through days. Contains statistics of these days.
 */
@Entity
@lombok.Getter
public class WorkMonth {
    @lombok.Setter @Id int id;
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<WorkDay> days;
    private transient YearMonth date;
    private String dateString;
    private long sumPerMonth;
    private long requiredMinPerMonth;
    
    public WorkMonth(int year, int month) {
        this.date = YearMonth.of(year, month);
        this.dateString = this.date.toString();
        this.days = new ArrayList<>();
    }
    
    
    /**
     * How many extra minutes are there for this month.
     * @return long Extra minutes for this month.
     */
    public long getExtraMinPerMonth() {
        return days.stream()
                .mapToLong(WorkDay::getExtraMinPerDay)
                .sum();
    }
    
    /**
     * Check all workdays in this workmonth and check 
     * if any of their date's equal the specified workday date. If yes,
     * then the specified workday is not new.
     * @param workDay The day to check.
     * @return boolean Whether the specified day is new or not.
     */
    public boolean isNewDate(WorkDay workDay) {
        boolean isNotNewDate = days.stream()
                .anyMatch(checkable -> workDay.getActualDay()
                        .equals(checkable.getActualDay())
                );
        return !isNotNewDate;
    }
    
    /**
     * Check if the specified day's date is in the same month as this workmonth.
     * @param workDay The day to check.
     * @return boolean Does specified day have same month as this workmonth.
     */
    public boolean isSameMonth(WorkDay workDay) {
        int inputMonth = workDay.getActualDay().getMonthValue();
        int selfMonth = date.getMonthValue();
        
        return inputMonth == selfMonth;
    }
    
    /**
     * Add a workday to this workmonth.
     * @param workDay Day to add.
     * @param isWeekendEnabled If true, workdays that are on weekends will be addable.
     * @exception NotTheSameMonthException Specified day has different month than this workmonth.
     * @exception NotNewDateException Specified day's date is already in this workmonth.
     * @exception WeekendNotEnabledException Specified day is a weekend, but weekends were not enabled.
     */
    public void addWorkDay(WorkDay workDay, boolean isWeekendEnabled) {
        if (!this.isSameMonth(workDay))
            throw new NotTheSameMonthException();
        
        if (!this.isNewDate(workDay))
            throw new NotNewDateException();
        
        if (!isWeekendEnabled && !Util.isWeekday(workDay))
            throw new WeekendNotEnabledException();
        
        days.add(workDay);
    }
    
    /**
     * Add a workday to this workmonth.
     * @param workDay Day to add.
     * @exception NotTheSameMonthException Specified day has different month than this workmonth.
     * @exception NotNewDateException Specified day's date is already in this workmonth.
     * @exception WeekendNotEnabledException Specified day is a weekend, but weekends were not enabled.
     */
    public void addWorkDay(WorkDay workDay) {
        boolean isWeekendEnabled = false;
        this.addWorkDay(workDay, isWeekendEnabled);
    }
    
    
    public WorkDay getDay(int index) {
        return days.get(index);
    }

    public List<WorkDay> getDays() {
        return new ArrayList<>(days);
    }

    /**
     * Update the sumPerMonth field and return it.
     * Sum per month is calculated by summing this workmonth's day's sumPerDay.
     * @return long Sum per day.
     */
    public long getSumPerMonth() {
        updateSumPerMonth();
        return sumPerMonth;
    }
    
    private void updateSumPerMonth() {
        sumPerMonth = days.stream()
                .mapToLong(WorkDay::getSumPerDay)
                .sum();
    }

    /**
     * Update the requiredMinPerMonth field and return it.
     * Required minutes per month is calculated by 
     * summing this workmonth's day's requiredMinPerDay.
     * @return long Required minimum per month.
     */
    public long getRequiredMinPerMonth() {
        updateRequiredMinPerMonth();
        return requiredMinPerMonth;
    }
    
    private void updateRequiredMinPerMonth() {
        requiredMinPerMonth = days.stream()
                .mapToLong(WorkDay::getRequiredMinPerDay)
                .sum();
    }
    
    public void setDateFromDateString() {
        date = YearMonth.parse(dateString);
    }
}
