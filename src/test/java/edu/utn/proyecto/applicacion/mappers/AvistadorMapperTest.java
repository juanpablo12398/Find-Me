package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.applicacion.dtos.AvistadorResponseDTO;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AvistadorMapperTest {

    private final AvistadorMapper mapper = new AvistadorMapper();

    @Test
    void fromRequestToDomain_copiaCampos_ySeteaCreadoEnAhora() {
        var req = new AvistadorRequestDTO();
        req.setDni("12345678");
        req.setNombre("ANA");
        req.setApellido("PEREZ");
        req.setEdad(28);
        req.setDireccion("Calle 123");
        req.setEmail("ana@x.com");
        req.setTelefono("111-222");

        var t0 = LocalDateTime.now();
        Avistador domain = mapper.fromRequestToDomain(req);
        var t1 = LocalDateTime.now();

        assertThat(domain.getDni()).isEqualTo("12345678");
        assertThat(domain.getNombre()).isEqualTo("ANA");
        assertThat(domain.getApellido()).isEqualTo("PEREZ");
        assertThat(domain.getEdad()).isEqualTo(28);
        assertThat(domain.getDireccion()).isEqualTo("Calle 123");
        assertThat(domain.getEmail()).isEqualTo("ana@x.com");
        assertThat(domain.getTelefono()).isEqualTo("111-222");
        assertThat(domain.getCreadoEn()).isNotNull();
        // dentro de una ventana razonable
        assertThat(!domain.getCreadoEn().isBefore(t0) && !domain.getCreadoEn().isAfter(t1))
                .as("creadoEn debe estar entre t0 y t1")
                .isTrue();
        // o: assertThat(Duration.between(t0, domain.getCreadoEn())).isLessThan(Duration.ofSeconds(2));
    }

    @Test
    void fromDomainToResponse_copiaTodosLosCampos() {
        var domain = new Avistador();
        domain.setId(java.util.UUID.randomUUID());
        domain.setDni("12345678");
        domain.setNombre("ANA");
        domain.setApellido("PEREZ");
        domain.setEdad(28);
        domain.setDireccion("Calle 123");
        domain.setEmail("ana@x.com");
        domain.setTelefono("111-222");
        domain.setCreadoEn(LocalDateTime.of(2025,1,1,12,0));

        AvistadorResponseDTO resp = mapper.fromDomainToResponse(domain);

        assertThat(resp.getId()).isEqualTo(domain.getId());
        assertThat(resp.getDni()).isEqualTo("12345678");
        assertThat(resp.getNombre()).isEqualTo("ANA");
        assertThat(resp.getApellido()).isEqualTo("PEREZ");
        assertThat(resp.getEdad()).isEqualTo(28);
        assertThat(resp.getDireccion()).isEqualTo("Calle 123");
        assertThat(resp.getEmail()).isEqualTo("ana@x.com");
        assertThat(resp.getTelefono()).isEqualTo("111-222");
        assertThat(resp.getCreadoEn()).isEqualTo(LocalDateTime.of(2025,1,1,12,0));
    }
}
