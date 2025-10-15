package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.dtos.AvistamientoResponseDTO;
import edu.utn.proyecto.applicacion.mappers.AvistamientoMapper;
import edu.utn.proyecto.applicacion.validation.avistamiento.AvistamientoCreatePolicy;
import edu.utn.proyecto.common.validation.abstraccion.Validator;
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

    public AvistamientoService(IRepoDeAvistamientos repo,
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

    @Transactional
    public AvistamientoResponseDTO crearAvistamiento(AvistamientoRequestDTO dto) {
        mapper.normalizeRequestInPlace(dto);
        createPolicy.validate(dto);
        var saved = repoAvistamientos.save(mapper.fromRequestToDomain(dto));
        return mapper.fromDomainToResponse(saved);
    }


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

    @Transactional(readOnly = true)
    public List<AvistamientoFrontDTO> obtenerParaMapa() {
        return repoAvistamientos.findPublicos().stream().map(a -> {
            var desaparecido = repoDesaparecidos.findById(a.getDesaparecidoId()).orElseThrow();
            var avistador = repoAvistadores.findById(a.getAvistadorId()).orElseThrow();

            AvistamientoFrontDTO base = mapper.fromDomainToFrontBasic(a);
            base.setDesaparecidoId(desaparecido.getId());
            base.setDesaparecidoNombre(desaparecido.getNombre());
            base.setDesaparecidoApellido(desaparecido.getApellido());
            base.setDesaparecidoFoto(desaparecido.getFoto());
            base.setAvistadorNombre(avistador.getNombre());
            return base;
        }).collect(Collectors.toList());
    }
}

