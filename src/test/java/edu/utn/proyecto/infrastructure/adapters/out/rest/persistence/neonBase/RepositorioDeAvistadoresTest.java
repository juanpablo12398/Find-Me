package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.neonBase;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.AvistadorEntity;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers.AvistadorPersistenceMapper;
import edu.utn.proyecto.infrastructure.ports.out.jpa.AvistadorJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepositorioDeAvistadoresTest {

    private AvistadorJpaRepository jpa;
    private AvistadorPersistenceMapper mapper;
    private RepositorioDeAvistadores repo;

    @BeforeEach
    void setUp() {
        jpa = mock(AvistadorJpaRepository.class);
        mapper = mock(AvistadorPersistenceMapper.class);
        repo = new RepositorioDeAvistadores(jpa, mapper);
    }

    @Test
    void save_maps_and_returns_domain() {
        var domain = new Avistador();
        var entity = new AvistadorEntity();
        var saved = new AvistadorEntity();
        var mappedBack = new Avistador();

        when(mapper.domainToEntity(domain)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(saved);
        when(mapper.entityToDomain(saved)).thenReturn(mappedBack);

        var result = repo.save(domain);
        assertSame(mappedBack, result);

        InOrder order = inOrder(mapper, jpa);
        order.verify(mapper).domainToEntity(domain);
        order.verify(jpa).save(entity);
        order.verify(mapper).entityToDomain(saved);
    }

    @Test
    void findByDni_present_maps() {
        var entity = new AvistadorEntity();
        var mapped = new Avistador();
        when(jpa.findByDni("123")).thenReturn(Optional.of(entity));
        when(mapper.entityToDomain(entity)).thenReturn(mapped);

        var opt = repo.findByDni("123");
        assertTrue(opt.isPresent());
        assertSame(mapped, opt.get());
    }

    @Test
    void findByDni_empty() {
        when(jpa.findByDni("x")).thenReturn(Optional.empty());
        assertTrue(repo.findByDni("x").isEmpty());
    }

    @Test
    void existsByDni_true_and_false() {
        when(jpa.findByDni("1")).thenReturn(Optional.of(new AvistadorEntity()));
        when(jpa.findByDni("2")).thenReturn(Optional.empty());

        assertTrue(repo.existsByDni("1"));
        assertFalse(repo.existsByDni("2"));
    }

    @Test
    void findById_delegates() {
        var id = UUID.randomUUID();
        var entity = new AvistadorEntity();
        when(jpa.findById(id)).thenReturn(Optional.of(entity));

        var res = repo.findById(id);
        assertTrue(res.isPresent());
        assertSame(entity, res.get());
    }
}
