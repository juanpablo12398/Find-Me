package edu.utn.proyecto.avistamiento.exception;
import org.springframework.http.HttpStatus;

public enum AvistamientoError {
    COORDS_INVALID("avistamiento.coords.invalidas", HttpStatus.UNPROCESSABLE_ENTITY),
    COORDS_LAT_REQUIRED("avistamiento.coords.lat.required", HttpStatus.UNPROCESSABLE_ENTITY),
    COORDS_LAT_RANGE("avistamiento.coords.lat.range", HttpStatus.UNPROCESSABLE_ENTITY),
    COORDS_LAT_NAN_INF("avistamiento.coords.lat.naninf", HttpStatus.UNPROCESSABLE_ENTITY),
    COORDS_LNG_REQUIRED("avistamiento.coords.lng.required", HttpStatus.UNPROCESSABLE_ENTITY),
    COORDS_LNG_RANGE("avistamiento.coords.lng.range", HttpStatus.UNPROCESSABLE_ENTITY),
    COORDS_LNG_NAN_INF("avistamiento.coords.lng.naninf", HttpStatus.UNPROCESSABLE_ENTITY),
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
