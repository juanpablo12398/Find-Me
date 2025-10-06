package edu.utn.proyecto.applicacion.usecase.desaparecido;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.applicacion.mappers.DesaparecidoMapper;
import edu.utn.proyecto.domain.service.DesaparecidoService;
import java.util.List;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoFrontDTO;
import org.springframework.stereotype.Service;

@Service
public class ReadDesaparecidoUseCase {
    private final DesaparecidoService desaparecidoService;
    // Cuando necestie exponer datos en el front
    // Los casos de uso que hacen la traducción específica
    private final DesaparecidoMapper desaparecidoMapper;

    public ReadDesaparecidoUseCase(DesaparecidoService desaparecidoService, DesaparecidoMapper desaparecidoMapper) {
        this.desaparecidoService = desaparecidoService;
        this.desaparecidoMapper = desaparecidoMapper;
    }

    public List<DesaparecidoFrontDTO> execute() {
        List<DesaparecidoResponseDTO> lista = desaparecidoService.obtenerDesaparecidos();
        return desaparecidoMapper.fromResponseListToFrontList(lista);
    }
}
