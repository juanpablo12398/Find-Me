package edu.utn.proyecto.applicacion.validation.avistador;
import edu.utn.proyecto.avistador.exception.AvistadorError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvistadorRegistrationPolicyTest {

    @Mock IRepoDeRenaper renaper;
    @Mock IRepoDeAvistadores repo;
    @Mock Rule<AvistadorRequestDTO> rule1;
    @Mock Rule<AvistadorRequestDTO> rule2;

    DniNormalizer dniNorm = new DniNormalizer();
    TextNormalizer txtNorm = new TextNormalizer();

    AvistadorRegistrationPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new AvistadorRegistrationPolicy(renaper, repo, dniNorm, txtNorm, List.of(rule1, rule2));
    }

    @Test
    void happyPath_reglas_ok_normaliza_renaperOk_noDuplicado() {
        var dto = buildDto("12.345.678", "ána", "pérez");

        var persona = new RenaperPersonaEntity();
        persona.setDni("12345678");
        persona.setNombre("Ana");
        persona.setApellido("Perez");

        when(renaper.findByDni("12345678")).thenReturn(Optional.of(persona));
        when(repo.existsByDni("12345678")).thenReturn(false);

        policy.validate(dto);

        verify(rule1).check(dto);
        verify(rule2).check(dto);
        verify(renaper).findByDni("12345678");
        verify(repo).existsByDni("12345678");

        assertThat(dto.getDni()).isEqualTo("12345678");
        assertThat(dto.getNombre()).isEqualTo("ANA");
        assertThat(dto.getApellido()).isEqualTo("PEREZ");
    }

    @Test
    void lanzaNotFound_siNoExisteEnRenaper() {
        var dto = buildDto("12345678", "Ana", "Perez");
        when(renaper.findByDni("12345678")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .extracting(e -> ((DomainException)e).getKey())
                .isEqualTo(AvistadorError.PADRON_NOT_FOUND.key);
    }

    @Test
    void lanzaNoMatch_siNombreApellidoNoCoincidenConRenaper() {
        var dto = buildDto("12345678", "Ana", "Perez");
        var persona = new RenaperPersonaEntity();
        persona.setDni("12345678");
        persona.setNombre("Juana"); // no coincide
        persona.setApellido("Perez");

        when(renaper.findByDni("12345678")).thenReturn(Optional.of(persona));

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .extracting(e -> ((DomainException)e).getKey())
                .isEqualTo(AvistadorError.PADRON_NO_MATCH.key);
    }

    @Test
    void lanzaConflict_siDniDuplicado() {
        var dto = buildDto("12345678", "Ana", "Perez");
        var persona = new RenaperPersonaEntity();
        persona.setDni("12345678");
        persona.setNombre("Ana");
        persona.setApellido("Perez");

        when(renaper.findByDni("12345678")).thenReturn(Optional.of(persona));
        when(repo.existsByDni("12345678")).thenReturn(true);

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .extracting(e -> ((DomainException)e).getKey())
                .isEqualTo(AvistadorError.DNI_DUP.key);
    }

    @Test
    void propagaError_deReglaDeNegocio() {
        var dto = buildDto("12345678", "Ana", "Perez");
        doThrow(DomainException.of(AvistadorError.UNDERAGE.key, AvistadorError.UNDERAGE.status, ""))
                .when(rule1).check(dto);

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .extracting(e -> ((DomainException)e).getKey())
                .isEqualTo(AvistadorError.UNDERAGE.key);

        verifyNoInteractions(renaper, repo);
    }

    private AvistadorRequestDTO buildDto(String dni, String nombre, String apellido) {
        var dto = new AvistadorRequestDTO();
        dto.setDni(dni);
        dto.setNombre(nombre);
        dto.setApellido(apellido);
        dto.setEdad(25);
        dto.setDireccion("Calle 123");
        return dto;
    }
}
