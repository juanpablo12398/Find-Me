package edu.utn.proyecto.applicacion.validation.desaparecido;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.desaparecido.exception.DesaparecidoError;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import edu.utn.proyecto.applicacion.validation.desaparecido.DesaparecidoCreatePolicy;

@ExtendWith(MockitoExtension.class)
class DesaparecidoCreatePolicyTest {

    @Mock IRepoDeDesaparecidos repo;
    @Mock Rule<DesaparecidoRequestDTO> rule1;
    @Mock Rule<DesaparecidoRequestDTO> rule2;

    DniNormalizer dniNorm = new DniNormalizer();
    TextNormalizer txtNorm = new TextNormalizer();

    DesaparecidoCreatePolicy policy;

    @BeforeEach
    void setUp() {
        policy = new DesaparecidoCreatePolicy(repo, dniNorm, txtNorm, List.of(rule1, rule2));
    }

    @Test
    void happyPath_ejecutaReglas_normaliza_y_noDuplica() {
        var dto = new DesaparecidoRequestDTO("ána", "pérez", 30, "12.345.678", "http://x", "descripcion suficientemente larga...");
        when(repo.existsByDni("12345678")).thenReturn(false);

        policy.validate(dto);

        // Reglas fueron chequeadas
        verify(rule1).check(dto);
        verify(rule2).check(dto);

        // Normalizaciones aplicadas
        assertThat(dto.getDni()).isEqualTo("12345678");
        assertThat(dto.getNombre()).isEqualTo("ANA");
        assertThat(dto.getApellido()).isEqualTo("PEREZ");

        // No se lanzó excepción
        verify(repo).existsByDni("12345678");
    }

    @Test
    void lanzaExcepcion_siAlgunaReglaFalla() {
        var dto = new DesaparecidoRequestDTO("Juan", "Lopez", 20, "12345678", null, "corta");
        doThrow(DomainException.of(DesaparecidoError.DESC_SHORT.key, DesaparecidoError.DESC_SHORT.status, ""))
                .when(rule1).check(dto);

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .extracting(e -> ((DomainException)e).getKey())
                .isEqualTo(DesaparecidoError.DESC_SHORT.key);

        verify(rule1).check(dto);
        verify(rule2, never()).check(dto); // corta la validación si falla la primera
        verifyNoInteractions(repo);
    }

    @Test
    void lanzaConflict_siDniDuplicado() {
        var dto = new DesaparecidoRequestDTO("Ana", "Perez", 30, "12.345.678", "http://x", "descripcion suficientemente larga...");
        when(repo.existsByDni("12345678")).thenReturn(true);

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> {
                    DomainException de = (DomainException) ex;
                    assertThat(de.getKey()).isEqualTo(DesaparecidoError.DNI_DUP.key);
                });

        verify(repo).existsByDni("12345678");
    }
}