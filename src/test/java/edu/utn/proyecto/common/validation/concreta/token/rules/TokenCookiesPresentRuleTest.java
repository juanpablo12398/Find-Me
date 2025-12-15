package edu.utn.proyecto.common.validation.concreta.token.rules;
import edu.utn.proyecto.auth.exception.TokenError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.TokenRequestDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenCookiesPresentRuleTest {

    @Test
    void cookiesNull_lanza() {
        var rule = new TokenCookiesPresentRule();

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getCookies()).thenReturn(null);

        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        when(dto.getRequest()).thenReturn(req);

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(TokenError.NOT_FOUND.key, ex.getKey());
    }

    @Test
    void cookiesPresentes_pasa() {
        var rule = new TokenCookiesPresentRule();

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getCookies()).thenReturn(new Cookie[]{ new Cookie("a","b") });

        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        when(dto.getRequest()).thenReturn(req);

        assertDoesNotThrow(() -> rule.check(dto));
    }
}
