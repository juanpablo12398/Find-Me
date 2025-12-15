package edu.utn.proyecto.applicacion.usecase.avistamiento;
import edu.utn.proyecto.applicacion.dtos.AvistamientoResponseDTO;
import edu.utn.proyecto.domain.service.AvistamientoService;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAvistamientoUseCaseTest {

    @Mock private AvistamientoService service;

    @Test
    @DisplayName("execute: delega en service.crearAvistamiento(dto)")
    void execute_ok() {
        CreateAvistamientoUseCase uc = new CreateAvistamientoUseCase(service);
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        AvistamientoResponseDTO expected = new AvistamientoResponseDTO();
        when(service.crearAvistamiento(dto)).thenReturn(expected);

        AvistamientoResponseDTO out = uc.execute(dto);

        assertSame(expected, out);
        verify(service).crearAvistamiento(dto);
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("execute: propaga excepción")
    void execute_propagates() {
        CreateAvistamientoUseCase uc = new CreateAvistamientoUseCase(service);
        AvistamientoRequestDTO dto = new AvistamientoRequestDTO();
        when(service.crearAvistamiento(dto)).thenThrow(new IllegalArgumentException("x"));
        assertThrows(IllegalArgumentException.class, () -> uc.execute(dto));
    }
}