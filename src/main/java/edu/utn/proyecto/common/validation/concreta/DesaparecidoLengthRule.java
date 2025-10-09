package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.common.exception.UnprocessableException;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class DesaparecidoLengthRule implements Rule<DesaparecidoRequestDTO> {
    @Override public void check(DesaparecidoRequestDTO dto) {
        if (dto.getDescripcion().length() < 20) {
            throw new UnprocessableException("La descripción es demasiado corta.");
        }
    }
}
