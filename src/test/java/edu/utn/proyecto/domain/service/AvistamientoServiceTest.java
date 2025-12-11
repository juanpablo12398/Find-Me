package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.dtos.AvistamientoResponseDTO;
import edu.utn.proyecto.applicacion.mappers.AvistamientoMapper;
import edu.utn.proyecto.applicacion.validation.avistamiento.AvistamientoCreatePolicy;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.domain.model.concreta.Avistamiento;
import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoFrontDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.AvistadorEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistamientos;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvistamientoServiceTest {

    @Mock private IRepoDeAvistamientos repoAv;
    @Mock private IRepoDeAvistadores repoAvistadores;
    @Mock private IRepoDeDesaparecidos repoDesap;
    @Mock private AvistamientoMapper mapper;
    @Mock private AvistamientoCreatePolicy policy;

    private AvistamientoService service;

    @BeforeEach
    void setUp() {
        service = new AvistamientoService(repoAv, repoAvistadores, repoDesap, mapper, policy);
    }

    @Test
    @DisplayName("crearAvistamiento: normaliza, valida, guarda y mapea response")
    void crearAvistamiento_ok() {
        AvistamientoRequestDTO req = new AvistamientoRequestDTO();
        Avistamiento domain = new Avistamiento();
        Avistamiento saved = new Avistamiento();
        AvistamientoResponseDTO resp = new AvistamientoResponseDTO();

        when(mapper.fromRequestToDomain(req)).thenReturn(domain);
        when(repoAv.save(domain)).thenReturn(saved);
        when(mapper.fromDomainToResponse(saved)).thenReturn(resp);

        AvistamientoResponseDTO out = service.crearAvistamiento(req);
        assertSame(resp, out);

        InOrder io = inOrder(mapper, policy, repoAv, mapper);
        io.verify(mapper).normalizeRequestInPlace(req);
        io.verify(policy).validate(req);
        io.verify(mapper).fromRequestToDomain(req);
        io.verify(repoAv).save(domain);
        io.verify(mapper).fromDomainToResponse(saved);
        verifyNoMoreInteractions(mapper, policy, repoAv);
    }

    @Test
    @DisplayName("obtenerAvistamientosPublicos: lista → map to DTO")
    void obtenerPublicos_ok() {
        Avistamiento a1 = new Avistamiento();
        Avistamiento a2 = new Avistamiento();
        when(repoAv.findPublicos()).thenReturn(List.of(a1, a2));

        AvistamientoResponseDTO r1 = new AvistamientoResponseDTO();
        AvistamientoResponseDTO r2 = new AvistamientoResponseDTO();
        when(mapper.fromDomainListToResponseList(List.of(a1, a2))).thenReturn(List.of(r1, r2));

        var out = service.obtenerAvistamientosPublicos();
        assertEquals(2, out.size());
        verify(repoAv).findPublicos();
        verify(mapper).fromDomainListToResponseList(List.of(a1, a2));
    }

    @Test
    @DisplayName("obtenerAvistamientosRecientes: calcula 'desde' y mapea")
    void obtenerRecientes_ok() {
        when(repoAv.findRecientes(any(LocalDateTime.class))).thenReturn(List.of());
        when(mapper.fromDomainListToResponseList(List.of())).thenReturn(List.of());

        var out = service.obtenerAvistamientosRecientes(3);
        assertNotNull(out);
        verify(repoAv).findRecientes(any(LocalDateTime.class));
        verify(mapper).fromDomainListToResponseList(List.of());
    }

    @Test
    @DisplayName("obtenerParaMapa: enriquece con datos de desaparecido y avistador")
    void obtenerParaMapa_enrich_ok() {
        UUID avistadorId = UUID.randomUUID();
        UUID desaparecidoId = UUID.randomUUID();

        Avistamiento a = new Avistamiento();
        a.setAvistadorId(avistadorId);
        a.setDesaparecidoId(desaparecidoId);
        when(repoAv.findPublicos()).thenReturn(List.of(a));

        // Desaparecido (dominio)
        Desaparecido d = new Desaparecido("N", "A", "1", "D", "F");
        d.setId(desaparecidoId);
        when(repoDesap.findById(desaparecidoId)).thenReturn(Optional.of(d));

        // Avistador **ENTITY** con nombre seteado (NO mock)
        AvistadorEntity avE = new AvistadorEntity();
        avE.setId(avistadorId);
        avE.setNombre("AVIST");
        when(repoAvistadores.findById(avistadorId)).thenReturn(Optional.of(avE));

        AvistamientoFrontDTO base =
                new AvistamientoFrontDTO(null, 0.0, 0.0, "ts", "desc", "foto", false);
        when(mapper.fromDomainToFrontBasic(a)).thenReturn(base);

        var out = service.obtenerParaMapa();

        assertEquals(1, out.size());
        var f = out.get(0);
        assertEquals(desaparecidoId, f.getDesaparecidoId());
        assertEquals("N", f.getDesaparecidoNombre());
        assertEquals("A", f.getDesaparecidoApellido());
        assertEquals("F", f.getDesaparecidoFoto());
        assertEquals("AVIST", f.getAvistadorNombre());
    }

    @Test
    @DisplayName("obtenerParaMapa: si falta el desaparecido, lanza")
    void obtenerParaMapa_missingDesaparecido_throws() {
        Avistamiento a = new Avistamiento();
        a.setDesaparecidoId(UUID.randomUUID());
        a.setAvistadorId(UUID.randomUUID());

        when(repoAv.findPublicos()).thenReturn(List.of(a));
        when(repoDesap.findById(a.getDesaparecidoId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.obtenerParaMapa());
    }
}
