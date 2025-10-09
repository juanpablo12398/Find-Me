package edu.utn.proyecto.avistador.exception;

import edu.utn.proyecto.common.exception.NotFoundException;

public class PersonaNoEncontradaException extends NotFoundException {
    public PersonaNoEncontradaException(String dni) {
        super("No existe en padrón RENAPER: " + dni);
    }
}