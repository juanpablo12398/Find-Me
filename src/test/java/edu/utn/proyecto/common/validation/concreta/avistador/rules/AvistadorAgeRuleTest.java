package edu.utn.proyecto.common.validation.concreta.avistador.rules;
import edu.utn.proyecto.avistador.exception.AvistadorError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AvistadorAgeRuleTest {

    private final AvistadorAgeRule rule = new AvistadorAgeRule();

    @Test
    void pasaCuandoEdadEsNull() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setEdad(null);
        assertDoesNotThrow(() -> rule.check(dto));
    }

    @Test
    void pasaCuandoEsMayorOIgualA18() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setEdad(18);
        assertDoesNotThrow(() -> rule.check(dto));

        dto.setEdad(30);
        assertDoesNotThrow(() -> rule.check(dto));
    }

    @Test
    void lanzaCuandoMenorDe18() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setEdad(17);

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(AvistadorError.UNDERAGE.key, ex.getKey());
        assertEquals(AvistadorError.UNDERAGE.status, ex.getStatus());
    }
}
