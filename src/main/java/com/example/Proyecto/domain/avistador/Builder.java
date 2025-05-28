package com.example.proyecto.domain.avistador;

import com.example.proyecto.domain.model.DtoDatosAvistador;

public interface Builder{
    public void reset();
    public void createDTO(DtoDatosAvistador datos);
    public Avistador geResult();

}
