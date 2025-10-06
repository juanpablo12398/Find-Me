package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import edu.utn.proyecto.applicacion.dtos.AvistadorResponseDTO;
import edu.utn.proyecto.applicacion.usecase.avistador.CreateAvistadorUseCase;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/avistadores")
@CrossOrigin(origins = "*")
public class AvistadorController {

    private final CreateAvistadorUseCase createAvistadorUseCase;

    public AvistadorController(CreateAvistadorUseCase createAvistadorUseCase) {
        this.createAvistadorUseCase = createAvistadorUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AvistadorResponseDTO> registrar(@RequestBody AvistadorRequestDTO request) {
        AvistadorResponseDTO dto = createAvistadorUseCase.execute(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(location).body(dto); // 201 + Location
    }
}