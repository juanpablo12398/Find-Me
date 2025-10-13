package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class LoginRenaperExistsRuleTest {

    @Mock IRepoDeRenaper renaper;
    LoginRenaperExistsRule rule;

    @BeforeEach
    void setUp() {
        rule = new LoginRenaperExistsRule(renaper);
    }

    @Test
    void ok_whenRenaperTienePersonaConEseDni() {
        var req = new LoginRequestDTO();
        req.setDni("44215507");

        // Devolvemos *alguna* persona. El tipo concreto no importa para esta regla.
        // Si tu repo devuelve, por ejemplo, PersonaPadron, esta línea compila igual
        // usando un cast crudo (sólo warning).
        when(renaper.findByDni(anyString()))
                .thenReturn((Optional) Optional.of(mock(Object.class)));

        assertDoesNotThrow(() -> rule.check(req));
    }

    @Test
    void throws_whenRenaperNoEncuentra() {
        var req = new LoginRequestDTO();
        req.setDni("999");

        when(renaper.findByDni(anyString())).thenReturn(Optional.empty());

        var ex = assertThrows(DomainException.class, () -> rule.check(req));
        assertEquals("auth.renaper.notfound", ex.getKey());
    }
}
