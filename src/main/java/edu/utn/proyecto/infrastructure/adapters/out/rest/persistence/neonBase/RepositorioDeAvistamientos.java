package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.neonBase;
import edu.utn.proyecto.domain.model.concreta.Avistamiento;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers.AvistamientoPersistenceMapper;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistamientos;
import edu.utn.proyecto.infrastructure.ports.out.jpa.AvistamientoJpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class RepositorioDeAvistamientos implements IRepoDeAvistamientos {

    private final AvistamientoJpaRepository jpa;
    private final AvistamientoPersistenceMapper mapper;

    public RepositorioDeAvistamientos(AvistamientoJpaRepository jpa,
                                      AvistamientoPersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Avistamiento save(Avistamiento avistamiento) {
        var saved = jpa.save(mapper.domainToEntity(avistamiento));
        return mapper.entityToDomain(saved);
    }

    @Override
    public Optional<Avistamiento> findById(UUID id) {
        return jpa.findById(id).map(mapper::entityToDomain);
    }

    @Override
    public List<Avistamiento> findAll() {
        return jpa.findAll().stream()
                .map(mapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Avistamiento> findByDesaparecidoId(UUID desaparecidoId) {
        return jpa.findByDesaparecidoId(desaparecidoId).stream()
                .map(mapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Avistamiento> findByAvistadorId(UUID avistadorId) {
        return jpa.findByAvistadorId(avistadorId).stream()
                .map(mapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Avistamiento> findPublicos() {
        return jpa.findByPublicoTrue().stream()
                .map(mapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Avistamiento> findInBounds(Double latMin, Double latMax,
                                           Double lngMin, Double lngMax) {
        return jpa.findInBounds(latMin, latMax, lngMin, lngMax).stream()
                .map(mapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Avistamiento> findRecientes(LocalDateTime desde) {
        return jpa.findByPublicoTrueAndFechaHoraAfterOrderByFechaHoraDesc(desde).stream()
                .map(mapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
