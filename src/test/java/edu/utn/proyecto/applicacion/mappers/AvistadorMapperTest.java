package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.applicacion.dtos.AvistadorResponseDTO;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class AvistadorMapperTest {

    private final AvistadorMapper mapper =
            new AvistadorMapper(new DniNormalizer(), new TextNormalizer());

    @Test
    void normalizeRequestInPlace_normaliza_dni_nombres_y_campos_blandos() {
        AvistadorRequestDTO req = new AvistadorRequestDTO();
        req.setDni("  12.345.678 ");
        req.setNombre("  ána ");
        req.setApellido(" pérez  ");
        req.setEdad(28);
        req.setDireccion("  Calle 123  ");
        req.setEmail("  User@Mail.com  ");
        req.setTelefono("  111-222  ");

        mapper.normalizeRequestInPlace(req);

        assertThat(req.getDni()).isEqualTo("12345678");        // sin puntos/espacios
        assertThat(req.getNombre()).isEqualTo("Ana");          // sin acentos + Title Case (no UPPER)
        assertThat(req.getApellido()).isEqualTo("Perez");      // sin acentos + Title Case (no UPPER)
        assertThat(req.getDireccion()).isEqualTo("Calle 123"); // trim/compact
        assertThat(req.getEmail()).isEqualTo("User@Mail.com"); // trim (case se mantiene)
        assertThat(req.getTelefono()).isEqualTo("111-222");    // trim
        assertThat(req.getEdad()).isEqualTo(28);
    }

    @Test
    void fromRequestToDomain_copiaCampos_ySeteaCreadoEnDentroDeRango() {
        AvistadorRequestDTO req = new AvistadorRequestDTO();
        req.setDni("12345678");
        req.setNombre("Ana");
        req.setApellido("Perez");
        req.setEdad(28);
        req.setDireccion("Calle 123");
        req.setEmail("ana@x.com");
        req.setTelefono("111-222");

        LocalDateTime t0 = LocalDateTime.now();
        Avistador domain = mapper.fromRequestToDomain(req);
        LocalDateTime t1 = LocalDateTime.now();

        assertThat(domain.getDni()).isEqualTo("12345678");
        assertThat(domain.getNombre()).isEqualTo("Ana");
        assertThat(domain.getApellido()).isEqualTo("Perez");
        assertThat(domain.getEdad()).isEqualTo(28);
        assertThat(domain.getDireccion()).isEqualTo("Calle 123");
        assertThat(domain.getEmail()).isEqualTo("ana@x.com");
        assertThat(domain.getTelefono()).isEqualTo("111-222");

        assertThat(domain.getCreadoEn()).isNotNull();
        assertThat(domain.getCreadoEn()).isAfterOrEqualTo(t0);
        assertThat(domain.getCreadoEn()).isBeforeOrEqualTo(t1);
    }

    @Test
    void fromDomainToResponse_copiaTodosLosCampos() {
        Avistador domain = new Avistador();
        domain.setId(java.util.UUID.randomUUID());
        domain.setDni("12345678");
        domain.setNombre("Ana");
        domain.setApellido("Perez");
        domain.setEdad(28);
        domain.setDireccion("Calle 123");
        domain.setEmail("ana@x.com");
        domain.setTelefono("111-222");
        domain.setCreadoEn(LocalDateTime.of(2025, 1, 1, 12, 0));

        AvistadorResponseDTO resp = mapper.fromDomainToResponse(domain);

        assertThat(resp.getId()).isEqualTo(domain.getId());
        assertThat(resp.getDni()).isEqualTo("12345678");
        assertThat(resp.getNombre()).isEqualTo("Ana");
        assertThat(resp.getApellido()).isEqualTo("Perez");
        assertThat(resp.getEdad()).isEqualTo(28);
        assertThat(resp.getDireccion()).isEqualTo("Calle 123");
        assertThat(resp.getEmail()).isEqualTo("ana@x.com");
        assertThat(resp.getTelefono()).isEqualTo("111-222");
        assertThat(resp.getCreadoEn()).isEqualTo(LocalDateTime.of(2025, 1, 1, 12, 0));
    }
}