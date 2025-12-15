package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.applicacion.mappers.DesaparecidoMapper;
import edu.utn.proyecto.applicacion.validation.desaparecido.DesaparecidoCreatePolicy;
import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
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
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesaparecidoServiceTest {

    @Mock private IRepoDeDesaparecidos repo;
    @Mock private DesaparecidoMapper mapper;
    @Mock private DesaparecidoCreatePolicy policy;

    private DesaparecidoService service;

    @BeforeEach
    void setUp() {
        service = new DesaparecidoService(repo, mapper, policy);
    }

    @Test
    @DisplayName("crearDesaparecido: normaliza, valida, guarda y mapea response")
    void crear_ok() {
        // Arrange
        DesaparecidoRequestDTO req = new DesaparecidoRequestDTO();
        Desaparecido dom   = new Desaparecido("N","A","1","D","F");
        Desaparecido saved = new Desaparecido("N","A","1","D","F");

        DesaparecidoResponseDTO resp = new DesaparecidoResponseDTO(
                UUID.randomUUID(),       // id
                "N",                     // nombre
                "A",                     // apellido
                "1",                     // dni
                LocalDateTime.of(2024,5,1,0,0), // fechaDesaparicion (o null si querés)
                "D",                     // descripcion
                "F"                      // foto
        );

        when(mapper.fromRequestToDomain(req)).thenReturn(dom);
        when(repo.save(dom)).thenReturn(saved);
        when(mapper.fromDomainToResponse(saved)).thenReturn(resp);

        // Act
        var out = service.crearDesaparecido(req);

        // Assert
        assertSame(resp, out);

        InOrder io = inOrder(mapper, policy, repo);
        io.verify(mapper).normalizeRequestInPlace(req);
        io.verify(policy).validate(req);
        io.verify(mapper).fromRequestToDomain(req);
        io.verify(repo).save(dom);
        io.verify(mapper).fromDomainToResponse(saved);
        verifyNoMoreInteractions(mapper, policy, repo);
    }

    @Test
    @DisplayName("obtenerDesaparecidos: trae lista del repo y la mapea")
    void obtener_ok() {
        // Arrange
        Desaparecido d1 = new Desaparecido("N1","A1","1","D1","F1");
        Desaparecido d2 = new Desaparecido("N2","A2","2","D2","F2");
        when(repo.getDesaparecidos()).thenReturn(List.of(d1, d2));

        DesaparecidoResponseDTO r1 = new DesaparecidoResponseDTO(
                UUID.randomUUID(),"N1","A1","1", null,"D1","F1"
        );
        DesaparecidoResponseDTO r2 = new DesaparecidoResponseDTO(
                UUID.randomUUID(),"N2","A2","2", null,"D2","F2"
        );

        when(mapper.fromDomainToResponse(d1)).thenReturn(r1);
        when(mapper.fromDomainToResponse(d2)).thenReturn(r2);

        // Act
        var out = service.obtenerDesaparecidos();

        // Assert
        assertEquals(2, out.size());
        assertEquals(List.of(r1, r2), out); // devuelve exactamente esas instancias

        verify(repo).getDesaparecidos();
        verify(mapper).fromDomainToResponse(d1);
        verify(mapper).fromDomainToResponse(d2);
        verifyNoMoreInteractions(repo, mapper);
    }
}
