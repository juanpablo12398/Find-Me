package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers;
import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.DesaparecidoEntity;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class DesaparecidoPersistenceMapperTest {

    private final DesaparecidoPersistenceMapper mapper = new DesaparecidoPersistenceMapper();

    @Test
    void domainToEntity_and_back_maps_all_fields() {
        var id = UUID.randomUUID();
        var fecha = LocalDateTime.now().minusDays(5);

        var d = new Desaparecido("Juan","Perez","123","desc","foto.png");
        d.setId(id);
        d.setFechaDesaparicion(fecha);

        DesaparecidoEntity e = mapper.domainToEntity(d);

        assertEquals(id, e.getId());
        assertEquals("Juan", e.getNombre());
        assertEquals("Perez", e.getApellido());
        assertEquals("123", e.getDni());
        assertEquals("desc", e.getDescripcion());
        assertEquals("foto.png", e.getFotoUrl());
        assertEquals(fecha, e.getFechaDesaparicion());

        Desaparecido d2 = mapper.entityToDomain(e);

        assertEquals(id, d2.getId());
        assertEquals("Juan", d2.getNombre());
        assertEquals("Perez", d2.getApellido());
        assertEquals("123", d2.getDni());
        assertEquals("desc", d2.getDescripcion());
        assertEquals("foto.png", d2.getFoto());
        assertEquals(fecha, d2.getFechaDesaparicion());
    }
}
