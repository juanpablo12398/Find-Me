package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.avistador.exception.AvistadorError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.concreta.avistador.rules.AvistadorRenaperExistsRule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AvistadorRenaperExistsRule - Tests")
class AvistadorRenaperExistsRuleTest {

    @Mock private IRepoDeRenaper renaper;
    @InjectMocks private AvistadorRenaperExistsRule rule;

    @Test
    @DisplayName("Rechaza cuando DNI NO existe en RENAPER")
    void dniNoExisteEnRenaper() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni("99999999");
        dto.setNombre("Juan");
        dto.setApellido("Fantasma");
        dto.setEdad(25);
        dto.setDireccion("Calle 1 123");

        when(renaper.findByDni("99999999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key","status")
                .containsExactly(AvistadorError.PADRON_NOT_FOUND.key, AvistadorError.PADRON_NOT_FOUND.status);

        verify(renaper).findByDni("99999999");
    }

    @Test
    @DisplayName("Pasa cuando DNI existe en RENAPER")
    void dniExisteEnRenaper() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni("12345678");
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setEdad(30);
        dto.setDireccion("Av. 9 de Julio 1000");

        RenaperPersonaEntity persona = new RenaperPersonaEntity();
        persona.setDni("12345678");
        persona.setNombre("Juan");
        persona.setApellido("Pérez");

        when(renaper.findByDni("12345678")).thenReturn(Optional.of(persona));

        assertThatCode(() -> rule.check(dto)).doesNotThrowAnyException();
        verify(renaper).findByDni("12345678");
    }

    @Test
    @DisplayName("Rechaza cuando DNI es null (sin consultar al repo)")
    void dniNull() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni(null);
        dto.setNombre("Pedro");
        dto.setApellido("Martínez");
        dto.setEdad(28);
        dto.setDireccion("Dorrego 55");

        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key","status")
                .containsExactly(AvistadorError.PADRON_NOT_FOUND.key, AvistadorError.PADRON_NOT_FOUND.status);

        verify(renaper, never()).findByDni(null);
    }

    @Test
    @DisplayName("Pasa con DNI normalizado")
    void dniNormalizado() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni("87654321");
        dto.setNombre("María");
        dto.setApellido("González");
        dto.setEdad(26);
        dto.setDireccion("Italia 500");

        RenaperPersonaEntity persona = new RenaperPersonaEntity();
        persona.setDni("87654321");
        persona.setNombre("María");
        persona.setApellido("González");

        when(renaper.findByDni("87654321")).thenReturn(Optional.of(persona));

        assertThatCode(() -> rule.check(dto)).doesNotThrowAnyException();
        verify(renaper).findByDni("87654321");
    }
}