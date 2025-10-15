package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.applicacion.mappers.DesaparecidoMapper;
import edu.utn.proyecto.applicacion.validation.desaparecido.DesaparecidoCreatePolicy;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.desaparecido.exception.DesaparecidoError;
import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesaparecidoServiceTest {

    @Mock IRepoDeDesaparecidos repo;
    @Mock DesaparecidoMapper mapper;
    @Mock DesaparecidoCreatePolicy policy;

    @Captor ArgumentCaptor<Desaparecido> desaparecidoCaptor;

    @Test
    void crearDesaparecido_orquesta_validate_mapear_save_y_mapearResponse() {
        var service = new DesaparecidoService(repo, mapper, policy);

        var req = new DesaparecidoRequestDTO("Ana","Perez",30,"12345678","foto","desc");
        var domain = new Desaparecido("Ana","Perez","12345678","desc","foto");
        var saved = new Desaparecido("Ana","Perez","12345678","desc","foto");
        saved.setId(UUID.randomUUID());
        saved.setFechaDesaparicion(LocalDateTime.of(2025, 1, 1, 10, 0));

        var expected = new DesaparecidoResponseDTO(
                saved.getId(), "Ana","Perez","12345678",
                saved.getFechaDesaparicion(),"desc","foto"
        );

        when(mapper.fromRequestToDomain(req)).thenReturn(domain);
        when(repo.save(any(Desaparecido.class))).thenReturn(saved);
        when(mapper.fromDomainToResponse(saved)).thenReturn(expected);

        var out = service.crearDesaparecido(req);

        InOrder io = inOrder(policy, mapper, repo, mapper);
        io.verify(policy).validate(req);
        io.verify(mapper).fromRequestToDomain(req);
        io.verify(repo).save(desaparecidoCaptor.capture());
        io.verify(mapper).fromDomainToResponse(saved);
        io.verifyNoMoreInteractions();

        assertThat(desaparecidoCaptor.getValue().getDni()).isEqualTo("12345678");
        assertThat(out).isSameAs(expected);
    }

    @Test
    void crearDesaparecido_siPolicyFalla_normaliza_pero_noMapeaNiGuarda_yPropagaDomainException() {
        var service = new DesaparecidoService(repo, mapper, policy);
        var req = new DesaparecidoRequestDTO("A","B",20,"1","f","d");

        doThrow(DomainException.of(DesaparecidoError.DNI_DUP.key,
                DesaparecidoError.DNI_DUP.status, "Duplicado"))
                .when(policy).validate(req);

        assertThatThrownBy(() -> service.crearDesaparecido(req))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> {
                    var de = (DomainException) ex;
                    assertThat(de.getKey()).isEqualTo(DesaparecidoError.DNI_DUP.key);
                    assertThat(de.getStatus()).isEqualTo(DesaparecidoError.DNI_DUP.status);
                });

        InOrder io = inOrder(mapper, policy);
        io.verify(mapper).normalizeRequestInPlace(req);
        io.verify(policy).validate(req);

        verifyNoInteractions(repo);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void obtenerDesaparecidos_leeRepo_y_mapeaLista() {
        var service = new DesaparecidoService(repo, mapper, policy);

        var d1 = new Desaparecido("A","B","1","d1","f1"); d1.setId(UUID.randomUUID());
        var d2 = new Desaparecido("C","D","2","d2","f2"); d2.setId(UUID.randomUUID());

        var r1 = new DesaparecidoResponseDTO(d1.getId(),"A","B","1", LocalDateTime.now(),"d1","f1");
        var r2 = new DesaparecidoResponseDTO(d2.getId(),"C","D","2", LocalDateTime.now(),"d2","f2");

        when(repo.getDesaparecidos()).thenReturn(List.of(d1, d2));
        when(mapper.fromDomainToResponse(d1)).thenReturn(r1);
        when(mapper.fromDomainToResponse(d2)).thenReturn(r2);

        var out = service.obtenerDesaparecidos();

        verify(repo).getDesaparecidos();
        verify(mapper).fromDomainToResponse(d1);
        verify(mapper).fromDomainToResponse(d2);
        verifyNoMoreInteractions(repo, mapper);

        assertThat(out).containsExactly(r1, r2);
    }
}
