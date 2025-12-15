package edu.utn.proyecto.security;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtCookieFilterTest {

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void when_valid_cookie_sets_auth_and_continues_chain() throws ServletException, IOException {
        String token = "jwt-token";
        var claims = new DefaultClaims();
        claims.setSubject("321");

        JwtVerifier verifier = mock(JwtVerifier.class);
        when(verifier.parse(token)).thenReturn(claims);

        JwtCookieFilter filter = new JwtCookieFilter(verifier);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setCookies(new Cookie(TokenService.COOKIE_NAME, token));
        MockHttpServletResponse res = new MockHttpServletResponse();

        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(req, res, chain);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("321", auth.getPrincipal());
        assertEquals(token,  auth.getCredentials());
        assertTrue(auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(chain).doFilter(req, res);
    }

    @Test
    void when_parse_throws_swallow_and_continue_without_auth() throws ServletException, IOException {
        JwtVerifier verifier = mock(JwtVerifier.class);
        when(verifier.parse("bad")).thenThrow(new RuntimeException("boom"));

        JwtCookieFilter filter = new JwtCookieFilter(verifier);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setCookies(new Cookie(TokenService.COOKIE_NAME, "bad"));
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(req, res, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(req, res);
    }

    @Test
    void when_no_cookie_leaves_context_empty_and_continues() throws ServletException, IOException {
        JwtVerifier verifier = mock(JwtVerifier.class);
        JwtCookieFilter filter = new JwtCookieFilter(verifier);

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(req, res, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(req, res);
        verifyNoInteractions(verifier);
    }
}