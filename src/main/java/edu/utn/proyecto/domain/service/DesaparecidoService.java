package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.applicacion.mappers.DesaparecidoMapper;
import edu.utn.proyecto.applicacion.validation.desaparecido.DesaparecidoCreatePolicy;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.utn.proyecto.common.validation.abstraccion.Validator;
import java.util.List;

@Service
public class DesaparecidoService {

    private final IRepoDeDesaparecidos repo;
    private final DesaparecidoMapper mapper;
    private final Validator<DesaparecidoRequestDTO> createPolicy;


    public DesaparecidoService(IRepoDeDesaparecidos repo,
                               DesaparecidoMapper mapper,
                               DesaparecidoCreatePolicy createPolicy) {
        this.repo = repo;
        this.mapper = mapper;
        this.createPolicy = createPolicy;
    }

    @Transactional
    public DesaparecidoResponseDTO crearDesaparecido(DesaparecidoRequestDTO dto) {
        mapper.normalizeRequestInPlace(dto);
        createPolicy.validate(dto);
        var domain = mapper.fromRequestToDomain(dto);
        var saved = repo.save(domain);
        return mapper.fromDomainToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<DesaparecidoResponseDTO> obtenerDesaparecidos() {
        return repo.getDesaparecidos().stream()
                .map(mapper::fromDomainToResponse)
                .toList();
    }
}
