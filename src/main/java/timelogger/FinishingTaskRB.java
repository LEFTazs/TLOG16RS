package timelogger;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class FinishingTaskRB {
    private int year;
    private int month;
    private int day;
    private String taskId;
    private String startTime;
    private String endTime;
}
