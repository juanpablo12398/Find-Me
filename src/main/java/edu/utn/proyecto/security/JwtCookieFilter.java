package edu.utn.proyecto.security;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JwtCookieFilter extends OncePerRequestFilter {
    private final String secret;

    public JwtCookieFilter(String secret) {
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (TokenService.COOKIE_NAME.equals(c.getName())) {
                    try {
                        var claims = Jwts.parserBuilder()
                                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                                .build()
                                .parseClaimsJws(c.getValue())
                                .getBody();

                        String dni = claims.getSubject();
                        if (dni != null) {
                            var auth = new AbstractAuthenticationToken(
                                    List.of(new SimpleGrantedAuthority("ROLE_USER"))) {
                                @Override public Object getCredentials() { return c.getValue(); }
                                @Override public Object getPrincipal() { return dni; }
                            };
                            auth.setAuthenticated(true);
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        }
                    } catch (Exception ignored) {}
                    break;
                }
            }
        }
        chain.doFilter(req, res);
    }
}
