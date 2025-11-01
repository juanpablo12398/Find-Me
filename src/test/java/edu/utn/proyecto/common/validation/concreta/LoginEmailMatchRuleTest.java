package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.auth.exception.AuthError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.concreta.login.rules.LoginEmailMatchRule;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginEmailMatchRule - Tests")
class LoginEmailMatchRuleTest {

    @Mock private IRepoDeAvistadores repo;
    @InjectMocks private LoginEmailMatchRule rule;

    private Avistador makeAvistador(String dni, String email) {
        Avistador a = new Avistador();
        a.setId(UUID.randomUUID());
        a.setDni(dni);
        a.setNombre("Juan");
        a.setApellido("Pérez");
        a.setEmail(email);
        return a;
    }

    @Test
    @DisplayName("Lanza excepción cuando el email NO coincide exactamente")
    void emailNoCoincide() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setDni("12345678");
        dto.setEmail("incorrecto@example.com");

        Avistador av = makeAvistador("12345678", "correcto@example.com");
        when(repo.findByDni("12345678")).thenReturn(Optional.of(av));

        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key","status")
                .containsExactly(AuthError.EMAIL_MISMATCH.key, AuthError.EMAIL_MISMATCH.status);

        verify(repo).findByDni("12345678");
    }

    @Test
    @DisplayName("Pasa cuando el email coincide exactamente (case-sensitive)")
    void emailCoincide() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setDni("12345678");
        dto.setEmail("juan@example.com");

        Avistador av = makeAvistador("12345678", "juan@example.com");
        when(repo.findByDni("12345678")).thenReturn(Optional.of(av));

        assertThatCode(() -> rule.check(dto)).doesNotThrowAnyException();
        verify(repo).findByDni("12345678");
    }

    @Test
    @DisplayName("Lanza excepción si cambia la capitalización (case-sensitive)")
    void emailDiferenteCapitalizacion() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setDni("12345678");
        dto.setEmail("Juan@Example.com");

        Avistador av = makeAvistador("12345678", "juan@example.com");
        when(repo.findByDni("12345678")).thenReturn(Optional.of(av));

        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key","status")
                .containsExactly(AuthError.EMAIL_MISMATCH.key, AuthError.EMAIL_MISMATCH.status);

        verify(repo).findByDni("12345678");
    }
}