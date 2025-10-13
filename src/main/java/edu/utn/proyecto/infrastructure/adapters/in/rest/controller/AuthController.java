package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import edu.utn.proyecto.applicacion.usecase.auth.LoginUseCase;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import edu.utn.proyecto.security.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final TokenService tokenService;

    public AuthController(LoginUseCase loginUseCase, TokenService tokenService) {
        this.loginUseCase = loginUseCase;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<SessionUserDTO> doLogin(@RequestBody LoginRequestDTO req,
                                                  HttpServletResponse response) {
        // Ejecutar el caso de uso (valida y obtiene el usuario)
        SessionUserDTO user = loginUseCase.execute(req);

        // Generar JWT y establecer cookie
        String jwt = tokenService.generate(user.getDni(), user.getEmail(), user.getNombre());
        tokenService.writeCookie(response, jwt);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<SessionUserDTO> me(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // El DNI está en el principal
        String dni = (String) auth.getPrincipal();

        // Aquí podrías buscar más datos del usuario si lo necesitás
        // Por ahora devuelvo solo el DNI
        SessionUserDTO user = new SessionUserDTO(dni, "", "");

        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        tokenService.clearCookie(response);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}