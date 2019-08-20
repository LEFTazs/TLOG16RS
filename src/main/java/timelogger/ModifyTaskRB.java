package timelogger;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class ModifyTaskRB {
    private int year;
    private int month;
    private int day;
    private String taskId;
    private String startTime;
    private String newTaskId;
    private String newComment;
    private String newStartTime;
    private String newEndTime;
}
