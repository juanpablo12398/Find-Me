package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class LoginMapperTest {

    private final LoginMapper mapper = new LoginMapper(new DniNormalizer(), new TextNormalizer());

    @Test
    void normalizeRequestInPlace_normalizaDniYEmail() {
        var req = new LoginRequestDTO();
        req.setDni("12.345.678");
        req.setEmail("  USER@mail.com  "); // con espacios y mayúsculas

        mapper.normalizeRequestInPlace(req);

        assertThat(req.getDni()).isEqualTo("12345678");
        // TextNormalizer.normalize() SOLO hace trim -> mantiene el case
        assertThat(req.getEmail()).isEqualTo("USER@mail.com");
    }

    @Test
    void fromLoginRequestToSession_mapeaCamposSimples_eId() {
        var req = new LoginRequestDTO();
        req.setDni("12345678");
        req.setEmail("user@mail.com");

        UUID avistadorId = UUID.randomUUID();
        SessionUserDTO s = mapper.fromLoginRequestToSession(req, avistadorId, "Juan");

        assertThat(s.getId()).isEqualTo(avistadorId);
        assertThat(s.getDni()).isEqualTo("12345678");
        assertThat(s.getEmail()).isEqualTo("user@mail.com");
        assertThat(s.getNombre()).isEqualTo("Juan");
    }
}