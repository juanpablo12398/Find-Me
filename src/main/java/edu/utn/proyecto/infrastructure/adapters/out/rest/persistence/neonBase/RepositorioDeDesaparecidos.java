package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.neonBase;

import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers.DesaparecidoPersistenceMapper;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import edu.utn.proyecto.infrastructure.ports.out.jpa.DesaparecidoJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

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
        var saved = jpa.save(mapper.domainToEntity(d));
        return mapper.entityToDomain(saved);
    }

    @Override
    public List<Desaparecido> getDesaparecidos() {
        return jpa.findAll().stream()
                .map(mapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Desaparecido> findById(UUID id) {
        return jpa.findById(id).map(mapper::entityToDomain); // delega en JPA
    }

    @Override
    public boolean existsByDni(String dni) {
        return jpa.existsByDni(dni); // delega en JPA
    }
}