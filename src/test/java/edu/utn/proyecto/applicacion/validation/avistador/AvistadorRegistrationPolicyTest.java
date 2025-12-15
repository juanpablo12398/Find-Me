package edu.utn.proyecto.applicacion.validation.avistador;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
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
class AvistadorRegistrationPolicyTest {

    @Mock private IRepoDeRenaper renaper;
    @Mock private IRepoDeAvistadores repo;
    @Mock private Rule<AvistadorRequestDTO> r1;
    @Mock private Rule<AvistadorRequestDTO> r2;

    @Test
    @DisplayName("validate: ejecuta reglas en orden y no usa repos")
    void validate_callsRules() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        AvistadorRegistrationPolicy policy =
                new AvistadorRegistrationPolicy(renaper, repo, List.of(r1, r2));

        policy.validate(dto);

        InOrder order = inOrder(r1, r2);
        order.verify(r1).check(dto);
        order.verify(r2).check(dto);
        verifyNoMoreInteractions(r1, r2);
        verifyNoInteractions(renaper, repo);
    }

    @Test
    @DisplayName("validate: propaga excepción y detiene cadena")
    void validate_propagatesAndStops() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        doThrow(new RuntimeException("boom")).when(r1).check(dto);

        AvistadorRegistrationPolicy policy =
                new AvistadorRegistrationPolicy(renaper, repo, List.of(r1, r2));

        assertThrows(RuntimeException.class, () -> policy.validate(dto));
        verify(r1).check(dto);
        verify(r2, never()).check(any());
        verifyNoInteractions(renaper, repo);
    }

    @Test
    @DisplayName("validate: lista vacía")
    void validate_emptyRules() {
        AvistadorRegistrationPolicy policy =
                new AvistadorRegistrationPolicy(renaper, repo, List.of());
        policy.validate(new AvistadorRequestDTO());
        verifyNoInteractions(r1, r2, renaper, repo);
    }
}