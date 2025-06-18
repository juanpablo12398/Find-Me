package edu.utn.proyecto.domain.model.concretas;

import edu.utn.proyecto.domain.model.abstraccion.IAvistador;
import edu.utn.proyecto.domain.model.enums.Rol;

import java.time.LocalDateTime;

public class Avistador implements IAvistador {
    public String nombre;
    public String apellido;
    public String dni;
    public String mail;
    public Rol rol;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Avistador() {
    }
    public Avistador(String nombre, String apellido, String dni, String mail, Rol rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.mail = mail;
        this.rol = rol;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
