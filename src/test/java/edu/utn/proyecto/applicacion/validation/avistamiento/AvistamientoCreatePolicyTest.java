package edu.utn.proyecto.applicacion.validation.avistamiento;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvistamientoCreatePolicyTest {

    @Mock private IRepoDeAvistadores repoAvistadores;
    @Mock private IRepoDeDesaparecidos repoDesaparecidos;
    @Mock private Rule<AvistamientoRequestDTO> r1;
    @Mock private Rule<AvistamientoRequestDTO> r2;

    @Test
    @DisplayName("validate: ejecuta reglas en orden y no usa repos")
    void validate_callsRules() {
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        AvistamientoCreatePolicy policy =
                new AvistamientoCreatePolicy(repoAvistadores, repoDesaparecidos, List.of(r1, r2));

        policy.validate(dto);

        InOrder order = inOrder(r1, r2);
        order.verify(r1).check(dto);
        order.verify(r2).check(dto);
        verifyNoMoreInteractions(r1, r2);
        verifyNoInteractions(repoAvistadores, repoDesaparecidos);
    }

    @Test
    @DisplayName("validate: propaga excepción y detiene cadena")
    void validate_propagatesAndStops() {
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        doThrow(new IllegalStateException("x")).when(r1).check(dto);

        AvistamientoCreatePolicy policy =
                new AvistamientoCreatePolicy(repoAvistadores, repoDesaparecidos, List.of(r1, r2));

        assertThrows(IllegalStateException.class, () -> policy.validate(dto));
        verify(r1).check(dto);
        verify(r2, never()).check(any());
        verifyNoInteractions(repoAvistadores, repoDesaparecidos);
    }

    @Test
    @DisplayName("validate: lista vacía")
    void validate_emptyRules() {
        AvistamientoCreatePolicy policy =
                new AvistamientoCreatePolicy(repoAvistadores, repoDesaparecidos, List.of());
        policy.validate(new AvistamientoRequestDTO());
        verifyNoInteractions(r1, r2, repoAvistadores, repoDesaparecidos);
    }
}
