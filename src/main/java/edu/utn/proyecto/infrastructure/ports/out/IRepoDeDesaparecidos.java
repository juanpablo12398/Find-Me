package edu.utn.proyecto.infrastructure.ports.out;
import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRepoDeDesaparecidos {
    Desaparecido save(Desaparecido desaparecido);
    List<Desaparecido> getDesaparecidos();
    Optional<Desaparecido> findById(UUID id);
    boolean existsByDni(String dni);
}
