package edu.utn.proyecto.infrastructure.adapters.out.rest.persitence;
import edu.utn.proyecto.domain.model.Avistador;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeAvistadores;
import org.springframework.stereotype.Repository;


@Repository
public class RepositorioDeAvistadores implements IRepoDeAvistadores {

    @Override
    public Avistador getAvistador(Avistador avistador) {
        return null;
    }

    @Override
    public void addAvistador(Avistador avistador) {

    }

    @Override
    public void updateAvistador(Avistador avistador) {

    }

    @Override
    public void deleteAvistador(Avistador avistador) {

    }
}
