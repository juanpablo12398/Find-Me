package edu.utn.proyecto.infrastructure.ports.out;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import java.util.Optional;

public interface IRepoDeRenaper {
    Optional<RenaperPersonaEntity> findByDni(String dni);
}