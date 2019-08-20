package timelogger;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class StartTaskRB {
    private int year;
    private int month;
    private int day;
    private String taskId;
    private String startTime;
    private String comment;
}
