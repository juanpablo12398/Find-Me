package edu.utn.proyecto.applicacion.validation.auth;
import edu.utn.proyecto.auth.exception.AuthError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginPolicyTest {

    @Mock IRepoDeRenaper renaper;
    @Mock IRepoDeAvistadores repo;
    @Mock Rule<LoginRequestDTO> rule1;
    @Mock Rule<LoginRequestDTO> rule2;

    DniNormalizer dniNorm = new DniNormalizer();
    TextNormalizer txtNorm = new TextNormalizer();

    LoginPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new LoginPolicy(renaper, repo, dniNorm, txtNorm, List.of(rule1, rule2));
    }

    @Test
    void happyPath_reglas_ok_renaper_y_usuario_existen_emailCoincide_caseInsensitive() {
        var dto = new LoginRequestDTO();
        dto.setDni("12.345.678");
        dto.setEmail("USER@mail.com");

        var persona = new RenaperPersonaEntity();
        persona.setDni("12345678");
        persona.setNombre("Juan");
        persona.setApellido("Perez");

        var av = new Avistador(UUID.randomUUID(), "12345678", "Juan", "Perez",
                30, "Dir", "user@mail.com", null, LocalDateTime.now());

        when(renaper.findByDni("12345678")).thenReturn(Optional.of(persona));
        when(repo.findByDni("12345678")).thenReturn(Optional.of(av));

        policy.validate(dto);

        // reglas ejecutadas
        verify(rule1).check(dto);
        verify(rule2).check(dto);

        // dto enriquecido
        assertThat(dto.getDni()).isEqualTo("12345678");
        assertThat(dto.getResolvedNombre()).isEqualTo("Juan");
        assertThat(dto.getEmail()).isEqualTo("user@mail.com");
    }

    @Test
    void lanzaNotFound_siNoExisteEnRenaper() {
        var dto = new LoginRequestDTO();
        dto.setDni("12345678");
        dto.setEmail("a@a.com");

        when(renaper.findByDni("12345678")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .extracting(e -> ((DomainException)e).getKey())
                .isEqualTo(AuthError.PADRON_NOT_FOUND.key);
    }

    @Test
    void lanzaUserNotFound_siNoExisteAvistador() {
        var dto = new LoginRequestDTO();
        dto.setDni("12345678");
        dto.setEmail("a@a.com");

        var persona = new RenaperPersonaEntity();
        persona.setDni("12345678");
        persona.setNombre("Juan");
        persona.setApellido("Perez");

        when(renaper.findByDni("12345678")).thenReturn(Optional.of(persona));
        when(repo.findByDni("12345678")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .extracting(e -> ((DomainException)e).getKey())
                .isEqualTo(AuthError.USER_NOT_FOUND.key);
    }

    @Test
    void lanzaEmailMismatch_siNoCoincideEmail() {
        var dto = new LoginRequestDTO();
        dto.setDni("12345678");
        dto.setEmail("otra@a.com");

        var persona = new RenaperPersonaEntity();
        persona.setDni("12345678");
        persona.setNombre("Juan");
        persona.setApellido("Perez");

        var av = new Avistador(UUID.randomUUID(), "12345678", "Juan", "Perez",
                30, "Dir", "user@mail.com", null, LocalDateTime.now());

        when(renaper.findByDni("12345678")).thenReturn(Optional.of(persona));
        when(repo.findByDni("12345678")).thenReturn(Optional.of(av));

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .extracting(e -> ((DomainException)e).getKey())
                .isEqualTo(AuthError.EMAIL_MISMATCH.key);
    }

    @Test
    void resolvedNombre_tomaPersonaSiAvistadorNoTieneNombre() {
        var dto = new LoginRequestDTO();
        dto.setDni("12345678");
        dto.setEmail("x@x.com");

        var persona = new RenaperPersonaEntity();
        persona.setDni("12345678");
        persona.setNombre("NombreRENAPER");
        persona.setApellido("ApellidoRENAPER");

        var av = new Avistador(UUID.randomUUID(), "12345678", "", "", // nombre/ape vacíos
                30, "Dir", "x@x.com", null, LocalDateTime.now());

        when(renaper.findByDni("12345678")).thenReturn(Optional.of(persona));
        when(repo.findByDni("12345678")).thenReturn(Optional.of(av));

        policy.validate(dto);

        assertThat(dto.getResolvedNombre()).isEqualTo("NombreRENAPER");
    }
}
