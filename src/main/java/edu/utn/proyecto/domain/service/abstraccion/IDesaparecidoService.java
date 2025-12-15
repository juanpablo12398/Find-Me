package edu.utn.proyecto.domain.service.abstraccion;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import java.util.List;

public interface IDesaparecidoService {
    DesaparecidoResponseDTO crearDesaparecido(DesaparecidoRequestDTO dto);
    List<DesaparecidoResponseDTO> obtenerDesaparecidos();
}
