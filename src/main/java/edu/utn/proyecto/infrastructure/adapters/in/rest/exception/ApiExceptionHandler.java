package edu.utn.proyecto.infrastructure.adapters.in.rest.exception;
import edu.utn.proyecto.common.exception.DomainException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    private final MessageSource messages;

    public ApiExceptionHandler(MessageSource messages) {
        this.messages = messages;
    }

    @ExceptionHandler(DomainException.class)
    ProblemDetail handle(DomainException ex) {

        // Se resuelve el mensaje desde messages.properties
        String message = messages.getMessage(
                ex.getKey(),
                null,
                ex.getMessage(),
                LocaleContextHolder.getLocale()
        );

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(ex.getStatus(), message);
        pd.setProperty("key", ex.getKey());
        return pd;
    }
}