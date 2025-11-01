package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.avistamiento.exception.AvistamientoError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.concreta.avistamiento.rules.DesaparecidoExistsRule;
import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("DesaparecidoExistsRule (Avistamiento) - Tests")
class DesaparecidoExistsRuleTest {

    @Mock
    private IRepoDeDesaparecidos repo;

    @InjectMocks
    private DesaparecidoExistsRule rule;

    @Test
    @DisplayName("Debe rechazar cuando desaparecidoId es null")
    void debeRechazarDesaparecidoIdNull() {
        // Given
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setAvistadorId(UUID.randomUUID().toString());
        dto.setDesaparecidoId(null);
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.DESAPARECIDO_NOT_FOUND.key,
                        AvistamientoError.DESAPARECIDO_NOT_FOUND.status
                );
    }

    @Test
    @DisplayName("Debe rechazar cuando el desaparecido NO existe en la BD")
    void debeRechazarDesaparecidoNoExiste() {
        // Given
        UUID desaparecidoId = UUID.randomUUID();
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setAvistadorId(UUID.randomUUID().toString());
        dto.setDesaparecidoId(desaparecidoId.toString());
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        when(repo.findById(desaparecidoId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.DESAPARECIDO_NOT_FOUND.key,
                        AvistamientoError.DESAPARECIDO_NOT_FOUND.status
                );

        verify(repo).findById(desaparecidoId);
    }

    @Test
    @DisplayName("Debe pasar cuando el desaparecido existe en la BD")
    void debePermitirDesaparecidoExiste() {
        // Given
        UUID desaparecidoId = UUID.randomUUID();
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setAvistadorId(UUID.randomUUID().toString());
        dto.setDesaparecidoId(desaparecidoId.toString());
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        Desaparecido desaparecido = new Desaparecido();
        desaparecido.setId(desaparecidoId);
        desaparecido.setNombre("María");
        desaparecido.setApellido("González");

        when(repo.findById(desaparecidoId)).thenReturn(Optional.of(desaparecido));

        // When & Then
        assertThatCode(() -> rule.check(dto))
                .doesNotThrowAnyException();

        verify(repo).findById(desaparecidoId);
    }

    @Test
    @DisplayName("Debe validar con UUID en formato correcto")
    void debeValidarConUUIDCorrecto() {
        // Given
        UUID desaparecidoId = UUID.fromString("660e8400-e29b-41d4-a716-446655440001");
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setAvistadorId(UUID.randomUUID().toString());
        dto.setDesaparecidoId(desaparecidoId.toString());
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        Desaparecido desaparecido = new Desaparecido();
        desaparecido.setId(desaparecidoId);

        when(repo.findById(desaparecidoId)).thenReturn(Optional.of(desaparecido));

        // When & Then
        assertThatCode(() -> rule.check(dto))
                .doesNotThrowAnyException();

        verify(repo).findById(desaparecidoId);
    }
}
