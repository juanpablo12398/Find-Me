package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.mappers.LoginMapper;
import edu.utn.proyecto.applicacion.validation.auth.LoginPolicy;
import edu.utn.proyecto.common.validation.abstraccion.Validator;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final Validator<LoginRequestDTO> loginPolicy;
    private final LoginMapper mapper;
    private final IRepoDeRenaper renaper;
    private final IRepoDeAvistadores repoAvistadores;

    public AuthService(LoginPolicy policy,
                       LoginMapper mapper,
                       IRepoDeRenaper renaper,
                       IRepoDeAvistadores repoAvistadores) {
        this.loginPolicy = policy;
        this.mapper = mapper;
        this.renaper = renaper;
        this.repoAvistadores = repoAvistadores;
    }

    @Transactional(readOnly = true)
    public SessionUserDTO login(LoginRequestDTO dto) {
        mapper.normalizeRequestInPlace(dto);
        loginPolicy.validate(dto);
        var avistador = repoAvistadores.findByDni(dto.getDni()).orElseThrow();
        String resolvedNombre = (avistador.getNombre() != null && !avistador.getNombre().isBlank())
                ? avistador.getNombre()
                : renaper.findByDni(dto.getDni()).map(RenaperPersonaEntity::getNombre).orElse(null);
        return mapper.fromLoginRequestToSession(dto,avistador.getId(),resolvedNombre);
    }
}
