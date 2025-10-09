package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.common.exception.UnprocessableException;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class AvistadorAgeRule implements Rule<AvistadorRequestDTO> {
    @Override public void check(AvistadorRequestDTO dto) {
        if (dto.getEdad() != null && dto.getEdad() < 18) {
            throw new UnprocessableException("Debés ser mayor de edad para registrarte.");
        }
    }
}
