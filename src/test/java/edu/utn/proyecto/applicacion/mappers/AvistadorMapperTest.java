package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.applicacion.dtos.AvistadorResponseDTO;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvistadorMapperTest {

    @Mock private DniNormalizer dniNormalizer;
    @Mock private TextNormalizer textNormalizer;

    private AvistadorMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AvistadorMapper(dniNormalizer, textNormalizer);
    }

    @Test
    @DisplayName("normalizeRequestInPlace: delega a normalizadores correctos y muta el DTO in-place")
    void normalizeRequestInPlace_ok() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni("  12.345.678  ");
        dto.setNombre("áNdrés");
        dto.setApellido("péRez");
        dto.setDireccion("  cAlle falsa 123  ");
        dto.setEmail("  a@MAIL.com  ");
        dto.setTelefono("  +54 9 11 2222-3333  ");

        when(dniNormalizer.normalize("  12.345.678  ")).thenReturn("12345678");
        when(textNormalizer.upperNoAccents("áNdrés")).thenReturn("ANDRES");
        when(textNormalizer.upperNoAccents("péRez")).thenReturn("PEREZ");
        when(textNormalizer.normalize("  cAlle falsa 123  ")).thenReturn("calle falsa 123");
        when(textNormalizer.normalize("  a@MAIL.com  ")).thenReturn("a@mail.com");
        when(textNormalizer.normalize("  +54 9 11 2222-3333  ")).thenReturn("+5491122223333");

        mapper.normalizeRequestInPlace(dto);

        assertEquals("12345678", dto.getDni());
        assertEquals("ANDRES", dto.getNombre());
        assertEquals("PEREZ", dto.getApellido());
        assertEquals("calle falsa 123", dto.getDireccion());
        assertEquals("a@mail.com", dto.getEmail());
        assertEquals("+5491122223333", dto.getTelefono());

        InOrder inOrder = inOrder(dniNormalizer, textNormalizer);
        inOrder.verify(dniNormalizer).normalize("  12.345.678  ");
        inOrder.verify(textNormalizer).upperNoAccents("áNdrés");
        inOrder.verify(textNormalizer).upperNoAccents("péRez");
        inOrder.verify(textNormalizer).normalize("  cAlle falsa 123  ");
        inOrder.verify(textNormalizer).normalize("  a@MAIL.com  ");
        inOrder.verify(textNormalizer).normalize("  +54 9 11 2222-3333  ");
        verifyNoMoreInteractions(dniNormalizer, textNormalizer);
    }

    @Test
    @DisplayName("normalizeRequestInPlace: si campos vienen null, propaga null sin romper")
    void normalizeRequestInPlace_nulls() {
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        dto.setDni(null);
        dto.setNombre(null);
        dto.setApellido(null);
        dto.setDireccion(null);
        dto.setEmail(null);
        dto.setTelefono(null);

        mapper.normalizeRequestInPlace(dto);

        assertNull(dto.getDni());
        assertNull(dto.getNombre());
        assertNull(dto.getApellido());
        assertNull(dto.getDireccion());
        assertNull(dto.getEmail());
        assertNull(dto.getTelefono());

        verify(dniNormalizer).normalize(null);
        verify(textNormalizer, times(2)).upperNoAccents(null); // nombre y apellido
        verify(textNormalizer, times(3)).normalize(null);      // direccion, email, telefono
        verifyNoMoreInteractions(dniNormalizer, textNormalizer);
    }

    @Test
    @DisplayName("fromRequestToDomain: mapea todos los campos y setea creadoEn ~ now()")
    void fromRequestToDomain_ok() {
        AvistadorRequestDTO req = new AvistadorRequestDTO();
        req.setDni("12345678");
        req.setNombre("ANDRES");
        req.setApellido("PEREZ");
        req.setEdad(30);
        req.setDireccion("calle falsa 123");
        req.setEmail("a@mail.com");
        req.setTelefono("+5491122223333");

        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        Avistador domain = mapper.fromRequestToDomain(req);

        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertEquals("12345678", domain.getDni());
        assertEquals("ANDRES", domain.getNombre());
        assertEquals("PEREZ", domain.getApellido());
        assertEquals(30, domain.getEdad());
        assertEquals("calle falsa 123", domain.getDireccion());
        assertEquals("a@mail.com", domain.getEmail());
        assertEquals("+5491122223333", domain.getTelefono());

        assertNotNull(domain.getCreadoEn());
        assertTrue(!domain.getCreadoEn().isBefore(before) && !domain.getCreadoEn().isAfter(after));

        verifyNoInteractions(dniNormalizer, textNormalizer);
    }

    @Test
    @DisplayName("fromDomainToResponse: mapea todos los campos 1:1 incluyendo creadoEn")
    void fromDomainToResponse_ok() {
        Avistador d = new Avistador();
        d.setDni("12345678");
        d.setNombre("ANDRES");
        d.setApellido("PEREZ");
        d.setEdad(30);
        d.setDireccion("calle falsa 123");
        d.setEmail("a@mail.com");
        d.setTelefono("+5491122223333");
        LocalDateTime ts = LocalDateTime.now().minusDays(1);
        d.setCreadoEn(ts);

        AvistadorResponseDTO r = mapper.fromDomainToResponse(d);

        assertNull(r.getId());
        assertEquals("12345678", r.getDni());
        assertEquals("ANDRES", r.getNombre());
        assertEquals("PEREZ", r.getApellido());
        assertEquals(30, r.getEdad());
        assertEquals("calle falsa 123", r.getDireccion());
        assertEquals("a@mail.com", r.getEmail());
        assertEquals("+5491122223333", r.getTelefono());
        assertEquals(ts, r.getCreadoEn());

        verifyNoInteractions(dniNormalizer, textNormalizer);
    }

    @Test
    @DisplayName("fromDomainToResponse: si el domain trae nulls, los propaga tal cual")
    void fromDomainToResponse_nulls() {
        Avistador d = new Avistador();
        d.setDni(null);
        d.setNombre(null);
        d.setApellido(null);
        d.setEdad(null);
        d.setDireccion(null);
        d.setEmail(null);
        d.setTelefono(null);
        d.setCreadoEn(null);

        AvistadorResponseDTO r = mapper.fromDomainToResponse(d);

        assertNull(r.getId());
        assertNull(r.getDni());
        assertNull(r.getNombre());
        assertNull(r.getApellido());
        assertNull(r.getEdad());
        assertNull(r.getDireccion());
        assertNull(r.getEmail());
        assertNull(r.getTelefono());
        assertNull(r.getCreadoEn());

        verifyNoInteractions(dniNormalizer, textNormalizer);
    }
}
