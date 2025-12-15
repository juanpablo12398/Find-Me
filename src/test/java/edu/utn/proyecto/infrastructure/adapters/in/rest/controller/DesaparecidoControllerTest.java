package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.applicacion.usecase.desaparecido.CreateDesaparecidoUseCase;
import edu.utn.proyecto.applicacion.usecase.desaparecido.ReadDesaparecidoUseCase;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoFrontDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DesaparecidoControllerTest {

    private CreateDesaparecidoUseCase createUC;
    private ReadDesaparecidoUseCase readUC;
    private MockMvc mvc;
    private ObjectMapper om;

    @BeforeEach
    void setup() {
        createUC = Mockito.mock(CreateDesaparecidoUseCase.class);
        readUC   = Mockito.mock(ReadDesaparecidoUseCase.class);
        mvc = MockMvcBuilders.standaloneSetup(new DesaparecidoController(createUC, readUC)).build();

        om = new ObjectMapper();
        om.registerModule(new JavaTimeModule()); // para LocalDateTime
    }

    @Test
    @DisplayName("POST /api/desaparecidos -> 201 Created + Location + body")
    void crear_created() throws Exception {
        var reqId = UUID.randomUUID();
        var req   = new DesaparecidoRequestDTO();

        var resp = new DesaparecidoResponseDTO(
                reqId,
                "Nombre",
                "Apellido",
                "12345678",
                LocalDateTime.of(2025, 1, 1, 12, 0),
                "Descripcion",
                "foto.jpg"
        );

        when(createUC.execute(any(DesaparecidoRequestDTO.class))).thenReturn(resp);

        mvc.perform(post("/api/desaparecidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/desaparecidos/" + reqId))
                .andExpect(content().json(om.writeValueAsString(resp)));

        verify(createUC).execute(any(DesaparecidoRequestDTO.class));
        verifyNoMoreInteractions(createUC, readUC);
    }

    @Test
    @DisplayName("GET /api/desaparecidos -> 200 OK + lista")
    void getAll_ok() throws Exception {
        var list = List.of(
                new DesaparecidoFrontDTO(
                        UUID.randomUUID(), "N1", "A1", "1", "D1", "F1", "01/01/2025 12:00"
                ),
                new DesaparecidoFrontDTO(
                        UUID.randomUUID(), "N2", "A2", "2", "D2", "F2", "02/01/2025 13:00"
                )
        );

        when(readUC.execute()).thenReturn(list);

        mvc.perform(get("/api/desaparecidos"))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(list)));

        verify(readUC).execute();
        verifyNoMoreInteractions(createUC, readUC);
    }
}