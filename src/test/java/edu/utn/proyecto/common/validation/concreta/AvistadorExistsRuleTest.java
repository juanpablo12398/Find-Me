package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.avistamiento.exception.AvistamientoError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.concreta.avistamiento.rules.AvistadorExistsRule;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.AvistadorEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
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
@DisplayName("AvistadorExistsRule (Avistamiento) - Tests")
class AvistadorExistsRuleTest {

    @Mock
    private IRepoDeAvistadores repo;

    @InjectMocks
    private AvistadorExistsRule rule;

    @Test
    @DisplayName("Debe rechazar cuando avistadorId es null")
    void debeRechazarAvistadorIdNull() {
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setAvistadorId(null);
        dto.setDesaparecidoId(UUID.randomUUID().toString());
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.AVISTADOR_NOT_FOUND.key,
                        AvistamientoError.AVISTADOR_NOT_FOUND.status
                );
    }

    @Test
    @DisplayName("Debe rechazar cuando el avistador NO existe en la BD")
    void debeRechazarAvistadorNoExiste() {
        UUID avistadorId = UUID.randomUUID();
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setAvistadorId(avistadorId.toString());
        dto.setDesaparecidoId(UUID.randomUUID().toString());
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        when(repo.findById(avistadorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistamientoError.AVISTADOR_NOT_FOUND.key,
                        AvistamientoError.AVISTADOR_NOT_FOUND.status
                );

        verify(repo).findById(avistadorId);
    }

    @Test
    @DisplayName("Debe pasar cuando el avistador existe en la BD")
    void debePermitirAvistadorExiste() {
        UUID avistadorId = UUID.randomUUID();
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setAvistadorId(avistadorId.toString());
        dto.setDesaparecidoId(UUID.randomUUID().toString());
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        // Usa AvistadorEntity (infra), no el domain model
        AvistadorEntity entity = new AvistadorEntity();
        entity.setId(avistadorId);
        entity.setNombre("Juan");
        entity.setApellido("Pérez");

        when(repo.findById(avistadorId)).thenReturn(Optional.of(entity));

        assertThatCode(() -> rule.check(dto)).doesNotThrowAnyException();

        verify(repo).findById(avistadorId);
    }

    @Test
    @DisplayName("Debe validar con UUID en formato correcto")
    void debeValidarConUUIDCorrecto() {
        UUID avistadorId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setAvistadorId(avistadorId.toString());
        dto.setDesaparecidoId(UUID.randomUUID().toString());
        dto.setLatitud(-34.6037);
        dto.setLongitud(-58.3816);
        dto.setDescripcion("Descripción de más de 20 caracteres");

        AvistadorEntity entity = new AvistadorEntity();
        entity.setId(avistadorId);

        when(repo.findById(avistadorId)).thenReturn(Optional.of(entity));

        assertThatCode(() -> rule.check(dto)).doesNotThrowAnyException();

        verify(repo).findById(avistadorId);
    }
}
