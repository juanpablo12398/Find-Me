package edu.utn.proyecto.security;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {
    public static final String COOKIE_NAME = "FM_TOKEN";
    @Value("${jwt.secret:CAMBIA_ESTE_SECRETO_LARGO_DEMO_32+_CHARS}")
    private String SECRET;

    public String generate(String avistadorId, String dni, String email, String nombre) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(7 * 24 * 3600); // 7 días

        return Jwts.builder()
                .setSubject(dni)
                .claim("id", avistadorId)
                .claim("email", email)
                .claim("name", nombre)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public void writeCookie(HttpServletResponse resp, String jwt) {
        Cookie c = new Cookie(COOKIE_NAME, jwt);
        c.setHttpOnly(true);
        c.setPath("/");
        c.setMaxAge(7 * 24 * 3600);  // 7 días
        c.setSecure(false);  // Cambiar a true en producción con HTTPS
        c.setAttribute("SameSite", "Lax");  // Protección CSRF
        resp.addCookie(c);
    }

    public void clearCookie(HttpServletResponse resp) {
        Cookie c = new Cookie(COOKIE_NAME, "");
        c.setHttpOnly(true);
        c.setPath("/");
        c.setMaxAge(0);
        resp.addCookie(c);
    }

    public String secret() {
        return SECRET;
    }
}
