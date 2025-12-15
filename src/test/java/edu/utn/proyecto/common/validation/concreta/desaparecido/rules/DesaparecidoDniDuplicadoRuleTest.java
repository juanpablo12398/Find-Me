package edu.utn.proyecto.common.validation.concreta.desaparecido.rules;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.desaparecido.exception.DesaparecidoError;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesaparecidoDniDuplicadoRuleTest {

    @Mock private IRepoDeDesaparecidos repo;

    @Test
    @DisplayName("check: DNI duplicado → DomainException DNI_DUP")
    void dniDuplicado_lanza() {
        var rule = new DesaparecidoDniDuplicadoRule(repo);
        var dto  = new DesaparecidoRequestDTO();
        dto.setDni("123");

        when(repo.existsByDni("123")).thenReturn(true);

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(DesaparecidoError.DNI_DUP.key, ex.getKey());
        verify(repo).existsByDni("123");
    }

    @Test
    @DisplayName("check: DNI null o no duplicado → pasa")
    void dniNullONoDuplicado_ok() {
        var rule = new DesaparecidoDniDuplicadoRule(repo);

        var dtoNull = new DesaparecidoRequestDTO(); // dni = null
        assertDoesNotThrow(() -> rule.check(dtoNull));
        verifyNoInteractions(repo);

        var dtoNoDup = new DesaparecidoRequestDTO();
        dtoNoDup.setDni("1");
        when(repo.existsByDni("1")).thenReturn(false);
        assertDoesNotThrow(() -> rule.check(dtoNoDup));
        verify(repo).existsByDni("1");
    }
}
