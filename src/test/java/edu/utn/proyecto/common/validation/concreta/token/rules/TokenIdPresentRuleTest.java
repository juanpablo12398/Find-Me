package edu.utn.proyecto.common.validation.concreta.token.rules;
import edu.utn.proyecto.auth.exception.TokenError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.TokenRequestDTO;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenIdPresentRuleTest {

    @Test
    void idNullOBlank_lanza() {
        var rule = new TokenIdPresentRule();

        TokenRequestDTO dto1 = mock(TokenRequestDTO.class);
        Claims claims1 = mock(Claims.class);
        when(dto1.getClaims()).thenReturn(claims1);
        when(claims1.get("id", String.class)).thenReturn(null);
        DomainException ex1 = assertThrows(DomainException.class, () -> rule.check(dto1));
        assertEquals(TokenError.DATA_MISSING.key, ex1.getKey());

        TokenRequestDTO dto2 = mock(TokenRequestDTO.class);
        Claims claims2 = mock(Claims.class);
        when(dto2.getClaims()).thenReturn(claims2);
        when(claims2.get("id", String.class)).thenReturn("   ");
        DomainException ex2 = assertThrows(DomainException.class, () -> rule.check(dto2));
        assertEquals(TokenError.DATA_MISSING.key, ex2.getKey());
    }

    @Test
    void idPresente_ok() {
        var rule = new TokenIdPresentRule();
        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        Claims claims = mock(Claims.class);
        when(dto.getClaims()).thenReturn(claims);
        when(claims.get("id", String.class)).thenReturn("0c9a8f0c-7b2f-4a61-9c40-7d9a2a9b1a10");

        assertDoesNotThrow(() -> rule.check(dto));
    }
}
