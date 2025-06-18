package edu.utn.proyecto.infrastructure.adapters.out.rest.persitence;
import edu.utn.proyecto.domain.model.concretas.Desaparecido;
import edu.utn.proyecto.infrastructure.ports.out.IRepoDeDesaparecidos;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositorioDeDesaparecidos implements IRepoDeDesaparecidos {
    private List<Desaparecido> desaparecidos;

    public RepositorioDeDesaparecidos() {
        this.desaparecidos = new ArrayList<>();
    }

    @Override
    public void setDesaparecidos(){
        this.desaparecidos = new ArrayList<>();
    }

    @Override
    public List<Desaparecido> getDesaparecidos() {
        return this.desaparecidos;
    }

    @Override
    public void save(Desaparecido desaparecido) {
        desaparecidos.add(desaparecido);
    }



}
