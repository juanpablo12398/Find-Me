package edu.utn.proyecto.common.validation.concreta;
import edu.utn.proyecto.avistador.exception.AvistadorError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.concreta.avistador.rules.AvistadorDniDuplicadoRule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
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
@DisplayName("AvistadorDniDuplicadoRule - Tests")
class AvistadorDniDuplicadoRuleTest {

    @Mock private IRepoDeAvistadores repo;
    @InjectMocks private AvistadorDniDuplicadoRule rule;

    @Test
    @DisplayName("Debe lanzar excepción cuando el DNI ya está registrado")
    void debeRechazarDniDuplicado() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni("12345678");
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setEdad(25);
        dto.setDireccion("Calle 1 123");

        when(repo.existsByDni("12345678")).thenReturn(true);

        assertThatThrownBy(() -> rule.check(dto))
                .isInstanceOf(DomainException.class)
                .extracting("key", "status")
                .containsExactly(
                        AvistadorError.DNI_DUP.key,
                        AvistadorError.DNI_DUP.status
                );

        verify(repo).existsByDni("12345678");
    }

    @Test
    @DisplayName("Debe pasar cuando el DNI NO está duplicado")
    void debePermitirDniNoDuplicado() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni("87654321");
        dto.setNombre("María");
        dto.setApellido("González");
        dto.setEdad(30);
        dto.setDireccion("Siempre Viva 742");

        when(repo.existsByDni("87654321")).thenReturn(false);

        assertThatCode(() -> rule.check(dto)).doesNotThrowAnyException();

        verify(repo).existsByDni("87654321");
    }

    @Test
    @DisplayName("Debe pasar cuando el DNI es null (otra rule valida esto)")
    void debePermitirDniNull() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni(null);
        dto.setNombre("Pedro");
        dto.setApellido("Martínez");
        dto.setEdad(28);
        dto.setDireccion("Calle Falsa 123");

        assertThatCode(() -> rule.check(dto)).doesNotThrowAnyException();

        verify(repo, never()).existsByDni(null);
    }

    @Test
    @DisplayName("Debe validar con DNI normalizado")
    void debeValidarConDniNormalizado() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni("12345678");
        dto.setNombre("Ana");
        dto.setApellido("López");
        dto.setEdad(26);
        dto.setDireccion("Av. Principal 1000");

        when(repo.existsByDni("12345678")).thenReturn(false);

        assertThatCode(() -> rule.check(dto)).doesNotThrowAnyException();

        verify(repo).existsByDni("12345678");
    }
}