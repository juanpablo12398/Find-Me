package edu.utn.proyecto.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class JwtVerifierTest {

    private static final String SECRET = "01234567890123456789012345678901"; // 32+

    @Test
    void parse_valid_token_returns_claims() {
        // build token with same secret
        String token = Jwts.builder()
                .setSubject("123")
                .claim("email", "a@b.com")
                .setIssuedAt(new Date())
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();

        JwtVerifier verifier = new JwtVerifier(SECRET);

        Claims claims = verifier.parse(token);

        assertEquals("123", claims.getSubject());
        assertEquals("a@b.com", claims.get("email"));
    }
}
