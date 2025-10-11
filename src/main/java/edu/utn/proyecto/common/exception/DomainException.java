package edu.utn.proyecto.common.exception;
import org.springframework.http.HttpStatus;

public class DomainException extends RuntimeException {
    private final String key;
    private final HttpStatus status;

    private DomainException(String key, HttpStatus status, String message) {
        super(message);
        this.key = key;
        this.status = status;
    }

    public static DomainException of(String key, HttpStatus status, Object... args) {
        // Si hay argumentos, úsalos como mensaje, sino usa un mensaje por defecto
        String msg;
        if (args != null && args.length > 0) {
            msg = String.join(" ", java.util.Arrays.stream(args)
                    .map(String::valueOf)
                    .toList());
        } else {
            // Mensaje por defecto basado en la key
            msg = "Error: " + key;
        }
        return new DomainException(key, status, msg);
    }

    public String getKey() { return key; }
    public HttpStatus getStatus() { return status; }
}