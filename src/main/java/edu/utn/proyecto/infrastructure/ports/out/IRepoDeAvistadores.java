package edu.utn.proyecto.infrastructure.ports.out;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.AvistadorEntity;
import java.util.Optional;
import java.util.UUID;

public interface IRepoDeAvistadores {
        Avistador save(Avistador avistador);
        Optional<Avistador> findByDni(String dni);   // <--- Optional para poder leer datos
        boolean existsByDni(String dni);
        Optional<AvistadorEntity> findById(UUID uuid);
}