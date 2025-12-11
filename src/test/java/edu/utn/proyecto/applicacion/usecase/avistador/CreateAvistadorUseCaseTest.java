package edu.utn.proyecto.applicacion.usecase.avistador;
import edu.utn.proyecto.applicacion.dtos.AvistadorResponseDTO;
import edu.utn.proyecto.domain.service.AvistadorService;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistadorRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAvistadorUseCaseTest {

    @Mock private AvistadorService service;

    @Test
    @DisplayName("execute: delega en service.registrar(dto)")
    void execute_ok() {
        CreateAvistadorUseCase uc = new CreateAvistadorUseCase(service);
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        AvistadorResponseDTO expected = new AvistadorResponseDTO();
        when(service.registrar(dto)).thenReturn(expected);

        AvistadorResponseDTO out = uc.execute(dto);

        assertSame(expected, out);
        verify(service).registrar(dto);
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("execute: propaga excepción del servicio")
    void execute_propagates() {
        CreateAvistadorUseCase uc = new CreateAvistadorUseCase(service);
        AvistadorRequestDTO dto = new AvistadorRequestDTO();
        when(service.registrar(dto)).thenThrow(new RuntimeException("boom"));
        assertThrows(RuntimeException.class, () -> uc.execute(dto));
    }
}

