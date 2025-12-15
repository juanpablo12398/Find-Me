package edu.utn.proyecto.applicacion.usecase.avistamiento;
import edu.utn.proyecto.domain.service.AvistamientoService;
import edu.utn.proyecto.infrastructure.adapters.in.rest.dtos.AvistamientoFrontDTO;
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
class ReadAvistamientoGeoUseCaseTest {

    @Mock private AvistamientoService service;

    @Test
    @DisplayName("paraMapa: delega y devuelve la misma lista")
    void paraMapa_ok() {
        ReadAvistamientoGeoUseCase uc = new ReadAvistamientoGeoUseCase(service);
        List<AvistamientoFrontDTO> expected = List.of(); // vacía
        when(service.obtenerParaMapa()).thenReturn(expected);

        List<AvistamientoFrontDTO> out = uc.paraMapa();

        assertSame(expected, out);
        verify(service).obtenerParaMapa();
    }

    @Test
    @DisplayName("porDesaparecido: delega con el UUID exacto")
    void porDesaparecido_ok() {
        ReadAvistamientoGeoUseCase uc = new ReadAvistamientoGeoUseCase(service);
        UUID id = UUID.randomUUID();
        List<AvistamientoFrontDTO> expected = List.of(
                new AvistamientoFrontDTO(id, 1.0, 2.0, "f", "d", "u", true)
        );
        when(service.obtenerPorDesaparecidoEnriquecido(id)).thenReturn(expected);

        List<AvistamientoFrontDTO> out = uc.porDesaparecido(id);

        assertSame(expected, out);
        verify(service).obtenerPorDesaparecidoEnriquecido(id);
    }

    @Test
    @DisplayName("enArea: delega con límites exactos")
    void enArea_ok() {
        ReadAvistamientoGeoUseCase uc = new ReadAvistamientoGeoUseCase(service);
        List<AvistamientoFrontDTO> expected = List.of();
        when(service.obtenerEnAreaEnriquecido(-1.0, 1.0, -2.0, 2.0)).thenReturn(expected);

        List<AvistamientoFrontDTO> out = uc.enArea(-1.0, 1.0, -2.0, 2.0);

        assertSame(expected, out);
        verify(service).obtenerEnAreaEnriquecido(-1.0, 1.0, -2.0, 2.0);
    }

    @Test
    @DisplayName("recientes: delega con días exactos")
    void recientes_ok() {
        ReadAvistamientoGeoUseCase uc = new ReadAvistamientoGeoUseCase(service);
        List<AvistamientoFrontDTO> expected = List.of();
        when(service.obtenerRecientesEnriquecido(7)).thenReturn(expected);

        List<AvistamientoFrontDTO> out = uc.recientes(7);

        assertSame(expected, out);
        verify(service).obtenerRecientesEnriquecido(7);
    }

    @Test
    @DisplayName("enRadio: delega con lat/lng/radio exactos")
    void enRadio_ok() {
        ReadAvistamientoGeoUseCase uc = new ReadAvistamientoGeoUseCase(service);
        List<AvistamientoFrontDTO> expected = List.of();
        when(service.obtenerEnRadio(10.0, 20.0, 3.5)).thenReturn(expected);

        List<AvistamientoFrontDTO> out = uc.enRadio(10.0, 20.0, 3.5);

        assertSame(expected, out);
        verify(service).obtenerEnRadio(10.0, 20.0, 3.5);
    }

    @Test
    @DisplayName("enPoligono: delega con WKT exacto")
    void enPoligono_ok() {
        ReadAvistamientoGeoUseCase uc = new ReadAvistamientoGeoUseCase(service);
        List<AvistamientoFrontDTO> expected = List.of();
        when(service.obtenerEnPoligono("POLYGON(...)")).thenReturn(expected);

        List<AvistamientoFrontDTO> out = uc.enPoligono("POLYGON(...)");

        assertSame(expected, out);
        verify(service).obtenerEnPoligono("POLYGON(...)");
    }

    @Test
    @DisplayName("masCercanos: delega con lat/lng/limite exactos")
    void masCercanos_ok() {
        ReadAvistamientoGeoUseCase uc = new ReadAvistamientoGeoUseCase(service);
        List<AvistamientoFrontDTO> expected = List.of();
        when(service.obtenerMasCercanos(1.0, 2.0, 5)).thenReturn(expected);

        List<AvistamientoFrontDTO> out = uc.masCercanos(1.0, 2.0, 5);

        assertSame(expected, out);
        verify(service).obtenerMasCercanos(1.0, 2.0, 5);
    }

    @Test
    @DisplayName("porDesaparecidoEnRadio: delega con todos los parámetros")
    void porDesaparecidoEnRadio_ok() {
        ReadAvistamientoGeoUseCase uc = new ReadAvistamientoGeoUseCase(service);
        UUID id = UUID.randomUUID();
        List<AvistamientoFrontDTO> expected = List.of();
        when(service.obtenerPorDesaparecidoEnRadio(id, 1.0, 2.0, 3.0)).thenReturn(expected);

        List<AvistamientoFrontDTO> out = uc.porDesaparecidoEnRadio(id, 1.0, 2.0, 3.0);

        assertSame(expected, out);
        verify(service).obtenerPorDesaparecidoEnRadio(id, 1.0, 2.0, 3.0);
    }

    @Test
    @DisplayName("contarEnArea: delega y devuelve el mismo Long")
    void contarEnArea_ok() {
        ReadAvistamientoGeoUseCase uc = new ReadAvistamientoGeoUseCase(service);
        when(service.contarEnArea(-1.0, 1.0, -2.0, 2.0)).thenReturn(42L);

        Long out = uc.contarEnArea(-1.0, 1.0, -2.0, 2.0);

        assertEquals(42L, out);
        verify(service).contarEnArea(-1.0, 1.0, -2.0, 2.0);
    }
}
