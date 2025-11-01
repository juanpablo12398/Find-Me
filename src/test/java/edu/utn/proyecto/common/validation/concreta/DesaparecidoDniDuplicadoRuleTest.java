package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.concreta.desaparecido.rules.DesaparecidoDniDuplicadoRule;
import edu.utn.proyecto.desaparecido.exception.DesaparecidoError;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("DesaparecidoDniDuplicadoRule - Tests")
class DesaparecidoDniDuplicadoRuleTest {

    @Mock
    private IRepoDeDesaparecidos repo;

    @InjectMocks
    private DesaparecidoDniDuplicadoRule rule;

    private DesaparecidoRequestDTO mkDto(
            String dni, String nombre, String apellido, int edad, String descripcion, String fotoUrl
    ) {
        // Tenés un constructor completo en el DTO
        return new DesaparecidoRequestDTO(nombre, apellido, edad, dni, fotoUrl, descripcion);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el DNI ya está registrado")
    void debeRechazarDniDuplicado() {
        DesaparecidoRequestDTO dto = mkDto(
                "12345678", "Juan", "Pérez", 25, "Descripción de más de 20 caracteres", "foto"
        );

        when(repo.existsByDni("12345678")).thenReturn(true);

        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        DesaparecidoError.DNI_DUP.key,
                        DesaparecidoError.DNI_DUP.status
                );

        verify(repo).existsByDni("12345678");
    }

    @Test
    @DisplayName("Debe pasar cuando el DNI NO está duplicado")
    void debePermitirDniNoDuplicado() {
        DesaparecidoRequestDTO dto = mkDto(
                "87654321", "María", "González", 30, "Descripción de más de 20 caracteres", "foto"
        );

        when(repo.existsByDni("87654321")).thenReturn(false);

        assertThatCode(() -> rule.check(dto)).doesNotThrowAnyException();

        verify(repo).existsByDni("87654321");
    }

    @Test
    @DisplayName("Debe pasar cuando el DNI es null (otra rule valida esto)")
    void debePermitirDniNull() {
        DesaparecidoRequestDTO dto = mkDto(
                null, "Pedro", "Martínez", 28, "Descripción de más de 20 caracteres", "foto"
        );

        assertThatCode(() -> rule.check(dto)).doesNotThrowAnyException();

        verify(repo, never()).existsByDni(null);
    }
}
