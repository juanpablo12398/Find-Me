package edu.utn.proyecto.infrastructure.ports.out.jpa;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.AvistamientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AvistamientoJpaRepository extends JpaRepository<AvistamientoEntity, UUID> {

    // Buscar por desaparecido
    List<AvistamientoEntity> findByDesaparecidoId(UUID desaparecidoId);

    // Buscar por avistador
    List<AvistamientoEntity> findByAvistadorId(UUID avistadorId);

    // Solo públicos
    List<AvistamientoEntity> findByPublicoTrue();

    // Búsqueda en área geográfica (bounding box)
    @Query("SELECT a FROM AvistamientoEntity a WHERE " +
            "a.latitud BETWEEN :latMin AND :latMax AND " +
            "a.longitud BETWEEN :lngMin AND :lngMax AND " +
            "a.publico = true " +
            "ORDER BY a.fechaHora DESC")
    List<AvistamientoEntity> findInBounds(
            @Param("latMin") Double latMin,
            @Param("latMax") Double latMax,
            @Param("lngMin") Double lngMin,
            @Param("lngMax") Double lngMax
    );

    // Avistamientos recientes
    List<AvistamientoEntity> findByPublicoTrueAndFechaHoraAfterOrderByFechaHoraDesc(
            LocalDateTime desde
    );
}
