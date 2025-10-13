package edu.utn.proyecto.domain.service;
import edu.utn.proyecto.applicacion.mappers.LoginMapper;
import edu.utn.proyecto.applicacion.validation.auth.LoginPolicy;
import edu.utn.proyecto.auth.exception.AuthError;
import edu.utn.proyecto.common.exception.DomainException;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock LoginPolicy policy;                 // implements Validator<LoginRequestDTO>
    @Mock LoginMapper mapper;

    @Test
    void login_orquesta_validateLuegoMapeo_y_retornaSession() {
        var service = new AuthService(policy, mapper);
        var req = new LoginRequestDTO();
        req.setDni("123");
        req.setEmail("a@a.com");
        req.setResolvedNombre("Juan");

        var expected = new SessionUserDTO("123", "a@a.com", "Juan");
        when(mapper.fromLoginRequestToSession(req)).thenReturn(expected);

        var out = service.login(req);

        // orden: primero policy.validate(dto), luego mapper
        InOrder io = inOrder(policy, mapper);
        io.verify(policy).validate(req);
        io.verify(mapper).fromLoginRequestToSession(req);
        io.verifyNoMoreInteractions();

        assertThat(out).isSameAs(expected);
    }

    @Test
    void login_siPolicyFalla_noMapea_y_propagacionDomainException() {
        var service = new AuthService(policy, mapper);
        var req = new LoginRequestDTO();

        doThrow(DomainException.of(AuthError.PADRON_NOT_FOUND.key,
                AuthError.PADRON_NOT_FOUND.status,
                "No existe en padrón"))
                .when(policy).validate(req);

        assertThatThrownBy(() -> service.login(req))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> {
                    var de = (DomainException) ex;
                    assertThat(de.getKey()).isEqualTo(AuthError.PADRON_NOT_FOUND.key);
                    assertThat(de.getStatus()).isEqualTo(AuthError.PADRON_NOT_FOUND.status);
                });

        verify(policy).validate(req);
        verifyNoInteractions(mapper);
    }
}
