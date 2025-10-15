package edu.utn.proyecto.avistamiento.exception;
import org.springframework.http.HttpStatus;

public enum AvistamientoError {
    COORDS_INVALID("avistamiento.coords.invalidas", HttpStatus.UNPROCESSABLE_ENTITY),
    DESC_SHORT("avistamiento.descripcion.corta", HttpStatus.UNPROCESSABLE_ENTITY),
    AVISTADOR_NOT_FOUND("avistamiento.avistador.notfound", HttpStatus.NOT_FOUND),
    DESAPARECIDO_NOT_FOUND("avistamiento.desaparecido.notfound", HttpStatus.NOT_FOUND);

    public final String key;
    public final HttpStatus status;

    AvistamientoError(String key, HttpStatus status) {
        this.key = key;
        this.status = status;
    }
}
