package edu.utn.proyecto.infrastructure.ports.out;
import edu.utn.proyecto.domain.model.Avistador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpringRepository extends JpaRepository<Avistador, Long> {
    Optional<Avistador> findByDni(String dni);
    void deleteByDni(String dni);

}
