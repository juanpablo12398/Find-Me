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
class AvistadorRenaperMatchRuleTest {

    @Mock IRepoDeRenaper renaper;

    @Test
    void coincideNombreYApellido_pasa() {
        AvistadorRenaperMatchRule rule = new AvistadorRenaperMatchRule(renaper);

        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni("1");
        dto.setNombre("JUAN");
        dto.setApellido("PEREZ");

        RenaperPersonaEntity persona = mock(RenaperPersonaEntity.class);
        when(persona.getNombre()).thenReturn("JUAN");
        when(persona.getApellido()).thenReturn("PEREZ");

        when(renaper.findByDni("1")).thenReturn(Optional.of(persona));

        assertDoesNotThrow(() -> rule.check(dto));
        verify(renaper).findByDni("1");
    }

    @Test
    void noCoincideAlguno_lanza() {
        AvistadorRenaperMatchRule rule = new AvistadorRenaperMatchRule(renaper);

        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni("1");
        dto.setNombre("JUAN");
        dto.setApellido("GOMEZ");

        RenaperPersonaEntity persona = mock(RenaperPersonaEntity.class);
        when(persona.getNombre()).thenReturn("JUAN");
        when(persona.getApellido()).thenReturn("PEREZ");

        when(renaper.findByDni("1")).thenReturn(Optional.of(persona));

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(AvistadorError.PADRON_NO_MATCH.key, ex.getKey());
        assertEquals(AvistadorError.PADRON_NO_MATCH.status, ex.getStatus());
    }

    @Test
    void personaNullProvocaNPE_bugActual() {
        // Documenta el comportamiento actual (potencial bug).
        AvistadorRenaperMatchRule rule = new AvistadorRenaperMatchRule(renaper);

        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni("1");
        dto.setNombre("A");
        dto.setApellido("B");

        when(renaper.findByDni("1")).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> rule.check(dto));
        verify(renaper).findByDni("1");
    }
}
