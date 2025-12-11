package edu.utn.proyecto.common.validation.concreta.token.rules;
import edu.utn.proyecto.auth.exception.TokenError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.TokenRequestDTO;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenClaimsPresentRuleTest {

    @Test
    void claimsNull_lanza() {
        var rule = new TokenClaimsPresentRule();
        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        when(dto.getClaims()).thenReturn(null);

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(TokenError.DATA_MISSING.key, ex.getKey());
    }

    @Test
    void claimsOk_pasa() {
        var rule = new TokenClaimsPresentRule();
        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        when(dto.getClaims()).thenReturn(mock(Claims.class));

        assertDoesNotThrow(() -> rule.check(dto));
    }
}
