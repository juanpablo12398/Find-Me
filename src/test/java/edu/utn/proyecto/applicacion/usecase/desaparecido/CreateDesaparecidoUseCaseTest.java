package edu.utn.proyecto.applicacion.usecase.desaparecido;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.domain.service.DesaparecidoService;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDesaparecidoUseCaseTest {

    @Mock private DesaparecidoService service;

    @Test
    @DisplayName("execute: delega en service.crearDesaparecido(dto)")
    void execute_ok() {
        CreateDesaparecidoUseCase uc = new CreateDesaparecidoUseCase(service);
        DesaparecidoRequestDTO dto = new DesaparecidoRequestDTO();
        DesaparecidoResponseDTO expected = new DesaparecidoResponseDTO(
                UUID.randomUUID(), "N", "A", "1",
                LocalDateTime.now(), "desc", "foto"
        );

        when(service.crearDesaparecido(dto)).thenReturn(expected);

        DesaparecidoResponseDTO out = uc.execute(dto);

        assertSame(expected, out);
        verify(service).crearDesaparecido(dto);
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("execute: propaga excepción del servicio")
    void execute_propagates() {
        CreateDesaparecidoUseCase uc = new CreateDesaparecidoUseCase(service);
        DesaparecidoRequestDTO dto = new DesaparecidoRequestDTO();

        when(service.crearDesaparecido(dto)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> uc.execute(dto));
    }
}