package ua.garage.lelviv.common.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ProblemDetailFactory problemDetailFactory;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex,
            Object body,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode statusCode,
            @NonNull WebRequest request) {

        if (body instanceof ProblemDetail problemDetail) {
            return ResponseEntity.status(statusCode).headers(headers).body(problemDetail);
        }

        HttpStatus status = HttpStatus.resolve(statusCode.value());
        String title = status != null ? status.getReasonPhrase() : "HTTP Error";
        ProblemDetail problemDetail = problemDetailFactory.create(
                status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR,
                title,
                "/errors/" + statusCode.value(),
                title);
        return ResponseEntity.status(statusCode).headers(headers).body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ProblemDetail problemDetail = problemDetailFactory.create(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                "/validation-failed",
                "Invalid request content. Please check the 'errors' field for details");
        problemDetail.setProperty("errors", errors);

        return ResponseEntity.status(status).headers(headers).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    protected ProblemDetail handleUncaughtException(Exception ex) {
        return problemDetailFactory.create(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "/internal-server",
                "An unexpected internal error occurred");
    }
}
