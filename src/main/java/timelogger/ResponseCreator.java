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
        //TODO: encode more exceptions here
        return 0;
    }
    
    private static HttpStatus chooseStatusCodeForException(Exception e) {
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        //TODO: different statuses can be added here for different exceptions
        return statusCode;
    }
}
