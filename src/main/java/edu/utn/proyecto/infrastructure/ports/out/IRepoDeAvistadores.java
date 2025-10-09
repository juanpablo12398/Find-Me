package edu.utn.proyecto.infrastructure.ports.out;
import edu.utn.proyecto.domain.model.concreta.Avistador;
import java.util.Optional;

public interface IRepoDeAvistadores {
        Avistador save(Avistador avistador);
        Optional<Avistador> findByDni(String dni);   // <--- Optional para poder leer datos
        boolean existsByDni(String dni);
}