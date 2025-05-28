package com.example.proyecto.domain.avistador;
import com.example.proyecto.domain.model.DtoDatosAvistador;


public class BuilderAvistador implements Builder{
    public Avistador avistador;

    public Avistador getAvistador() {
        return avistador;
    }

    private void setAvistador(Avistador avistador) {
        this.avistador = avistador;
    }

    public void reset() {
        this.avistador = new Avistador();
    }

    public void createDTO(DtoDatosAvistador datos) {
        this.avistador.setDni(datos.getDni());
        this.avistador.setApellido(datos.getApellido());
        this.avistador.setMail(datos.getMail());
        this.avistador.setNombre(datos.getNombre());
    }


    public Avistador geResult() {
        return this.avistador;
    }
}
