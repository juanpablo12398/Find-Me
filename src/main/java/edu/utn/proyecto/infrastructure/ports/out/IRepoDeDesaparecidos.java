package edu.utn.proyecto.infrastructure.ports.out;
import edu.utn.proyecto.domain.model.concretas.Desaparecido;
import java.util.List;

public interface IRepoDeDesaparecidos {
    public void save(Desaparecido desaparecido);
    public void setDesaparecidos();
    public List<Desaparecido> getDesaparecidos();
}
