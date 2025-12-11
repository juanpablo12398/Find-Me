package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers;
import edu.utn.proyecto.domain.model.concreta.Avistamiento;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.AvistamientoEntity;
import edu.utn.proyecto.infrastructure.normalize.GeometryNormalizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AvistamientoPersistenceMapperTest {

    private GeometryNormalizer geom;
    private AvistamientoPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        geom = mock(GeometryNormalizer.class);
        mapper = new AvistamientoPersistenceMapper(geom);
    }

    @Test
    void toDomain_maps_all_fields_and_reads_latlng_from_point() {
        var now = LocalDateTime.now();
        var id = UUID.randomUUID();

        var entity = new AvistamientoEntity();
        entity.setId(id);
        entity.setAvistadorId(UUID.randomUUID());
        entity.setDesaparecidoId(UUID.randomUUID());
        entity.setUbicacion(null); // stub: no necesitamos un Point real
        entity.setFechaHora(now);
        entity.setDescripcion("desc");
        entity.setFotoUrl("u.jpg");
        entity.setVerificado(true);
        entity.setPublico(false);
        entity.setCreadoEn(now.minusDays(1));

        when(geom.fromPointToLatitud(null)).thenReturn(-34.6);
        when(geom.fromPointToLongitud(null)).thenReturn(-58.38);

        Avistamiento d = mapper.toDomain(entity);

        assertEquals(id, d.getId());
        assertEquals(entity.getAvistadorId(), d.getAvistadorId());
        assertEquals(entity.getDesaparecidoId(), d.getDesaparecidoId());
        assertEquals(-34.6, d.getLatitud());
        assertEquals(-58.38, d.getLongitud());
        assertEquals(now, d.getFechaHora());
        assertEquals("desc", d.getDescripcion());
        assertEquals("u.jpg", d.getFotoUrl());
        assertTrue(d.getVerificado());
        assertFalse(d.getPublico());
        assertEquals(now.minusDays(1), d.getCreadoEn());
    }

    @Test
    void toEntity_maps_all_fields_and_builds_point_from_latlng() {
        var now = LocalDateTime.now();

        var d = new Avistamiento();
        d.setId(UUID.randomUUID());
        d.setAvistadorId(UUID.randomUUID());
        d.setDesaparecidoId(UUID.randomUUID());
        d.setLatitud(-34.61);
        d.setLongitud(-58.39);
        d.setFechaHora(now);
        d.setDescripcion("desc2");
        d.setFotoUrl("v.jpg");
        d.setVerificado(false);
        d.setPublico(true);
        d.setCreadoEn(now.minusHours(3));

        // Devolvemos null: es válido para cualquier tipo de Point y nos permite verificar la llamada
        when(geom.toPoint(d.getLatitud(), d.getLongitud())).thenReturn(null);

        AvistamientoEntity e = mapper.toEntity(d);

        verify(geom).toPoint(eq(-34.61), eq(-58.39));

        assertEquals(d.getId(), e.getId());
        assertEquals(d.getAvistadorId(), e.getAvistadorId());
        assertEquals(d.getDesaparecidoId(), e.getDesaparecidoId());
        assertNull(e.getUbicacion()); // viene del mock
        assertEquals(now, e.getFechaHora());
        assertEquals("desc2", e.getDescripcion());
        assertEquals("v.jpg", e.getFotoUrl());
        assertFalse(e.getVerificado());
        assertTrue(e.getPublico());
        assertEquals(now.minusHours(3), e.getCreadoEn());
    }
}
