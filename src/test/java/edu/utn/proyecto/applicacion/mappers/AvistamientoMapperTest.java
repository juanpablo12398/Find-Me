package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.applicacion.dtos.AvistamientoResponseDTO;
import edu.utn.proyecto.common.normalize.DateTimeNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.common.normalize.UrlNormalizer;
import edu.utn.proyecto.domain.model.concreta.Avistamiento;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoFrontDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvistamientoMapperTest {

    @Mock private TextNormalizer textNormalizer;
    @Mock private UrlNormalizer urlNormalizer;
    @Mock private DateTimeNormalizer dateTimeNormalizer;

    private AvistamientoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AvistamientoMapper(textNormalizer, urlNormalizer, dateTimeNormalizer);
    }

    // ========= normalizeRequestInPlace =========

    @Test
    @DisplayName("normalizeRequestInPlace: normaliza descripción (sentenceCase) y URL (normalizeOptional)")
    void normalizeRequestInPlace_ok() {
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setDescripcion("   hola MUNDO  ");
        dto.setFotoUrl("  HTTP://EXAMPLE.com/IMG.JPG  ");

        when(textNormalizer.sentenceCase("   hola MUNDO  ")).thenReturn("Hola mundo");
        when(urlNormalizer.normalizeOptional("  HTTP://EXAMPLE.com/IMG.JPG  "))
                .thenReturn("http://example.com/img.jpg");

        mapper.normalizeRequestInPlace(dto);

        assertEquals("Hola mundo", dto.getDescripcion());
        assertEquals("http://example.com/img.jpg", dto.getFotoUrl());

        InOrder inOrder = inOrder(textNormalizer, urlNormalizer);
        inOrder.verify(textNormalizer).sentenceCase("   hola MUNDO  ");
        inOrder.verify(urlNormalizer).normalizeOptional("  HTTP://EXAMPLE.com/IMG.JPG  ");
        verifyNoMoreInteractions(textNormalizer, urlNormalizer);
        verifyNoInteractions(dateTimeNormalizer);
    }

    @Test
    @DisplayName("normalizeRequestInPlace: soporta valores null (propaga null sin romper)")
    void normalizeRequestInPlace_nulls() {
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setDescripcion(null);
        dto.setFotoUrl(null);

        mapper.normalizeRequestInPlace(dto);

        assertNull(dto.getDescripcion());
        assertNull(dto.getFotoUrl());

        verify(textNormalizer).sentenceCase(null);
        verify(urlNormalizer).normalizeOptional(null);
        verifyNoMoreInteractions(textNormalizer, urlNormalizer);
        verifyNoInteractions(dateTimeNormalizer);
    }

    // ========= fromRequestToDomain =========

    @Test
    @DisplayName("fromRequestToDomain: mapea campos y cuando 'publico' es null, default true")
    void fromRequestToDomain_publicoNullDefaultsTrue() {
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        dto.setAvistadorId(UUID.randomUUID().toString());
        dto.setDesaparecidoId(UUID.randomUUID().toString());
        dto.setLatitud(-34.60);
        dto.setLongitud(-58.38);
        dto.setDescripcion("Desc");
        dto.setFotoUrl("http://img");
        dto.setPublico(null);

        Avistamiento a = mapper.fromRequestToDomain(dto);

        assertEquals(dto.getLatitud(), a.getLatitud());
        assertEquals(dto.getLongitud(), a.getLongitud());
        assertEquals(dto.getDescripcion(), a.getDescripcion());
        assertEquals(dto.getFotoUrl(), a.getFotoUrl());
        assertTrue(a.getPublico(), "Si dto.publico es null, debe quedar true");
        assertEquals(UUID.fromString(dto.getAvistadorId()), a.getAvistadorId());
        assertEquals(UUID.fromString(dto.getDesaparecidoId()), a.getDesaparecidoId());

        verifyNoInteractions(textNormalizer, urlNormalizer, dateTimeNormalizer);
    }

    @Test
    @DisplayName("fromRequestToDomain: respeta 'publico=false' y 'publico=true'")
    void fromRequestToDomain_publicoExplicit() {
        AvistamientoRequestDTO dtoFalse = new AvistamientoRequestDTO();
        dtoFalse.setAvistadorId(UUID.randomUUID().toString());
        dtoFalse.setDesaparecidoId(UUID.randomUUID().toString());
        dtoFalse.setPublico(false);

        Avistamiento aFalse = mapper.fromRequestToDomain(dtoFalse);
        assertFalse(aFalse.getPublico());

        AvistamientoRequestDTO dtoTrue = new AvistamientoRequestDTO();
        dtoTrue.setAvistadorId(UUID.randomUUID().toString());
        dtoTrue.setDesaparecidoId(UUID.randomUUID().toString());
        dtoTrue.setPublico(true);

        Avistamiento aTrue = mapper.fromRequestToDomain(dtoTrue);
        assertTrue(aTrue.getPublico());
    }

    @Test
    @DisplayName("fromRequestToDomain: UUID inválido lanza IllegalArgumentException (avistadorId y/o desaparecidoId)")
    void fromRequestToDomain_invalidUuid_throws() {
        AvistamientoRequestDTO dto1 = new AvistamientoRequestDTO();
        dto1.setAvistadorId("no-uuid");
        dto1.setDesaparecidoId(UUID.randomUUID().toString());
        assertThrows(IllegalArgumentException.class, () -> mapper.fromRequestToDomain(dto1));

        AvistamientoRequestDTO dto2 = new AvistamientoRequestDTO();
        dto2.setAvistadorId(UUID.randomUUID().toString());
        dto2.setDesaparecidoId("bad-uuid");
        assertThrows(IllegalArgumentException.class, () -> mapper.fromRequestToDomain(dto2));
    }

    // ========= fromDomainToFrontBasic =========

    @Test
    @DisplayName("fromDomainToFrontBasic: usa DateTimeNormalizer y mapea campos básicos")
    void fromDomainToFrontBasic_ok() {
        LocalDateTime fh = LocalDateTime.of(2025, 11, 8, 12, 30);
        Avistamiento a = new Avistamiento();
        UUID id = UUID.randomUUID();
        a.setId(id);
        a.setLatitud(-34.61);
        a.setLongitud(-58.39);
        a.setFechaHora(fh);
        a.setDescripcion("Algo visto");
        a.setFotoUrl("http://img");
        a.setVerificado(Boolean.TRUE);

        when(dateTimeNormalizer.formatForDisplay(fh)).thenReturn("08/11/2025 12:30");

        AvistamientoFrontDTO front = mapper.fromDomainToFrontBasic(a);

        assertEquals(id, front.getId());
        assertEquals(-34.61, front.getLatitud());
        assertEquals(-58.39, front.getLongitud());
        assertEquals("Algo visto", front.getDescripcion());
        assertEquals("http://img", front.getFotoUrl());
        assertEquals(Boolean.TRUE, front.getVerificado());
        assertEquals("08/11/2025 12:30", front.getFechaFormateada());

        verify(dateTimeNormalizer).formatForDisplay(fh);
        verifyNoMoreInteractions(dateTimeNormalizer);
        verifyNoInteractions(textNormalizer, urlNormalizer);
    }

    // ========= fromDomainToResponse / List =========

    @Test
    @DisplayName("fromDomainToResponse: mapea todos los campos incluyendo verificado, publico y creadoEn")
    void fromDomainToResponse_ok() {
        Avistamiento a = new Avistamiento();
        UUID id = UUID.randomUUID();
        UUID avId = UUID.randomUUID();
        UUID desId = UUID.randomUUID();
        LocalDateTime fh = LocalDateTime.now().minusHours(2);
        LocalDateTime created = LocalDateTime.now().minusHours(3);

        a.setId(id);
        a.setAvistadorId(avId);
        a.setDesaparecidoId(desId);
        a.setLatitud(-34.5);
        a.setLongitud(-58.4);
        a.setFechaHora(fh);
        a.setDescripcion("desc");
        a.setFotoUrl("url");
        a.setVerificado(Boolean.FALSE);
        a.setPublico(Boolean.TRUE);
        a.setCreadoEn(created);

        AvistamientoResponseDTO dto = mapper.fromDomainToResponse(a);

        assertEquals(id, dto.getId());
        assertEquals(avId, dto.getAvistadorId());
        assertEquals(desId, dto.getDesaparecidoId());
        assertEquals(-34.5, dto.getLatitud());
        assertEquals(-58.4, dto.getLongitud());
        assertEquals(fh, dto.getFechaHora());
        assertEquals("desc", dto.getDescripcion());
        assertEquals("url", dto.getFotoUrl());
        assertEquals(Boolean.FALSE, dto.getVerificado());
        assertEquals(Boolean.TRUE, dto.getPublico());
        assertEquals(created, dto.getCreadoEn());

        verifyNoInteractions(textNormalizer, urlNormalizer, dateTimeNormalizer);
    }

    @Test
    @DisplayName("fromDomainListToResponseList: mapea lista conservando tamaño y campos")
    void fromDomainListToResponseList_ok() {
        Avistamiento a1 = new Avistamiento(); a1.setId(UUID.randomUUID());
        Avistamiento a2 = new Avistamiento(); a2.setId(UUID.randomUUID());

        List<AvistamientoResponseDTO> out = mapper.fromDomainListToResponseList(List.of(a1, a2));
        assertEquals(2, out.size());
        assertEquals(a1.getId(), out.get(0).getId());
        assertEquals(a2.getId(), out.get(1).getId());
    }

    // ========= fromResponseToFront / List =========

    @Test
    @DisplayName("fromResponseToFront: usa DateTimeNormalizer y mapea campos para el front")
    void fromResponseToFront_ok() {
        AvistamientoResponseDTO res = new AvistamientoResponseDTO();
        UUID id = UUID.randomUUID();
        LocalDateTime fh = LocalDateTime.of(2025, 11, 8, 10, 0);

        res.setId(id);
        res.setLatitud(-34.7);
        res.setLongitud(-58.5);
        res.setFechaHora(fh);
        res.setDescripcion("d");
        res.setFotoUrl("u");
        res.setVerificado(Boolean.TRUE);

        when(dateTimeNormalizer.formatForDisplay(fh)).thenReturn("08/11/2025 10:00");

        AvistamientoFrontDTO front = mapper.fromResponseToFront(res);

        assertEquals(id, front.getId());
        assertEquals(-34.7, front.getLatitud());
        assertEquals(-58.5, front.getLongitud());
        assertEquals("d", front.getDescripcion());
        assertEquals("u", front.getFotoUrl());
        assertEquals(Boolean.TRUE, front.getVerificado());
        assertEquals("08/11/2025 10:00", front.getFechaFormateada());

        verify(dateTimeNormalizer).formatForDisplay(fh);
        verifyNoMoreInteractions(dateTimeNormalizer);
        verifyNoInteractions(textNormalizer, urlNormalizer);
    }

    @Test
    @DisplayName("fromResponseToFrontList: mapea lista conservando tamaño y orden")
    void fromResponseToFrontList_ok() {
        AvistamientoResponseDTO r1 = new AvistamientoResponseDTO(); r1.setId(UUID.randomUUID());
        AvistamientoResponseDTO r2 = new AvistamientoResponseDTO(); r2.setId(UUID.randomUUID());

        when(dateTimeNormalizer.formatForDisplay(any())).thenReturn("x");

        List<AvistamientoFrontDTO> out = mapper.fromResponseToFrontList(List.of(r1, r2));
        assertEquals(2, out.size());
        assertEquals(r1.getId(), out.get(0).getId());
        assertEquals(r2.getId(), out.get(1).getId());
        verify(dateTimeNormalizer, times(2)).formatForDisplay(any());
    }
}

