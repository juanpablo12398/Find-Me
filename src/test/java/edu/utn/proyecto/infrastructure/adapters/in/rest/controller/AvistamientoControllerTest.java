package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utn.proyecto.applicacion.dtos.AvistamientoResponseDTO;
import edu.utn.proyecto.applicacion.usecase.avistamiento.CreateAvistamientoUseCase;
import edu.utn.proyecto.applicacion.usecase.avistamiento.ReadAvistamientoGeoUseCase;
import edu.utn.proyecto.applicacion.usecase.avistamiento.ReadAvistamientoUseCase;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoFrontDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AvistamientoControllerTest {

    private MockMvc mvc;
    private CreateAvistamientoUseCase createUC;
    private ReadAvistamientoUseCase readUC;
    private ReadAvistamientoGeoUseCase readGeoUC;
    private ObjectMapper om;

    @RestControllerAdvice
    static class TestAdvice {
        @ExceptionHandler(RuntimeException.class)
        ResponseEntity<String> re(RuntimeException ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @BeforeEach
    void setup() {
        createUC = mock(CreateAvistamientoUseCase.class);
        readUC   = mock(ReadAvistamientoUseCase.class);
        readGeoUC= mock(ReadAvistamientoGeoUseCase.class);
        mvc = MockMvcBuilders.standaloneSetup(
                new AvistamientoController(createUC, readUC, readGeoUC)
        ).setControllerAdvice(new TestAdvice()).build();
        om = new ObjectMapper();
    }

    private AvistamientoRequestDTO validReq() {
        var r = new AvistamientoRequestDTO();
        r.setLatitud(-34.60);
        r.setLongitud(-58.38);
        r.setDescripcion("Descripción válida");
        r.setAvistadorId(UUID.randomUUID().toString());
        r.setDesaparecidoId(UUID.randomUUID().toString());
        return r;
    }

    @Test
    @DisplayName("POST /api/avistamientos → 201 Created + Location + body")
    void crear_created() throws Exception {
        var req  = validReq();
        var resp = new AvistamientoResponseDTO();
        resp.setId(UUID.randomUUID());

        when(createUC.execute(any(AvistamientoRequestDTO.class))).thenReturn(resp);

        mvc.perform(post("/api/avistamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        containsString("/api/avistamientos/" + resp.getId())))
                .andExpect(content().json(om.writeValueAsString(resp)));

        verify(createUC).execute(any(AvistamientoRequestDTO.class));
        verifyNoMoreInteractions(createUC, readUC, readGeoUC);
    }

    @Test
    @DisplayName("POST /api/avistamientos → use case lanza → 500 estable")
    void crear_serviceThrow_500() throws Exception {
        var req = validReq();
        when(createUC.execute(any(AvistamientoRequestDTO.class)))
                .thenThrow(new RuntimeException("boom"));

        mvc.perform(post("/api/avistamientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/avistamientos/mapa → 200 y shape básico OK")
    void mapa_ok() throws Exception {
        var f = new AvistamientoFrontDTO(
                UUID.randomUUID(), -34.6, -58.38, "2025-01-01T12:00:00Z",
                "desc", "foto.jpg", false
        );
        when(readGeoUC.paraMapa()).thenReturn(List.of(f));

        mvc.perform(get("/api/avistamientos/mapa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                // nombres reales del JSON:
                .andExpect(jsonPath("$[0].latitud").value(-34.6))
                .andExpect(jsonPath("$[0].longitud").value(-58.38))
                .andExpect(jsonPath("$[0].descripcion").value("desc"));
        // Evitamos afirmar campos opcionales como "foto"/"timestamp"
    }

    @Test
    @DisplayName("GET /api/avistamientos/mapa → use case lanza → 500")
    void mapa_serviceThrows() throws Exception {
        when(readGeoUC.paraMapa()).thenThrow(new RuntimeException("err"));

        mvc.perform(get("/api/avistamientos/mapa"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("GET /api/avistamientos → 200 lista")
    void getAll_ok() throws Exception {
        var r = new AvistamientoResponseDTO();
        r.setId(UUID.randomUUID());
        when(readUC.obtenerTodos()).thenReturn(List.of(r));

        mvc.perform(get("/api/avistamientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(readUC).obtenerTodos();
    }

    @Test
    @DisplayName("GET /api/avistamientos/desaparecido/{id} → 200")
    void porDesaparecido_ok() throws Exception {
        var list = List.of(new AvistamientoFrontDTO(
                UUID.randomUUID(), -1.0, -1.0, "ts", "d", "f", false));
        var id = UUID.randomUUID();
        when(readGeoUC.porDesaparecido(id)).thenReturn(list);

        mvc.perform(get("/api/avistamientos/desaparecido/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(readGeoUC).porDesaparecido(eq(id));
    }

    @Test
    @DisplayName("GET /api/avistamientos/mapa/area → 200")
    void area_ok() throws Exception {
        when(readGeoUC.enArea(-35.0, -34.0, -59.0, -58.0))
                .thenReturn(List.of());
        mvc.perform(get("/api/avistamientos/mapa/area")
                        .param("latMin", "-35")
                        .param("latMax", "-34")
                        .param("lngMin", "-59")
                        .param("lngMax", "-58"))
                .andExpect(status().isOk());
        verify(readGeoUC).enArea(-35.0, -34.0, -59.0, -58.0);
    }

    @Test
    @DisplayName("GET /api/avistamientos/radio → 200")
    void radio_ok() throws Exception {
        when(readGeoUC.enRadio(-34.6, -58.38, 5.0)).thenReturn(List.of());
        mvc.perform(get("/api/avistamientos/radio")
                        .param("lat", "-34.6")
                        .param("lng", "-58.38")
                        .param("radioKm", "5"))
                .andExpect(status().isOk());
        verify(readGeoUC).enRadio(-34.6, -58.38, 5.0);
    }

    @Test
    @DisplayName("GET /api/avistamientos/cercanos → 200")
    void cercanos_ok() throws Exception {
        when(readGeoUC.masCercanos(-34.6, -58.38, 3)).thenReturn(List.of());
        mvc.perform(get("/api/avistamientos/cercanos")
                        .param("lat", "-34.6")
                        .param("lng", "-58.38")
                        .param("limite", "3"))
                .andExpect(status().isOk());
        verify(readGeoUC).masCercanos(-34.6, -58.38, 3);
    }

    @Test
    @DisplayName("POST /api/avistamientos/poligono → 200")
    void poligono_ok() throws Exception {
        when(readGeoUC.enPoligono("POLYGON((...))")).thenReturn(List.of());
        mvc.perform(post("/api/avistamientos/poligono")
                        .param("wkt", "POLYGON((...))"))
                .andExpect(status().isOk());
        verify(readGeoUC).enPoligono("POLYGON((...))");
    }

    @Test
    @DisplayName("GET /api/avistamientos/desaparecido/{id}/radio → 200")
    void porDesaparecidoRadio_ok() throws Exception {
        var id = UUID.randomUUID();
        when(readGeoUC.porDesaparecidoEnRadio(id, -34.6, -58.38, 1.2)).thenReturn(List.of());
        mvc.perform(get("/api/avistamientos/desaparecido/{id}/radio", id)
                        .param("lat", "-34.6")
                        .param("lng", "-58.38")
                        .param("radioKm", "1.2"))
                .andExpect(status().isOk());
        verify(readGeoUC).porDesaparecidoEnRadio(id, -34.6, -58.38, 1.2);
    }

    @Test
    @DisplayName("GET /api/avistamientos/area/count → 200 con número")
    void contarArea_ok() throws Exception {
        when(readGeoUC.contarEnArea(-35.0, -34.0, -59.0, -58.0)).thenReturn(42L);
        mvc.perform(get("/api/avistamientos/area/count")
                        .param("latMin", "-35")
                        .param("latMax", "-34")
                        .param("lngMin", "-59")
                        .param("lngMax", "-58"))
                .andExpect(status().isOk())
                .andExpect(content().string("42"));
        verify(readGeoUC).contarEnArea(-35.0, -34.0, -59.0, -58.0);
    }
}