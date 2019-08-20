package timelogger;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class DeleteTaskRB {
    private int year;
    private int month;
    private int day;
    private String taskId;
    private String startTime;
}
