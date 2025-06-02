package com.example.proyecto.infrastructure.persitence;
import com.example.proyecto.domain.avistador.Avistador;
import com.example.proyecto.domain.model.DtoDatosAvistador;

public interface IRepoDeAvistadores {
        public Avistador getAvistador(Avistador avistador);
        public void addAvistador(Avistador avistador);
        public void updateAvistador(Avistador avistador);
        public void deleteAvistador(Avistador avistador);
}
