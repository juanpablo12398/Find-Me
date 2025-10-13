package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DesaparecidoLengthRuleTest {

    private final DesaparecidoLengthRule rule = new DesaparecidoLengthRule();

    @Test
    void ok_whenDescripcionTiene20oMas() {
        var dto = new DesaparecidoRequestDTO();
        dto.setDescripcion("abcdefghijklmnopqrst"); // 20
        assertDoesNotThrow(() -> rule.check(dto));
    }

    @Test
    void throws_whenDescripcionEsNull() {
        var dto = new DesaparecidoRequestDTO();
        dto.setDescripcion(null);
        var ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals("desaparecido.descripcion.corta", ex.getKey());
    }

    @Test
    void throws_whenDescripcionMenorA20() {
        var dto = new DesaparecidoRequestDTO();
        dto.setDescripcion("muy corta");
        var ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals("desaparecido.descripcion.corta", ex.getKey());
    }
}
