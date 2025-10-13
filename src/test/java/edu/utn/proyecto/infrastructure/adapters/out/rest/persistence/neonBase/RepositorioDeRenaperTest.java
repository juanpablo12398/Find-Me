package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.neonBase;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import edu.utn.proyecto.infrastructure.ports.out.jpa.RenaperPersonaJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepositorioDeRenaperTest {

    @Mock RenaperPersonaJpaRepository jpa;

    @Test
    void findByDni_delegaA_JPA_y_retornaOptional() {
        var repo = new RepositorioDeRenaper(jpa);
        var e = new RenaperPersonaEntity();

        when(jpa.findByDni("123")).thenReturn(Optional.of(e));
        assertThat(repo.findByDni("123")).containsSame(e);

        when(jpa.findByDni("123")).thenReturn(Optional.empty());
        assertThat(repo.findByDni("123")).isEmpty();

        verify(jpa, times(2)).findByDni("123");
    }
}
