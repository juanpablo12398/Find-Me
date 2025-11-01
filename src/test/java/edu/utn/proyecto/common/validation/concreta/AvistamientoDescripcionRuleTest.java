package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.avistamiento.exception.AvistamientoError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.concreta.avistamiento.rules.AvistamientoDescripcionRule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(MockitoExtension.class)
@DisplayName("AvistamientoDescripcionRule - Tests")
class AvistamientoDescripcionRuleTest {

    @InjectMocks
    private AvistamientoDescripcionRule rule;

    @Test
    @DisplayName("Debe rechazar descripción null")
    void debeRechazarDescripcionNull() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion(null);

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.DESC_SHORT.key,
                        AvistamientoError.DESC_SHORT.status
                );
    }

    @Test
    @DisplayName("Debe rechazar descripción vacía")
    void debeRechazarDescripcionVacia() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("");

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.DESC_SHORT.key,
                        AvistamientoError.DESC_SHORT.status
                );
    }

    @Test
    @DisplayName("Debe rechazar descripción con solo espacios")
    void debeRechazarDescripcionSoloEspacios() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("                    ");

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.DESC_SHORT.key,
                        AvistamientoError.DESC_SHORT.status
                );
    }

    @Test
    @DisplayName("Debe rechazar descripción con menos de 20 caracteres")
    void debeRechazarDescripcionCorta() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("Corta");  // 5 caracteres

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.DESC_SHORT.key,
                        AvistamientoError.DESC_SHORT.status
                );
    }

    @Test
    @DisplayName("Debe rechazar descripción con 19 caracteres (justo debajo del límite)")
    void debeRechazarDescripcion19Caracteres() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("1234567890123456789");  // 19 caracteres

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.DESC_SHORT.key,
                        AvistamientoError.DESC_SHORT.status
                );
    }

    @Test
    @DisplayName("Debe aceptar descripción con exactamente 20 caracteres")
    void debeAceptarDescripcion20Caracteres() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("12345678901234567890");  // 20 caracteres

        // When & Then
        assertThatCode(() -> rule.check(dto))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Debe aceptar descripción válida de más de 20 caracteres")
    void debeAceptarDescripcionValida() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("Vi a la persona cerca de la plaza principal a las 15hs");

        // When & Then
        assertThatCode(() -> rule.check(dto))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Debe contar solo caracteres sin espacios al inicio/final")
    void debeContarCaracteresSinEspaciosLaterales() {
        // Given - 20 caracteres + espacios laterales
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("   12345678901234567890   ");  // trim() = 20 caracteres

        // When & Then
        assertThatCode(() -> rule.check(dto))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Debe rechazar descripción con espacios que queda corta después de trim")
    void debeRechazarDescripcionCortaDespuesDeTrim() {
        // Given - Parece larga pero después de trim es corta
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("   Corta   ");  // trim() = "Corta" = 5 caracteres

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.DESC_SHORT.key,
                        AvistamientoError.DESC_SHORT.status
                );
    }
}
