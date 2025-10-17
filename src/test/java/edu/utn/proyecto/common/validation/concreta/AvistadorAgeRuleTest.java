package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.concreta.avistador.rules.AvistadorAgeRule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AvistadorAgeRuleTest {

    private final AvistadorAgeRule rule = new AvistadorAgeRule();

    @Test
    void ok_whenEdadEsNull() {
        var dto = new AvistadorRequestDTO();
        dto.setEdad(null);
        assertDoesNotThrow(() -> rule.check(dto));
    }

    @Test
    void ok_whenMayorIgual18() {
        var dto = new AvistadorRequestDTO();
        dto.setEdad(18);
        assertDoesNotThrow(() -> rule.check(dto));
        dto.setEdad(25);
        assertDoesNotThrow(() -> rule.check(dto));
    }

    @Test
    void throws_whenMenorDe18() {
        var dto = new AvistadorRequestDTO();
        dto.setEdad(17);
        var ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals("avistador.edad.menor", ex.getKey());
    }
}
