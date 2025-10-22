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
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@Import(ApiExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @org.springframework.boot.test.mock.mockito.MockBean LoginUseCase loginUseCase;
    @SpyBean TokenService tokenService;
    @org.springframework.boot.test.mock.mockito.MockBean MessageSource messageSource;

    @Test
    void login_ok_devuelve200_body_y_setCookie() throws Exception {
        var req = new LoginRequestDTO();
        req.setDni("12345678");
        req.setEmail("user@mail.com");

        UUID avistadorId = UUID.randomUUID();
        // ⭐ CAMBIO: Ahora SessionUserDTO incluye el ID
        var session = new SessionUserDTO(avistadorId, "12345678", "user@mail.com", "Juan");
        given(loginUseCase.execute(any(LoginRequestDTO.class))).willReturn(session);

        // ⭐ CAMBIO: generate ahora recibe 4 parámetros
        doReturn("JWT.TOKEN.X")
                .when(tokenService).generate(
                        eq(avistadorId.toString()),
                        eq("12345678"),
                        eq("user@mail.com"),
                        eq("Juan")
                );

        var res = mvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(avistadorId.toString())) // ⭐ Verificar ID
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.email").value("user@mail.com"))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andReturn();

        verify(loginUseCase).execute(any(LoginRequestDTO.class));
        verify(tokenService).generate(avistadorId.toString(), "12345678", "user@mail.com", "Juan");
        verify(tokenService).writeCookie(any(), eq("JWT.TOKEN.X"));

        var setCookies = res.getResponse().getHeaders("Set-Cookie");
        assertThat(setCookies).isNotEmpty();
        assertThat(setCookies).anySatisfy(c -> {
            assertThat(c).contains("FM_TOKEN=");
            assertThat(c).contains("HttpOnly");
            assertThat(c).contains("Path=/");
            assertThat(c).contains("Max-Age=604800");
        });
    }

    @Test
    void login_errorPolicy_traducidoAProblemDetail() throws Exception {
        var req = new LoginRequestDTO();
        req.setDni("111");
        req.setEmail("x@x.com");

        var ex = edu.utn.proyecto.common.exception.DomainException.of(
                "auth.user.not_found", HttpStatus.NOT_FOUND, "Usuario no registrado.");
        given(loginUseCase.execute(any(LoginRequestDTO.class))).willThrow(ex);

        mvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Usuario no registrado."))
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
