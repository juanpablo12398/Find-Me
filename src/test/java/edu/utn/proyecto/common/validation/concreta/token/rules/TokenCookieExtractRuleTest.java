package edu.utn.proyecto.common.validation.concreta.token.rules;
import edu.utn.proyecto.auth.exception.TokenError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.TokenRequestDTO;
import edu.utn.proyecto.security.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenCookieExtractRuleTest {

    @Test
    void extraeToken_ok() {
        var rule = new TokenCookieExtractRule();

        Cookie good = new Cookie(TokenService.COOKIE_NAME, "jwt-123");
        Cookie other = new Cookie("other", "x");
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getCookies()).thenReturn(new Cookie[]{other, good});

        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        when(dto.getRequest()).thenReturn(req);

        assertDoesNotThrow(() -> rule.check(dto));
        verify(dto).setToken("jwt-123");
    }

    @Test
    void sinCookieConNombre_esperaExcepcion() {
        var rule = new TokenCookieExtractRule();

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getCookies()).thenReturn(new Cookie[]{ new Cookie("other","x") });

        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        when(dto.getRequest()).thenReturn(req);

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(TokenError.NOT_FOUND.key, ex.getKey());
        verify(dto, never()).setToken(any());
    }
}
