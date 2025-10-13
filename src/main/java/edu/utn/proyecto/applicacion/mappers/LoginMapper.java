package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {
    public SessionUserDTO fromLoginRequestToSession(LoginRequestDTO dto) {
        return new SessionUserDTO(
                dto.getDni(),
                dto.getEmail(),
                dto.getResolvedNombre()
        );
    }
}