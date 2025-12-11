package edu.utn.proyecto.common.validation.concreta.token.rules;
import edu.utn.proyecto.auth.exception.TokenError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.TokenRequestDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenValidRuleTest {

    private static final String SECRET = "CAMBIA_ESTE_SECRETO_LARGO_DEMO_32+_CHARS";

    private static void injectSecret(TokenValidRule rule, String secret) throws Exception {
        Field f = TokenValidRule.class.getDeclaredField("SECRET");
        f.setAccessible(true);
        f.set(rule, secret);
    }

    @Test
    void tokenValido_seteaClaims_ok() throws Exception {
        var rule = new TokenValidRule();
        injectSecret(rule, SECRET);

        var key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder()
                .setSubject("12345678")
                .claim("id", UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .signWith(key)
                .compact();

        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        when(dto.getToken()).thenReturn(jwt);

        assertDoesNotThrow(() -> rule.check(dto));
        verify(dto).setClaims(any(Claims.class));
    }

    @Test
    void tokenExpirado_lanzaExpired() throws Exception {
        var rule = new TokenValidRule();
        injectSecret(rule, SECRET);

        var key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder()
                .setSubject("12345678")
                .claim("id", UUID.randomUUID().toString())
                .setIssuedAt(Date.from(Instant.now().minusSeconds(3600)))
                .setExpiration(Date.from(Instant.now().minusSeconds(10)))
                .signWith(key)
                .compact();

        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        when(dto.getToken()).thenReturn(jwt);

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(TokenError.EXPIRED.key, ex.getKey());
        verify(dto, never()).setClaims(any());
    }

    @Test
    void tokenFirmaInvalida_lanzaInvalid() throws Exception {
        var rule = new TokenValidRule();
        injectSecret(rule, SECRET); // el validador usa SECRET

        // Firmamos con otra clave diferente
        String otherSecret = "OTRO_SECRETO_DISTINTO_MAS_DE_32_CHARS_X";
        var otherKey = Keys.hmacShaKeyFor(otherSecret.getBytes(StandardCharsets.UTF_8));
        String jwt = Jwts.builder()
                .setSubject("12345678")
                .claim("id", UUID.randomUUID().toString())
                .signWith(otherKey)
                .compact();

        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        when(dto.getToken()).thenReturn(jwt);

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(TokenError.INVALID.key, ex.getKey());
        verify(dto, never()).setClaims(any());
    }

    @Test
    void tokenMalFormado_lanzaInvalid() throws Exception {
        var rule = new TokenValidRule();
        injectSecret(rule, SECRET);

        TokenRequestDTO dto = mock(TokenRequestDTO.class);
        when(dto.getToken()).thenReturn("no-es-un-jwt");

        DomainException ex = assertThrows(DomainException.class, () -> rule.check(dto));
        assertEquals(TokenError.INVALID.key, ex.getKey());
        verify(dto, never()).setClaims(any());
    }
}
