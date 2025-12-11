package edu.utn.proyecto.common.validation.concreta.login.rules;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import edu.utn.proyecto.auth.exception.LoginError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginRenaperExistsRuleTest {

    @Mock private IRepoDeRenaper renaper;

    @Test
    void noExiste_lanza() {
        var rule = new LoginRenaperExistsRule(renaper);
        var dto = new LoginRequestDTO();
        dto.setDni("1");

        when(renaper.findByDni("1")).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(LoginError.RENAPER_NOT_FOUND.key, ex.getKey());
        verify(renaper).findByDni("1");
    }

    @Test
    void existe_ok() {
        var rule = new LoginRenaperExistsRule(renaper);
        var dto = new LoginRequestDTO();
        dto.setDni("1");

        when(renaper.findByDni("1")).thenReturn(Optional.of(mock(RenaperPersonaEntity.class)));

        assertDoesNotThrow(() -> rule.check(dto));
        verify(renaper).findByDni("1");
    }
}
