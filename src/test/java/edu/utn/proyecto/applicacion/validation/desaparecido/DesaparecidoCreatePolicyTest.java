package edu.utn.proyecto.applicacion.validation.desaparecido;
import edu.utn.proyecto.applicacion.validation.desaparecido.DesaparecidoCreatePolicy;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.desaparecido.exception.DesaparecidoError;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesaparecidoCreatePolicyTest {

    @Mock IRepoDeDesaparecidos repo;
    @Mock Rule<DesaparecidoRequestDTO> rule1;
    @Mock Rule<DesaparecidoRequestDTO> rule2;

    DesaparecidoCreatePolicy policy;

    @BeforeEach
    void setUp() {
        policy = new DesaparecidoCreatePolicy(repo, List.of(rule1, rule2));
    }

    @Test
    void happyPath_ejecuta_reglas() {
        var dto = new DesaparecidoRequestDTO("ANA", "PEREZ", 30, "12345678", "http://x",
                "descripcion suficientemente larga...");

        policy.validate(dto);

        verify(rule1).check(dto);
        verify(rule2).check(dto);
        verifyNoInteractions(repo);
    }

    @Test
    void propaga_DESC_SHORT_si_regla_falla() {
        var dto = new DesaparecidoRequestDTO("ANA", "PEREZ", 30, "12345678", null, "desc muy corta");

        doThrow(DomainException.of(DesaparecidoError.DESC_SHORT.key, DesaparecidoError.DESC_SHORT.status, ""))
                .when(rule1).check(dto);

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> {
                    var de = (DomainException) ex;
                    org.assertj.core.api.Assertions.assertThat(de.getKey())
                            .isEqualTo(DesaparecidoError.DESC_SHORT.key);
                });

        verify(rule1).check(dto);
        verify(rule2, never()).check(dto);
        verifyNoInteractions(repo);
    }

    @Test
    void propaga_DNI_DUP_si_regla_falla() {
        var dto = new DesaparecidoRequestDTO("ANA", "PEREZ", 30, "12345678", "http://x",
                "descripcion suficientemente larga...");

        doThrow(DomainException.of(DesaparecidoError.DNI_DUP.key, DesaparecidoError.DNI_DUP.status, ""))
                .when(rule2).check(dto);

        assertThatThrownBy(() -> policy.validate(dto))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> {
                    var de = (DomainException) ex;
                    org.assertj.core.api.Assertions.assertThat(de.getKey())
                            .isEqualTo(DesaparecidoError.DNI_DUP.key);
                });

        verify(rule1).check(dto);
        verify(rule2).check(dto);
    }
}