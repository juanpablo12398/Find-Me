package edu.utn.proyecto.infrastructure.adapters.in.rest.dtos;

public class SessionUserDTO {
    private String dni;
    private String email;
    private String nombre;

    public SessionUserDTO(String dni, String email, String nombre) {
        this.dni = dni; this.email = email; this.nombre = nombre;
    }

    public String getDni() { return dni; }
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
}