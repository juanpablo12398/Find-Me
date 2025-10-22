package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utn.proyecto.applicacion.dtos.AvistadorResponseDTO;
import edu.utn.proyecto.applicacion.usecase.avistador.CreateAvistadorUseCase;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.exception.ApiExceptionHandler;
import edu.utn.proyecto.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.validation.Validator;
import java.util.Collections;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AvistadorController.class)
@Import(ApiExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class AvistadorControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean CreateAvistadorUseCase createAvistadorUseCase;
    @SpyBean TokenService tokenService;
    @MockBean MessageSource messageSource;
    @MockBean Validator beanValidator;

    @BeforeEach
    void disableBeanValidation() {
        when(beanValidator.validate(any(), (Class<?>[]) any()))
                .thenReturn(Collections.emptySet());
    }

    @Test
    void registrar_ok_201_location_y_cookie() throws Exception {
        var req = new AvistadorRequestDTO();
        req.setDni("12345678");
        req.setNombre("ANA");
        req.setApellido("PEREZ");
        req.setEdad(28);
        req.setEmail("a@a.com");

        var id = UUID.randomUUID();
        var resp = new AvistadorResponseDTO();
        resp.setId(id);
        resp.setDni("12345678");
        resp.setNombre("ANA");
        resp.setApellido("PEREZ");
        resp.setEmail("a@a.com");

        given(createAvistadorUseCase.execute(any(AvistadorRequestDTO.class))).willReturn(resp);

        // ⭐ CAMBIO: Ahora generate recibe 4 parámetros (id primero)
        doReturn("JWT123").when(tokenService)
                .generate(eq(id.toString()), eq("12345678"), eq("a@a.com"), eq("ANA"));

        var res = mvc.perform(post("/api/avistadores")
                        .contentType("application/json")
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/avistadores/" + id)))
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andReturn();

        verify(createAvistadorUseCase).execute(any(AvistadorRequestDTO.class));
        verify(tokenService).generate(id.toString(), "12345678", "a@a.com", "ANA"); // ⭐ 4 params
        verify(tokenService).writeCookie(any(), eq("JWT123"));

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
    void registrar_errorPolicy_problemDetail_409() throws Exception {
        var req = new AvistadorRequestDTO();
        req.setDni("12345678");
        req.setNombre("ANA");
        req.setApellido("PEREZ");
        req.setEdad(16);
        req.setEmail("a@a.com");

        var ex = DomainException.of("avistador.underage", HttpStatus.BAD_REQUEST, "Debe ser mayor de edad");
        given(createAvistadorUseCase.execute(any(AvistadorRequestDTO.class))).willThrow(ex);

        mvc.perform(post("/api/avistadores")
                        .contentType("application/json")
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.key").value("avistador.underage"))
                .andExpect(jsonPath("$.detail").value("Debe ser mayor de edad"));

        verifyNoInteractions(tokenService);
    }
}