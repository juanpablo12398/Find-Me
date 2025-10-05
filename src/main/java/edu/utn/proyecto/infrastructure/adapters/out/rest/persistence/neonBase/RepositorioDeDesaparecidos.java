package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.neonBase;
import edu.utn.proyecto.domain.model.concretas.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers.DesaparecidoPersistenceMapper;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import edu.utn.proyecto.infrastructure.ports.out.jpa.DesaparecidoJpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class RepositorioDeDesaparecidos implements IRepoDeDesaparecidos {

    private final DesaparecidoJpaRepository jpa;
    private final DesaparecidoPersistenceMapper mapper;

    public RepositorioDeDesaparecidos(DesaparecidoJpaRepository jpa,
                                      DesaparecidoPersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Desaparecido save(Desaparecido d) {
        var savedEntity = jpa.save(mapper.domainToEntity(d));
        return mapper.entityToDomain(savedEntity);
    }

    @Override
    public List<Desaparecido> getDesaparecidos() {
        return jpa.findAll().stream()
                .map(mapper::entityToDomain)
                .toList();
    }
}
