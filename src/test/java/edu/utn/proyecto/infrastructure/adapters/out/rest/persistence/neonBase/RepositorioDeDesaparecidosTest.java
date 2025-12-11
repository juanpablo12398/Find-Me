package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.neonBase;
import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.DesaparecidoEntity;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers.DesaparecidoPersistenceMapper;
import edu.utn.proyecto.infrastructure.ports.out.jpa.DesaparecidoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RepositorioDeDesaparecidosTest {

    private DesaparecidoJpaRepository jpa;
    private DesaparecidoPersistenceMapper mapper;
    private RepositorioDeDesaparecidos repo;

    @BeforeEach
    void setUp() {
        jpa = mock(DesaparecidoJpaRepository.class);
        mapper = mock(DesaparecidoPersistenceMapper.class);
        repo = new RepositorioDeDesaparecidos(jpa, mapper);
    }

    @Test
    void save_maps_roundtrip() {
        var d = new Desaparecido("n","a","1","d","f");
        var e = new DesaparecidoEntity();
        var saved = new DesaparecidoEntity();
        var back = new Desaparecido("n","a","1","d","f");

        when(mapper.domainToEntity(d)).thenReturn(e);
        when(jpa.save(e)).thenReturn(saved);
        when(mapper.entityToDomain(saved)).thenReturn(back);

        assertSame(back, repo.save(d));
    }

    @Test
    void getDesaparecidos_maps_list() {
        when(jpa.findAll()).thenReturn(List.of(new DesaparecidoEntity(), new DesaparecidoEntity()));
        when(mapper.entityToDomain(any(DesaparecidoEntity.class)))
                .thenReturn(new Desaparecido("n","a","1","d","f"),
                        new Desaparecido("n","a","1","d","f"));

        assertEquals(2, repo.getDesaparecidos().size());
    }

    @Test
    void findById_maps_optional() {
        var id = UUID.randomUUID();
        var e = new DesaparecidoEntity();
        var d = new Desaparecido("n","a","1","d","f");

        when(jpa.findById(id)).thenReturn(Optional.of(e));
        when(mapper.entityToDomain(e)).thenReturn(d);

        var res = repo.findById(id);
        assertTrue(res.isPresent());
        assertSame(d, res.get());
    }

    @Test
    void existsByDni_delegates() {
        when(jpa.existsByDni("123")).thenReturn(true);
        assertTrue(repo.existsByDni("123"));
        verify(jpa).existsByDni("123");
    }
}
