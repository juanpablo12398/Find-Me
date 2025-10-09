package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.neonBase;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.AvistadorEntity;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers.AvistadorPersistenceMapper;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.jpa.AvistadorJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class RepositorioDeAvistadores implements IRepoDeAvistadores {

    private final AvistadorJpaRepository jpa;
    private final AvistadorPersistenceMapper mapper;

    public RepositorioDeAvistadores(AvistadorJpaRepository jpa, AvistadorPersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Avistador save(Avistador d) {
        AvistadorEntity saved = jpa.save(mapper.domainToEntity(d));
        return mapper.entityToDomain(saved);
    }

    @Override
    public Optional<Avistador> findByDni(String dni) {
        return jpa.findByDni(dni).map(mapper::entityToDomain);
    }

    @Override
    public boolean existsByDni(String dni) {
        return jpa.findByDni(dni).isPresent();
    }
}