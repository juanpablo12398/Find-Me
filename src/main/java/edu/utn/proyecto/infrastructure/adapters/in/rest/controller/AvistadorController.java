package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import edu.utn.proyecto.applicacion.dtos.AvistadorResponseDTO;
import edu.utn.proyecto.applicacion.usecase.avistador.CreateAvistadorUseCase;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.security.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/avistadores")
public class AvistadorController {

    private final CreateAvistadorUseCase createAvistadorUseCase;
    private final TokenService tokenService;

    public AvistadorController(CreateAvistadorUseCase createAvistadorUseCase,
                               TokenService tokenService) {
        this.createAvistadorUseCase = createAvistadorUseCase;
        this.tokenService = tokenService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AvistadorResponseDTO> registrar(@Valid @RequestBody AvistadorRequestDTO request,
                                                          HttpServletResponse response) {
        // Crear el avistador
        AvistadorResponseDTO dto = createAvistadorUseCase.execute(request);

        // IMPORTANTE: Generar JWT y establecer cookie para auto-login
        String jwt = tokenService.generate(
                dto.getDni(),
                dto.getEmail() != null ? dto.getEmail() : "",
                dto.getNombre()
        );
        tokenService.writeCookie(response, jwt);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(location).body(dto);
    }
}
