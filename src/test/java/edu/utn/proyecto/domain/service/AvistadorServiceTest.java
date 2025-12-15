package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.dtos.AvistadorResponseDTO;
import edu.utn.proyecto.applicacion.mappers.AvistadorMapper;
import edu.utn.proyecto.applicacion.validation.avistador.AvistadorRegistrationPolicy;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvistadorServiceTest {

    @Mock private IRepoDeAvistadores repo;
    @Mock private AvistadorMapper mapper;
    @Mock private AvistadorRegistrationPolicy policy;

    private AvistadorService service;

    @BeforeEach
    void setUp() {
        service = new AvistadorService(repo, mapper, policy);
    }

    @Test
    @DisplayName("registrar: normaliza, valida, mapea, guarda y devuelve response")
    void registrar_ok() {
        AvistadorRequestDTO req = new AvistadorRequestDTO();
        Avistador domain = new Avistador();
        Avistador saved = new Avistador();
        AvistadorResponseDTO resp = new AvistadorResponseDTO();

        when(mapper.fromRequestToDomain(req)).thenReturn(domain);
        when(repo.save(domain)).thenReturn(saved);
        when(mapper.fromDomainToResponse(saved)).thenReturn(resp);

        AvistadorResponseDTO out = service.registrar(req);

        assertSame(resp, out);

        InOrder io = inOrder(mapper, policy, repo, mapper);
        io.verify(mapper).normalizeRequestInPlace(req);
        io.verify(policy).validate(req);
        io.verify(mapper).fromRequestToDomain(req);
        io.verify(repo).save(domain);
        io.verify(mapper).fromDomainToResponse(saved);
        verifyNoMoreInteractions(mapper, policy, repo);
    }

    @Test
    @DisplayName("registrar: si la policy lanza, se propaga")
    void registrar_policyThrows_propagates() {
        AvistadorRequestDTO req = new AvistadorRequestDTO();
        doThrow(new RuntimeException("boom")).when(policy).validate(req);

        assertThrows(RuntimeException.class, () -> service.registrar(req));

        // Se hizo la normalización previa a la policy
        verify(mapper).normalizeRequestInPlace(req);
        // La policy se ejecutó y falló
        verify(policy).validate(req);

        // Nada más debe ocurrir
        verify(mapper, never()).fromRequestToDomain(any());
        verify(mapper, never()).fromDomainToResponse(any());
        verify(repo, never()).save(any());

        verifyNoMoreInteractions(mapper, policy);
        verifyNoInteractions(repo);
    }
}
