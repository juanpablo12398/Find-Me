package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.common.normalize.DateTimeNormalizer;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.common.normalize.UrlNormalizer;
import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class DesaparecidoMapperTest {

    private final DesaparecidoMapper mapper = new DesaparecidoMapper(
            new TextNormalizer(),
            new DniNormalizer(),
            new UrlNormalizer(),
            new DateTimeNormalizer()
    );

    @Test
    void fromRequestToDomain_mapeaCamposBasicos_conNormalizacionPrevia() {
        var req = new DesaparecidoRequestDTO(
                "Ana", "Perez", 30, "12.345.678", " http://foto ",
                "  Texto de descripcion  "
        );

        // Emular flujo real: normalizar antes
        mapper.normalizeRequestInPlace(req);
        Desaparecido d = mapper.fromRequestToDomain(req);

        assertThat(d.getNombre()).isEqualTo("ANA");            // upperNoAccents
        assertThat(d.getApellido()).isEqualTo("PEREZ");        // upperNoAccents
        assertThat(d.getDni()).isEqualTo("12345678");          // digits only
        assertThat(d.getFoto()).isEqualTo("http://foto");      // trim + optional
        assertThat(d.getDescripcion()).isEqualTo("Texto de descripcion"); // trim
    }

    @Test
    void fromRequestListToDomainList_mapeaCadaElemento_conNormalizacionPrevia() {
        var reqs = List.of(
                new DesaparecidoRequestDTO("A","B",20,"1.1"," f1 "," d1 "),
                new DesaparecidoRequestDTO("C","D",21,"2"," f2 "," d2 ")
        );

        // normalizar cada elemento antes de mapear
        reqs.forEach(mapper::normalizeRequestInPlace);

        var out = mapper.fromRequestToDomain(reqs);
        assertThat(out).hasSize(2);
        assertThat(out.get(0).getNombre()).isEqualTo("A");
        assertThat(out.get(0).getDni()).isEqualTo("11");
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
    void normalizeRequestInPlace_esIdempotente() {
        var req = new DesaparecidoRequestDTO(
                "Ána", "Pérez", 30, "12.345.678", "  http://f  ",
                "  Texto  "
        );
        mapper.normalizeRequestInPlace(req);
        var snapshot = List.of(
                req.getNombre(), req.getApellido(), req.getDni(),
                req.getFotoUrl(), req.getDescripcion()
        );
        mapper.normalizeRequestInPlace(req);
        assertThat(List.of(
                req.getNombre(), req.getApellido(), req.getDni(),
                req.getFotoUrl(), req.getDescripcion()
        )).isEqualTo(snapshot);
    }
}
