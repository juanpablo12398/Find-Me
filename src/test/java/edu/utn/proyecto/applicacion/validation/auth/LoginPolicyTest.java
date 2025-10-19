package edu.utn.proyecto.applicacion.validation.auth;
import edu.utn.proyecto.auth.exception.AuthError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginPolicyTest {

    @Mock IRepoDeRenaper renaper;
    @Mock IRepoDeAvistadores repo;
    @Mock Rule<LoginRequestDTO> rule1;
    @Mock Rule<LoginRequestDTO> rule2;

    LoginPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new LoginPolicy(renaper, repo, List.of(rule1, rule2));
    }

    @Test
    void happyPath_solo_ejecuta_reglas() {
        var dto = new LoginRequestDTO();
        dto.setDni("12345678");
        dto.setEmail("user@mail.com");

        policy.validate(dto);

        verify(rule1).check(dto);
        verify(rule2).check(dto);
        verifyNoInteractions(renaper, repo);
    }

    @Test
    void propaga_PADRON_NOT_FOUND_si_una_regla_falla() {
        var dto = new LoginRequestDTO();
        dto.setDni("123");
        dto.setEmail("a@a.com");

        doThrow(DomainException.of(
                AuthError.PADRON_NOT_FOUND.key,
                AuthError.PADRON_NOT_FOUND.status,
                "no existe"))
                .when(rule1).check(dto);

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .extracting(e -> ((DomainException) e).getKey())
                .isEqualTo(AuthError.PADRON_NOT_FOUND.key);

        verify(rule1).check(dto);
        verify(rule2, never()).check(dto);
        verifyNoInteractions(renaper, repo);
    }

    @Test
    void propaga_USER_NOT_FOUND_si_una_regla_falla() {
        var dto = new LoginRequestDTO();
        dto.setDni("123");
        dto.setEmail("a@a.com");

        doThrow(DomainException.of(
                AuthError.USER_NOT_FOUND.key,
                AuthError.USER_NOT_FOUND.status,
                "no registrado"))
                .when(rule1).check(dto);

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .extracting(e -> ((DomainException) e).getKey())
                .isEqualTo(AuthError.USER_NOT_FOUND.key);

        verify(rule1).check(dto);
        verify(rule2, never()).check(dto);
        verifyNoInteractions(renaper, repo);
    }

    @Test
    void propaga_EMAIL_MISMATCH_si_una_regla_falla() {
        var dto = new LoginRequestDTO();
        dto.setDni("123");
        dto.setEmail("x@y.com");

        // rule1 ok, falla rule2
        doNothing().when(rule1).check(dto);
        doThrow(DomainException.of(
                AuthError.EMAIL_MISMATCH.key,
                AuthError.EMAIL_MISMATCH.status,
                "mismatch"))
                .when(rule2).check(dto);

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .extracting(e -> ((DomainException) e).getKey())
                .isEqualTo(AuthError.EMAIL_MISMATCH.key);

        verify(rule1).check(dto);
        verify(rule2).check(dto);
        verifyNoInteractions(renaper, repo);
    }
}
