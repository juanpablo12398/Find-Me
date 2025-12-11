package edu.utn.proyecto.common.validation.concreta.avistador.rules;
import edu.utn.proyecto.avistador.exception.AvistadorError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvistadorRenaperExistsRuleTest {

    @Mock IRepoDeRenaper renaper;

    @Test
    void dniNullLanzaYNoConsultaRenaper() {
        AvistadorRenaperExistsRule rule = new AvistadorRenaperExistsRule(renaper);
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni(null);

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(AvistadorError.PADRON_NOT_FOUND.key, ex.getKey());
        assertEquals(AvistadorError.PADRON_NOT_FOUND.status, ex.getStatus());
        verifyNoInteractions(renaper);
    }

    @Test
    void noEncontradoEnRenaperLanza() {
        AvistadorRenaperExistsRule rule = new AvistadorRenaperExistsRule(renaper);
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni("1");

        when(renaper.findByDni("1")).thenReturn(Optional.empty());

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(AvistadorError.PADRON_NOT_FOUND.key, ex.getKey());
        verify(renaper).findByDni("1");
    }

    @Test
    void encontradoEnRenaperPasa() {
        AvistadorRenaperExistsRule rule = new AvistadorRenaperExistsRule(renaper);
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni("1");

        when(renaper.findByDni("1"))
                .thenReturn(Optional.of(mock(RenaperPersonaEntity.class)));

        assertDoesNotThrow(() -> rule.check(dto));
        verify(renaper).findByDni("1");
    }
}
