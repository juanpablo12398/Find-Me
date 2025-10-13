package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.mappers.LoginMapper;
import edu.utn.proyecto.applicacion.validation.auth.LoginPolicy;
import edu.utn.proyecto.common.validation.abstraccion.Validator;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final Validator<LoginRequestDTO> loginPolicy;
    private final LoginMapper mapper;

    public AuthService(LoginPolicy policy, LoginMapper mapper) {
        this.loginPolicy = policy;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public SessionUserDTO login(LoginRequestDTO dto) {
        // Toda la lógica vive en la Policy
        loginPolicy.validate(dto);
        // El DTO ya viene normalizado y enriquecido; mapeo a SessionUserDTO
        return mapper.fromLoginRequestToSession(dto);
    }
}
