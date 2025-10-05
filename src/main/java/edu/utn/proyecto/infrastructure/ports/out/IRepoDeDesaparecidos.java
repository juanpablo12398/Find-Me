package edu.utn.proyecto.infrastructure.ports.out;
import edu.utn.proyecto.domain.model.concretas.Desaparecido;
import java.util.List;

public interface IRepoDeDesaparecidos {
    public Desaparecido save(Desaparecido desaparecido);
    public List<Desaparecido> getDesaparecidos();
}
