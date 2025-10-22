package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import edu.utn.proyecto.applicacion.usecase.auth.LoginUseCase;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import edu.utn.proyecto.security.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final TokenService tokenService;

    // ⭐ IMPORTANTE: Necesitamos el secret para parsear el JWT en /me
    @Value("${jwt.secret:CAMBIA_ESTE_SECRETO_LARGO_DEMO_32+_CHARS}")
    private String SECRET;

    public AuthController(LoginUseCase loginUseCase, TokenService tokenService) {
        this.loginUseCase = loginUseCase;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<SessionUserDTO> doLogin(@RequestBody LoginRequestDTO req,
                                                  HttpServletResponse response) {
        // 1. Ejecutar caso de uso (devuelve SessionUserDTO con ID)
        SessionUserDTO user = loginUseCase.execute(req);

        // 2. Generar JWT - Convertir UUID a String
        String jwt = tokenService.generate(
                user.getId().toString(),
                user.getDni(),
                user.getEmail(),
                user.getNombre()
        );

        // 3. Enviar cookie al navegador
        tokenService.writeCookie(response, jwt);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<SessionUserDTO> me(HttpServletRequest request) {
        // 1. Buscar cookie
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. Buscar la cookie FM_TOKEN
        for (Cookie cookie : cookies) {
            if (TokenService.COOKIE_NAME.equals(cookie.getName())) {
                try {
                    // 3. Parsear JWT
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                            .build()
                            .parseClaimsJws(cookie.getValue())
                            .getBody();

                    // 4. Extraer datos del token
                    String dni = claims.getSubject();
                    String idStr = claims.get("id", String.class);
                    String email = claims.get("email", String.class);
                    String nombre = claims.get("name", String.class);

                    // 5. Construir SessionUserDTO - Convertir String a UUID
                    SessionUserDTO user = new SessionUserDTO(
                            UUID.fromString(idStr),  // ⭐ String → UUID
                            dni,
                            email != null ? email : "",
                            nombre != null ? nombre : ""
                    );

                    return ResponseEntity.ok(user);

                } catch (Exception e) {
                    // Token inválido o expirado
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
            }
        }

        // Cookie no encontrada
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        tokenService.clearCookie(response);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}