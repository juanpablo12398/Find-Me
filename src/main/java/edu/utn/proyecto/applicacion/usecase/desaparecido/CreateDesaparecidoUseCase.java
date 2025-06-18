package edu.utn.proyecto.applicacion.usecase.desaparecido;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.domain.service.DesaparecidoService;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class CreateDesaparecidoUseCase {
    private final DesaparecidoService desaparecidoService;

    public CreateDesaparecidoUseCase(DesaparecidoService desaparecidoService) {
        this.desaparecidoService = desaparecidoService;
    }

    public DesaparecidoResponseDTO execute(DesaparecidoRequestDTO dto) {
        // Aca podría ir la excepción respectiva
        return desaparecidoService.crearDesaparecido(dto);
    }
}
