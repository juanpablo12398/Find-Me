package edu.utn.proyecto.common.validation.concreta.token.rules;
import edu.utn.proyecto.auth.exception.TokenError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.TokenRequestDTO;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenIdUuidRuleTest {

    @Test
    void idValidoUUID_ok() {
        var rule = new TokenIdUuidRule();
        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        Claims claims = mock(Claims.class);
        when(dto.getClaims()).thenReturn(claims);
        when(claims.get("id", String.class)).thenReturn(UUID.randomUUID().toString());

        assertDoesNotThrow(() -> rule.check(dto));
    }

    @Test
    void idInvalido_lanzaMalformed() {
        var rule = new TokenIdUuidRule();
        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        Claims claims = mock(Claims.class);
        when(dto.getClaims()).thenReturn(claims);
        when(claims.get("id", String.class)).thenReturn("not-a-uuid");

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(TokenError.MALFORMED.key, ex.getKey());
    }
}
