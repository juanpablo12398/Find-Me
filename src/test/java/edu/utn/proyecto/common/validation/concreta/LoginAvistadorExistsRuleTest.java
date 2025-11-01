package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.auth.exception.AuthError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.concreta.login.rules.LoginAvistadorExistsRule;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
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
@DisplayName("LoginAvistadorExistsRule - Tests")
class LoginAvistadorExistsRuleTest {

    @Mock
    private IRepoDeAvistadores repo;

    @InjectMocks
    private LoginAvistadorExistsRule rule;

    @Test
    @DisplayName("Debe lanzar excepción cuando el avistador NO existe")
    void debeRechazarAvistadorNoExiste() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setDni("99999999");
        dto.setEmail("juan@example.com");

        when(repo.findByDni("99999999")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AuthError.USER_NOT_FOUND.key,
                        AuthError.USER_NOT_FOUND.status
                );

        verify(repo).findByDni("99999999");
    }

    @Test
    @DisplayName("Debe pasar cuando el avistador existe")
    void debePermitirAvistadorExiste() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setDni("12345678");
        dto.setEmail("juan@example.com");

        Avistador avistador = new Avistador();
        avistador.setId(UUID.randomUUID());
        avistador.setDni("12345678");
        avistador.setNombre("Juan");
        avistador.setApellido("Pérez");
        avistador.setEmail("juan@example.com");

        when(repo.findByDni("12345678")).thenReturn(Optional.of(avistador));

        // When & Then
        assertThatCode(() -> rule.check(dto))
                .doesNotThrowAnyException();

        verify(repo).findByDni("12345678");
    }

    @Test
    @DisplayName("Debe validar con DNI normalizado")
    void debeValidarConDniNormalizado() {
        // Given
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setDni("87654321");
        dto.setEmail("maria@example.com");

        Avistador avistador = new Avistador();
        avistador.setId(UUID.randomUUID());
        avistador.setDni("87654321");
        avistador.setNombre("María");
        avistador.setApellido("González");
        avistador.setEmail("maria@example.com");

        when(repo.findByDni("87654321")).thenReturn(Optional.of(avistador));

        // When & Then
        assertThatCode(() -> rule.check(dto))
                .doesNotThrowAnyException();

        verify(repo).findByDni("87654321");
    }
}
