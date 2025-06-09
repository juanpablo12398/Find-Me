package edu.utn.proyecto.domain.model.abstraccion;
import edu.utn.proyecto.domain.model.enums.Rol;

public interface IAvistador {
    String getNombre();
    void setNombre(String nombre);

    String getApellido();
    void setApellido(String apellido);

    String getDni();
    void setDni(String dni);

    String getMail();
    void setMail(String mail);

    Rol getRol();
    void setRol(Rol rol);
}
