package edu.utn.proyecto.applicacion.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public class DesaparecidoResponseDTO {
    private UUID id;
    private String nombre;
    private String apellido;
    private String dni;
    private LocalDateTime fechaDesaparicion;
    private String descripcion;
    private String foto;

    public DesaparecidoResponseDTO(UUID id, String nombre, String apellido, String dni,
                                   LocalDateTime fechaDesaparicion, String descripcion, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaDesaparicion = fechaDesaparicion;
        this.descripcion = descripcion;
        this.foto = foto;
    }

    // Getters and Setters
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

    public UUID getId() {
        return id;
    }

    public LocalDateTime getFechaDesaparicion() {
        return fechaDesaparicion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setFechaDesaparicion(LocalDateTime fechaDesaparicion) {
        this.fechaDesaparicion = fechaDesaparicion;
    }
}
