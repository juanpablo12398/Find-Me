package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.avistador.exception.AvistadorError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.concreta.avistador.rules.AvistadorRenaperMatchRule;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("AvistadorRenaperMatchRule - Tests")
class AvistadorRenaperMatchRuleTest {

    @Mock
    private IRepoDeRenaper renaper;

    @InjectMocks
    private AvistadorRenaperMatchRule rule;

    private AvistadorRequestDTO mkDto(String dni, String nombre, String apellido, Integer edad) {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni(dni);
        dto.setNombre(nombre);
        dto.setApellido(apellido);
        dto.setEdad(edad);
        // Campos no usados por la rule (opcionales aquí):
        dto.setDireccion("x"); // NotBlank en el DTO, pero NO se valida en estos unit tests
        dto.setEmail(null);
        dto.setTelefono(null);
        return dto;
    }

    private RenaperPersonaEntity mkRenaper(String dni, String nombre, String apellido) {
        RenaperPersonaEntity p = new RenaperPersonaEntity();
        p.setDni(dni);
        p.setNombre(nombre);
        p.setApellido(apellido);
        return p;
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre NO coincide con RENAPER")
    void debeRechazarNombreNoCoincide() {
        AvistadorRequestDTO dto = mkDto("12345678", "Pedro", "Pérez", 30);

        when(renaper.findByDni("12345678"))
                .thenReturn(Optional.of(mkRenaper("12345678", "Juan", "Pérez")));

        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistadorError.PADRON_NO_MATCH.key,
                        AvistadorError.PADRON_NO_MATCH.status
                );

        verify(renaper).findByDni("12345678");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el apellido NO coincide con RENAPER")
    void debeRechazarApellidoNoCoincide() {
        AvistadorRequestDTO dto = mkDto("12345678", "Juan", "González", 30);

        when(renaper.findByDni("12345678"))
                .thenReturn(Optional.of(mkRenaper("12345678", "Juan", "Pérez")));

        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistadorError.PADRON_NO_MATCH.key,
                        AvistadorError.PADRON_NO_MATCH.status
                );

        verify(renaper).findByDni("12345678");
    }

    @Test
    @DisplayName("Debe pasar cuando nombre y apellido coinciden exactamente")
    void debePermitirDatosCoinciden() {
        AvistadorRequestDTO dto = mkDto("12345678", "Juan", "Pérez", 30);

        when(renaper.findByDni("12345678"))
                .thenReturn(Optional.of(mkRenaper("12345678", "Juan", "Pérez")));

        assertThatCode(() -> rule.check(dto)).doesNotThrowAnyException();

        verify(renaper).findByDni("12345678");
    }

    @Test
    @DisplayName("Debe validar con datos normalizados (sin tildes ni mayúsculas)")
    void debeValidarDatosNormalizados() {
        AvistadorRequestDTO dto = mkDto("87654321", "maria", "gomez", 28);

        when(renaper.findByDni("87654321"))
                .thenReturn(Optional.of(mkRenaper("87654321", "maria", "gomez")));

        assertThatCode(() -> rule.check(dto)).doesNotThrowAnyException();

        verify(renaper).findByDni("87654321");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando ambos NO coinciden")
    void debeRechazarAmbosDatosNoCoinciden() {
        AvistadorRequestDTO dto = mkDto("12345678", "Pedro", "González", 30);

        when(renaper.findByDni("12345678"))
                .thenReturn(Optional.of(mkRenaper("12345678", "Juan", "Pérez")));

        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistadorError.PADRON_NO_MATCH.key,
                        AvistadorError.PADRON_NO_MATCH.status
                );

        verify(renaper).findByDni("12345678");
    }
}
