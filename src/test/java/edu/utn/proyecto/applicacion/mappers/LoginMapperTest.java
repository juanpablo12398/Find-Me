package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LoginMapperTest {

    private final LoginMapper mapper = new LoginMapper();

    @Test
    void fromLoginRequestToSession_mapeaCamposSimples() {
        var req = new LoginRequestDTO();
        req.setDni("12345678");
        req.setEmail("user@mail.com");
        req.setResolvedNombre("Juan");

        SessionUserDTO s = mapper.fromLoginRequestToSession(req);

        assertThat(s.getDni()).isEqualTo("12345678");
        assertThat(s.getEmail()).isEqualTo("user@mail.com");
        assertThat(s.getNombre()).isEqualTo("Juan");
    }
}
