package edu.utn.proyecto.common.validation.concreta.token.rules;
import edu.utn.proyecto.auth.exception.TokenError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.TokenRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenRequestPresentRuleTest {

    @Test
    void requestNull_lanza() {
        var rule = new TokenRequestPresentRule();
        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        when(dto.getRequest()).thenReturn(null);

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(TokenError.NOT_FOUND.key, ex.getKey());
    }

    @Test
    void requestPresente_ok() {
        var rule = new TokenRequestPresentRule();
        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        when(dto.getRequest()).thenReturn(mock(HttpServletRequest.class));

        assertDoesNotThrow(() -> rule.check(dto));
    }
}
