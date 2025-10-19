import edu.utn.proyecto.auth.exception.AuthError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.concreta.login.rules.LoginEmailMatchRule;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginAvistadorEmailRuleTest {

    @Mock IRepoDeAvistadores repo;
    LoginEmailMatchRule rule;

    @BeforeEach
    void setUp() {
        rule = new LoginEmailMatchRule(repo);
    }

    @Test
    void ok_cuando_email_coincide_exactamente() {
        var req = new LoginRequestDTO();
        req.setDni("1");
        req.setEmail("user@mail.com");

        var av = new Avistador();
        av.setEmail("user@mail.com");
        when(repo.findByDni("1")).thenReturn(Optional.of(av));

        assertDoesNotThrow(() -> rule.check(req));
    }

    @Test
    void lanza_EMAIL_MISMATCH_cuando_no_coincide() {
        var req = new LoginRequestDTO();
        req.setDni("2");
        req.setEmail("user@mail.com");

        var av = new Avistador();
        av.setEmail("otro@mail.com");
        when(repo.findByDni("2")).thenReturn(Optional.of(av));

        var ex = assertThrows(DomainException.class, () -> rule.check(req));
        assertEquals(AuthError.EMAIL_MISMATCH.key, ex.getKey());
    }
}