package edu.utn.proyecto.avistador.exception;

import edu.utn.proyecto.common.exception.ConflictException;

public class DniDuplicadoException extends ConflictException {
    public DniDuplicadoException(String dni) {
        super("El DNI ya está registrado: " + dni);
    }
}