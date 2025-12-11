package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.neonBase;
import edu.utn.proyecto.domain.model.concreta.Avistamiento;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.AvistamientoEntity;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers.AvistamientoPersistenceMapper;
import edu.utn.proyecto.infrastructure.ports.out.jpa.AvistamientoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RepositorioDeAvistamientosTest {

    private AvistamientoJpaRepository jpa;
    private AvistamientoPersistenceMapper mapper;
    private RepositorioDeAvistamientos repo;

    @BeforeEach
    void setUp() {
        jpa = mock(AvistamientoJpaRepository.class);
        mapper = mock(AvistamientoPersistenceMapper.class);
        repo = new RepositorioDeAvistamientos(jpa, mapper);
    }

    @Test
    void save_maps_roundtrip() {
        var domain = new Avistamiento();
        var entity = new AvistamientoEntity();
        var saved = new AvistamientoEntity();
        var mapped = new Avistamiento();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.toDomain(saved)).thenReturn(mapped);

        assertSame(mapped, repo.save(domain));
    }

    @Test
    void findById_maps_optional() {
        var id = UUID.randomUUID();
        var entity = new AvistamientoEntity();
        var mapped = new Avistamiento();

        when(jpa.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(mapped);

        var res = repo.findById(id);
        assertTrue(res.isPresent());
        assertSame(mapped, res.get());
    }

    @Test
    void findAll_maps_list() {
        when(jpa.findAll()).thenReturn(List.of(new AvistamientoEntity(), new AvistamientoEntity()));
        when(mapper.toDomain(any(AvistamientoEntity.class)))
                .thenReturn(new Avistamiento(), new Avistamiento());

        var list = repo.findAll();
        assertEquals(2, list.size());
        verify(mapper, times(2)).toDomain(any(AvistamientoEntity.class));
    }

    @Test
    void findByDesaparecidoId_maps() {
        var id = UUID.randomUUID();
        when(jpa.findByDesaparecidoId(id)).thenReturn(List.of(new AvistamientoEntity()));
        when(mapper.toDomain(any())).thenReturn(new Avistamiento());

        assertEquals(1, repo.findByDesaparecidoId(id).size());
    }

    @Test
    void findByAvistadorId_maps() {
        var id = UUID.randomUUID();
        when(jpa.findByAvistadorId(id)).thenReturn(List.of(new AvistamientoEntity()));
        when(mapper.toDomain(any())).thenReturn(new Avistamiento());

        assertEquals(1, repo.findByAvistadorId(id).size());
    }

    @Test
    void findPublicos_maps() {
        when(jpa.findByPublicoTrue()).thenReturn(List.of(new AvistamientoEntity()));
        when(mapper.toDomain(any())).thenReturn(new Avistamiento());

        assertEquals(1, repo.findPublicos().size());
    }

    @Test
    void findRecientes_maps() {
        var desde = LocalDateTime.now().minusDays(2);
        when(jpa.findRecientes(desde)).thenReturn(List.of(new AvistamientoEntity()));
        when(mapper.toDomain(any())).thenReturn(new Avistamiento());

        assertEquals(1, repo.findRecientes(desde).size());
    }

    @Test
    void deleteById_delegates() {
        var id = UUID.randomUUID();
        repo.deleteById(id);
        verify(jpa).deleteById(id);
    }

    @Test
    void findInBounds_maps() {
        when(jpa.findInBounds(-1.0, 1.0, -2.0, 2.0)).thenReturn(List.of(new AvistamientoEntity()));
        when(mapper.toDomain(any())).thenReturn(new Avistamiento());

        assertEquals(1, repo.findInBounds(-1.0, 1.0, -2.0, 2.0).size());
    }

    @Test
    void findWithinRadius_converts_km_to_m() {
        when(jpa.findWithinRadius(anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(List.of(new AvistamientoEntity()));
        when(mapper.toDomain(any())).thenReturn(new Avistamiento());

        repo.findWithinRadius(-34.6, -58.4, 5.0);
        verify(jpa).findWithinRadius(eq(-34.6), eq(-58.4), eq(5000.0));
    }

    @Test
    void findInPolygon_maps() {
        when(jpa.findInPolygon("POLY")).thenReturn(List.of(new AvistamientoEntity()));
        when(mapper.toDomain(any())).thenReturn(new Avistamiento());

        assertEquals(1, repo.findInPolygon("POLY").size());
    }

    @Test
    void findNearestN_maps() {
        when(jpa.findNearestN(-34.6, -58.4, 3)).thenReturn(List.of(new AvistamientoEntity()));
        when(mapper.toDomain(any())).thenReturn(new Avistamiento());

        assertEquals(1, repo.findNearestN(-34.6, -58.4, 3).size());
    }

    @Test
    void findByDesaparecidoWithinRadius_converts_km_to_m() {
        var id = UUID.randomUUID();
        when(jpa.findByDesaparecidoWithinRadius(any(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(List.of(new AvistamientoEntity()));
        when(mapper.toDomain(any())).thenReturn(new Avistamiento());

        repo.findByDesaparecidoWithinRadius(id, -34.6, -58.4, 1.2);
        verify(jpa).findByDesaparecidoWithinRadius(eq(id), eq(-34.6), eq(-58.4), eq(1200.0));
    }

    @Test
    void countInBounds_delegates_and_returns_value() {
        when(jpa.countInBounds(1.0, 2.0, 3.0, 4.0)).thenReturn(42L);
        assertEquals(42L, repo.countInBounds(1.0, 2.0, 3.0, 4.0));
    }
}
