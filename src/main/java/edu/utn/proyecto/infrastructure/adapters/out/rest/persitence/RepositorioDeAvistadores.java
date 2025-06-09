package edu.utn.proyecto.infrastructure.adapters.out.rest.persitence;
import edu.utn.proyecto.domain.model.Avistador;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import edu.utn.proyecto.infrastructure.ports.out.SpringRepository;
import org.springframework.stereotype.Repository;


@Repository
public class RepositorioDeAvistadores implements IRepoDeAvistadores {

    private final SpringRepository jpaRepository;

    public RepositorioDeAvistadores(SpringRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Avistador getAvistador(Avistador avistador) {

        return  jpaRepository.findByDni(avistador.getDni())
                .orElseThrow(() -> new RuntimeException("Avistador not found with DNI: " + avistador.getDni()));
    }

    @Override
    public void addAvistador(Avistador avistador) {
        jpaRepository.save(avistador);
    }

}
