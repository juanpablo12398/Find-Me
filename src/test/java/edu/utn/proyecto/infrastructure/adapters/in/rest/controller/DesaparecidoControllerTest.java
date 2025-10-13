package edu.utn.proyecto.infrastructure.adapters.in.rest.controller;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.applicacion.usecase.desaparecido.CreateDesaparecidoUseCase;
import edu.utn.proyecto.applicacion.usecase.desaparecido.ReadDesaparecidoUseCase;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoFrontDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.exception.ApiExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DesaparecidoController.class)
@Import(ApiExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class DesaparecidoControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean CreateDesaparecidoUseCase createUseCase;
    @MockBean ReadDesaparecidoUseCase readUseCase;

    // ✅ mock explícito para el handler
    @MockBean MessageSource messageSource;

    @Test
    void crear_ok_201_location_y_body() throws Exception {
        var req = new DesaparecidoRequestDTO("Ana","Perez",30,"12345678","foto","desc");
        var id = UUID.randomUUID();
        var resp = new DesaparecidoResponseDTO(
                id,"Ana","Perez","12345678",
                LocalDateTime.of(2025,1,1,12,0),"desc","foto");

        given(createUseCase.execute(any(DesaparecidoRequestDTO.class))).willReturn(resp);

        mvc.perform(post("/api/desaparecidos")
                        .contentType("application/json")
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/desaparecidos/" + id)))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.dni").value("12345678"));
    }

    @Test
    void crear_errorPolicy_problemDetail_409() throws Exception {
        var req = new DesaparecidoRequestDTO("Ana","Perez",30,"12345678","foto","desc");
        var ex = DomainException.of("desaparecido.dni.dup", HttpStatus.CONFLICT, "DNI duplicado");

        given(createUseCase.execute(any(DesaparecidoRequestDTO.class))).willThrow(ex);

        mvc.perform(post("/api/desaparecidos")
                        .contentType("application/json")
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.key").value("desaparecido.dni.dup"))
                .andExpect(jsonPath("$.detail").value("DNI duplicado"));
    }

    @Test
    void getAll_ok_200_listaFrontDTO() throws Exception {
        var list = List.of(
                new DesaparecidoFrontDTO("A","B","1","d1","f1","01/01/2025 10:00"),
                new DesaparecidoFrontDTO("C","D","2","d2","f2","02/01/2025 11:00")
        );
        given(readUseCase.execute()).willReturn(list);

        var mvcRes = mvc.perform(get("/api/desaparecidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();

        var body = mvcRes.getResponse().getContentAsString();
        var parsed = om.readValue(body, new TypeReference<List<DesaparecidoFrontDTO>>() {});
        org.assertj.core.api.Assertions.assertThat(parsed).hasSize(2);
    }
}
