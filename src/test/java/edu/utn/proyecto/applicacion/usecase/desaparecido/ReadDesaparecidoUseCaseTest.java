package edu.utn.proyecto.applicacion.usecase.desaparecido;
import edu.utn.proyecto.applicacion.dtos.DesaparecidoResponseDTO;
import edu.utn.proyecto.applicacion.mappers.DesaparecidoMapper;
import edu.utn.proyecto.domain.service.DesaparecidoService;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.DesaparecidoFrontDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadDesaparecidoUseCaseTest {

    @Mock private DesaparecidoService service;
    @Mock private DesaparecidoMapper mapper;

    @Test
    @DisplayName("execute: obtiene lista del servicio y la transforma con el mapper (devuelve la lista mapeada)")
    void execute_ok() {
        ReadDesaparecidoUseCase uc = new ReadDesaparecidoUseCase(service, mapper);

        // Lista del servicio (puede ser no vacía)
        List<DesaparecidoResponseDTO> serviceList = List.of(
                new DesaparecidoResponseDTO(
                        UUID.randomUUID(), "N", "A", "1", LocalDateTime.now(), "D", "F"
                )
        );
        // Lista mapeada para front
        List<DesaparecidoFrontDTO> expectedFront = List.of(
                new DesaparecidoFrontDTO(
                        UUID.randomUUID(), "Nf", "Af", "1", "Df", "F", "fecha"
                )
        );

        when(service.obtenerDesaparecidos()).thenReturn(serviceList);
        when(mapper.fromResponseListToFrontList(serviceList)).thenReturn(expectedFront);

        List<DesaparecidoFrontDTO> out = uc.execute();

        assertSame(expectedFront, out);
        verify(service).obtenerDesaparecidos();
        verify(mapper).fromResponseListToFrontList(serviceList);
        verifyNoMoreInteractions(service, mapper);
    }

    @Test
    @DisplayName("execute: si el servicio lanza, se propaga; el mapper no se ejecuta")
    void execute_serviceThrows() {
        ReadDesaparecidoUseCase uc = new ReadDesaparecidoUseCase(service, mapper);
        when(service.obtenerDesaparecidos()).thenThrow(new IllegalStateException("x"));

        assertThrows(IllegalStateException.class, uc::execute);
        verify(service).obtenerDesaparecidos();
        verifyNoInteractions(mapper);
    }
}
