package edu.utn.proyecto.infrastructure.ports.out;
import edu.utn.proyecto.domain.model.Avistador;

public interface IRepoDeAvistadores {
        public Avistador getAvistador(Avistador avistador);
        public void addAvistador(Avistador avistador);
        public void updateAvistador(Avistador avistador);
        public void deleteAvistador(Avistador avistador);
}
