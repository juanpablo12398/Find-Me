package edu.utn.proyecto.common.validation.concreta.login.rules;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.auth.exception.LoginError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginAvistadorExistsRuleTest {

    @Mock private IRepoDeAvistadores repo;

    @Test
    void noExiste_lanza() {
        var rule = new LoginAvistadorExistsRule(repo);
        var dto = new LoginRequestDTO();
        dto.setDni("1");
        when(repo.findByDni("1")).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(LoginError.USER_NOT_FOUND.key, ex.getKey());
        verify(repo).findByDni("1");
    }

    @Test
    void existe_ok() {
        var rule = new LoginAvistadorExistsRule(repo);
        var dto = new LoginRequestDTO();
        dto.setDni("1");
        when(repo.findByDni("1")).thenReturn(Optional.of(mock(Avistador.class)));

        assertDoesNotThrow(() -> rule.check(dto));
        verify(repo).findByDni("1");
    }
}
