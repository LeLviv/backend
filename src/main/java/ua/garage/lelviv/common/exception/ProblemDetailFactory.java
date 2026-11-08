package ua.garage.lelviv.common.exception;

import java.net.URI;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class ProblemDetailFactory {

    private final ExceptionProperties exceptionProperties;

    public ProblemDetail create(HttpStatus httpStatus, String title, String errorPath, String detail) {
        URI errorUri = UriComponentsBuilder.fromUriString(exceptionProperties.baseErrorsUrl())
                .path(errorPath)
                .build()
                .toUri();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(errorUri);
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}
