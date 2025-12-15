package edu.utn.proyecto.applicacion.mappers;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenMapperTest {

    @Mock private Claims claims;
    private TokenMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TokenMapper();
    }

    @Test
    @DisplayName("toSessionUser: mapea subject, id, email y nombre (no nulos)")
    void toSessionUser_ok() {
        String dni = "12345678";
        String idStr = UUID.randomUUID().toString();

        when(claims.getSubject()).thenReturn(dni);
        when(claims.get(eq("id"), eq(String.class))).thenReturn(idStr);
        when(claims.get(eq("email"), eq(String.class))).thenReturn("a@b.com");
        when(claims.get(eq("name"), eq(String.class))).thenReturn("JUAN");

        SessionUserDTO out = mapper.toSessionUser(claims);

        assertEquals(UUID.fromString(idStr), out.getId());
        assertEquals("12345678", out.getDni());
        assertEquals("a@b.com", out.getEmail());
        assertEquals("JUAN", out.getNombre());

        verify(claims, times(1)).getSubject();
        verify(claims, times(1)).get(eq("id"), eq(String.class));
        // Con valores no nulos, el ternario evalúa dos veces
        verify(claims, times(2)).get(eq("email"), eq(String.class));
        verify(claims, times(2)).get(eq("name"), eq(String.class));
        verifyNoMoreInteractions(claims);
    }

    @Test
    @DisplayName("toSessionUser: email y nombre null → se transforman a \"\" (solo 1 get por campo)")
    void toSessionUser_emailNameNull_defaultsToEmpty() {
        String idStr = UUID.randomUUID().toString();
        when(claims.getSubject()).thenReturn("87654321");
        when(claims.get(eq("id"), eq(String.class))).thenReturn(idStr);
        when(claims.get(eq("email"), eq(String.class))).thenReturn(null);
        when(claims.get(eq("name"), eq(String.class))).thenReturn(null);

        SessionUserDTO out = mapper.toSessionUser(claims);

        assertEquals("", out.getEmail());
        assertEquals("", out.getNombre());

        verify(claims, times(1)).getSubject();
        verify(claims, times(1)).get(eq("id"), eq(String.class));
        // Con null, el ternario NO evalúa la segunda rama → solo 1 invocación por campo
        verify(claims, times(1)).get(eq("email"), eq(String.class));
        verify(claims, times(1)).get(eq("name"), eq(String.class));
        verifyNoMoreInteractions(claims);
    }

    @Test
    @DisplayName("toSessionUser: id inválido → IllegalArgumentException")
    void toSessionUser_invalidUuid_throws() {
        when(claims.getSubject()).thenReturn("dni");
        when(claims.get(eq("id"), eq(String.class))).thenReturn("not-a-uuid");
        when(claims.get(eq("email"), eq(String.class))).thenReturn("e@e.com");
        when(claims.get(eq("name"), eq(String.class))).thenReturn("N");

        assertThrows(IllegalArgumentException.class, () -> mapper.toSessionUser(claims));
    }

    @Test
    @DisplayName("toSessionUser: id null → NullPointerException")
    void toSessionUser_nullId_throws() {
        when(claims.getSubject()).thenReturn("dni");
        when(claims.get(eq("id"), eq(String.class))).thenReturn(null);
        when(claims.get(eq("email"), eq(String.class))).thenReturn("e@e.com");
        when(claims.get(eq("name"), eq(String.class))).thenReturn("N");

        assertThrows(NullPointerException.class, () -> mapper.toSessionUser(claims));
    }
}
