package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.neonBase;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.RenaperPersonaEntity;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeRenaper;
import edu.utn.proyecto.infrastructure.ports.out.jpa.RenaperPersonaJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class RepositorioDeRenaper implements IRepoDeRenaper {

    private final RenaperPersonaJpaRepository jpa;

    public RepositorioDeRenaper(RenaperPersonaJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<RenaperPersonaEntity> findByDni(String dni) {
        return jpa.findByDni(dni);
    }
}