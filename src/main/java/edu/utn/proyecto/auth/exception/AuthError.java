package edu.utn.proyecto.auth.exception;
import org.springframework.http.HttpStatus;

public enum AuthError {
    PADRON_NOT_FOUND("auth.renaper.notfound", HttpStatus.NOT_FOUND),
    AVISTADOR_NOT_FOUND("auth.avistador.notfound", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("auth.user.notfound", HttpStatus.NOT_FOUND),
    EMAIL_MISMATCH("auth.email.mismatch", HttpStatus.UNPROCESSABLE_ENTITY),
    UNAUTHORIZED("auth.unauthorized", HttpStatus.UNAUTHORIZED);

    public final String key;
    public final HttpStatus status;

    AuthError(String key, HttpStatus status) {
        this.key = key;
        this.status = status;
    }
}
