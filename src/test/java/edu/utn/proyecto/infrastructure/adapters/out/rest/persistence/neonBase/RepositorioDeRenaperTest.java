package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.neonBase;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import edu.utn.proyecto.infrastructure.ports.out.jpa.RenaperPersonaJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepositorioDeRenaperTest {

    private RenaperPersonaJpaRepository jpa;
    private RepositorioDeRenaper repo;

    @BeforeEach
    void setUp() {
        jpa = mock(RenaperPersonaJpaRepository.class);
        repo = new RepositorioDeRenaper(jpa);
    }

    @Test
    void findByDni_delegates_to_jpa() {
        var e = new RenaperPersonaEntity();
        when(jpa.findByDni("123")).thenReturn(Optional.of(e));

        var res = repo.findByDni("123");
        assertTrue(res.isPresent());
        assertSame(e, res.get());
        verify(jpa).findByDni("123");
    }
}
