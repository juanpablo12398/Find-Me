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
}