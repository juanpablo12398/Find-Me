package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utn.proyecto.applicacion.usecase.auth.LoginUseCase;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.exception.ApiExceptionHandler;
import edu.utn.proyecto.security.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@Import(ApiExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean LoginUseCase loginUseCase;
    @SpyBean TokenService tokenService; // Spy para verificar writeCookie/clearCookie sin fijar el JWT
    @MockBean MessageSource messageSource;

    @Test
    void login_ok_devuelve200_body_y_setCookie() throws Exception {
        // Arrange
        var req = new LoginRequestDTO();
        req.setDni("12345678");
        req.setEmail("user@mail.com");

        UUID avistadorId = UUID.randomUUID();
        // IMPORTANTE: tu SessionUserDTO debe tener este ctor (UUID id, String dni, String email, String nombre)
        var session = new SessionUserDTO(avistadorId, "12345678", "user@mail.com", "Juan");
        given(loginUseCase.execute(any(LoginRequestDTO.class))).willReturn(session);

        // Act
        var res = mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(avistadorId.toString()))
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.email").value("user@mail.com"))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andReturn();

        // Assert interacciones
        verify(loginUseCase).execute(any(LoginRequestDTO.class));
        // No verifico parámetros de generate para no acoplar al número/orden de claims.
        // Me alcanza con verificar que escribiste la cookie con algún JWT.
        verify(tokenService).writeCookie(any(), any(String.class));

        // Assert cookie
        var setCookies = res.getResponse().getHeaders("Set-Cookie");
        assertThat(setCookies).isNotEmpty();
        assertThat(setCookies).anySatisfy(c -> {
            assertThat(c).contains("FM_TOKEN=");
            assertThat(c).contains("HttpOnly");
            assertThat(c).contains("Path=/");
            // 7 días = 604800
            assertThat(c).contains("Max-Age=604800");
        });
    }

    @Test
    void login_errorPolicy_problemDetail_sin_detail() throws Exception {
        // Arrange
        var req = new LoginRequestDTO();
        req.setDni("111");
        req.setEmail("x@x.com");

        var ex = edu.utn.proyecto.common.exception.DomainException.of(
                "auth.user.not_found", HttpStatus.NOT_FOUND, "Usuario no registrado.");
        given(loginUseCase.execute(any(LoginRequestDTO.class))).willThrow(ex);

        // Act & Assert
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                // Tu ApiExceptionHandler actual incluye "key" en la raíz y
                // (según el body que mostraste) no envía "detail". No lo asertamos.
                .andExpect(jsonPath("$.key").value("auth.user.not_found"));
    }

    @Test
    void me_sinAuth_devuelve401() throws Exception {
        mvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_clears_cookie_204() throws Exception {
        mvc.perform(post("/api/auth/logout"))
                .andExpect(status().isNoContent());

        verify(tokenService).clearCookie(any());
    }
}
