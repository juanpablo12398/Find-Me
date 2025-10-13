package edu.utn.proyecto.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    private TokenService service;
    private static final String SECRET_OK = "THIS_IS_A_DEMO_SECRET_32+_BYTES_123456";
    private static final String SECRET_SHORT = "short-secret";

    @BeforeEach
    void setUp() {
        service = new TokenService();
        ReflectionTestUtils.setField(service, "SECRET", SECRET_OK);
    }

    @Test
    void generate_incluyeClaims_yExpiraEn7Dias_yFirmaConSecret() {
        String dni = "12345678";
        String email = "user@mail.com";
        String nombre = "Juan Pérez";

        Instant t0 = Instant.now();
        String jwt = service.generate(dni, email, nombre);
        Instant t1 = Instant.now();

        var key = Keys.hmacShaKeyFor(SECRET_OK.getBytes(StandardCharsets.UTF_8));

        // ✅ Parser para jjwt 0.12+
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        // subject y claims
        assertThat(claims.getSubject()).isEqualTo(dni);
        assertThat(claims.get("email", String.class)).isEqualTo(email);
        assertThat(claims.get("name", String.class)).isEqualTo(nombre);

        // iat / exp (7 días)
        Date iat = claims.getIssuedAt();
        Date exp = claims.getExpiration();
        assertThat(iat).isNotNull();
        assertThat(exp).isNotNull();

        // un poquito de tolerancia por relojes/precisión
        assertThat(iat.toInstant())
                .isBetween(t0.minusSeconds(1), t1.plusSeconds(1));

        var sevenDays = Duration.ofDays(7);
        Instant esperado = iat.toInstant().plus(sevenDays);

        // tolerancia 5s
        assertThat(exp.toInstant()).isCloseTo(esperado, within(5, ChronoUnit.SECONDS));
    }

    @Test
    void writeCookie_seteaFlagsEsperados() {
        HttpServletResponse resp = mock(HttpServletResponse.class);
        var cap = org.mockito.ArgumentCaptor.forClass(Cookie.class);

        String token = "abc.def.ghi";
        service.writeCookie(resp, token);

        verify(resp).addCookie(cap.capture());
        Cookie c = cap.getValue();

        assertThat(c.getName()).isEqualTo(TokenService.COOKIE_NAME);
        assertThat(c.getValue()).isEqualTo(token);
        assertThat(c.isHttpOnly()).isTrue();
        assertThat(c.getSecure()).isFalse();
        assertThat(c.getPath()).isEqualTo("/");
        assertThat(c.getMaxAge()).isEqualTo(7 * 24 * 3600);
        // Servlet 6 (Jakarta)
        assertThat(c.getAttribute("SameSite")).isEqualTo("Lax");
    }

    @Test
    void clearCookie_borraCookieConMaxAgeCero() {
        HttpServletResponse resp = mock(HttpServletResponse.class);
        var cap = org.mockito.ArgumentCaptor.forClass(Cookie.class);

        service.clearCookie(resp);

        verify(resp).addCookie(cap.capture());
        Cookie c = cap.getValue();

        assertThat(c.getName()).isEqualTo(TokenService.COOKIE_NAME);
        assertThat(c.getValue()).isEmpty();
        assertThat(c.isHttpOnly()).isTrue();
        assertThat(c.getPath()).isEqualTo("/");
        assertThat(c.getMaxAge()).isZero();
    }

    @Test
    void secret_devuelveElValorInyectado() {
        assertThat(service.secret()).isEqualTo(SECRET_OK);
    }

    @Test
    void generate_conSecretDemasiadoCorto_lanzaWeakKeyException() {
        ReflectionTestUtils.setField(service, "SECRET", SECRET_SHORT);

        assertThatThrownBy(() -> service.generate("1", "a@a.com", "x"))
                .isInstanceOf(WeakKeyException.class); // ✅ en 0.11.x también es WeakKeyException
    }
}
