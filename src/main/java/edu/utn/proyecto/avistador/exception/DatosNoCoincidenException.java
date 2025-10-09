package edu.utn.proyecto.avistador.exception;

import edu.utn.proyecto.common.exception.UnprocessableException;

public class DatosNoCoincidenException extends UnprocessableException {
    public DatosNoCoincidenException() {
        super("Los datos no coinciden con el padrón.");
    }
}