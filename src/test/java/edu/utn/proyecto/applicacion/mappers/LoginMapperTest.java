package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.common.normalize.DniNormalizer;
import edu.utn.proyecto.common.normalize.TextNormalizer;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginMapperTest {

    @Mock private DniNormalizer dniNormalizer;
    @Mock private TextNormalizer textNormalizer;

    private LoginMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new LoginMapper(dniNormalizer, textNormalizer);
    }

    // ==== normalizeRequestInPlace ====

    @Test
    @DisplayName("normalizeRequestInPlace: normaliza DNI y Email; muta in-place y respeta orden")
    void normalizeRequestInPlace_ok() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setDni(" 12.345.678 ");
        dto.setEmail("  Foo@BAR.Com  ");

        when(dniNormalizer.normalize(" 12.345.678 ")).thenReturn("12345678");
        when(textNormalizer.normalize("  Foo@BAR.Com  ")).thenReturn("foo@bar.com");

        mapper.normalizeRequestInPlace(dto);

        assertEquals("12345678", dto.getDni());
        assertEquals("foo@bar.com", dto.getEmail());

        InOrder inOrder = inOrder(dniNormalizer, textNormalizer);
        inOrder.verify(dniNormalizer).normalize(" 12.345.678 ");
        inOrder.verify(textNormalizer).normalize("  Foo@BAR.Com  ");
        verifyNoMoreInteractions(dniNormalizer, textNormalizer);
    }

    @Test
    @DisplayName("normalizeRequestInPlace: si email es null → setea \"\" y NO usa TextNormalizer; DNI puede ser null")
    void normalizeRequestInPlace_emailNull() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setDni(null);
        dto.setEmail(null);

        mapper.normalizeRequestInPlace(dto);

        // DNI: se intentó normalizar (con null), y queda null (porque mock devuelve null por defecto)
        verify(dniNormalizer).normalize(null);
        // Email: no se invoca TextNormalizer si viene null
        verifyNoInteractions(textNormalizer);

        assertNull(dto.getDni());
        assertEquals("", dto.getEmail());
    }

    // ==== fromLoginRequestToSession ====

    @Test
    @DisplayName("fromLoginRequestToSession: mapea los campos 1:1 sin invocar normalizadores")
    void fromLoginRequestToSession_ok() {
        UUID avistadorId = UUID.randomUUID();
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setDni("12345678");
        dto.setEmail("x@y.com");

        SessionUserDTO session = mapper.fromLoginRequestToSession(dto, avistadorId, "PEPITO");

        assertEquals(avistadorId, session.getId());
        assertEquals("12345678", session.getDni());
        assertEquals("x@y.com", session.getEmail());
        assertEquals("PEPITO", session.getNombre());

        verifyNoInteractions(dniNormalizer, textNormalizer);
    }
}