package edu.java.Controller;

import edu.java.Response.ApiErrorResponse;
import java.util.Arrays;
import edu.java.exceptions.AlreadyExistException;
import edu.java.exceptions.NotExistException;
import edu.java.exceptions.RepeatedRegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ScrapperException {
    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> exceptionBadRequest(AlreadyExistException exception) {
        return ResponseEntity.badRequest()
            .body(new ApiErrorResponse(
                "Некорректные параметры запроса",
                "400",
                exception.getClass().getName(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)
            ));
    }

    @ExceptionHandler(RepeatedRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> exceptionNotFound(RepeatedRegistrationException exception) {
        return ResponseEntity.badRequest()
            .body(new ApiErrorResponse(
                "Чата не существует",
                "404",
                exception.getClass().getName(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)
            ));
    }

    @ExceptionHandler(NotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> exceptionNotFound(NotExistException exception) {
        return ResponseEntity.badRequest()
            .body(new ApiErrorResponse(
                "Чата/Ссылки не существует",
                "404",
                exception.getClass().getName(),
                exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)
            ));
    }
}
