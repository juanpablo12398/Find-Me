package edu.utn.proyecto.applicacion.usecase.avistamiento;
import edu.utn.proyecto.applicacion.dtos.AvistamientoResponseDTO;
import edu.utn.proyecto.domain.service.AvistamientoService;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoFrontDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ReadAvistamientoUseCase {

    private final AvistamientoService service;

    public ReadAvistamientoUseCase(AvistamientoService service) {
        this.service = service;
    }

    public List<AvistamientoResponseDTO> obtenerTodos() {
        return service.obtenerAvistamientosPublicos();
    }

    public List<AvistamientoResponseDTO> obtenerRecientes(int dias) {
        return service.obtenerAvistamientosRecientes(dias);
    }

    public List<AvistamientoFrontDTO> obtenerParaMapa() {
        return service.obtenerParaMapa();
    }

    public List<AvistamientoFrontDTO> obtenerPorDesaparecido(UUID desaparecidoId) {
        return service.obtenerPorDesaparecidoEnriquecido(desaparecidoId);
    }

    public List<AvistamientoFrontDTO> obtenerEnArea(
            Double latMin, Double latMax,
            Double lngMin, Double lngMax) {
        return service.obtenerEnAreaEnriquecido(latMin, latMax, lngMin, lngMax);
    }

    public List<AvistamientoFrontDTO> obtenerRecientesEnriquecido(int dias) {
        return service.obtenerRecientesEnriquecido(dias);
    }

}
