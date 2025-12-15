package edu.utn.proyecto.applicacion.usecase.auth;
import edu.utn.proyecto.domain.service.abstraccion.IAuthService;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.TokenRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCurrentUserUseCaseTest {

    @Mock private IAuthService authService;
    @Mock private HttpServletRequest servletRequest;

    @Test
    @DisplayName("execute: delega en authService.currentUser(TokenRequestDTO) y retorna el mismo SessionUserDTO")
    void execute_ok() {
        GetCurrentUserUseCase uc = new GetCurrentUserUseCase(authService);

        SessionUserDTO expected = new SessionUserDTO(null, "dni", "mail", "nombre");
        when(authService.currentUser(any(TokenRequestDTO.class))).thenReturn(expected);

        SessionUserDTO out = uc.execute(servletRequest);
        assertSame(expected, out);

        ArgumentCaptor<TokenRequestDTO> cap = ArgumentCaptor.forClass(TokenRequestDTO.class);
        verify(authService).currentUser(cap.capture());
        assertNotNull(cap.getValue(), "Se debe construir un TokenRequestDTO");
        verifyNoMoreInteractions(authService);
    }

    @Test
    @DisplayName("execute: si el servicio lanza excepción, se propaga")
    void execute_propagates() {
        GetCurrentUserUseCase uc = new GetCurrentUserUseCase(authService);
        when(authService.currentUser(any(TokenRequestDTO.class))).thenThrow(new RuntimeException("boom"));
        assertThrows(RuntimeException.class, () -> uc.execute(servletRequest));
    }
}
