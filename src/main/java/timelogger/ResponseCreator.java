package timelogger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import timelogger.exceptions.*;

public class ResponseCreator {
    public static ResponseEntity<Object> createOkResponse(Object body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
    
    public static ResponseEntity<Object> createExceptionResponse(Exception e) {
        HttpStatus statusCode = chooseStatusCodeForException(e);
        int id = chooseIdForException(e);
        return new ResponseEntity<>(id, statusCode);
    }

    private static int chooseIdForException(Exception e) {
        if (e instanceof WeekendNotEnabledException)
            return 1;
        if (e instanceof FutureWorkException)
            return 2;
        if (e instanceof NegativeMinutesOfWorkException)
            return 3;
        if (e instanceof InvalidTaskIdException)
            return 4;
        if (e instanceof NotSeparatedTimesException)
            return 5;
        if (e instanceof NotExpectedTimeOrderException)
            return 6;
        if (e instanceof EmptyTimeFieldException)
            return 7;
        if (e instanceof NoTaskIdException)
            return 8;
        if (e instanceof NotNewDateException)
            return 9;
        if (e instanceof NotNewMonthException)
            return 10;
        if (e instanceof NotTheSameMonthException)
            return 11;
        return 0;
    }
    
    private static HttpStatus chooseStatusCodeForException(Exception e) {
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        //TODO: different statuses can be added here for different exceptions
        return statusCode;
    }
}
