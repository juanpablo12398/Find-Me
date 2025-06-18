package edu.utn.proyecto.infrastructure.adapters.in.rest.dtos;

public class DesaparecidoFrontDTO {
    private String nombre;
    private String apellido;
    private String dni;
    private String descripcion;
    private String foto;
    private String fechaFormateada;  // ya como String listo para mostrar

    public DesaparecidoFrontDTO(String nombre, String apellido, String dni, String descripcion, String foto, String fechaFormateada) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.descripcion = descripcion;
        this.foto = foto;
        this.fechaFormateada = fechaFormateada;
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

    public String getFechaFormateada() {
        return fechaFormateada;
    }

    public void setFechaFormateada(String fechaFormateada) {
        this.fechaFormateada = fechaFormateada;
    }
}