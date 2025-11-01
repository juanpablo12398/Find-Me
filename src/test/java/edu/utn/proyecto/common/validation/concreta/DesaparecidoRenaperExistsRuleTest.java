package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.concreta.desaparecido.rules.DesaparecidoRenaperExistsRule;
import edu.utn.proyecto.desaparecido.exception.DesaparecidoError;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
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
@DisplayName("DesaparecidoRenaperExistsRule - Tests")
class DesaparecidoRenaperExistsRuleTest {

    @Mock
    private IRepoDeRenaper renaper;

    @InjectMocks
    private DesaparecidoRenaperExistsRule rule;

    private DesaparecidoRequestDTO mkDto(String dni, String nombre, String apellido, int edad, String descripcion) {
        DesaparecidoRequestDTO dto = new DesaparecidoRequestDTO();
        dto.setDni(dni);
        dto.setNombre(nombre);
        dto.setApellido(apellido);
        dto.setEdad(edad);
        dto.setDescripcion(descripcion);
        dto.setFotoUrl(null);
        return dto;
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el DNI NO existe en RENAPER")
    void debeRechazarDniNoExisteEnRenaper() {
        // Given
        DesaparecidoRequestDTO dto = mkDto(
                "99999999", "Juan", "Fantasma", 25, "Descripción de más de 20 caracteres"
        );

        when(renaper.findByDni("99999999")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        DesaparecidoError.RENAPER_NOT_FOUND.key,
                        DesaparecidoError.RENAPER_NOT_FOUND.status
                );

        verify(renaper).findByDni("99999999");
    }

    @Test
    @DisplayName("Debe pasar cuando el DNI existe en RENAPER")
    void debePermitirDniExisteEnRenaper() {
        // Given
        DesaparecidoRequestDTO dto = mkDto(
                "12345678", "Juan", "Pérez", 30, "Descripción de más de 20 caracteres"
        );

        RenaperPersonaEntity persona = new RenaperPersonaEntity();
        persona.setDni("12345678");
        persona.setNombre("Juan");
        persona.setApellido("Pérez");

        when(renaper.findByDni("12345678")).thenReturn(Optional.of(persona));

        // When & Then
        assertThatCode(() -> rule.check(dto)).doesNotThrowAnyException();

        verify(renaper).findByDni("12345678");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el DNI es null (short-circuit, sin consultar RENAPER)")
    void debeRechazarDniNull() {
        // Given
        DesaparecidoRequestDTO dto = mkDto(
                null, "Pedro", "Martínez", 28, "Descripción de más de 20 caracteres"
        );

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        DesaparecidoError.RENAPER_NOT_FOUND.key,
                        DesaparecidoError.RENAPER_NOT_FOUND.status
                );

        // Como la regla hace short-circuit, NO debe llamar al repo
        verify(renaper, never()).findByDni(any());
        // alternativamente:
        // verifyNoInteractions(renaper);
    }

    @Test
    @DisplayName("Debe validar con DNI normalizado (sin puntos/espacios)")
    void debeValidarDniNormalizado() {
        // Given
        DesaparecidoRequestDTO dto = mkDto(
                "87654321", "María", "González", 26, "Descripción de más de 20 caracteres"
        );

        RenaperPersonaEntity persona = new RenaperPersonaEntity();
        persona.setDni("87654321");
        persona.setNombre("María");
        persona.setApellido("González");

        when(renaper.findByDni("87654321")).thenReturn(Optional.of(persona));

        // When & Then
        assertThatCode(() -> rule.check(dto)).doesNotThrowAnyException();

        verify(renaper).findByDni("87654321");
    }
}
