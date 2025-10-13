package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.neonBase;
import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.DesaparecidoEntity;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers.DesaparecidoPersistenceMapper;
import edu.utn.proyecto.infrastructure.ports.out.jpa.DesaparecidoJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepositorioDeDesaparecidosTest {

    @Mock DesaparecidoJpaRepository jpa;
    @Mock DesaparecidoPersistenceMapper mapper;

    @Captor ArgumentCaptor<DesaparecidoEntity> entityCaptor;

    @Test
    void save_mapeaDomainAEntity_y_devuelveDomainMapeado() {
        var repo = new RepositorioDeDesaparecidos(jpa, mapper);

        var domain = new Desaparecido("Ana","Perez","123","desc","foto");
        var entity = new DesaparecidoEntity();
        var savedEntity = new DesaparecidoEntity();
        var mappedBack = new Desaparecido("Ana","Perez","123","desc","foto");
        mappedBack.setId(UUID.randomUUID());
        mappedBack.setFechaDesaparicion(LocalDateTime.of(2025,1,1,10,0));

        when(mapper.domainToEntity(domain)).thenReturn(entity);
        when(jpa.save(any(DesaparecidoEntity.class))).thenReturn(savedEntity);
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
    void getDesaparecidos_findAll_y_mapeaLista() {
        var repo = new RepositorioDeDesaparecidos(jpa, mapper);

        var e1 = new DesaparecidoEntity();
        var e2 = new DesaparecidoEntity();
        var d1 = new Desaparecido("A","B","1","d1","f1");
        var d2 = new Desaparecido("C","D","2","d2","f2");

        when(jpa.findAll()).thenReturn(List.of(e1, e2));
        when(mapper.entityToDomain(e1)).thenReturn(d1);
        when(mapper.entityToDomain(e2)).thenReturn(d2);

        var out = repo.getDesaparecidos();

        verify(jpa).findAll();
        verify(mapper).entityToDomain(e1);
        verify(mapper).entityToDomain(e2);
        verifyNoMoreInteractions(jpa, mapper);

        assertThat(out).containsExactly(d1, d2);
    }

    @Test
    void findById_delegaA_JPA_y_mapeaOptional() {
        var repo = new RepositorioDeDesaparecidos(jpa, mapper);
        var id = UUID.randomUUID();
        var entity = new DesaparecidoEntity();
        var mapped = new Desaparecido("A","B","1","d","f");

        when(jpa.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.entityToDomain(entity)).thenReturn(mapped);

        var out = repo.findById(id);

        assertThat(out).containsSame(mapped);
        verify(jpa).findById(id);
        verify(mapper).entityToDomain(entity);
        verifyNoMoreInteractions(jpa, mapper);
    }

    @Test
    void existsByDni_delegaA_JPA() {
        var repo = new RepositorioDeDesaparecidos(jpa, mapper);

        when(jpa.existsByDni("123")).thenReturn(true);
        assertThat(repo.existsByDni("123")).isTrue();

        when(jpa.existsByDni("123")).thenReturn(false);
        assertThat(repo.existsByDni("123")).isFalse();

        verify(jpa, times(2)).existsByDni("123");
        verifyNoInteractions(mapper);
    }
}
