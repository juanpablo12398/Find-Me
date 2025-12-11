package edu.utn.proyecto.common.validation.concreta.desaparecido.rules;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.desaparecido.exception.DesaparecidoError;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesaparecidoRenaperExistsRuleTest {

    @Mock private IRepoDeRenaper renaper;

    @Test
    @DisplayName("check: dni null o no existe en RENAPER → DomainException RENAPER_NOT_FOUND")
    void noExiste_lanza() {
        var rule = new DesaparecidoRenaperExistsRule(renaper);

        var dtoNull = new DesaparecidoRequestDTO(); // dni = null
        DomainException ex1 = assertThrows(DomainException.class, () -> rule.check(dtoNull));
        assertEquals(DesaparecidoError.RENAPER_NOT_FOUND.key, ex1.getKey());
        verifyNoInteractions(renaper);

        var dto = new DesaparecidoRequestDTO();
        dto.setDni("1");
        when(renaper.findByDni("1")).thenReturn(Optional.empty());

        DomainException ex2 = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(DesaparecidoError.RENAPER_NOT_FOUND.key, ex2.getKey());
        verify(renaper).findByDni("1");
    }

    @Test
    @DisplayName("check: existe en RENAPER → pasa")
    void existe_ok() {
        var rule = new DesaparecidoRenaperExistsRule(renaper);
        var dto = new DesaparecidoRequestDTO();
        dto.setDni("1");

        when(renaper.findByDni("1")).thenReturn(Optional.of(mock(RenaperPersonaEntity.class)));

        assertDoesNotThrow(() -> rule.check(dto));
        verify(renaper).findByDni("1");
    }
}
