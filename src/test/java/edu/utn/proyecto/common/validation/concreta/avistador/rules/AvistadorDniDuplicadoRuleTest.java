package edu.utn.proyecto.common.validation.concreta.avistador.rules;
import edu.utn.proyecto.avistador.exception.AvistadorError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvistadorDniDuplicadoRuleTest {

    @Mock IRepoDeAvistadores repo;

    @Test
    void noConsultaRepoSiDniNull() {
        AvistadorDniDuplicadoRule rule = new AvistadorDniDuplicadoRule(repo);
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni(null);

        assertDoesNotThrow(() -> rule.check(dto));
        verifyNoInteractions(repo);
    }

    @Test
    void pasaSiNoExisteDuplicado() {
        AvistadorDniDuplicadoRule rule = new AvistadorDniDuplicadoRule(repo);
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni("123");

        when(repo.existsByDni("123")).thenReturn(false);

        assertDoesNotThrow(() -> rule.check(dto));
        verify(repo).existsByDni("123");
        verifyNoMoreInteractions(repo);
    }

    @Test
    void lanzaSiDniDuplicado() {
        AvistadorDniDuplicadoRule rule = new AvistadorDniDuplicadoRule(repo);
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni("123");

        when(repo.existsByDni("123")).thenReturn(true);

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(AvistadorError.DNI_DUP.key, ex.getKey());
        assertEquals(AvistadorError.DNI_DUP.status, ex.getStatus());
        verify(repo).existsByDni("123");
    }
}

