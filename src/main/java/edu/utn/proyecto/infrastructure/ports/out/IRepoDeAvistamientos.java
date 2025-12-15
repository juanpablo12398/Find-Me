package edu.utn.proyecto.infrastructure.ports.out;
import edu.utn.proyecto.domain.model.concreta.Avistamiento;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRepoDeAvistamientos {
    Avistamiento save(Avistamiento avistamiento);
    Optional<Avistamiento> findById(UUID id);
    List<Avistamiento> findAll();
    List<Avistamiento> findByDesaparecidoId(UUID desaparecidoId);
    List<Avistamiento> findByAvistadorId(UUID avistadorId);
    List<Avistamiento> findPublicos();
    List<Avistamiento> findInBounds(Double latMin, Double latMax, Double lngMin, Double lngMax);
    List<Avistamiento> findRecientes(LocalDateTime desde);
    void deleteById(UUID id);
    List<Avistamiento> findWithinRadius(Double lat, Double lng, Double radioKm);
    List<Avistamiento> findInPolygon(String polygonWKT);
    List<Avistamiento> findNearestN(Double lat, Double lng, Integer limite);
    List<Avistamiento> findByDesaparecidoWithinRadius(UUID desaparecidoId, Double lat, Double lng, Double radioKm);
    Long countInBounds(Double latMin, Double latMax, Double lngMin, Double lngMax);
}