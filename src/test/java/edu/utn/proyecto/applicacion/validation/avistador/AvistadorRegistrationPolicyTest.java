package edu.utn.proyecto.applicacion.validation.avistador;
import edu.utn.proyecto.avistador.exception.AvistadorError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvistadorRegistrationPolicyTest {

    @Mock IRepoDeRenaper renaper;
    @Mock IRepoDeAvistadores repo;
    @Mock Rule<AvistadorRequestDTO> rule1;
    @Mock Rule<AvistadorRequestDTO> rule2;

    AvistadorRegistrationPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new AvistadorRegistrationPolicy(renaper, repo, List.of(rule1, rule2));
    }

    @Test
    void happyPath_ejecuta_reglas() {
        var dto = buildDto();

        policy.validate(dto);

        verify(rule1).check(dto);
        verify(rule2).check(dto);
        verifyNoInteractions(renaper, repo);
    }

    @Test
    void propaga_PADRON_NOT_FOUND() {
        var dto = buildDto();
        doThrow(DomainException.of(AvistadorError.PADRON_NOT_FOUND.key, AvistadorError.PADRON_NOT_FOUND.status, ""))
                .when(rule1).check(dto);

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .extracting(e -> ((DomainException) e).getKey())
                .isEqualTo(AvistadorError.PADRON_NOT_FOUND.key);

        verify(rule1).check(dto);
        verify(rule2, never()).check(dto);
        verifyNoInteractions(renaper, repo);
    }

    @Test
    void propaga_PADRON_NO_MATCH() {
        var dto = buildDto();
        doThrow(DomainException.of(AvistadorError.PADRON_NO_MATCH.key, AvistadorError.PADRON_NO_MATCH.status, ""))
                .when(rule1).check(dto);

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .extracting(e -> ((DomainException) e).getKey())
                .isEqualTo(AvistadorError.PADRON_NO_MATCH.key);
    }

    @Test
    void propaga_DNI_DUP() {
        var dto = buildDto();
        doThrow(DomainException.of(AvistadorError.DNI_DUP.key, AvistadorError.DNI_DUP.status, ""))
                .when(rule2).check(dto);

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .extracting(e -> ((DomainException) e).getKey())
                .isEqualTo(AvistadorError.DNI_DUP.key);

        verify(rule1).check(dto); // pasó la primera
        verify(rule2).check(dto);
    }

    private AvistadorRequestDTO buildDto() {
        var dto = new AvistadorRequestDTO();
        dto.setDni("12345678");
        dto.setNombre("ANA");
        dto.setApellido("PEREZ");
        dto.setEdad(25);
        dto.setDireccion("Calle 123");
        dto.setEmail("a@a.com");
        dto.setTelefono("111");
        return dto;
    }
}