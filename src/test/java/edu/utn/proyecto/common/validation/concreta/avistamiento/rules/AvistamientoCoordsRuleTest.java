package edu.utn.proyecto.common.validation.concreta.avistamiento.rules;
import edu.utn.proyecto.avistamiento.exception.AvistamientoError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AvistamientoCoordsRuleTest {

    private final AvistamientoCoordsRule rule = new AvistamientoCoordsRule();

    @Test
    void coordenadasValidas_pasa() {
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-34.6);
        dto.setLongitud(-58.4);
        assertDoesNotThrow(() -> rule.check(dto));
    }

    @Test
    void latitudNull_lanza() {
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(null);
        dto.setLongitud(-58.4);
        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(AvistamientoError.COORDS_INVALID.key, ex.getKey());
    }

    @Test
    void longitudNaN_lanza() {
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(10.0);
        dto.setLongitud(Double.NaN);
        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(AvistamientoError.COORDS_INVALID.key, ex.getKey());
    }

    @Test
    void longitudInfinita_lanza() {
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(10.0);
        dto.setLongitud(Double.POSITIVE_INFINITY);
        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(AvistamientoError.COORDS_INVALID.key, ex.getKey());
    }

    @Test
    void fueraDeRango_lanza() {
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(91.0); // > 90
        dto.setLongitud(0.0);
        assertThrows(DomainException.class, () -> rule.check(dto));

        dto.setLatitud(0.0);
        dto.setLongitud(-181.0); // < -180
        assertThrows(DomainException.class, () -> rule.check(dto));
    }
}
