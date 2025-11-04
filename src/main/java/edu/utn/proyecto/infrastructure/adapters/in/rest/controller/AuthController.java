package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import edu.utn.proyecto.applicacion.usecase.auth.LoginUseCase;
import edu.utn.proyecto.applicacion.usecase.auth.GetCurrentUserUseCase;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import edu.utn.proyecto.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final TokenService tokenService;

    public AuthController(LoginUseCase loginUseCase,
                          GetCurrentUserUseCase getCurrentUserUseCase,
                          TokenService tokenService) {
        this.loginUseCase = loginUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<SessionUserDTO> doLogin(@RequestBody LoginRequestDTO req,
                                                  HttpServletResponse response) {
        SessionUserDTO user = loginUseCase.execute(req);
        String jwt = tokenService.generate(
                user.getId().toString(),
                user.getDni(),
                user.getEmail(),
                user.getNombre()
        );
        tokenService.writeCookie(response, jwt);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me")
    public ResponseEntity<SessionUserDTO> me(HttpServletRequest request) {
        SessionUserDTO user = getCurrentUserUseCase.execute(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        tokenService.clearCookie(response);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}