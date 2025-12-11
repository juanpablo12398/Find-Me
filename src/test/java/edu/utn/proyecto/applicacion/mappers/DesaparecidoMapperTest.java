package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.common.normalize.DateTimeNormalizer;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.common.normalize.UrlNormalizer;
import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoFrontDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
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
class DesaparecidoMapperTest {

    @Mock private TextNormalizer textNormalizer;
    @Mock private DniNormalizer dniNormalizer;
    @Mock private UrlNormalizer urlNormalizer;
    @Mock private DateTimeNormalizer dateTimeNormalizer;

    private DesaparecidoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DesaparecidoMapper(textNormalizer, dniNormalizer, urlNormalizer, dateTimeNormalizer);
    }

    // ========= normalizeRequestInPlace =========

    @Test
    @DisplayName("normalizeRequestInPlace: normaliza nombre/apellido (upperNoAccents), DNI y descripción, y URL opcional")
    void normalizeRequestInPlace_ok() {
        DesaparecidoRequestDTO dto = new DesaparecidoRequestDTO();
        dto.setNombre("jóse");
        dto.setApellido("péRez");
        dto.setDni("  12.345.678 ");
        dto.setDescripcion("  texto  ");
        dto.setFotoUrl("  https://IMG.JPG  ");

        when(textNormalizer.upperNoAccents("jóse")).thenReturn("JOSE");
        when(textNormalizer.upperNoAccents("péRez")).thenReturn("PEREZ");
        when(dniNormalizer.normalize("  12.345.678 ")).thenReturn("12345678");
        when(textNormalizer.normalize("  texto  ")).thenReturn("texto");
        when(urlNormalizer.normalizeOptional("  https://IMG.JPG  ")).thenReturn("https://img.jpg");

        mapper.normalizeRequestInPlace(dto);

        assertEquals("JOSE", dto.getNombre());
        assertEquals("PEREZ", dto.getApellido());
        assertEquals("12345678", dto.getDni());
        assertEquals("texto", dto.getDescripcion());
        assertEquals("https://img.jpg", dto.getFotoUrl());

        InOrder inOrder = inOrder(textNormalizer, dniNormalizer, urlNormalizer);
        inOrder.verify(textNormalizer).upperNoAccents("jóse");
        inOrder.verify(textNormalizer).upperNoAccents("péRez");
        inOrder.verify(dniNormalizer).normalize("  12.345.678 ");
        inOrder.verify(textNormalizer).normalize("  texto  ");
        inOrder.verify(urlNormalizer).normalizeOptional("  https://IMG.JPG  ");
        verifyNoMoreInteractions(textNormalizer, dniNormalizer, urlNormalizer);
        verifyNoInteractions(dateTimeNormalizer);
    }

    @Test
    @DisplayName("normalizeRequestInPlace: soporta nulls sin NPE y los propaga")
    void normalizeRequestInPlace_nulls() {
        DesaparecidoRequestDTO dto = new DesaparecidoRequestDTO();
        dto.setNombre(null);
        dto.setApellido(null);
        dto.setDni(null);
        dto.setDescripcion(null);
        dto.setFotoUrl(null);

        mapper.normalizeRequestInPlace(dto);

        assertNull(dto.getNombre());
        assertNull(dto.getApellido());
        assertNull(dto.getDni());
        assertNull(dto.getDescripcion());
        assertNull(dto.getFotoUrl());

        verify(textNormalizer, times(2)).upperNoAccents(null); // nombre y apellido
        verify(dniNormalizer).normalize(null);
        verify(textNormalizer).normalize(null);
        verify(urlNormalizer).normalizeOptional(null);
        verifyNoMoreInteractions(textNormalizer, dniNormalizer, urlNormalizer);
        verifyNoInteractions(dateTimeNormalizer);
    }

    // ========= fromRequestToDomain (single y list) =========

    @Test
    @DisplayName("fromRequestToDomain: crea dominio con los 5 campos del request")
    void fromRequestToDomain_ok() {
        DesaparecidoRequestDTO req = new DesaparecidoRequestDTO();
        req.setNombre("JOSE");
        req.setApellido("PEREZ");
        req.setDni("12345678");
        req.setDescripcion("desc");
        req.setFotoUrl("url");

        Desaparecido d = mapper.fromRequestToDomain(req);

        assertEquals("JOSE", d.getNombre());
        assertEquals("PEREZ", d.getApellido());
        assertEquals("12345678", d.getDni());
        assertEquals("desc", d.getDescripcion());
        assertEquals("url", d.getFoto());
        verifyNoInteractions(textNormalizer, dniNormalizer, urlNormalizer, dateTimeNormalizer);
    }

    @Test
    @DisplayName("fromRequestToDomain(List): mapea lista conservando tamaño y orden")
    void fromRequestToDomain_list_ok() {
        DesaparecidoRequestDTO r1 = new DesaparecidoRequestDTO(); r1.setNombre("A");
        DesaparecidoRequestDTO r2 = new DesaparecidoRequestDTO(); r2.setNombre("B");
        List<Desaparecido> out = mapper.fromRequestToDomain(List.of(r1, r2));
        assertEquals(2, out.size());
        assertEquals("A", out.get(0).getNombre());
        assertEquals("B", out.get(1).getNombre());
    }

    // ========= fromDomainToResponse (single y list) =========

    @Test
    @DisplayName("fromDomainToResponse: copia 1:1 incluyendo fecha(LocalDateTime), descripción y foto")
    void fromDomainToResponse_ok() {
        Desaparecido d = new Desaparecido("N", "A", "1", "D", "F");
        UUID id = UUID.randomUUID();
        d.setId(id);
        d.setFechaDesaparicion(LocalDateTime.of(2024, 5, 1, 0, 0));

        DesaparecidoResponseDTO dto = mapper.fromDomainToResponse(d);

        assertEquals(id, dto.getId());
        assertEquals("N", dto.getNombre());
        assertEquals("A", dto.getApellido());
        assertEquals("1", dto.getDni());
        assertEquals(LocalDateTime.of(2024, 5, 1, 0, 0), dto.getFechaDesaparicion());
        assertEquals("D", dto.getDescripcion());
        assertEquals("F", dto.getFoto());
        verifyNoInteractions(textNormalizer, dniNormalizer, urlNormalizer, dateTimeNormalizer);
    }

    @Test
    @DisplayName("fromDomainToResponse(List): mapea lista conservando tamaño")
    void fromDomainToResponse_list_ok() {
        Desaparecido d1 = new Desaparecido("N1", "A1", "1", "D1", "F1");
        d1.setId(UUID.randomUUID());
        Desaparecido d2 = new Desaparecido("N2", "A2", "2", "D2", "F2");
        d2.setId(UUID.randomUUID());

        List<DesaparecidoResponseDTO> out = mapper.fromDomainToResponse(List.of(d1, d2));
        assertEquals(2, out.size());
        assertEquals(d1.getId(), out.get(0).getId());
        assertEquals(d2.getId(), out.get(1).getId());
    }

    // ========= Front (response -> front) =========

    @Test
    @DisplayName("fromResponseToFront: usa DateTimeNormalizer(LocalDateTime) y mapea campos")
    void fromResponseToFront_ok() {
        DesaparecidoResponseDTO res = new DesaparecidoResponseDTO(
                UUID.randomUUID(), "N", "A", "1", LocalDateTime.of(2023, 1, 2, 0, 0), "D", "F"
        );
        when(dateTimeNormalizer.formatForDisplay(LocalDateTime.of(2023, 1, 2, 0, 0)))
                .thenReturn("02/01/2023 00:00");

        DesaparecidoFrontDTO front = mapper.fromResponseToFront(res);

        assertEquals(res.getId(), front.getId());
        assertEquals("N", front.getNombre());
        assertEquals("A", front.getApellido());
        assertEquals("1", front.getDni());
        assertEquals("D", front.getDescripcion());
        assertEquals("F", front.getFoto());
        assertEquals("02/01/2023 00:00", front.getFechaFormateada());

        verify(dateTimeNormalizer).formatForDisplay(LocalDateTime.of(2023, 1, 2, 0, 0));
        verifyNoMoreInteractions(dateTimeNormalizer);
        verifyNoInteractions(textNormalizer, dniNormalizer, urlNormalizer);
    }

    @Test
    @DisplayName("fromResponseToFront: fecha null → formatea null y la coloca en el front")
    void fromResponseToFront_nullDate() {
        DesaparecidoResponseDTO res = new DesaparecidoResponseDTO(
                UUID.randomUUID(), "N", "A", "1", null, "D", "F"
        );
        when(dateTimeNormalizer.formatForDisplay((LocalDateTime) null)).thenReturn("-");

        DesaparecidoFrontDTO front = mapper.fromResponseToFront(res);

        assertEquals("-", front.getFechaFormateada());
        verify(dateTimeNormalizer).formatForDisplay((LocalDateTime) null);
    }

    @Test
    @DisplayName("fromResponseListToFrontList: mapea lista conservando tamaño y orden")
    void fromResponseListToFrontList_ok() {
        DesaparecidoResponseDTO r1 = new DesaparecidoResponseDTO(UUID.randomUUID(),"N1","A1","1",null,"D1","F1");
        DesaparecidoResponseDTO r2 = new DesaparecidoResponseDTO(UUID.randomUUID(),"N2","A2","2",null,"D2","F2");
        when(dateTimeNormalizer.formatForDisplay((LocalDateTime) null)).thenReturn("—");

        List<DesaparecidoFrontDTO> out = mapper.fromResponseListToFrontList(List.of(r1, r2));
        assertEquals(2, out.size());
        assertEquals(r1.getId(), out.get(0).getId());
        assertEquals(r2.getId(), out.get(1).getId());
        verify(dateTimeNormalizer, times(2)).formatForDisplay((LocalDateTime) null);
    }
}