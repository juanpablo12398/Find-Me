package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DesaparecidoMapperTest {

    private final DesaparecidoMapper mapper = new DesaparecidoMapper();

    @Test
    void fromRequestToDomain_mapeaCamposBasicos() {
        var req = new DesaparecidoRequestDTO("Ana", "Perez", 30, "12345678",
                "http://foto", "Texto de descripcion");
        Desaparecido d = mapper.fromRequestToDomain(req);

        assertThat(d.getNombre()).isEqualTo("Ana");
        assertThat(d.getApellido()).isEqualTo("Perez");
        assertThat(d.getDni()).isEqualTo("12345678");
        assertThat(d.getFoto()).isEqualTo("http://foto");
        assertThat(d.getDescripcion()).isEqualTo("Texto de descripcion");
    }

    @Test
    void fromRequestListToDomainList_mapeaCadaElemento() {
        var reqs = List.of(
                new DesaparecidoRequestDTO("A","B",20,"1","f1","d1"),
                new DesaparecidoRequestDTO("C","D",21,"2","f2","d2")
        );

        var out = mapper.fromRequestToDomain(reqs);
        assertThat(out).hasSize(2);
        assertThat(out.get(0).getNombre()).isEqualTo("A");
        assertThat(out.get(1).getDni()).isEqualTo("2");
    }

    @Test
    void fromDomainToResponse_mapeaCamposYConservaFecha() {
        var dom = new Desaparecido("Ana","Perez","123","desc","fotoUrl");
        UUID id = UUID.randomUUID();
        dom.setId(id);
        dom.setFechaDesaparicion(LocalDateTime.of(2025, 7, 1, 10, 30));

        DesaparecidoResponseDTO resp = mapper.fromDomainToResponse(dom);

        assertThat(resp.getId()).isEqualTo(id);
        assertThat(resp.getNombre()).isEqualTo("Ana");
        assertThat(resp.getApellido()).isEqualTo("Perez");
        assertThat(resp.getDni()).isEqualTo("123");
        assertThat(resp.getDescripcion()).isEqualTo("desc");
        assertThat(resp.getFoto()).isEqualTo("fotoUrl");
        assertThat(resp.getFechaDesaparicion()).isEqualTo(LocalDateTime.of(2025,7,1,10,30));
    }

    @Test
    void fromDomainListToResponseList_ok() {
        var d1 = new Desaparecido("A","B","1","d1","f1"); d1.setId(UUID.randomUUID());
        var d2 = new Desaparecido("C","D","2","d2","f2"); d2.setId(UUID.randomUUID());

        var out = mapper.fromDomainToResponse(List.of(d1, d2));
        assertThat(out).hasSize(2);
        assertThat(out.get(0).getId()).isEqualTo(d1.getId());
        assertThat(out.get(1).getDni()).isEqualTo("2");
    }

    @Test
    void fromResponseToFront_formateaFecha_ddMMyyyyHHmm() {
        UUID id = UUID.randomUUID();
        var resp = new DesaparecidoResponseDTO(
                id, "Ana","Perez","123",
                LocalDateTime.of(2025, 3, 4, 9, 5),
                "desc","foto"
        );
        var front = mapper.fromResponseToFront(resp);

        assertThat(front.getNombre()).isEqualTo("Ana");
        assertThat(front.getApellido()).isEqualTo("Perez");
        assertThat(front.getDni()).isEqualTo("123");
        assertThat(front.getDescripcion()).isEqualTo("desc");
        assertThat(front.getFoto()).isEqualTo("foto");
        assertThat(front.getFechaFormateada()).isEqualTo("04/03/2025 09:05");
    }

    @Test
    void fromResponseListToFrontList_ok() {
        var r1 = new DesaparecidoResponseDTO(UUID.randomUUID(),"A","B","1", LocalDateTime.now(),"d1","f1");
        var r2 = new DesaparecidoResponseDTO(UUID.randomUUID(),"C","D","2", LocalDateTime.now(),"d2","f2");

        var out = mapper.fromResponseListToFrontList(List.of(r1, r2));
        assertThat(out).hasSize(2);
        assertThat(out.get(0).getNombre()).isEqualTo("A");
    }
}
