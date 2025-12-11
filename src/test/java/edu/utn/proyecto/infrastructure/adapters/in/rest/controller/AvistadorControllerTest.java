package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utn.proyecto.applicacion.dtos.AvistadorResponseDTO;
import edu.utn.proyecto.applicacion.usecase.avistador.CreateAvistadorUseCase;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.security.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.UUID;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AvistadorControllerTest {

    private MockMvc mvc;
    private CreateAvistadorUseCase createUC;
    private TokenService tokenService;
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
        createUC = mock(CreateAvistadorUseCase.class);
        tokenService = mock(TokenService.class);
        mvc = MockMvcBuilders
                .standaloneSetup(new AvistadorController(createUC, tokenService))
                .setControllerAdvice(new TestAdvice())
                .build();
        om = new ObjectMapper();
    }

    private AvistadorRequestDTO validReq(String email) {
        var req = new AvistadorRequestDTO();
        req.setDni("12345678");
        req.setNombre("Juan");
        req.setApellido("Pérez");
        req.setDireccion("Calle 123");
        req.setEdad(30);
        req.setEmail(email);
        return req;
    }

    @Test
    @DisplayName("POST /api/avistadores → 201, Location, cookie y body")
    void registrar_ok() throws Exception {
        var req = validReq("a@b.com");

        var resp = new AvistadorResponseDTO();
        resp.setId(UUID.randomUUID());
        resp.setDni("12345678");
        resp.setNombre("Juan");
        resp.setEmail("a@b.com");

        when(createUC.execute(any(AvistadorRequestDTO.class))).thenReturn(resp);
        when(tokenService.generate(anyString(), anyString(), anyString(), anyString())).thenReturn("jwt");

        mvc.perform(post("/api/avistadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/avistadores/")))
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.nombre").value("Juan"));

        verify(createUC).execute(any(AvistadorRequestDTO.class));
        verify(tokenService).generate(eq(resp.getId().toString()), eq("12345678"), eq("a@b.com"), eq("Juan"));
        verify(tokenService).writeCookie(any(HttpServletResponse.class), eq("jwt"));
        verifyNoMoreInteractions(createUC, tokenService);
    }

    @Test
    @DisplayName("POST /api/avistadores con email null → genera JWT con \"\"")
    void registrar_emailNull_usaVacio() throws Exception {
        var req = validReq(null);

        var resp = new AvistadorResponseDTO();
        resp.setId(UUID.randomUUID());
        resp.setDni("999");
        resp.setNombre("SinMail");
        resp.setEmail(null);

        when(createUC.execute(any(AvistadorRequestDTO.class))).thenReturn(resp);
        when(tokenService.generate(anyString(), anyString(), anyString(), anyString())).thenReturn("jwt");

        mvc.perform(post("/api/avistadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(tokenService).generate(eq(resp.getId().toString()), eq("999"), eq(""), eq("SinMail"));
    }

    @Test
    @DisplayName("POST /api/avistadores → servicio lanza → 500 y no escribe cookie")
    void registrar_serviceThrow_500() throws Exception {
        var req = validReq("x@y.com");
        when(createUC.execute(any(AvistadorRequestDTO.class))).thenThrow(new RuntimeException("boom"));

        mvc.perform(post("/api/avistadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isInternalServerError());

        verify(createUC).execute(any(AvistadorRequestDTO.class));
        verifyNoInteractions(tokenService);
    }
}