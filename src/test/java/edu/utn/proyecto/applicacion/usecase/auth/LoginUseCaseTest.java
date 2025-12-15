package edu.utn.proyecto.applicacion.usecase.auth;
import edu.utn.proyecto.domain.service.AuthService;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.LoginRequestDTO;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.SessionUserDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock private AuthService authService;

    @Test
    @DisplayName("execute: delega en authService.login(dto) y retorna el mismo objeto")
    void execute_ok() {
        LoginUseCase uc = new LoginUseCase(authService);
        LoginRequestDTO dto = new LoginRequestDTO();
        SessionUserDTO expected = new SessionUserDTO(null, "dni", "email", "nombre");
        when(authService.login(dto)).thenReturn(expected);

        SessionUserDTO out = uc.execute(dto);

        assertSame(expected, out);
        verify(authService).login(dto);
        verifyNoMoreInteractions(authService);
    }

    @Test
    @DisplayName("execute: propaga la excepción del servicio")
    void execute_propagates() {
        LoginUseCase uc = new LoginUseCase(authService);
        LoginRequestDTO dto = new LoginRequestDTO();
        when(authService.login(dto)).thenThrow(new IllegalStateException("x"));
        assertThrows(IllegalStateException.class, () -> uc.execute(dto));
    }
}
