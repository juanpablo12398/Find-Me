package edu.utn.proyecto.infrastructure.ports.out.jpa;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.AvistadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AvistadorJpaRepository extends JpaRepository<AvistadorEntity, UUID> {
    Optional<AvistadorEntity> findByDni(String dni);
}
