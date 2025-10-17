package edu.utn.proyecto.common.validation.concreta;
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
    void ok_whenEmailCoincide_caseInsensitiveYTrim() {
        var req = new LoginRequestDTO();
        req.setDni("44215507");
        req.setEmail("  USER@mail.com  ");

        var av = new Avistador();
        av.setEmail("user@MAIL.com"); // distinto case, debería coincidir
        when(repo.findByDni("44215507")).thenReturn(Optional.of(av));

        assertDoesNotThrow(() -> rule.check(req));
    }

    @Test
    void ok_whenAmbosEmailsSonNullOVacios() {
        var req = new LoginRequestDTO();
        req.setDni("1");
        req.setEmail(null);

        var av = new Avistador();
        av.setEmail(null);

        when(repo.findByDni("1")).thenReturn(Optional.of(av));

        assertDoesNotThrow(() -> rule.check(req)); // "" vs "" → coinciden
    }

    @Test
    void throws_whenAvistadorNoExiste() {
        var req = new LoginRequestDTO();
        req.setDni("2");
        req.setEmail("a@a.com");

        when(repo.findByDni("2")).thenReturn(Optional.empty());

        var ex = assertThrows(DomainException.class, () -> rule.check(req));
        assertEquals("auth.avistador.notfound", ex.getKey());
    }

    @Test
    void throws_whenEmailNoCoincide() {
        var req = new LoginRequestDTO();
        req.setDni("3");
        req.setEmail("user@mail.com");

        var av = new Avistador();
        av.setEmail("otro@mail.com");

        when(repo.findByDni("3")).thenReturn(Optional.of(av));

        var ex = assertThrows(DomainException.class, () -> rule.check(req));
        assertEquals("auth.email.mismatch", ex.getKey());
    }
}
