package edu.utn.proyecto.applicacion.usecase.avistamiento;
import edu.utn.proyecto.applicacion.dtos.AvistamientoResponseDTO;
import edu.utn.proyecto.domain.service.AvistamientoService;
import edu.utn.proyecto.domain.service.abstraccion.IAvistamientoService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ReadAvistamientoUseCase {

    private final IAvistamientoService service;

    public ReadAvistamientoUseCase(AvistamientoService service) {
        this.service = service;
    }

    public List<AvistamientoResponseDTO> obtenerTodos() {
        return service.obtenerAvistamientosPublicos();
    }

    public List<AvistamientoResponseDTO> obtenerRecientes(int dias) {
        return service.obtenerAvistamientosRecientes(dias);
    }

    public List<AvistamientoResponseDTO> obtenerPorDesaparecido(UUID desaparecidoId) {
        return service.obtenerPorDesaparecido(desaparecidoId);
    }

    public List<AvistamientoResponseDTO> obtenerEnArea(
            Double latMin, Double latMax, Double lngMin, Double lngMax) {
        return service.obtenerEnArea(latMin, latMax, lngMin, lngMax);
    }
}
