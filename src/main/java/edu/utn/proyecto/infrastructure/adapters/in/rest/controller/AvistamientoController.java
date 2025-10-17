package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import edu.utn.proyecto.applicacion.dtos.AvistamientoResponseDTO;
import edu.utn.proyecto.applicacion.usecase.avistamiento.CreateAvistamientoUseCase;
import edu.utn.proyecto.applicacion.usecase.avistamiento.ReadAvistamientoUseCase;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoFrontDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/avistamientos")
@CrossOrigin(origins = "*")
public class AvistamientoController {

    private final CreateAvistamientoUseCase createUseCase;
    private final ReadAvistamientoUseCase readUseCase;

    public AvistamientoController(
            CreateAvistamientoUseCase createUseCase,
            ReadAvistamientoUseCase readUseCase) {
        this.createUseCase = createUseCase;
        this.readUseCase = readUseCase;
    }

    // ============================================
    // POST: Crear nuevo avistamiento
    // ============================================
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AvistamientoResponseDTO> crear(
            @Valid @RequestBody AvistamientoRequestDTO request) {

        AvistamientoResponseDTO dto = createUseCase.execute(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();

        return ResponseEntity.created(location).body(dto);
    }

    // ============================================
    // GET: Avistamientos para el MAPA
    // Este endpoint devuelve FrontDTO con fecha formateada
    // y datos enriquecidos del desaparecido y avistador
    // ============================================
    @GetMapping("/mapa")
    public ResponseEntity<List<AvistamientoFrontDTO>> obtenerParaMapa() {
        List<AvistamientoFrontDTO> lista = readUseCase.obtenerParaMapa();
        return ResponseEntity.ok(lista);
    }

    // ============================================
    // GET: Buscar avistamientos en un área del mapa
    // Params: latMin, latMax, lngMin, lngMax
    // ============================================
    @GetMapping("/mapa/area")
    public ResponseEntity<List<AvistamientoFrontDTO>> obtenerEnArea(
            @RequestParam Double latMin,
            @RequestParam Double latMax,
            @RequestParam Double lngMin,
            @RequestParam Double lngMax) {

        List<AvistamientoFrontDTO> lista = readUseCase.obtenerEnArea(
                latMin, latMax, lngMin, lngMax
        );
        return ResponseEntity.ok(lista);
    }

    // ============================================
    // GET: Avistamientos de un desaparecido específico
    // Para mostrar timeline en la página del desaparecido
    // ============================================
    @GetMapping("/desaparecido/{desaparecidoId}")
    public ResponseEntity<List<AvistamientoFrontDTO>> obtenerPorDesaparecido(
            @PathVariable UUID desaparecidoId) {

        List<AvistamientoFrontDTO> lista = readUseCase.obtenerPorDesaparecido(desaparecidoId);
        return ResponseEntity.ok(lista);
    }

    // ============================================
    // GET: Todos los avistamientos públicos (simple)
    // Sin datos enriquecidos, solo ResponseDTO
    // ============================================
    @GetMapping
    public ResponseEntity<List<AvistamientoResponseDTO>> obtenerTodos() {
        List<AvistamientoResponseDTO> lista = readUseCase.obtenerTodos();
        return ResponseEntity.ok(lista);
    }
}
