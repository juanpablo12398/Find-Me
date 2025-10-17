package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.dtos.AvistamientoResponseDTO;
import edu.utn.proyecto.applicacion.mappers.AvistamientoMapper;
import edu.utn.proyecto.applicacion.validation.avistamiento.AvistamientoCreatePolicy;
import edu.utn.proyecto.common.validation.abstraccion.Validator;
import edu.utn.proyecto.domain.model.concreta.Avistamiento;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoFrontDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistamientos;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AvistamientoService {

    private final IRepoDeAvistamientos repoAvistamientos;
    private final IRepoDeAvistadores repoAvistadores;
    private final IRepoDeDesaparecidos repoDesaparecidos;
    private final AvistamientoMapper mapper;
    private final Validator<AvistamientoRequestDTO> createPolicy;

    public AvistamientoService(
            IRepoDeAvistamientos repo,
            IRepoDeAvistadores repoAvistadores,
            IRepoDeDesaparecidos repoDesaparecidos,
            AvistamientoMapper mapper,
            AvistamientoCreatePolicy createPolicy) {
        this.repoAvistamientos = repo;
        this.repoAvistadores = repoAvistadores;
        this.repoDesaparecidos = repoDesaparecidos;
        this.mapper = mapper;
        this.createPolicy = createPolicy;
    }

    // ============================================
    // CREAR
    // ============================================
    @Transactional
    public AvistamientoResponseDTO crearAvistamiento(AvistamientoRequestDTO dto) {
        mapper.normalizeRequestInPlace(dto);
        createPolicy.validate(dto);
        var saved = repoAvistamientos.save(mapper.fromRequestToDomain(dto));
        return mapper.fromDomainToResponse(saved);
    }

    // ============================================
    // LEER - ResponseDTO simple (sin enriquecer)
    // ============================================
    @Transactional(readOnly = true)
    public List<AvistamientoResponseDTO> obtenerAvistamientosPublicos() {
        var lista = repoAvistamientos.findPublicos();
        return mapper.fromDomainListToResponseList(lista);
    }

    @Transactional(readOnly = true)
    public List<AvistamientoResponseDTO> obtenerAvistamientosRecientes(int dias) {
        LocalDateTime desde = LocalDateTime.now().minusDays(dias);
        var lista = repoAvistamientos.findRecientes(desde);
        return mapper.fromDomainListToResponseList(lista);
    }

    @Transactional(readOnly = true)
    public List<AvistamientoResponseDTO> obtenerPorDesaparecido(UUID desaparecidoId) {
        var lista = repoAvistamientos.findByDesaparecidoId(desaparecidoId);
        return mapper.fromDomainListToResponseList(lista);
    }

    @Transactional(readOnly = true)
    public List<AvistamientoResponseDTO> obtenerEnArea(
            Double latMin, Double latMax,
            Double lngMin, Double lngMax) {
        var lista = repoAvistamientos.findInBounds(latMin, latMax, lngMin, lngMax);
        return mapper.fromDomainListToResponseList(lista);
    }

    // ============================================
    // LEER - FrontDTO enriquecido (para el mapa)
    // ============================================
    @Transactional(readOnly = true)
    public List<AvistamientoFrontDTO> obtenerParaMapa() {
        return enrichAvistamientos(repoAvistamientos.findPublicos());
    }

    @Transactional(readOnly = true)
    public List<AvistamientoFrontDTO> obtenerPorDesaparecidoEnriquecido(UUID desaparecidoId) {
        return enrichAvistamientos(repoAvistamientos.findByDesaparecidoId(desaparecidoId));
    }

    @Transactional(readOnly = true)
    public List<AvistamientoFrontDTO> obtenerEnAreaEnriquecido(
            Double latMin, Double latMax,
            Double lngMin, Double lngMax) {
        return enrichAvistamientos(
                repoAvistamientos.findInBounds(latMin, latMax, lngMin, lngMax)
        );
    }

    // ============================================
    // MÉTODO PRIVADO: Enriquecer avistamientos
    // (reutilizable para todos los casos)
    // ============================================
    private List<AvistamientoFrontDTO> enrichAvistamientos(List<Avistamiento> avistamientos) {
        return avistamientos.stream().map(a -> {
            var desaparecido = repoDesaparecidos.findById(a.getDesaparecidoId())
                    .orElseThrow(() -> new RuntimeException("Desaparecido no encontrado"));
            var avistador = repoAvistadores.findById(a.getAvistadorId())
                    .orElseThrow(() -> new RuntimeException("Avistador no encontrado"));

            AvistamientoFrontDTO front = mapper.fromDomainToFrontBasic(a);
            front.setDesaparecidoId(desaparecido.getId());
            front.setDesaparecidoNombre(desaparecido.getNombre());
            front.setDesaparecidoApellido(desaparecido.getApellido());
            front.setDesaparecidoFoto(desaparecido.getFoto());
            front.setAvistadorNombre(avistador.getNombre());
            return front;
        }).collect(Collectors.toList());
    }
}

