package edu.utn.proyecto.infrastructure.ports.out.jpa;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.DesaparecidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface DesaparecidoJpaRepository extends JpaRepository<DesaparecidoEntity, UUID> {
    Optional<DesaparecidoEntity> findByDni(String dni);
    boolean existsByDni(String dni);
}
