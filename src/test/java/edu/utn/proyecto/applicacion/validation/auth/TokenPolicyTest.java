package edu.utn.proyecto.applicacion.validation.auth;
import edu.utn.proyecto.common.validation.abstraccion.Rule;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.TokenRequestDTO;
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
class TokenPolicyTest {

    @Mock private Rule<TokenRequestDTO> r1;
    @Mock private Rule<TokenRequestDTO> r2;

    @Test
    @DisplayName("validate: ejecuta reglas en orden")
    void validate_callsRules() {
        TokenRequestDTO dto = new TokenRequestDTO();
        TokenPolicy policy = new TokenPolicy(List.of(r1, r2));

        policy.validate(dto);

        InOrder order = inOrder(r1, r2);
        order.verify(r1).check(dto);
        order.verify(r2).check(dto);
        verifyNoMoreInteractions(r1, r2);
    }

    @Test
    @DisplayName("validate: propaga excepción y detiene cadena")
    void validate_propagatesAndStops() {
        TokenRequestDTO dto = new TokenRequestDTO();
        doThrow(new IllegalStateException("x")).when(r1).check(dto);

        TokenPolicy policy = new TokenPolicy(List.of(r1, r2));

        assertThrows(IllegalStateException.class, () -> policy.validate(dto));
        verify(r1).check(dto);
        verify(r2, never()).check(any());
    }

    @Test
    @DisplayName("validate: lista vacía")
    void validate_emptyRules() {
        TokenPolicy policy = new TokenPolicy(List.of());
        policy.validate(new TokenRequestDTO());
        verifyNoInteractions(r1, r2);
    }
}
