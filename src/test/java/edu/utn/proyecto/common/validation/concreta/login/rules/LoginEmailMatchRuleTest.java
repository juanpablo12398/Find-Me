package edu.utn.proyecto.common.validation.concreta.login.rules;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.auth.exception.LoginError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginEmailMatchRuleTest {

    @Mock private IRepoDeAvistadores repo;

    @Test
    @DisplayName("email coincide → pasa")
    void emailMatch_ok() {
        var rule = new LoginEmailMatchRule(repo);
        var dto  = new LoginRequestDTO();
        dto.setDni("1");
        dto.setEmail("a@b.com");

        Avistador av = mock(Avistador.class);
        when(av.getEmail()).thenReturn("a@b.com");
        when(repo.findByDni("1")).thenReturn(Optional.of(av));

        assertDoesNotThrow(() -> rule.check(dto));
        verify(repo).findByDni("1");
        verify(av).getEmail();
    }

    @Test
    @DisplayName("email NO coincide → DomainException EMAIL_MISMATCH")
    void emailMismatch_lanza() {
        var rule = new LoginEmailMatchRule(repo);
        var dto  = new LoginRequestDTO();
        dto.setDni("1");
        dto.setEmail("x@x.com");

        Avistador av = mock(Avistador.class);
        when(av.getEmail()).thenReturn("a@b.com");
        when(repo.findByDni("1")).thenReturn(Optional.of(av));

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(LoginError.EMAIL_MISMATCH.key, ex.getKey());
    }

    @Test
    @DisplayName("avistador inexistente → NPE (según implementación actual)")
    void avistadorNull_npe() {
        var rule = new LoginEmailMatchRule(repo);
        var dto  = new LoginRequestDTO();
        dto.setDni("1");
        dto.setEmail("a@b.com");

        when(repo.findByDni("1")).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> rule.check(dto));
    }
}
