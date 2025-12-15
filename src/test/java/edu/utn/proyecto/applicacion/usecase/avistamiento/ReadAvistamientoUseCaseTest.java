package edu.utn.proyecto.applicacion.usecase.avistamiento;
import edu.utn.proyecto.applicacion.dtos.AvistamientoResponseDTO;
import edu.utn.proyecto.domain.service.AvistamientoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadAvistamientoUseCaseTest {

    @Mock private AvistamientoService service;

    @Test
    @DisplayName("obtenerTodos: delega y devuelve misma lista")
    void obtenerTodos_ok() {
        ReadAvistamientoUseCase uc = new ReadAvistamientoUseCase(service);
        List<AvistamientoResponseDTO> expected = List.of();
        when(service.obtenerAvistamientosPublicos()).thenReturn(expected);

        List<AvistamientoResponseDTO> out = uc.obtenerTodos();

        assertSame(expected, out);
        verify(service).obtenerAvistamientosPublicos();
    }

    @Test
    @DisplayName("obtenerRecientes: delega con días exactos")
    void obtenerRecientes_ok() {
        ReadAvistamientoUseCase uc = new ReadAvistamientoUseCase(service);
        List<AvistamientoResponseDTO> expected = List.of();
        when(service.obtenerAvistamientosRecientes(3)).thenReturn(expected);

        List<AvistamientoResponseDTO> out = uc.obtenerRecientes(3);

        assertSame(expected, out);
        verify(service).obtenerAvistamientosRecientes(3);
    }

    @Test
    @DisplayName("obtenerPorDesaparecido: delega con UUID exacto")
    void obtenerPorDesaparecido_ok() {
        ReadAvistamientoUseCase uc = new ReadAvistamientoUseCase(service);
        UUID id = UUID.randomUUID();
        List<AvistamientoResponseDTO> expected = List.of();
        when(service.obtenerPorDesaparecido(id)).thenReturn(expected);

        List<AvistamientoResponseDTO> out = uc.obtenerPorDesaparecido(id);

        assertSame(expected, out);
        verify(service).obtenerPorDesaparecido(id);
    }

    @Test
    @DisplayName("obtenerEnArea: delega con límites exactos")
    void obtenerEnArea_ok() {
        ReadAvistamientoUseCase uc = new ReadAvistamientoUseCase(service);
        List<AvistamientoResponseDTO> expected = List.of();
        when(service.obtenerEnArea(-1.0, 1.0, -2.0, 2.0)).thenReturn(expected);

        List<AvistamientoResponseDTO> out = uc.obtenerEnArea(-1.0, 1.0, -2.0, 2.0);

        assertSame(expected, out);
        verify(service).obtenerEnArea(-1.0, 1.0, -2.0, 2.0);
    }
}
