package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.neonBase;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.AvistadorEntity;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers.AvistadorPersistenceMapper;
import edu.utn.proyecto.infrastructure.ports.out.jpa.AvistadorJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepositorioDeAvistadoresTest {

    @Mock AvistadorJpaRepository jpa;
    @Mock AvistadorPersistenceMapper mapper;

    @Captor ArgumentCaptor<AvistadorEntity> entityCaptor;

    @Test
    void save_mapeaDomainAEntity_guardaYDevuelveDomainMapeado() {
        var repo = new RepositorioDeAvistadores(jpa, mapper);

        var domain = new Avistador(
                UUID.randomUUID(), "12345678", "ANA", "PEREZ",
                28, "Calle 1", "a@a.com", "111",
                LocalDateTime.now()
        );
        var entity = new AvistadorEntity();
        var savedEntity = new AvistadorEntity(); // lo que devuelve JPA
        var mappedBack = new Avistador(
                domain.getId(), domain.getDni(), domain.getNombre(), domain.getApellido(),
                domain.getEdad(), domain.getDireccion(), domain.getEmail(), domain.getTelefono(),
                domain.getCreadoEn()
        );

        when(mapper.domainToEntity(domain)).thenReturn(entity);
        when(jpa.save(any(AvistadorEntity.class))).thenReturn(savedEntity);
        when(mapper.entityToDomain(savedEntity)).thenReturn(mappedBack);

        var out = repo.save(domain);

        verify(mapper).domainToEntity(domain);
        verify(jpa).save(entityCaptor.capture());
        verify(mapper).entityToDomain(savedEntity);
        verifyNoMoreInteractions(jpa, mapper);

        assertThat(entityCaptor.getValue()).isSameAs(entity);
        assertThat(out).isSameAs(mappedBack);
    }

    @Test
    void findByDni_delegaA_JPA_y_mapeaOptional() {
        var repo = new RepositorioDeAvistadores(jpa, mapper);

        var entity = new AvistadorEntity();
        var mapped = new Avistador();
        when(jpa.findByDni("123")).thenReturn(Optional.of(entity));
        when(mapper.entityToDomain(entity)).thenReturn(mapped);

        var out = repo.findByDni("123");

        assertThat(out).containsSame(mapped);
        verify(jpa).findByDni("123");
        verify(mapper).entityToDomain(entity);
        verifyNoMoreInteractions(jpa, mapper);
    }

    @Test
    void existsByDni_true_siJPAEncuentra() {
        var repo = new RepositorioDeAvistadores(jpa, mapper);
        when(jpa.findByDni("123")).thenReturn(Optional.of(new AvistadorEntity()));

        assertThat(repo.existsByDni("123")).isTrue();
        verify(jpa).findByDni("123");
        verifyNoInteractions(mapper);
    }

    @Test
    void existsByDni_false_siJPADevuelveEmpty() {
        var repo = new RepositorioDeAvistadores(jpa, mapper);
        when(jpa.findByDni("123")).thenReturn(Optional.empty());

        assertThat(repo.existsByDni("123")).isFalse();
        verify(jpa).findByDni("123");
        verifyNoInteractions(mapper);
    }
}
