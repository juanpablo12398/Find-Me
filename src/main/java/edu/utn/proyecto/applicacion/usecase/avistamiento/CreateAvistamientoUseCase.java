package edu.utn.proyecto.applicacion.usecase.avistamiento;
import edu.utn.proyecto.applicacion.dtos.AvistamientoResponseDTO;
import edu.utn.proyecto.domain.service.AvistamientoService;
import edu.utn.proyecto.domain.service.abstraccion.IAvistamientoService;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class CreateAvistamientoUseCase {
    private final IAvistamientoService service;

    public CreateAvistamientoUseCase(AvistamientoService service) {
        this.service = service;
    }

    public AvistamientoResponseDTO execute(AvistamientoRequestDTO dto) {
        return service.crearAvistamiento(dto);
    }
}
