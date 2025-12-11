package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.mappers.LoginMapper;
import edu.utn.proyecto.applicacion.mappers.TokenMapper;
import edu.utn.proyecto.applicacion.validation.auth.LoginPolicy;
import edu.utn.proyecto.applicacion.validation.auth.TokenPolicy;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.TokenRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private LoginPolicy loginPolicy;
    @Mock private TokenPolicy tokenPolicy;
    @Mock private LoginMapper loginMapper;
    @Mock private TokenMapper tokenMapper;
    @Mock private IRepoDeRenaper renaper;
    @Mock private IRepoDeAvistadores repoAvistadores;

    private AuthService service;

    @BeforeEach
    void setUp() {
        service = new AuthService(loginPolicy, tokenPolicy, loginMapper, tokenMapper, renaper, repoAvistadores);
    }

    @Test
    @DisplayName("login: usa nombre del avistador si está presente (no consulta RENAPER)")
    void login_usesAvistadorName_whenPresent() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setDni("123");
        UUID id = UUID.randomUUID();

        Avistador av = new Avistador();
        av.setId(id);
        av.setNombre("PEPE");

        SessionUserDTO expected = new SessionUserDTO(id, "123", "e@e.com", "PEPE");

        when(repoAvistadores.findByDni("123")).thenReturn(Optional.of(av));
        when(loginMapper.fromLoginRequestToSession(req, id, "PEPE")).thenReturn(expected);

        SessionUserDTO out = service.login(req);

        assertSame(expected, out);

        InOrder inOrder = inOrder(loginMapper, loginPolicy, repoAvistadores);
        inOrder.verify(loginMapper).normalizeRequestInPlace(req);
        inOrder.verify(loginPolicy).validate(req);
        inOrder.verify(repoAvistadores).findByDni("123");

        verifyNoInteractions(renaper);
        verify(loginMapper).fromLoginRequestToSession(req, id, "PEPE");
        verifyNoMoreInteractions(loginMapper, loginPolicy, repoAvistadores);
    }

    @Test
    @DisplayName("login: cuando el nombre del avistador está vacío, lo resuelve desde RENAPER")
    void login_resolvesName_fromRenaper_whenBlank() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setDni("456");
        UUID id = UUID.randomUUID();

        Avistador av = new Avistador();
        av.setId(id);
        av.setNombre("   "); // blank

        RenaperPersonaEntity ren = mock(RenaperPersonaEntity.class);
        when(ren.getNombre()).thenReturn("RENAPER_NOMBRE");

        SessionUserDTO expected = new SessionUserDTO(id, "456", "", "RENAPER_NOMBRE");

        when(repoAvistadores.findByDni("456")).thenReturn(Optional.of(av));
        when(renaper.findByDni("456")).thenReturn(Optional.of(ren));
        when(loginMapper.fromLoginRequestToSession(req, id, "RENAPER_NOMBRE")).thenReturn(expected);

        SessionUserDTO out = service.login(req);

        assertSame(expected, out);
        verify(renaper).findByDni("456");
        verify(loginMapper).fromLoginRequestToSession(req, id, "RENAPER_NOMBRE");
    }

    @Test
    @DisplayName("login: si RENAPER tampoco tiene nombre, se pasa null como resolvedNombre")
    void login_resolvedName_null_whenRenaperEmpty() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setDni("789");
        UUID id = UUID.randomUUID();

        Avistador av = new Avistador();
        av.setId(id);
        av.setNombre(null);

        SessionUserDTO expected = new SessionUserDTO(id, "789", "", null);

        when(repoAvistadores.findByDni("789")).thenReturn(Optional.of(av));
        when(renaper.findByDni("789")).thenReturn(Optional.empty());
        when(loginMapper.fromLoginRequestToSession(req, id, null)).thenReturn(expected);

        SessionUserDTO out = service.login(req);

        assertSame(expected, out);
    }

    @Test
    @DisplayName("currentUser: valida con TokenPolicy y mapea Claims a SessionUser")
    void currentUser_ok() {
        TokenRequestDTO tr = mock(TokenRequestDTO.class);
        Claims claims = mock(Claims.class);
        when(tr.getClaims()).thenReturn(claims);

        SessionUserDTO expected = new SessionUserDTO(UUID.randomUUID(), "1", "", "N");
        when(tokenMapper.toSessionUser(claims)).thenReturn(expected);

        SessionUserDTO out = service.currentUser(tr);

        assertSame(expected, out);
        verify(tokenPolicy).validate(tr);
        verify(tokenMapper).toSessionUser(claims);
        verifyNoMoreInteractions(tokenPolicy, tokenMapper);
    }
}