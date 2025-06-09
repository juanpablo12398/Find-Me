package edu.utn.proyecto.infrastructure.ports.out;
import edu.utn.proyecto.domain.model.Avistador;
import java.util.Optional;

public interface IRepoDeAvistadores {
        public Avistador getAvistador(Avistador avistador);
        public void addAvistador(Avistador avistador);
}
