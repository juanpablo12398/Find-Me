package edu.utn.proyecto.desaparecido.exception;
import org.springframework.http.HttpStatus;

public enum DesaparecidoError {
    DNI_DUP("desaparecido.dni.duplicado", HttpStatus.CONFLICT),
    DESC_SHORT("desaparecido.descripcion.corta", HttpStatus.UNPROCESSABLE_ENTITY);

    public final String key;
    public final HttpStatus status;
    DesaparecidoError(String key, HttpStatus status) {
        this.key = key; this.status = status;
    }
}
