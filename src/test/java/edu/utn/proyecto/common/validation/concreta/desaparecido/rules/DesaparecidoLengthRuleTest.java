package edu.utn.proyecto.common.validation.concreta.desaparecido.rules;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.desaparecido.exception.DesaparecidoError;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DesaparecidoLengthRuleTest {

    @Test
    void descCortaONull_lanza() {
        var rule = new DesaparecidoLengthRule();

        var dtoNull = new DesaparecidoRequestDTO(); // descripcion = null
        DomainException ex1 = assertThrows(DomainException.class, () -> rule.check(dtoNull));
        assertEquals(DesaparecidoError.DESC_SHORT.key, ex1.getKey());

        var dtoCorta = new DesaparecidoRequestDTO();
        dtoCorta.setDescripcion("demasiado corta"); // < 20
        DomainException ex2 = assertThrows(DomainException.class, () -> rule.check(dtoCorta));
        assertEquals(DesaparecidoError.DESC_SHORT.key, ex2.getKey());
    }

    @Test
    void descLarga_ok() {
        var rule = new DesaparecidoLengthRule();
        var dto  = new DesaparecidoRequestDTO();
        dto.setDescripcion("Descripción suficientemente larga (>=20).");
        assertDoesNotThrow(() -> rule.check(dto));
    }
}
