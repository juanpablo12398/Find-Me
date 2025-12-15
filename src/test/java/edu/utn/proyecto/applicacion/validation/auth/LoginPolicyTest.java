package edu.utn.proyecto.applicacion.validation.auth;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
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
class LoginPolicyTest {

    @Mock private IRepoDeRenaper renaper;
    @Mock private IRepoDeAvistadores repo;
    @Mock private Rule<LoginRequestDTO> r1;
    @Mock private Rule<LoginRequestDTO> r2;

    @Test
    @DisplayName("validate: ejecuta todas las reglas en orden")
    void validate_callsAllRulesInOrder() {
        LoginRequestDTO dto = new LoginRequestDTO();
        LoginPolicy policy = new LoginPolicy(renaper, repo, List.of(r1, r2));

        policy.validate(dto);

        InOrder order = inOrder(r1, r2);
        order.verify(r1).check(dto);
        order.verify(r2).check(dto);
        verifyNoMoreInteractions(r1, r2);
        verifyNoInteractions(renaper, repo);
    }

    @Test
    @DisplayName("validate: si una regla falla, se propaga y no ejecuta las siguientes")
    void validate_propagatesAndStops() {
        LoginRequestDTO dto = new LoginRequestDTO();
        RuntimeException boom = new RuntimeException("boom");
        doThrow(boom).when(r1).check(dto);

        LoginPolicy policy = new LoginPolicy(renaper, repo, List.of(r1, r2));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> policy.validate(dto));
        assertSame(boom, thrown);

        verify(r1).check(dto);
        verify(r2, never()).check(any());
        verifyNoInteractions(renaper, repo);
    }

    @Test
    @DisplayName("validate: sin reglas no hace nada")
    void validate_emptyRules() {
        LoginRequestDTO dto = new LoginRequestDTO();
        LoginPolicy policy = new LoginPolicy(renaper, repo, List.of());

        policy.validate(dto);

        verifyNoInteractions(r1, r2, renaper, repo);
    }
}
