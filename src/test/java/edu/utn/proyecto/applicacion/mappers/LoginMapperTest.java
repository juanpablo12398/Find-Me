package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LoginMapperTest {

    private final LoginMapper mapper = new LoginMapper(new DniNormalizer(), new TextNormalizer());

    @Test
    void normalizeRequestInPlace_normalizaDniYEmail() {
        var req = new LoginRequestDTO();
        req.setDni("12.345.678");
        req.setEmail("  USER@mail.com  ");

        mapper.normalizeRequestInPlace(req);

        assertThat(req.getDni()).isEqualTo("12345678");
        assertThat(req.getEmail()).isEqualTo("USER@mail.com"); // trim aplicado (no cambia case)
    }

    @Test
    void fromLoginRequestToSession_mapeaCamposSimples() {
        var req = new LoginRequestDTO();
        req.setDni("12345678");
        req.setEmail("user@mail.com");

        SessionUserDTO s = mapper.fromLoginRequestToSession(req, "Juan");

        assertThat(s.getDni()).isEqualTo("12345678");
        assertThat(s.getEmail()).isEqualTo("user@mail.com");
        assertThat(s.getNombre()).isEqualTo("Juan");
    }
}