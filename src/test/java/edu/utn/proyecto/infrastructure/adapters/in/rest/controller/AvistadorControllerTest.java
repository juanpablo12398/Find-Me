package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utn.proyecto.applicacion.dtos.AvistadorResponseDTO;
import edu.utn.proyecto.applicacion.usecase.avistador.CreateAvistadorUseCase;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
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
import java.time.LocalDateTime;
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

    @MockBean CreateAvistadorUseCase createUseCase;
    @SpyBean  TokenService tokenService;

    // Necesario porque ApiExceptionHandler inyecta MessageSource
    @MockBean MessageSource messageSource;

    @Test
    void registrar_ok_201_location_body_y_cookie() throws Exception {
        // request válido (pasa @Valid)
        var req = new AvistadorRequestDTO();
        req.setDni("12345678");
        req.setNombre("Ana");
        req.setApellido("Perez");
        req.setEdad(28);
        req.setDireccion("Calle 123");
        req.setEmail("ana@x.com");
        req.setTelefono("111-222");

        // respuesta del use case
        var id = UUID.randomUUID();
        var resp = new AvistadorResponseDTO();
        resp.setId(id);
        resp.setDni("12345678");
        resp.setNombre("Ana");
        resp.setApellido("Perez");
        resp.setEdad(28);
        resp.setDireccion("Calle 123");
        resp.setEmail("ana@x.com");
        resp.setTelefono("111-222");
        resp.setCreadoEn(LocalDateTime.of(2025,1,1,12,0));

        given(createUseCase.execute(any(AvistadorRequestDTO.class))).willReturn(resp);

        // stub del JWT
        doReturn("JWT.TOKEN.X").when(tokenService)
                .generate(eq(id.toString()), eq("12345678"), eq("ana@x.com"), eq("Ana"));

        var mvcRes = mvc.perform(post("/api/avistadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/avistadores/" + id)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andReturn();

        // verificaciones de colaboración
        verify(createUseCase).execute(any(AvistadorRequestDTO.class));
        verify(tokenService).generate(id.toString(), "12345678", "ana@x.com", "Ana");
        verify(tokenService).writeCookie(any(), eq("JWT.TOKEN.X"));

        // verifica Set-Cookie
        var setCookies = mvcRes.getResponse().getHeaders("Set-Cookie");
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
    void registrar_errorPolicy_problemDetail_400_sin_detail() throws Exception {
        var req = new AvistadorRequestDTO();
        req.setDni("12345678");
        req.setNombre("Ana");
        req.setApellido("Perez");
        req.setEdad(15); // forzamos underage (o lo que dispare tu regla)
        req.setDireccion("Calle 123");
        req.setEmail("ana@x.com");

        var ex = edu.utn.proyecto.common.exception.DomainException.of(
                "avistador.underage", HttpStatus.BAD_REQUEST, "Menor de edad no permitido");

        given(createUseCase.execute(any(AvistadorRequestDTO.class))).willThrow(ex);

        mvc.perform(post("/api/avistadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                // tu handler pone "key" en el root y NO incluye "detail"
                .andExpect(jsonPath("$.key").value("avistador.underage"));
    }
}