package edu.utn.proyecto.common.validation.concreta.avistamiento.rules;
import edu.utn.proyecto.avistamiento.exception.AvistamientoError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AvistamientoDescripcionRuleTest {

    private final AvistamientoDescripcionRule rule = new AvistamientoDescripcionRule();

    @Test
    void nullOLargaInsuficiente_lanza() {
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setDescripcion(null);
        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(AvistamientoError.DESC_SHORT.key, ex.getKey());

        dto.setDescripcion(" ".repeat(10));
        assertThrows(DomainException.class, () -> rule.check(dto));

        dto.setDescripcion("x".repeat(19)); // < 20
        assertThrows(DomainException.class, () -> rule.check(dto));
    }

    @Test
    void justo20OPorEncima_pasa() {
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setDescripcion("x".repeat(20));
        assertDoesNotThrow(() -> rule.check(dto));

        dto.setDescripcion("x".repeat(50));
        assertDoesNotThrow(() -> rule.check(dto));
    }
}
