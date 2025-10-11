package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.avistador.exception.AvistadorError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class AvistadorAgeRule implements Rule<AvistadorRequestDTO> {
    @Override
    public void check(AvistadorRequestDTO dto) {

        if (dto.getEdad() != null && dto.getEdad() < 18) {
            // IMPORTANTE: Agregar el mensaje como tercer parámetro!
            throw DomainException.of(
                    AvistadorError.UNDERAGE.key,
                    AvistadorError.UNDERAGE.status
            );
        }
    }
}
