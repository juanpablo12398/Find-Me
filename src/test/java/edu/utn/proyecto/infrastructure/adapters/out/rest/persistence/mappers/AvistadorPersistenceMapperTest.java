package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.AvistadorEntity;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class AvistadorPersistenceMapperTest {

    private final AvistadorPersistenceMapper mapper = new AvistadorPersistenceMapper();

    @Test
    void domainToEntity_maps_all_fields() {
        var now = LocalDateTime.now();
        var id = UUID.randomUUID();

        var d = new Avistador();
        d.setId(id);
        d.setDni("123");
        d.setNombre("Juan");
        d.setApellido("Perez");
        d.setEdad(33);
        d.setDireccion("Calle 1");
        d.setEmail("a@b.com");
        d.setTelefono("111-222");
        d.setCreadoEn(now);

        AvistadorEntity e = mapper.domainToEntity(d);

        assertEquals(id, e.getId());
        assertEquals("123", e.getDni());
        assertEquals("Juan", e.getNombre());
        assertEquals("Perez", e.getApellido());
        assertEquals(33, e.getEdad());
        assertEquals("Calle 1", e.getDireccion());
        assertEquals("a@b.com", e.getEmail());
        assertEquals("111-222", e.getTelefono());
        assertEquals(now, e.getCreadoEn());
    }

    @Test
    void entityToDomain_maps_all_fields() {
        var now = LocalDateTime.now();
        var id = UUID.randomUUID();

        var e = new AvistadorEntity();
        e.setId(id);
        e.setDni("321");
        e.setNombre("Ana");
        e.setApellido("Gomez");
        e.setEdad(28);
        e.setDireccion("Calle 2");
        e.setEmail("b@b.com");
        e.setTelefono("333-444");
        e.setCreadoEn(now);

        Avistador d = mapper.entityToDomain(e);

        assertEquals(id, d.getId());
        assertEquals("321", d.getDni());
        assertEquals("Ana", d.getNombre());
        assertEquals("Gomez", d.getApellido());
        assertEquals(28, d.getEdad());
        assertEquals("Calle 2", d.getDireccion());
        assertEquals("b@b.com", d.getEmail());
        assertEquals("333-444", d.getTelefono());
        assertEquals(now, d.getCreadoEn());
    }
}
