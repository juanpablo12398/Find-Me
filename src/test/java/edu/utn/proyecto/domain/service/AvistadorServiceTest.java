package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.dtos.AvistadorResponseDTO;
import edu.utn.proyecto.applicacion.mappers.AvistadorMapper;
import edu.utn.proyecto.applicacion.validation.avistador.AvistadorRegistrationPolicy;
import edu.utn.proyecto.avistador.exception.AvistadorError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvistadorServiceTest {

    @Mock IRepoDeAvistadores repo;
    @Mock AvistadorMapper mapper;
    @Mock AvistadorRegistrationPolicy policy;

    @Captor ArgumentCaptor<Avistador> avistadorCaptor;

    @Test
    void registrar_orquesta_validate_mapear_save_y_mapearResponse() {
        var service = new AvistadorService(repo, mapper, policy);

        var req = new AvistadorRequestDTO();
        req.setDni("12345678");
        req.setNombre("ANA");
        req.setApellido("PEREZ");
        req.setEdad(28);
        req.setDireccion("Calle 1");
        req.setEmail("a@a.com");
        req.setTelefono("111");

        var domain = new Avistador(
                UUID.randomUUID(), "12345678", "ANA", "PEREZ",
                28, "Calle 1", "a@a.com", "111", LocalDateTime.now()
        );

        var saved = new Avistador(
                domain.getId(), domain.getDni(), domain.getNombre(), domain.getApellido(),
                domain.getEdad(), domain.getDireccion(), domain.getEmail(), domain.getTelefono(),
                domain.getCreadoEn()
        );

        var expected = new AvistadorResponseDTO();
        expected.setId(saved.getId());
        expected.setDni(saved.getDni());
        expected.setNombre(saved.getNombre());
        expected.setApellido(saved.getApellido());

        when(mapper.fromRequestToDomain(req)).thenReturn(domain);
        when(repo.save(any(Avistador.class))).thenReturn(saved);
        when(mapper.fromDomainToResponse(saved)).thenReturn(expected);

        var out = service.registrar(req);

        InOrder io = inOrder(policy, mapper, repo, mapper);
        io.verify(policy).validate(req);
        io.verify(mapper).fromRequestToDomain(req);
        io.verify(repo).save(avistadorCaptor.capture());
        io.verify(mapper).fromDomainToResponse(saved);
        io.verifyNoMoreInteractions();

        assertThat(avistadorCaptor.getValue().getDni()).isEqualTo("12345678");
        assertThat(out).isSameAs(expected);
    }

    @Test
    void registrar_siPolicyFalla_noMapeaNiGuarda_yPropagaDomainException() {
        var service = new AvistadorService(repo, mapper, policy);
        var req = new AvistadorRequestDTO();

        doThrow(DomainException.of(AvistadorError.UNDERAGE.key,
                AvistadorError.UNDERAGE.status,
                "Debe ser mayor de edad"))
                .when(policy).validate(req);

        assertThatThrownBy(() -> service.registrar(req))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> {
                    var de = (DomainException) ex;
                    assertThat(de.getKey()).isEqualTo(AvistadorError.UNDERAGE.key);
                    assertThat(de.getStatus()).isEqualTo(AvistadorError.UNDERAGE.status);
                });

        verify(policy).validate(req);
        verifyNoInteractions(mapper, repo);
    }
}
