package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.mappers.LoginMapper;
import edu.utn.proyecto.applicacion.validation.auth.LoginPolicy;
import edu.utn.proyecto.auth.exception.AuthError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock LoginPolicy policy;
    @Mock LoginMapper mapper;
    @Mock IRepoDeRenaper renaper;
    @Mock IRepoDeAvistadores repoAvistadores;

    @Test
    void login_orquesta_normalizeLuegoValidate_luegoRepos_yMapea() {
        var service = new AuthService(policy, mapper, renaper, repoAvistadores);

        var req = new LoginRequestDTO();
        req.setDni("12345678");
        req.setEmail("a@a.com");

        UUID avistadorId = UUID.randomUUID();
        var av = new Avistador(avistadorId, "12345678", "Juan", "Perez",
                30, "Dir", "a@a.com", null, LocalDateTime.now());
        when(repoAvistadores.findByDni("12345678")).thenReturn(Optional.of(av));

        // ⭐ CAMBIO: Ahora incluye el ID como primer parámetro
        var expected = new SessionUserDTO(avistadorId, "12345678", "a@a.com", "Juan");
        when(mapper.fromLoginRequestToSession(req, avistadorId, "Juan")).thenReturn(expected);

        var out = service.login(req);

        // Orden esperado
        InOrder io = inOrder(mapper, policy, repoAvistadores);
        io.verify(mapper).normalizeRequestInPlace(req);
        io.verify(policy).validate(req);
        io.verify(repoAvistadores).findByDni("12345678");
        io.verify(mapper).fromLoginRequestToSession(req, avistadorId, "Juan"); // ⭐ Con ID
        io.verifyNoMoreInteractions();

        verifyNoInteractions(renaper);

        assertThat(out).isSameAs(expected);
    }

    @Test
    void login_siPolicyFalla_noMapea_y_propagacionDomainException() {
        var service = new AuthService(policy, mapper, renaper, repoAvistadores);
        var req = new LoginRequestDTO();

        doThrow(DomainException.of(AuthError.PADRON_NOT_FOUND.key,
                AuthError.PADRON_NOT_FOUND.status, "No existe en padrón"))
                .when(policy).validate(req);

        assertThatThrownBy(() -> service.login(req))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> {
                    var de = (DomainException) ex;
                    assertThat(de.getKey()).isEqualTo(AuthError.PADRON_NOT_FOUND.key);
                    assertThat(de.getStatus()).isEqualTo(AuthError.PADRON_NOT_FOUND.status);
                });

        verify(mapper).normalizeRequestInPlace(req);
        verify(policy).validate(req);
        verifyNoInteractions(repoAvistadores, renaper);
        verify(mapper, never()).fromLoginRequestToSession(any(), any(), any()); // ⭐ 3 parámetros
    }

    @Test
    void login_resolvedNombre_caeEnRenaper_siAvistadorNoTieneNombre() {
        var service = new AuthService(policy, mapper, renaper, repoAvistadores);

        var req = new LoginRequestDTO();
        req.setDni("12345678");
        req.setEmail("a@a.com");

        UUID avistadorId = UUID.randomUUID();
        // Avistador sin nombre
        var av = new Avistador(avistadorId, "12345678", "", "",
                30, "Dir", "a@a.com", null, LocalDateTime.now());
        when(repoAvistadores.findByDni("12345678")).thenReturn(Optional.of(av));

        // Renaper con nombre
        var persona = new RenaperPersonaEntity();
        persona.setDni("12345678");
        persona.setNombre("NombreRENAPER");
        when(renaper.findByDni("12345678")).thenReturn(Optional.of(persona));

        // ⭐ CAMBIO: Incluir ID
        var expected = new SessionUserDTO(avistadorId, "12345678", "a@a.com", "NombreRENAPER");
        when(mapper.fromLoginRequestToSession(req, avistadorId, "NombreRENAPER")).thenReturn(expected);

        var out = service.login(req);
        assertThat(out.getNombre()).isEqualTo("NombreRENAPER");

        InOrder io = inOrder(mapper, policy, repoAvistadores, renaper, mapper);
        io.verify(mapper).normalizeRequestInPlace(req);
        io.verify(policy).validate(req);
        io.verify(repoAvistadores).findByDni("12345678");
        io.verify(renaper).findByDni("12345678");
        io.verify(mapper).fromLoginRequestToSession(req, avistadorId, "NombreRENAPER"); // ⭐ Con ID
    }
}
