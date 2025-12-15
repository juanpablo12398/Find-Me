package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utn.proyecto.applicacion.usecase.auth.GetCurrentUserUseCase;
import edu.utn.proyecto.applicacion.usecase.auth.LoginUseCase;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import edu.utn.proyecto.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mvc;
    private LoginUseCase loginUC;
    private GetCurrentUserUseCase meUC;
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
        loginUC = mock(LoginUseCase.class);
        meUC = mock(GetCurrentUserUseCase.class);
        tokenService = mock(TokenService.class);
        mvc = MockMvcBuilders
                .standaloneSetup(new AuthController(loginUC, meUC, tokenService))
                .setControllerAdvice(new TestAdvice())
                .build();
        om = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /api/auth/login → 200, set cookie, body OK")
    void login_ok() throws Exception {
        var req = new LoginRequestDTO();
        var user = new SessionUserDTO(UUID.randomUUID(), "123", "a@b.com","Juan");
        when(loginUC.execute(any(LoginRequestDTO.class))).thenReturn(user);
        when(tokenService.generate(anyString(), anyString(), anyString(), anyString())).thenReturn("jwt");

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dni").value("123"))
                .andExpect(jsonPath("$.email").value("a@b.com"))
                .andExpect(jsonPath("$.nombre").value("Juan"));

        verify(loginUC).execute(any(LoginRequestDTO.class));
        verify(tokenService).generate(eq(user.getId().toString()), eq("123"), eq("a@b.com"), eq("Juan"));
        verify(tokenService).writeCookie(any(HttpServletResponse.class), eq("jwt"));
        verifyNoMoreInteractions(loginUC, meUC, tokenService);
    }

    @Test
    @DisplayName("POST /api/auth/login con email null → sigue OK (pasa null al generate)")
    void login_nullEmail_ok() throws Exception {
        var req = new LoginRequestDTO();
        var user = new SessionUserDTO(UUID.randomUUID(), "555", null,"SinMail");
        when(loginUC.execute(any(LoginRequestDTO.class))).thenReturn(user);
        when(tokenService.generate(anyString(), anyString(), isNull(), anyString())).thenReturn("jwt2");

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(tokenService).generate(eq(user.getId().toString()), eq("555"), isNull(), eq("SinMail"));
        verify(tokenService).writeCookie(any(HttpServletResponse.class), eq("jwt2"));
    }

    @Test
    @DisplayName("POST /api/auth/login → service lanza → 500")
    void login_serviceThrows_500() throws Exception {
        when(loginUC.execute(any(LoginRequestDTO.class))).thenThrow(new RuntimeException("boom"));

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(new LoginRequestDTO())))
                .andExpect(status().isInternalServerError());

        verify(loginUC).execute(any(LoginRequestDTO.class));
        verifyNoMoreInteractions(loginUC);
        verifyNoInteractions(tokenService);
    }

    @Test
    @DisplayName("GET /api/auth/me → 200")
    void me_ok() throws Exception {
        var user = new SessionUserDTO(null, "321",null,null);
        when(meUC.execute(any(HttpServletRequest.class))).thenReturn(user);

        mvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("321"));

        verify(meUC).execute(any(HttpServletRequest.class));
        verifyNoMoreInteractions(meUC);
    }

    @Test
    @DisplayName("GET /api/auth/me → service lanza → 500")
    void me_serviceThrows_500() throws Exception {
        when(meUC.execute(any(HttpServletRequest.class))).thenThrow(new RuntimeException("x"));

        mvc.perform(get("/api/auth/me"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("POST /api/auth/logout → 204 y limpia cookie")
    void logout_ok() throws Exception {
        mvc.perform(post("/api/auth/logout"))
                .andExpect(status().isNoContent())
                .andExpect(header().doesNotExist("Location"))
                .andExpect(content().string(""));

        verify(tokenService).clearCookie(any(HttpServletResponse.class));
        verifyNoMoreInteractions(tokenService);
    }
}