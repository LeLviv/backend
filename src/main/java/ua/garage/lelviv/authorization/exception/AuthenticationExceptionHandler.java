package ua.garage.lelviv.authorization.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.garage.lelviv.common.exception.ProblemDetailFactory;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@RequiredArgsConstructor
public class AuthenticationExceptionHandler {

    private final ProblemDetailFactory problemDetailFactory;

    @ExceptionHandler(UserAlreadyExistException.class)
    public ProblemDetail handleUserAlreadyExists(UserAlreadyExistException ex) {
        return problemDetailFactory.create(
                HttpStatus.CONFLICT, "User Already Exists", "/user-already-exists", ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleUnauthorized(AuthenticationException ex) {
        return problemDetailFactory.create(HttpStatus.UNAUTHORIZED, "Unauthorized", "/unauthorized", ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        return problemDetailFactory.create(HttpStatus.FORBIDDEN, "Forbidden", "/forbidden", ex.getMessage());
    }
}
