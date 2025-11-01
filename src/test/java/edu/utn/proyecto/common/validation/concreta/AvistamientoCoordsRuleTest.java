package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.avistamiento.exception.AvistamientoError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.concreta.avistamiento.rules.AvistamientoCoordsRule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(MockitoExtension.class)
@DisplayName("AvistamientoCoordsRule - Tests")
class AvistamientoCoordsRuleTest {

    @InjectMocks
    private AvistamientoCoordsRule rule;

    @Test
    @DisplayName("Debe rechazar latitud null")
    void debeRechazarLatitudNull() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(null);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.COORDS_INVALID.key,
                        AvistamientoError.COORDS_INVALID.status
                );
    }

    @Test
    @DisplayName("Debe rechazar latitud menor a -90")
    void debeRechazarLatitudMenorAMenos90() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-91.0);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.COORDS_INVALID.key,
                        AvistamientoError.COORDS_INVALID.status
                );
    }

    @Test
    @DisplayName("Debe rechazar latitud mayor a 90")
    void debeRechazarLatitudMayorA90() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(91.0);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.COORDS_INVALID.key,
                        AvistamientoError.COORDS_INVALID.status
                );
    }

    @Test
    @DisplayName("Debe rechazar longitud null")
    void debeRechazarLongitudNull() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-34.6037);
        dto.setLongitud(null);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.COORDS_INVALID.key,
                        AvistamientoError.COORDS_INVALID.status
                );
    }

    @Test
    @DisplayName("Debe rechazar longitud menor a -180")
    void debeRechazarLongitudMenorAMenos180() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-34.6037);
        dto.setLongitud(-181.0);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.COORDS_INVALID.key,
                        AvistamientoError.COORDS_INVALID.status
                );
    }

    @Test
    @DisplayName("Debe rechazar longitud mayor a 180")
    void debeRechazarLongitudMayorA180() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-34.6037);
        dto.setLongitud(181.0);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.COORDS_INVALID.key,
                        AvistamientoError.COORDS_INVALID.status
                );
    }

    @Test
    @DisplayName("Debe aceptar coordenadas válidas (Buenos Aires)")
    void debeAceptarCoordenadasValidasBuenosAires() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        // When & Then
        assertThatCode(() -> rule.check(dto))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Debe aceptar coordenadas en los límites (-90, -180)")
    void debeAceptarCoordenadasEnLimitesInferiores() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-90.0);
        dto.setLongitud(-180.0);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        // When & Then
        assertThatCode(() -> rule.check(dto))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Debe aceptar coordenadas en los límites (90, 180)")
    void debeAceptarCoordenadasEnLimitesSuperiores() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(90.0);
        dto.setLongitud(180.0);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        // When & Then
        assertThatCode(() -> rule.check(dto))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Debe aceptar coordenadas en el ecuador y meridiano 0")
    void debeAceptarCoordenadasEnEcuadorYMeridiano0() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(0.0);
        dto.setLongitud(0.0);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        // When & Then
        assertThatCode(() -> rule.check(dto))
                .doesNotThrowAnyException();
    }
}
