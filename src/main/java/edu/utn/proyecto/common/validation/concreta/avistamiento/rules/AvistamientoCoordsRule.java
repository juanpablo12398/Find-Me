package edu.utn.proyecto.common.validation.concreta.avistamiento.rules;
import edu.utn.proyecto.avistamiento.exception.AvistamientoError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class AvistamientoCoordsRule implements Rule<AvistamientoRequestDTO> {

    @Override
    public void check(AvistamientoRequestDTO dto) {

        if (dto.getLatitud() == null ||
                dto.getLatitud() < -90 || dto.getLatitud() > 90) {
            throw DomainException.of(
                    AvistamientoError.COORDS_INVALID.key,
                    AvistamientoError.COORDS_INVALID.status,
                    "Latitud debe estar entre -90 y 90"
            );
        }

        if (dto.getLongitud() == null ||
                dto.getLongitud() < -180 || dto.getLongitud() > 180) {
            throw DomainException.of(
                    AvistamientoError.COORDS_INVALID.key,
                    AvistamientoError.COORDS_INVALID.status,
                    "Longitud debe estar entre -180 y 180"
            );
        }
    }
}
