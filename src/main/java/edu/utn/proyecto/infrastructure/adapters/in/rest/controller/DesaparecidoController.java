package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.applicacion.usecase.desaparecido.CreateDesaparecidoUseCase;
import edu.utn.proyecto.applicacion.usecase.desaparecido.GetAllDesaparecidosUseCase;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoFrontDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/desaparecidos")
@CrossOrigin(origins = "*") // según tu puerto front
// http://localhost:63342
// El local host para pruebas complejas en spring es:
// http://localhost:8080/index.html
public class DesaparecidoController {

    private final CreateDesaparecidoUseCase crearDesaparecidoUseCase;
    private final GetAllDesaparecidosUseCase getAllDesaparecidosUseCase;

    public DesaparecidoController(CreateDesaparecidoUseCase crearDesaparecidoUseCase, GetAllDesaparecidosUseCase getAllDesaparecidosUseCase) {
        this.crearDesaparecidoUseCase = crearDesaparecidoUseCase;
        this.getAllDesaparecidosUseCase = getAllDesaparecidosUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DesaparecidoResponseDTO> crear(@RequestBody DesaparecidoRequestDTO requestDto) {
        DesaparecidoResponseDTO dto = crearDesaparecidoUseCase.execute(requestDto);
        URI location = URI.create("/api/desaparecidos/" + dto.getId());
        return ResponseEntity.created(location).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<DesaparecidoFrontDTO>> getAll() {
        List<DesaparecidoFrontDTO> lista = getAllDesaparecidosUseCase.execute();
        return ResponseEntity.ok(lista);
    }
}
