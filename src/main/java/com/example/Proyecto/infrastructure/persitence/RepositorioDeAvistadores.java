package com.example.proyecto.infrastructure.persitence;
import com.example.proyecto.domain.avistador.Avistador;
import com.example.proyecto.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class RepositorioDeAvistadores implements IRepoDeAvistadores{

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
